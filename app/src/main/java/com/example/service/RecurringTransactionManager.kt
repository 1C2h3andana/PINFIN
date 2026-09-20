package com.example.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.data.dao.BankDao
import com.example.data.dao.BudgetGoalDao
import com.example.data.dao.RecurringTransactionDao
import com.example.data.db.BankDatabase
import com.example.data.model.NotificationEntity
import com.example.data.model.NotificationType
import com.example.data.model.RecurringTransactionEntity
import com.example.data.model.RiskLevel
import com.example.data.model.Transaction
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Result summary of processing recurring subscriptions.
 */
data class RecurringExecutionSummary(
    val processedCount: Int,
    val totalAmount: Double,
    val processedTitles: List<String>
)

/**
 * Autonomous Recurring Transaction Engine.
 *
 * Automatically inspects due recurring subscriptions and scheduled auto-debits,
 * inserts debit transaction records into the Room database, updates account balances,
 * increments budget goal progress, records notification logs, and alerts the user
 * via Android system notifications.
 */
object RecurringTransactionManager {

    private const val TAG = "RecurringTxManager"
    const val CHANNEL_ID = "recurring_transactions_channel"

    /**
     * Inspects active [RecurringTransactionEntity] items in the Room database.
     * Any item whose nextDueTimestamp <= current time (or all active if [forceAll] is true)
     * will be executed, inserted into Room, and notified.
     */
    suspend fun processDueRecurringTransactions(
        context: Context,
        forceAll: Boolean = false
    ): RecurringExecutionSummary = withContext(Dispatchers.IO) {
        val appContext = context.applicationContext
        val db = BankDatabase.getDatabase(appContext)

        // 1. Ensure baseline subscriptions exist in Room
        seedDefaultRecurringIfEmpty(appContext)

        processDueTransactionsWithDaos(
            bankDao = db.bankDao(),
            budgetGoalDao = db.budgetGoalDao(),
            recurringDao = db.recurringTransactionDao(),
            context = appContext,
            forceAll = forceAll
        )
    }

