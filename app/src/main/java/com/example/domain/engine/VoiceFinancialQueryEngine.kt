package com.example.domain.engine

import com.example.data.model.BudgetEntity
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Parsed intent of a voice financial query.
 */
enum class VoiceQueryIntentType {
    CATEGORY_SPENDING,
    TOTAL_SPENDING,
    TOTAL_INCOME,
    ACCOUNT_BALANCE,
    TRANSACTION_LIST,
    BUDGET_CHECK
}

/**
 * Comprehensive structured result of a voice query against the Room database.
 */
data class VoiceQueryResult(
    val query: String,
    val intentType: VoiceQueryIntentType,
    val detectedCategory: TransactionCategory?,
    val timeframeLabel: String,
    val totalAmount: Double,
    val transactionCount: Int,
    val budgetLimit: Double?,
    val budgetRemaining: Double?,
    val budgetPercentage: Float?,
    val spokenAnswer: String,
    val matchingTransactions: List<TransactionEntity> = emptyList()
)

/**
 * Intelligent parser and query engine that interprets speech-to-text queries
 * and evaluates real-time data from the local Room database.
 */
object VoiceFinancialQueryEngine {

    /**
     * Evaluates a speech-to-text query against local Room transactions and budgets.
     */
    fun processQuery(
        rawQuery: String,
        allTransactions: List<TransactionEntity>,
        allBudgets: List<BudgetEntity>,
        totalAccountBalance: Double
    ): VoiceQueryResult {
        val query = rawQuery.trim()
        val queryLower = query.lowercase(Locale.getDefault())

        // 1. Determine timeframe
        val (startTime, timeframeLabel) = resolveTimeframe(queryLower)

        // 2. Determine Intent & Category
        val detectedCategory = detectCategory(queryLower)
        val isIncomeQuery = queryLower.contains("income") || queryLower.contains("earn") ||
                queryLower.contains("deposit") || queryLower.contains("salary") || queryLower.contains("made")
        val isBalanceQuery = queryLower.contains("balance") || queryLower.contains("net worth") ||
                queryLower.contains("how much do i have") || queryLower.contains("total money")

        val intentType = when {
            isBalanceQuery -> VoiceQueryIntentType.ACCOUNT_BALANCE
            isIncomeQuery -> VoiceQueryIntentType.TOTAL_INCOME
            detectedCategory != null -> VoiceQueryIntentType.CATEGORY_SPENDING
            queryLower.contains("budget") || queryLower.contains("limit") -> VoiceQueryIntentType.BUDGET_CHECK
            else -> VoiceQueryIntentType.TOTAL_SPENDING
        }

        // 3. Filter transactions within timeframe
        val timeFilteredTx = allTransactions.filter { it.timestamp >= startTime }

        return when (intentType) {
            VoiceQueryIntentType.ACCOUNT_BALANCE -> {
                VoiceQueryResult(
                    query = query,
                    intentType = intentType,
                    detectedCategory = null,
                    timeframeLabel = "Current",
                    totalAmount = totalAccountBalance,
                    transactionCount = allTransactions.size,
                    budgetLimit = null,
                    budgetRemaining = null,
                    budgetPercentage = null,
                    spokenAnswer = "Your current total balance across all active accounts is $${"%,.2f".format(totalAccountBalance)}.",
                    matchingTransactions = allTransactions.take(5)
                )
            }

            VoiceQueryIntentType.TOTAL_INCOME -> {
                val incomeTx = timeFilteredTx.filter { it.type == TransactionType.CREDIT }
                val totalIncome = incomeTx.sumOf { it.amount }
                VoiceQueryResult(
                    query = query,
                    intentType = intentType,
                    detectedCategory = null,
                    timeframeLabel = timeframeLabel,
                    totalAmount = totalIncome,
                    transactionCount = incomeTx.size,
                    budgetLimit = null,
                    budgetRemaining = null,
                    budgetPercentage = null,
                    spokenAnswer = "You received a total of $${"%,.2f".format(totalIncome)} in income $timeframeLabel across ${incomeTx.size} deposits.",
                    matchingTransactions = incomeTx
                )
            }

            VoiceQueryIntentType.CATEGORY_SPENDING -> {
                val category = detectedCategory ?: TransactionCategory.FOOD
                val catExpenses = timeFilteredTx.filter {
                    it.category == category && it.type == TransactionType.DEBIT
                }
                val totalSpent = catExpenses.sumOf { it.amount }
                val budget = allBudgets.find { it.category == category }
                val limit = budget?.monthlyLimit
                val remaining = limit?.let { it - totalSpent }
                val pct = limit?.let { if (it > 0) ((totalSpent / it) * 100.0).toFloat() else 0f }

                val catName = formatCategoryName(category)
                val budgetMsg = if (limit != null && limit > 0) {
                    val formattedLimit = "%,.0f".format(limit)
                    val formattedRem = "%,.2f".format(remaining ?: 0.0)
                    if (totalSpent > limit) {
                        " This exceeds your $$formattedLimit monthly limit by $${"%,.2f".format(totalSpent - limit)}."
                    } else {
                        " You have $$formattedRem remaining of your $$formattedLimit limit (${pct?.toInt() ?: 0}% used)."
                    }
                } else ""

                val spoken = "You spent $${"%,.2f".format(totalSpent)} on $catName $timeframeLabel across ${catExpenses.size} transactions.$budgetMsg"

                VoiceQueryResult(
                    query = query,
                    intentType = intentType,
                    detectedCategory = category,
                    timeframeLabel = timeframeLabel,
                    totalAmount = totalSpent,
                    transactionCount = catExpenses.size,
                    budgetLimit = limit,
                    budgetRemaining = remaining,
                    budgetPercentage = pct,
                    spokenAnswer = spoken,
                    matchingTransactions = catExpenses
                )
            }

            VoiceQueryIntentType.TOTAL_SPENDING, VoiceQueryIntentType.BUDGET_CHECK -> {
                val expenseTx = timeFilteredTx.filter { it.type == TransactionType.DEBIT }
                val totalSpent = expenseTx.sumOf { it.amount }
                val totalBudgetLimit = allBudgets.sumOf { it.monthlyLimit }
                val pct = if (totalBudgetLimit > 0) ((totalSpent / totalBudgetLimit) * 100.0).toFloat() else 0f

                val spoken = "You have spent a total of $${"%,.2f".format(totalSpent)} $timeframeLabel across ${expenseTx.size} debit transactions (overall budget utilization: ${pct.toInt()}%)."

                VoiceQueryResult(
                    query = query,
                    intentType = intentType,
                    detectedCategory = null,
                    timeframeLabel = timeframeLabel,
                    totalAmount = totalSpent,
                    transactionCount = expenseTx.size,
                    budgetLimit = totalBudgetLimit,
                    budgetRemaining = totalBudgetLimit - totalSpent,
                    budgetPercentage = pct,
                    spokenAnswer = spoken,
                    matchingTransactions = expenseTx
                )
            }

            else -> {
                VoiceQueryResult(
                    query = query,
                    intentType = intentType,
                    detectedCategory = null,
                    timeframeLabel = timeframeLabel,
                    totalAmount = 0.0,
                    transactionCount = 0,
                    budgetLimit = null,
                    budgetRemaining = null,
                    budgetPercentage = null,
                    spokenAnswer = "I found your financial records. Ask me specifically about categories like groceries, shopping, bills, or your balance.",
                    matchingTransactions = emptyList()
                )
            }
        }
    }

