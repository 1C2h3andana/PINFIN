package com.example.service

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

/**
 * Scheduled background WorkManager [CoroutineWorker] that automatically checks
 * and inserts due recurring transactions and subscriptions into the Room database.
 */
class RecurringTransactionWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "Executing RecurringTransactionWorker background task...")
        return try {
            val forceAll = inputData.getBoolean(KEY_FORCE_ALL, false)
            val summary = RecurringTransactionManager.processDueRecurringTransactions(
                context = applicationContext,
                forceAll = forceAll
            )
            Log.d(
                TAG,
                "RecurringTransactionWorker finished. Inserted ${summary.processedCount} transactions " +
                        "totalling $${summary.totalAmount} for: ${summary.processedTitles.joinToString()}"
            )
            Result.success(
                workDataOf(
                    OUTPUT_PROCESSED_COUNT to summary.processedCount,
                    OUTPUT_TOTAL_AMOUNT to summary.totalAmount
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "RecurringTransactionWorker encountered error", e)
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "RecurringTxWorker"
        const val UNIQUE_PERIODIC_WORK_NAME = "recurring_transaction_periodic_worker"
        const val UNIQUE_ONE_TIME_WORK_NAME = "recurring_transaction_onetime_worker"
        const val KEY_FORCE_ALL = "key_force_all"
        const val OUTPUT_PROCESSED_COUNT = "output_processed_count"
        const val OUTPUT_TOTAL_AMOUNT = "output_total_amount"

        /**
         * Schedules a recurring periodic WorkManager job to automatically check
         * and insert due subscriptions (defaults to every 12 hours).
         */
        fun schedulePeriodic(context: Context, intervalHours: Long = 12) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(true)
                .build()

            val periodicRequest = PeriodicWorkRequestBuilder<RecurringTransactionWorker>(
                intervalHours, TimeUnit.HOURS,
                15, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag("recurring_subscription_worker")
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
            Log.d(TAG, "Enqueued periodic WorkManager job for recurring transactions (every $intervalHours hrs).")
        }

        /**
         * Enqueues an immediate one-time WorkManager job to process due subscriptions right away.
         *
         * @param forceAll If true, processes all active subscriptions immediately (useful for testing).
         */
        fun runOnce(context: Context, forceAll: Boolean = false) {
            val data = workDataOf(KEY_FORCE_ALL to forceAll)
            val oneTimeRequest = OneTimeWorkRequestBuilder<RecurringTransactionWorker>()
                .setInputData(data)
                .addTag("recurring_subscription_immediate")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_ONE_TIME_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                oneTimeRequest
            )
            Log.d(TAG, "Enqueued immediate WorkManager execution for recurring transactions (forceAll=$forceAll).")
        }

        /**
         * Cancels all scheduled recurring transaction WorkManager jobs.
         */
        fun cancelAll(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_PERIODIC_WORK_NAME)
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_ONE_TIME_WORK_NAME)
            Log.d(TAG, "Cancelled all recurring transaction WorkManager jobs.")
        }
    }
}
