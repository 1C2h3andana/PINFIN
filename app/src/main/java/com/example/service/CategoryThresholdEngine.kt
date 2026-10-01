package com.example.service

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
import com.example.R
import com.example.data.db.BankDatabase
import com.example.data.model.BudgetEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.NotificationType
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Result of a category spending limit evaluation.
 */
data class CategoryThresholdEvaluation(
    val category: TransactionCategory,
    val monthlyLimit: Double,
    val currentSpent: Double,
    val percentage: Float,
    val status: ThresholdStatus,
    val notificationDispatched: Boolean = false,
    val alertMessage: String = ""
)

enum class ThresholdStatus {
    NORMAL,            // < 80%
    APPROACHING_80,    // 80% to 99.9%
    EXCEEDED_100       // >= 100%
}

/**
 * Core engine that manages user-defined category spending thresholds.
 * Evaluates monthly spending against limits and triggers local Android system notifications
 * when spending reaches or exceeds 80% or 100% of the set limits.
 */
object CategoryThresholdEngine {

    private const val TAG = "CategoryThresholdEngine"
    const val CHANNEL_ID = "category_spending_alerts_channel"
    private const val PREFS_NAME = "category_spending_prefs"

    @Volatile
    private var storedContext: Context? = null

    fun init(context: Context) {
        storedContext = context.applicationContext
        ensureChannel(context)
    }

    fun getStoredContext(): Context? = storedContext

    /**
     * Ensures the notification channel is registered on Android O+.
     */
    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Category Spending Limit Alerts"
            val descriptionText = "Notifications when category spending reaches 80% warning or 100% limit"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                setShowBadge(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Evaluates a single category threshold and triggers local notification if exceeded.
     */
    suspend fun evaluateAndNotifyCategory(
        context: Context,
        category: TransactionCategory,
        monthlyLimit: Double,
        currentSpent: Double,
        forceNotify: Boolean = false
    ): CategoryThresholdEvaluation = withContext(Dispatchers.IO) {
        ensureChannel(context)
        val appContext = context.applicationContext

        val ratio = if (monthlyLimit > 0.0) (currentSpent / monthlyLimit) else 0.0
        val percentage = (ratio * 100.0).toFloat()

        val status = when {
            ratio >= 1.0 -> ThresholdStatus.EXCEEDED_100
            ratio >= 0.80 -> ThresholdStatus.APPROACHING_80
            else -> ThresholdStatus.NORMAL
        }

        var notificationSent = false
        var alertTitle = ""
        var alertMessage = ""

        if (status != ThresholdStatus.NORMAL) {
            val formattedSpent = "%,.2f".format(currentSpent)
            val formattedLimit = "%,.2f".format(monthlyLimit)
            val percentInt = percentage.toInt()
            val categoryLabel = formatCategoryName(category)

            if (status == ThresholdStatus.EXCEEDED_100) {
                alertTitle = "🚨 Category Limit Exceeded: $categoryLabel"
                alertMessage = "You've spent $$formattedSpent ($percentInt%) on $categoryLabel, exceeding your $$formattedLimit monthly limit!"
            } else {
                alertTitle = "⚠️ 80% Budget Alert: $categoryLabel"
                alertMessage = "You've reached $$formattedSpent ($percentInt%) of your $$formattedLimit monthly limit for $categoryLabel."
            }

            val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val lastStatusKey = "last_status_${category.name}"
            val lastTimeKey = "last_time_${category.name}"
            val lastStatus = prefs.getString(lastStatusKey, null)
            val lastTime = prefs.getLong(lastTimeKey, 0L)
            val now = System.currentTimeMillis()
            val cooldownMs = 2 * 3600 * 1000L // 2 hour cooldown unless forceNotify or status escalated

            val isEscalation = (lastStatus == ThresholdStatus.APPROACHING_80.name && status == ThresholdStatus.EXCEEDED_100)
            val shouldSend = forceNotify || isEscalation || lastStatus != status.name || (now - lastTime > cooldownMs)

            if (shouldSend) {
                notificationSent = dispatchNotification(
                    context = appContext,
                    notificationId = 30000 + category.ordinal,
                    title = alertTitle,
                    message = alertMessage
                )

                if (notificationSent) {
                    prefs.edit()
                        .putString(lastStatusKey, status.name)
                        .putLong(lastTimeKey, now)
                        .apply()

                    // Insert in-app notification in Room
                    try {
                        val db = BankDatabase.getDatabase(appContext)
                        db.bankDao().insertNotification(
                            NotificationEntity(
                                title = alertTitle,
                                message = alertMessage,
                                type = NotificationType.TRANSACTION_ALERT,
                                timestamp = now,
                                isRead = false,
                                actionRoute = "analytics"
                            )
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to insert notification in Room", e)
                    }
                }
            }
        }

        CategoryThresholdEvaluation(
            category = category,
            monthlyLimit = monthlyLimit,
            currentSpent = currentSpent,
            percentage = percentage,
            status = status,
            notificationDispatched = notificationSent,
            alertMessage = alertMessage
        )
    }

    /**
     * Evaluates all stored categories in Room against transactions and triggers notifications.
     */
    suspend fun evaluateAllCategories(
        context: Context,
        forceNotify: Boolean = false
    ): List<CategoryThresholdEvaluation> = withContext(Dispatchers.IO) {
        val db = BankDatabase.getDatabase(context)
        val budgets = db.bankDao().getAllBudgets().first()
        val results = mutableListOf<CategoryThresholdEvaluation>()

        for (budget in budgets) {
            val eval = evaluateAndNotifyCategory(
                context = context,
                category = budget.category,
                monthlyLimit = budget.monthlyLimit,
                currentSpent = budget.currentSpent,
                forceNotify = forceNotify
            )
            results.add(eval)
        }
        results
    }

    private fun dispatchNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String
    ): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val hasPermission = ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
                if (!hasPermission) {
                    Log.w(TAG, "POST_NOTIFICATIONS permission not granted.")
                    return false
                }
            }

            val launchIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("NAVIGATE_TO", "analytics")
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

            val manager = NotificationManagerCompat.from(context)
            manager.notify(notificationId, builder.build())
            Log.d(TAG, "Dispatched category threshold notification ID: $notificationId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error dispatching system notification", e)
            false
        }
    }

    fun formatCategoryName(category: TransactionCategory): String {
        return when (category) {
            TransactionCategory.FOOD -> "Food & Groceries"
            TransactionCategory.SHOPPING -> "Shopping & Retail"
            TransactionCategory.BILLS -> "Bills & Utilities"
            TransactionCategory.ENTERTAINMENT -> "Entertainment & Leisure"
            TransactionCategory.HEALTHCARE -> "Healthcare & Medical"
            TransactionCategory.EDUCATION -> "Education & Learning"
            TransactionCategory.INVESTMENT -> "Investments"
            TransactionCategory.SALARY -> "Income & Salary"
            TransactionCategory.TRANSFER -> "Transfers"
            TransactionCategory.DEPOSIT -> "Deposits"
            TransactionCategory.WITHDRAWAL -> "Withdrawals"
            TransactionCategory.OTHER -> "General & Miscellaneous"
            else -> category.name
        }
    }
}
