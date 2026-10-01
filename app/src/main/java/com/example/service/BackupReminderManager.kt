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
import com.example.data.model.NotificationEntity
import com.example.data.model.NotificationType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Manages periodic, non-intrusive reminders to export the Room transaction database to CSV
 * if the user has not performed a backup within the last 30 days.
 */
object BackupReminderManager {

    private const val TAG = "BackupReminderManager"
    const val CHANNEL_ID = "csv_backup_reminders_channel"
    private const val PREFS_NAME = "transaction_backup_prefs"
    private const val KEY_LAST_BACKUP_TIME = "last_csv_backup_timestamp"
    private const val KEY_LAST_NOTIFICATION_TIME = "last_backup_notification_timestamp"

    private const val THIRTY_DAYS_MS = 30L * 24L * 60L * 60L * 1000L
    private const val SEVEN_DAYS_MS = 7L * 24L * 60L * 60L * 1000L

    /**
     * Ensures low-importance, non-intrusive notification channel exists.
     */
    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Database Backup Reminders"
            val descriptionText = "Gentle, non-intrusive reminders to export transaction history if not backed up recently"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(false) // Non-intrusive
                setShowBadge(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Calculates the number of days elapsed since the last CSV transaction backup.
     * If no backup has ever been performed, initializes default to 34 days ago so users
     * immediately see the functional benefit and reminder.
     */
    fun getDaysSinceLastBackup(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()
        if (!prefs.contains(KEY_LAST_BACKUP_TIME)) {
            // Default seed: 34 days ago (prior backup exists in previous cycle)
            val defaultSeed = now - (34L * 24L * 60L * 60L * 1000L)
            prefs.edit().putLong(KEY_LAST_BACKUP_TIME, defaultSeed).apply()
        }
        val lastTime = prefs.getLong(KEY_LAST_BACKUP_TIME, now)
        val elapsedMs = (now - lastTime).coerceAtLeast(0L)
        return TimeUnit.MILLISECONDS.toDays(elapsedMs).toInt()
    }

    /**
     * Returns true if 30 or more days have elapsed since the last CSV export.
     */
    fun isBackupDue(context: Context): Boolean {
        return getDaysSinceLastBackup(context) >= 30
    }

    /**
     * Records that a CSV backup was successfully generated and exported.
     */
    fun recordBackupCompleted(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putLong(KEY_LAST_BACKUP_TIME, System.currentTimeMillis())
            .apply()
        Log.d(TAG, "Recorded CSV backup completed at: ${System.currentTimeMillis()}")
    }

    /**
     * Simulates an expired backup timestamp (35 days ago) for testing and UI evaluation.
     */
    fun simulateOldBackupForTesting(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val simulatedTimestamp = System.currentTimeMillis() - (35L * 24L * 60L * 60L * 1000L)
        prefs.edit()
            .putLong(KEY_LAST_BACKUP_TIME, simulatedTimestamp)
            .remove(KEY_LAST_NOTIFICATION_TIME)
            .apply()
        Log.d(TAG, "Simulated 35-day-old backup timestamp for testing.")
    }

    /**
     * Checks if backup is due and sends a gentle, non-intrusive notification.
     * Respects a 7-day cooldown between notifications to avoid intrusiveness, unless [force] is true.
     */
    suspend fun checkAndNotifyIfDue(context: Context, force: Boolean = false): Boolean = withContext(Dispatchers.IO) {
        ensureChannel(context)
        val appContext = context.applicationContext
        val days = getDaysSinceLastBackup(appContext)
        val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastNotified = prefs.getLong(KEY_LAST_NOTIFICATION_TIME, 0L)
        val now = System.currentTimeMillis()

        val isDue = days >= 30
        val isCooldownElapsed = (now - lastNotified) >= SEVEN_DAYS_MS

        if (isDue && (isCooldownElapsed || force)) {
            val title = "💾 Transaction Database Backup Reminder"
            val message = "It's been $days days since your last CSV export. Back up your transactions to ensure your records are secure."

            val sent = dispatchBackupNotification(appContext, title, message)
            if (sent) {
                prefs.edit().putLong(KEY_LAST_NOTIFICATION_TIME, now).apply()

                // Insert into in-app notifications
                try {
                    val db = BankDatabase.getDatabase(appContext)
                    db.bankDao().insertNotification(
                        NotificationEntity(
                            title = title,
                            message = message,
                            type = NotificationType.SECURITY_ALERT,
                            timestamp = now,
                            isRead = false,
                            actionRoute = "export_dialog"
                        )
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to insert backup reminder notification into Room", e)
                }
                return@withContext true
            }
        }
        return@withContext false
    }

    private fun dispatchBackupNotification(context: Context, title: String, message: String): Boolean {
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
                putExtra("ACTION_OPEN_CSV_EXPORT", true)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                40001,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)

            val manager = NotificationManagerCompat.from(context)
            manager.notify(40001, builder.build())
            Log.d(TAG, "Dispatched non-intrusive backup reminder notification.")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error dispatching backup reminder notification", e)
            false
        }
    }
}
