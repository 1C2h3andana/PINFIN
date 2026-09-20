package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a scheduled recurring transaction or subscription
 * (e.g. Monthly Netflix, Spotify Family, Cloud Hosting, Utility Bill, Gym Membership).
 *
 * @property id Unique auto-generated identifier.
 * @property title Name of the subscription or bill (e.g., "Netflix Premium 4K").
 * @property payee The entity or merchant receiving payment (e.g., "Netflix Inc.").
 * @property amount Recurring monetary amount in USD.
 * @property category Category string (e.g., "Entertainment", "Utilities", "Cloud", "Fitness").
 * @property frequency Recurrence periodicity ("DAILY", "WEEKLY", "MONTHLY", "YEARLY").
 * @property intervalMillis Time in milliseconds between executions (defaults to 30 days).
 * @property nextDueTimestamp Timestamp (epoch ms) when this transaction is next due for insertion.
 * @property lastExecutedTimestamp Timestamp (epoch ms) when last automatically debited/inserted.
 * @property accountId Associated account ID from which funds are debited (defaults to 1).
 * @property accountName Human-readable name of the debit account.
 * @property isActive Whether automatic execution is enabled or paused by the user.
 * @property executionCount Total number of automatic executions completed.
 * @property note User or system note.
 */
@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val payee: String,
    val amount: Double,
    val category: String,
    val frequency: String = "MONTHLY",
    val intervalMillis: Long = 30L * 24 * 60 * 60 * 1000L,
    val nextDueTimestamp: Long,
    val lastExecutedTimestamp: Long? = null,
    val accountId: Long = 1L,
    val accountName: String = "Premier Checking (...4829)",
    val isActive: Boolean = true,
    val executionCount: Int = 0,
    val note: String = "Automated subscription charge"
)
