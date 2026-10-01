package com.example.service

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

/**
 * Periodic WorkManager worker that inspects the last CSV database backup timestamp.
 * If 30+ days have passed without a backup, issues a non-intrusive reminder.
 */
class BackupReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "Executing BackupReminderWorker check...")
        return try {
            val notified = BackupReminderManager.checkAndNotifyIfDue(applicationContext, force = false)
            Log.d(TAG, "BackupReminderWorker finished. Notification sent: $notified")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "BackupReminderWorker encountered error", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "BackupReminderWorker"
        const val PERIODIC_WORK_NAME = "PeriodicTransactionBackupReminderWork"

        /**
         * Schedules periodic daily check via WorkManager.
         */
        fun schedulePeriodic(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .build()

            val periodicRequest = PeriodicWorkRequestBuilder<BackupReminderWorker>(
                1, TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
            Log.d(TAG, "Scheduled PeriodicTransactionBackupReminderWork (1 day interval)")
        }

        /**
         * Triggers an immediate one-time check.
         */
        fun runOnce(context: Context) {
            val oneTimeRequest = OneTimeWorkRequestBuilder<BackupReminderWorker>().build()
            WorkManager.getInstance(context).enqueue(oneTimeRequest)
            Log.d(TAG, "Enqueued OneTime BackupReminderWorker")
        }
    }
}
