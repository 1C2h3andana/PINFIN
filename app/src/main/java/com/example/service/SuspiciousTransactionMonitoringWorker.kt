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
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.MainActivity
import com.example.data.db.BankDatabase
import com.example.data.model.NotificationEntity
import com.example.data.model.NotificationType
import com.example.data.model.RiskLevel
import com.example.data.model.TransactionEntity
import com.example.domain.engine.FraudDetectionEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Background WorkManager worker that periodically inspects recent Room database transactions,
 * evaluates them against the [FraudDetectionEngine] for high risk scores, suspicious keywords,
 * sudden velocity surges, and dispatches high-priority local Android notifications to the user.
 */
class SuspiciousTransactionMonitoringWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d(TAG, "Starting SuspiciousTransactionMonitoringWorker execution...")
        return@withContext try {
            val flaggedCount = analyzeTransactionsAndNotify(applicationContext)
            Log.d(TAG, "SuspiciousTransactionMonitoringWorker completed. Flagged $flaggedCount transactions.")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "SuspiciousTransactionMonitoringWorker execution encountered error", e)
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }

    companion object {
        private const val TAG = "SuspiciousTxWorker"
        const val UNIQUE_PERIODIC_WORK = "suspicious_transaction_periodic_worker"
        const val UNIQUE_ONETIME_WORK = "suspicious_transaction_onetime_worker"
        const val NOTIFICATION_CHANNEL_ID = "suspicious_transaction_alerts_channel"
        private const val PREFS_NAME = "suspicious_tx_alerts_prefs"
        private const val KEY_ALERTED_IDS = "alerted_tx_ids"

        /**
         * Enqueues a periodic background check using Android WorkManager.
         */
        fun schedulePeriodic(context: Context, intervalHours: Long = 4) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(true)
                .build()

            val periodicRequest = PeriodicWorkRequestBuilder<SuspiciousTransactionMonitoringWorker>(
                intervalHours, TimeUnit.HOURS,
                15, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag(UNIQUE_PERIODIC_WORK)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
            Log.i(TAG, "Enqueued periodic suspicious transaction monitor every $intervalHours hours.")
        }

        /**
         * Triggers an immediate one-time background scan of transactions.
         */
        fun runImmediateCheck(context: Context) {
            val oneTimeRequest = OneTimeWorkRequestBuilder<SuspiciousTransactionMonitoringWorker>()
                .addTag(UNIQUE_ONETIME_WORK)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_ONETIME_WORK,
                ExistingWorkPolicy.REPLACE,
                oneTimeRequest
            )
            Log.i(TAG, "Triggered immediate suspicious transaction scan.")
        }

        /**
         * Creates notification channel for suspicious fraud and security alerts.
         */
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Fraud & Suspicious Activity Alerts"
                val descriptionText = "Urgent notifications when unusual or suspicious transactions are detected."
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                    enableLights(true)
                    enableVibration(true)
                }
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        /**
         * Core analysis logic: examines recent transactions in Room database, flags any suspicious
         * activity, saves in-app notification records, and triggers native Android notification.
         */
        suspend fun analyzeTransactionsAndNotify(context: Context): Int {
            val db = BankDatabase.getDatabase(context)
            val bankDao = db.bankDao()
            val transactionDao = db.transactionDao()
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val alertedSet = prefs.getStringSet(KEY_ALERTED_IDS, emptySet())?.toMutableSet() ?: mutableSetOf()

            // Fetch recent transactions from Room
            val transactions = try {
                bankDao.getAllTransactions().first()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load transactions from Room", e)
                emptyList()
            }

            if (transactions.isEmpty()) return 0

            createNotificationChannel(context)

            var newlyFlagged = 0
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            for (tx in transactions) {
                val txIdStr = tx.id.toString()
                if (alertedSet.contains(txIdStr)) continue

                // Check using FraudDetectionEngine
                val evaluation = FraudDetectionEngine.evaluateTransaction(
                    amount = tx.amount,
                    recipient = tx.recipient,
                    upiOrHandle = tx.upiOrHandle.ifBlank { tx.recipient },
                    currentBalance = 5000.0,
                    isNewBeneficiary = true,
                    note = tx.note.ifBlank { tx.title }
                )

                val isSuspicious = evaluation.level == RiskLevel.HIGH ||
                        evaluation.level == RiskLevel.CRITICAL ||
                        tx.riskScore >= 40 ||
                        tx.amount >= 2500.0 ||
                        (evaluation.flags.isNotEmpty() && evaluation.flags.first() != "No suspicious behavioral patterns detected")

                if (isSuspicious) {
                    val alertTitle = "🚨 Suspicious Activity Detected"
                    val alertMessage = "Transaction of $${"%,.2f".format(tx.amount)} to '${tx.recipient}' flagged: ${
                        evaluation.flags.firstOrNull() ?: tx.riskReason.ifBlank { "High-risk financial pattern" }
                    }"

                    // 1. Insert in-app notification into Room database
                    try {
                        bankDao.insertNotification(
                            NotificationEntity(
                                title = alertTitle,
                                message = alertMessage,
                                timestamp = System.currentTimeMillis(),
                                isRead = false,
                                type = NotificationType.FRAUD_WARNING
                            )
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error saving in-app notification to Room", e)
                    }

                    // 2. Dispatch local Android system notification
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.stat_notify_error)
                            .setContentTitle(alertTitle)
                            .setContentText(alertMessage)
                            .setStyle(NotificationCompat.BigTextStyle().bigText(alertMessage))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build()

                        try {
                            NotificationManagerCompat.from(context).notify(tx.id.toInt() + 1000, notification)
                        } catch (e: SecurityException) {
                            Log.e(TAG, "Permission denied for posting notification", e)
                        }
                    }

                    alertedSet.add(txIdStr)
                    newlyFlagged++
                }
            }

            prefs.edit().putStringSet(KEY_ALERTED_IDS, alertedSet).apply()
            return newlyFlagged
        }
    }
}
