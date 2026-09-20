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
import java.util.concurrent.TimeUnit

/**
 * Background WorkManager [CoroutineWorker] that periodically checks local Room [BudgetGoal]
 * progress against user transactions and delivers system notifications when approaching (>= 80%)
 * or exceeding (>= 100%) target limits.
 */
class BudgetGoalMonitoringWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "Executing BudgetGoalMonitoringWorker...")
        return try {
            val results = BudgetGoalMonitor.checkBudgetGoalsAndNotify(applicationContext)
            val alertsDispatched = results.count { it.notificationSent }
            Log.d(TAG, "BudgetGoalMonitoringWorker finished successfully. Dispatched $alertsDispatched alert notifications.")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "BudgetGoalMonitoringWorker failed", e)
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "BudgetGoalWorker"
        const val UNIQUE_PERIODIC_WORK_NAME = "budget_goal_monitoring_periodic_worker"
        const val UNIQUE_ONE_TIME_WORK_NAME = "budget_goal_monitoring_onetime_worker"

        /**
         * Enqueues a periodic background check (default every 6 hours).
         */
        fun schedulePeriodic(context: Context, intervalHours: Long = 6) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(true)
                .build()

            val periodicRequest = PeriodicWorkRequestBuilder<BudgetGoalMonitoringWorker>(
                intervalHours, TimeUnit.HOURS,
                15, TimeUnit.MINUTES // Flex interval
            )
                .setConstraints(constraints)
                .addTag("budget_goal_worker")
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
            Log.d(TAG, "Scheduled periodic budget goal check every $intervalHours hours.")
        }

        /**
         * Enqueues an immediate one-time check of budget goals against transactions.
         */
        fun runOnce(context: Context) {
            val oneTimeRequest = OneTimeWorkRequestBuilder<BudgetGoalMonitoringWorker>()
                .addTag("budget_goal_worker_immediate")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_ONE_TIME_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                oneTimeRequest
            )
            Log.d(TAG, "Enqueued immediate one-time budget goal check.")
        }

        /**
         * Cancels all scheduled budget monitoring work.
         */
        fun cancelAll(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_PERIODIC_WORK_NAME)
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_ONE_TIME_WORK_NAME)
            Log.d(TAG, "Cancelled all budget goal monitoring work.")
        }
    }
}