    private fun detectCategory(query: String): TransactionCategory? {
        return when {
            query.contains("grocer") || query.contains("food") || query.contains("dining") ||
                    query.contains("restaurant") || query.contains("eat") || query.contains("supermarket") ||
                    query.contains("coffee") || query.contains("starbucks") -> TransactionCategory.FOOD

            query.contains("shop") || query.contains("cloth") || query.contains("amazon") ||
                    query.contains("purchase") || query.contains("store") || query.contains("retail") -> TransactionCategory.SHOPPING

            query.contains("bill") || query.contains("utilit") || query.contains("electric") ||
                    query.contains("water") || query.contains("rent") || query.contains("phone") ||
                    query.contains("subscription") -> TransactionCategory.BILLS

            query.contains("entertain") || query.contains("movie") || query.contains("netflix") ||
                    query.contains("game") || query.contains("concert") || query.contains("spotify") -> TransactionCategory.ENTERTAINMENT

            query.contains("health") || query.contains("doctor") || query.contains("medic") ||
                    query.contains("pharmacy") || query.contains("hospital") || query.contains("dental") -> TransactionCategory.HEALTHCARE

            query.contains("educat") || query.contains("school") || query.contains("book") ||
                    query.contains("tuition") || query.contains("course") || query.contains("learn") -> TransactionCategory.EDUCATION

            query.contains("travel") || query.contains("flight") || query.contains("uber") ||
                    query.contains("taxi") || query.contains("gas") || query.contains("fuel") ||
                    query.contains("hotel") || query.contains("transport") -> TransactionCategory.OTHER

            query.contains("invest") || query.contains("stock") || query.contains("crypto") ||
                    query.contains("dividend") -> TransactionCategory.INVESTMENT

            else -> null
        }
    }

    private fun resolveTimeframe(query: String): Pair<Long, String> {
        val calendar = Calendar.getInstance()
        return when {
            query.contains("today") -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                Pair(calendar.timeInMillis, "today")
            }

            query.contains("week") -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                Pair(calendar.timeInMillis, "in the past 7 days")
            }

            query.contains("all") || query.contains("ever") -> {
                Pair(0L, "all-time")
            }

            else -> {
                // Default to this month
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
                Pair(calendar.timeInMillis, "this month ($monthName)")
            }
        }
    }

    private fun formatCategoryName(cat: TransactionCategory): String {
        return when (cat) {
            TransactionCategory.FOOD -> "Food & Groceries"
            TransactionCategory.SHOPPING -> "Shopping"
            TransactionCategory.BILLS -> "Bills & Utilities"
            TransactionCategory.ENTERTAINMENT -> "Entertainment"
            TransactionCategory.HEALTHCARE -> "Healthcare"
            TransactionCategory.EDUCATION -> "Education"
            TransactionCategory.INVESTMENT -> "Investments"
            TransactionCategory.SALARY -> "Income & Salary"
            TransactionCategory.TRANSFER -> "Transfers"
            TransactionCategory.DEPOSIT -> "Deposits"
            TransactionCategory.WITHDRAWAL -> "Withdrawals"
            TransactionCategory.OTHER -> "General Expenses"
            else -> cat.name
        }
    }
}
