package com.example.service

/**
 * Severity level of budget spending relative to the target amount.
 */
enum class BudgetAlertLevel {
    NORMAL,
    APPROACHING, // >= 80% of target amount
    EXCEEDED     // >= 100% of target amount
}

/**
 * Result data model returned after checking a [BudgetGoal] against Room transactions.
 */
data class BudgetAlertResult(
    val goalId: Long,
    val category: String,
    val targetAmount: Double,
    val currentSpent: Double,
    val percentage: Float,
    val level: BudgetAlertLevel,
    val notificationSent: Boolean,
    val alertTitle: String,
    val alertMessage: String
)
