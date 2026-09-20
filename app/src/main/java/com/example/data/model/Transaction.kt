package com.example.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Room @Entity representing a user financial transaction.
 *
 * @property id Unique auto-generated identifier for the transaction.
 * @property amount The monetary value of the transaction.
 * @property category The category of the transaction (e.g., Food, Salary, Bills, Shopping).
 * @property date The date or timestamp of the transaction in milliseconds.
 * @property type The transaction type, typically "income" or "expense".
 */
@Entity(tableName = "transaction_records")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val date: Long = System.currentTimeMillis(),
    val type: String // "income" or "expense"
) {
    @Ignore
    constructor(
        id: Long = 0,
        amount: Double,
        category: String,
        dateString: String,
        type: String
    ) : this(
        id = id,
        amount = amount,
        category = category,
        date = dateString.toLongOrNull() ?: System.currentTimeMillis(),
        type = type
    )

    @Ignore
    constructor(
        id: Int,
        amount: Double,
        category: String,
        date: Long = System.currentTimeMillis(),
        type: String
    ) : this(
        id = id.toLong(),
        amount = amount,
        category = category,
        date = date,
        type = type
    )
}
