package com.example.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Android Background [Service] that evaluates local Room [BudgetGoal] progress
 * against transactions and triggers system notifications when spending thresholds are breached.
 */
class BudgetGoalMonitorService : Service() {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "BudgetGoalMonitorService started with action: ${intent?.action}")

        val forceNotify = intent?.getBooleanExtra(EXTRA_FORCE_NOTIFY, true) ?: true

        serviceScope.launch {
            try {
                val results = BudgetGoalMonitor.checkBudgetGoalsAndNotify(
                    context = applicationContext,
                    forceNotify = forceNotify
                )
                val notifiedCount = results.count { it.notificationSent }
                Log.d(TAG, "Service evaluated ${results.size} goals. Sent $notifiedCount notifications.")
            } catch (e: Exception) {
                Log.e(TAG, "Error executing budget evaluation in Service", e)
            } finally {
                stopSelf(startId)
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Log.d(TAG, "BudgetGoalMonitorService destroyed.")
    }

    companion object {
        private const val TAG = "BudgetGoalService"
        const val ACTION_CHECK_NOW = "com.example.service.action.CHECK_BUDGET_NOW"
        const val EXTRA_FORCE_NOTIFY = "com.example.service.extra.FORCE_NOTIFY"

        /**
         * Starts [BudgetGoalMonitorService] to execute an immediate check of Room budget goals.
         */
        fun startCheck(context: Context, forceNotify: Boolean = true) {
            val intent = Intent(context, BudgetGoalMonitorService::class.java).apply {
                action = ACTION_CHECK_NOW
                putExtra(EXTRA_FORCE_NOTIFY, forceNotify)
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start BudgetGoalMonitorService", e)
            }
        }
    }
}