    /**
     * Internal processor accepting DAOs directly (supports in-memory tests and injection).
     */
    suspend fun processDueTransactionsWithDaos(
        bankDao: BankDao,
        budgetGoalDao: BudgetGoalDao?,
        recurringDao: RecurringTransactionDao,
        context: Context,
        forceAll: Boolean = false
    ): RecurringExecutionSummary = withContext(Dispatchers.IO) {
        val appContext = context.applicationContext
        val budgetDao = budgetGoalDao

        val now = System.currentTimeMillis()
        val dueList = try {
            if (forceAll) {
                recurringDao.getActiveRecurringTransactions().first()
            } else {
                recurringDao.getDueRecurringTransactions(now)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to query due recurring transactions from Room", e)
            emptyList()
        }

        if (dueList.isEmpty()) {
            Log.d(TAG, "No recurring transactions currently due for execution.")
            return@withContext RecurringExecutionSummary(0, 0.0, emptyList())
        }

        var processedCount = 0
        var totalAmount = 0.0
        val processedTitles = mutableListOf<String>()

        createNotificationChannel(appContext)

        for (recurring in dueList) {
            try {
                // Determine transaction category enum
                val categoryEnum = parseCategory(recurring.category)

                // A. Insert into primary `transactions` Room table
                val transactionEntity = TransactionEntity(
                    accountId = recurring.accountId,
                    title = recurring.title,
                    recipient = recurring.payee,
                    upiOrHandle = "${recurring.payee.lowercase().replace(" ", "")}@subscription",
                    amount = recurring.amount,
                    category = categoryEnum,
                    type = TransactionType.DEBIT,
                    timestamp = now,
                    status = TransactionStatus.COMPLETED,
                    riskScore = 1,
                    riskLevel = RiskLevel.LOW,
                    riskReason = "Verified automated recurring subscription payment",
                    is2FaVerified = true,
                    deviceOrigin = "Autonomous Background Worker",
                    location = "Scheduled Engine",
                    note = "Automated ${recurring.frequency} debit for ${recurring.title}"
                )
                bankDao.insertTransaction(transactionEntity)

                // B. Insert into `transaction_records` Room table (for offline-first accounting ledger)
                val transactionRecord = Transaction(
                    amount = recurring.amount,
                    category = recurring.category,
                    date = now,
                    type = "expense"
                )
                bankDao.insertTransactionRecord(transactionRecord)

                // C. Deduct monetary balance from user's account in Room
                bankDao.updateAccountBalance(recurring.accountId, -recurring.amount)

                // D. Increment budget goal progress in Room if matching category exists
                try {
                    val allGoals = budgetDao?.getAllBudgetGoals()?.first() ?: emptyList()
                    val matchingGoal = allGoals.firstOrNull {
                        it.category.contains(recurring.category, ignoreCase = true) ||
                                recurring.category.contains(it.category, ignoreCase = true)
                    }
                    if (matchingGoal != null) {
                        budgetDao?.addProgress(matchingGoal.id, recurring.amount)
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to update budget goal progress for recurring tx", e)
                }

                // E. Update next execution schedule in `recurring_transactions` table
                val nextDue = now + recurring.intervalMillis
                recurringDao.markExecuted(recurring.id, now, nextDue)

                // F. Insert in-app NotificationEntity into Room database
                val inAppNotification = NotificationEntity(
                    title = "Subscription Auto-Debited",
                    message = "$${"%,.2f".format(recurring.amount)} debited for ${recurring.title} from ${recurring.accountName}.",
                    type = NotificationType.TRANSACTION_ALERT,
                    timestamp = now,
                    isRead = false,
                    actionRoute = "banking_scheduled"
                )
                bankDao.insertNotification(inAppNotification)

                // G. Send Android system notification to status bar
                sendSystemNotification(
                    context = appContext,
                    notificationId = (recurring.id + 7000).toInt(),
                    title = "Auto-Debit: $${"%,.2f".format(recurring.amount)}",
                    message = "Successfully processed ${recurring.frequency.lowercase()} payment for '${recurring.title}'."
                )

                processedCount++
                totalAmount += recurring.amount
                processedTitles.add(recurring.title)

                Log.d(TAG, "Processed recurring transaction '${recurring.title}' for $${recurring.amount}")
            } catch (e: Exception) {
                Log.e(TAG, "Error executing recurring transaction #${recurring.id}", e)
            }
        }

        RecurringExecutionSummary(processedCount, totalAmount, processedTitles)
    }

    /**
     * Executes a single recurring transaction immediately on demand,
     * regardless of whether nextDueTimestamp has elapsed.
     */
    suspend fun executeSingleRecurringTransaction(
        context: Context,
        recurringId: Long
    ): Boolean = withContext(Dispatchers.IO) {
        val appContext = context.applicationContext
        val db = BankDatabase.getDatabase(appContext)
        val recurringDao = db.recurringTransactionDao()
        val bankDao = db.bankDao()
        val budgetDao = db.budgetGoalDao()

        val recurring = recurringDao.getRecurringTransactionById(recurringId) ?: return@withContext false
        val now = System.currentTimeMillis()

        try {
            createNotificationChannel(appContext)
            val categoryEnum = parseCategory(recurring.category)

            val txEntity = TransactionEntity(
                accountId = recurring.accountId,
                title = recurring.title,
                recipient = recurring.payee,
                upiOrHandle = "${recurring.payee.lowercase().replace(" ", "")}@subscription",
                amount = recurring.amount,
                category = categoryEnum,
                type = TransactionType.DEBIT,
                timestamp = now,
                status = TransactionStatus.COMPLETED,
                riskScore = 1,
                riskLevel = RiskLevel.LOW,
                riskReason = "On-demand execution of recurring subscription",
                is2FaVerified = true,
                deviceOrigin = "Autonomous Background Worker",
                location = "Scheduled Engine",
                note = "Manual trigger: ${recurring.frequency} debit for ${recurring.title}"
            )
            bankDao.insertTransaction(txEntity)

            val txRecord = Transaction(
                amount = recurring.amount,
                category = recurring.category,
                date = now,
                type = "expense"
            )
            bankDao.insertTransactionRecord(txRecord)

            bankDao.updateAccountBalance(recurring.accountId, -recurring.amount)

            // Update matching budget goal if present
            try {
                val goals = budgetDao.getAllBudgetGoals().first()
                val match = goals.firstOrNull {
                    it.category.contains(recurring.category, ignoreCase = true) ||
                            recurring.category.contains(it.category, ignoreCase = true)
                }
                if (match != null) {
                    budgetDao.addProgress(match.id, recurring.amount)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error updating budget goal for manual recurring execution", e)
            }

            val nextDue = now + recurring.intervalMillis
            recurringDao.markExecuted(recurring.id, now, nextDue)

            val inAppNotification = NotificationEntity(
                title = "Subscription Auto-Debited",
                message = "$${"%,.2f".format(recurring.amount)} debited for ${recurring.title} from ${recurring.accountName}.",
                type = NotificationType.TRANSACTION_ALERT,
                timestamp = now,
                isRead = false,
                actionRoute = "banking_scheduled"
            )
            bankDao.insertNotification(inAppNotification)

            sendSystemNotification(
                context = appContext,
                notificationId = (recurring.id + 8000).toInt(),
                title = "Subscription Charged: $${"%,.2f".format(recurring.amount)}",
                message = "Executed scheduled payment for '${recurring.title}'."
            )
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed executing recurring transaction #$recurringId", e)
            false
        }
    }

    /**
     * Seeds initial recurring subscriptions into the Room database if the table is empty.
     */
    suspend fun seedDefaultRecurringIfEmpty(context: Context) = withContext(Dispatchers.IO) {
        val db = BankDatabase.getDatabase(context)
        val dao = db.recurringTransactionDao()

        try {
            val existing = dao.getAllRecurringTransactions().first()
            if (existing.isEmpty()) {
                val now = System.currentTimeMillis()
                val oneDay = 24L * 60 * 60 * 1000L
                val thirtyDays = 30L * oneDay

                val defaults = listOf(
                    RecurringTransactionEntity(
                        title = "Netflix Premium 4K",
                        payee = "Netflix Inc.",
                        amount = 22.99,
                        category = "Entertainment",
                        frequency = "MONTHLY",
                        intervalMillis = thirtyDays,
                        nextDueTimestamp = now - 1000L, // Due now!
                        accountId = 1L,
                        accountName = "Premier Checking (...4829)",
                        isActive = true,
                        note = "Monthly streaming family plan"
                    ),
                    RecurringTransactionEntity(
                        title = "Spotify Premium Family",
                        payee = "Spotify AB",
                        amount = 16.99,
                        category = "Entertainment",
                        frequency = "MONTHLY",
                        intervalMillis = thirtyDays,
                        nextDueTimestamp = now + (4 * oneDay),
                        accountId = 1L,
                        accountName = "Premier Checking (...4829)",
                        isActive = true,
                        note = "Music streaming subscription"
                    ),
                    RecurringTransactionEntity(
                        title = "AWS Cloud Infrastructure",
                        payee = "Amazon Web Services",
                        amount = 64.50,
                        category = "Cloud & Dev",
                        frequency = "MONTHLY",
                        intervalMillis = thirtyDays,
                        nextDueTimestamp = now - 500L, // Due now!
                        accountId = 1L,
                        accountName = "Apex High Yield (...1042)",
                        isActive = true,
                        note = "Cloud servers & database hosting"
                    ),
                    RecurringTransactionEntity(
                        title = "ChatGPT Plus Team",
                        payee = "OpenAI LLC",
                        amount = 20.00,
                        category = "AI & Software",
                        frequency = "MONTHLY",
                        intervalMillis = thirtyDays,
                        nextDueTimestamp = now + (11 * oneDay),
                        accountId = 1L,
                        accountName = "Premier Checking (...4829)",
                        isActive = true,
                        note = "AI workspace assistant"
                    ),
                    RecurringTransactionEntity(
                        title = "ConEdison Clean Power",
                        payee = "ConEdison Utility",
                        amount = 135.20,
                        category = "Utilities",
                        frequency = "MONTHLY",
                        intervalMillis = thirtyDays,
                        nextDueTimestamp = now + (18 * oneDay),
                        accountId = 1L,
                        accountName = "Premier Checking (...4829)",
                        isActive = true,
                        note = "Monthly renewable electricity"
                    ),
                    RecurringTransactionEntity(
                        title = "Equinox Luxury Fitness",
                        payee = "Equinox Holdings",
                        amount = 180.00,
                        category = "Health & Fitness",
                        frequency = "MONTHLY",
                        intervalMillis = thirtyDays,
                        nextDueTimestamp = now + (24 * oneDay),
                        accountId = 1L,
                        accountName = "Premier Checking (...4829)",
                        isActive = true,
                        note = "Gym and wellness facility access"
                    )
                )

                dao.insertRecurringTransactions(defaults)
                Log.d(TAG, "Seeded ${defaults.size} default recurring subscriptions in Room.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking/seeding recurring transactions in Room", e)
        }
    }

    private fun parseCategory(cat: String): TransactionCategory {
        return when (cat.uppercase()) {
            "FOOD", "FOOD & DINING" -> TransactionCategory.FOOD
            "SHOPPING", "RETAIL" -> TransactionCategory.SHOPPING
            "BILLS", "UTILITIES" -> TransactionCategory.BILLS
            "ENTERTAINMENT", "STREAMING" -> TransactionCategory.ENTERTAINMENT
            "HEALTHCARE", "HEALTH & FITNESS", "FITNESS" -> TransactionCategory.HEALTHCARE
            "EDUCATION" -> TransactionCategory.EDUCATION
            "INVESTMENT" -> TransactionCategory.INVESTMENT
            else -> TransactionCategory.BILLS
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recurring Subscriptions & Auto-Debits",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when scheduled recurring subscriptions and auto-debits are processed"
                enableLights(true)
                enableVibration(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(channel)
        }
    }

    private fun sendSystemNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                Log.w(TAG, "POST_NOTIFICATIONS not granted. Skipping system notification.")
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("nav_destination", "banking_scheduled")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_upload_done)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_EVENT)

        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (e: Exception) {
            Log.e(TAG, "Error sending system notification for recurring debit", e)
        }
    }
}
