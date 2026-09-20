package com.example.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Room @Entity representing a financial budget goal.
 *
 * @property id Unique auto-generated identifier for the budget goal.
 * @property category The category of spending or budgeting (e.g., "Groceries", "Entertainment", "Savings").
 * @property targetAmount The target amount allocated for this goal.
 * @property currentProgress The current progress or amount saved/allocated toward this goal.
 * @property deadline The target deadline date (e.g., "2026-12-31" or "Dec 2026").
 */
@Entity(tableName = "budget_goals")
data class BudgetGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val targetAmount: Double,
    val currentProgress: Double = 0.0,
    val deadline: String = ""
) {
    @Ignore
    constructor(
        id: Int,
        category: String,
        targetAmount: Double,
        currentProgress: Double = 0.0,
        deadline: String = ""
    ) : this(
        id = id.toLong(),
        category = category,
        targetAmount = targetAmount,
        currentProgress = currentProgress,
        deadline = deadline
    )

    val progressPercentage: Float
        get() = if (targetAmount > 0.0) ((currentProgress / targetAmount) * 100.0).toFloat().coerceIn(0f, 100f) else 0f

    val remainingAmount: Double
        get() = (targetAmount - currentProgress).coerceAtLeast(0.0)

    val isCompleted: Boolean
        get() = currentProgress >= targetAmount && targetAmount > 0.0
}
