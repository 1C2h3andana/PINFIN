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
import com.example.data.db.BankDatabase
import com.example.data.model.BudgetGoal
import com.example.data.model.NotificationEntity
import com.example.data.model.NotificationType
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Core monitoring engine that evaluates local Room [BudgetGoal] records against user transactions.
 * Dispatches Android system notifications when spending approaches (>= 80%) or exceeds (>= 100%) targets.
 */
object BudgetGoalMonitor {

    private const val TAG = "BudgetGoalMonitor"
    const val CHANNEL_ID = "budget_goal_alerts_channel"
    private const val PREFS_NAME = "budget_goal_notifications_prefs"

    /**
     * Checks all Room [BudgetGoal] items against local Room transactions.
     * Dispatches system notifications and logs in-app notification records.
     *
     * @param context Application context
     * @param forceNotify If true, bypasses SharedPreferences deduplication (e.g. For manual user check)
     * @return List of [BudgetAlertResult] summarizing each budget goal's evaluation status
     */
    suspend fun checkBudgetGoalsAndNotify(
        context: Context,
        forceNotify: Boolean = false
    ): List<BudgetAlertResult> = withContext(Dispatchers.IO) {
        val appContext = context.applicationContext
        val db = BankDatabase.getDatabase(appContext)
        val budgetGoalDao = db.budgetGoalDao()
        val bankDao = db.bankDao()
        val transactionDao = db.transactionDao()

        // 1. Fetch budget goals from Room
        val goals: List<BudgetGoal> = try {
            budgetGoalDao.getAllBudgetGoals().first()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching budget goals from Room", e)
            emptyList()
        }

        if (goals.isEmpty()) {
            Log.d(TAG, "No BudgetGoal entities found in Room database.")
            return@withContext emptyList()
        }

        // 2. Fetch completed debit/expense transactions from Room
        val bankTransactions: List<TransactionEntity> = try {
            bankDao.getAllTransactions().first().filter {
                it.type == TransactionType.DEBIT
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching transactions from Room", e)
            emptyList()
        }

        val recordTransactions = try {
            transactionDao.getAllTransactions().first().filter {
                it.type.equals("expense", ignoreCase = true)
            }
        } catch (e: Exception) {
            emptyList()
        }

        ensureNotificationChannel(appContext)

        val results = mutableListOf<BudgetAlertResult>()
        val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 3. Evaluate each budget goal
        for (goal in goals) {
            val matchingBankTxSum = bankTransactions.filter { tx ->
                isCategoryMatch(tx.category.name, goal.category)
            }.sumOf { it.amount }

            val matchingRecordTxSum = recordTransactions.filter { tx ->
                isCategoryMatch(tx.category, goal.category)
            }.sumOf { it.amount }

            // Aggregate total spent (taking maximum between tracked progress and calculated transaction sum)
            val computedSpend = maxOf(matchingBankTxSum, matchingRecordTxSum)
            val currentSpent = if (computedSpend > 0) {
                maxOf(goal.currentProgress, computedSpend)
            } else {
                goal.currentProgress
            }

            // Sync updated progress to Room if transaction spending exceeds stored progress
            if (currentSpent > goal.currentProgress) {
                try {
                    budgetGoalDao.updateBudgetGoal(goal.copy(currentProgress = currentSpent))
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to update budget goal progress in Room: ${goal.id}", e)
                }
            }

            val target = goal.targetAmount
            val ratio = if (target > 0.0) (currentSpent / target) else 0.0
            val percentage = (ratio * 100.0).toFloat()

            val level = when {
                ratio >= 1.0 -> BudgetAlertLevel.EXCEEDED
                ratio >= 0.80 -> BudgetAlertLevel.APPROACHING
                else -> BudgetAlertLevel.NORMAL
            }

            var notificationSent = false
            var alertTitle = ""
            var alertMessage = ""

            if (level != BudgetAlertLevel.NORMAL) {
                val formattedSpent = "%,.2f".format(currentSpent)
                val formattedTarget = "%,.2f".format(target)
                val percentInt = percentage.toInt()

                if (level == BudgetAlertLevel.EXCEEDED) {
                    alertTitle = "🚨 Budget Exceeded: ${goal.category}"
                    alertMessage = "You have spent $$formattedSpent ($percentInt%) on ${goal.category}, exceeding your $$formattedTarget target!"
                } else {
                    alertTitle = "⚠️ Budget Alert: Approaching Limit"
                    alertMessage = "You've reached $$formattedSpent ($percentInt%) of your $$formattedTarget budget for ${goal.category}."
                }

                val lastNotifiedLevel = prefs.getString("last_level_${goal.id}", null)
                val lastNotifiedTime = prefs.getLong("last_time_${goal.id}", 0L)
                val now = System.currentTimeMillis()
                val sixHoursMs = 6 * 3600 * 1000L

                val shouldSend = forceNotify ||
                        lastNotifiedLevel != level.name ||
                        (now - lastNotifiedTime > sixHoursMs)

                if (shouldSend) {
                    // Send Android System Notification
                    val sent = sendSystemNotification(
                        context = appContext,
                        notificationId = (20000 + goal.id).toInt(),
                        title = alertTitle,
                        message = alertMessage,
                        goalId = goal.id
                    )

                    if (sent) {
                        notificationSent = true
                        prefs.edit()
                            .putString("last_level_${goal.id}", level.name)
                            .putLong("last_time_${goal.id}", now)
                            .apply()

                        // Record in Room Database notifications table for in-app alert visibility
                        try {
                            bankDao.insertNotification(
                                NotificationEntity(
                                    title = alertTitle,
                                    message = alertMessage,
                                    type = NotificationType.TRANSACTION_ALERT,
                                    timestamp = now,
                                    isRead = false,
                                    actionRoute = "budget_goals"
                                )
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Error inserting NotificationEntity in Room", e)
                        }
                    }
                }
            }

            results.add(
                BudgetAlertResult(
                    goalId = goal.id,
                    category = goal.category,
                    targetAmount = target,
                    currentSpent = currentSpent,
                    percentage = percentage,
                    level = level,
                    notificationSent = notificationSent,
                    alertTitle = alertTitle,
                    alertMessage = alertMessage
                )
            )
        }

        Log.d(TAG, "Completed budget goals evaluation: ${results.size} goals checked.")
        results
    }

    /**
     * Checks whether a transaction category name matches a budget goal category.
     */
    private fun isCategoryMatch(txCategory: String, goalCategory: String): Boolean {
        val tx = txCategory.trim().lowercase()
        val goal = goalCategory.trim().lowercase()
        return tx == goal ||
                goal.contains(tx) ||
                tx.contains(goal) ||
                goal.contains("general") ||
                (goal.contains("dining") && tx.contains("food")) ||
                (goal.contains("groceries") && tx.contains("food")) ||
                (goal.contains("tech") && tx.contains("shopping"))
    }

    /**
     * Creates and registers the Android Notification Channel (API 26+).
     */
    private fun ensureNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Budget Alerts & Goal Limits"
            val descriptionText = "System notifications when spending approaches or exceeds budget targets"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                setShowBadge(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Dispatches an Android system notification using NotificationCompat.
     */
    private fun sendSystemNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String,
        goalId: Long
    ): Boolean {
        // Check POST_NOTIFICATIONS permission on Android 13+ (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                Log.w(TAG, "POST_NOTIFICATIONS permission not granted. Skipping system notification.")
                return false
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("nav_destination", "budget_goals")
            putExtra("goal_id", goalId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)

        return try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
            Log.d(TAG, "System notification dispatched for goal $goalId: $title")
            true
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException sending system notification", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Exception sending system notification", e)
            false
        }
    }
}
