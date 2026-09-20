package com.example.data.model

/**
 * Data model representing aggregated daily spending used for the D3.js line chart
 * and spending trend analytics over the last 30 days.
 *
 * @property date Short readable date label (e.g., "Aug 12", "Sep 01").
 * @property fullDate Full ISO date representation (e.g., "2026-08-12").
 * @property timestamp Epoch milliseconds representing the start of the day.
 * @property amount Total monetary expenditure (debit transactions) on this day.
 * @property transactionCount Number of debit transactions recorded on this day.
 */
data class DailySpendingPoint(
    val date: String,
    val fullDate: String,
    val timestamp: Long,
    val amount: Double,
    val transactionCount: Int = 0
)
