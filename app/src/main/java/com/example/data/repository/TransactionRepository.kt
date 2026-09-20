package com.example.data.repository

import com.example.data.dao.BudgetGoalDao
import com.example.data.dao.RecurringTransactionDao
import com.example.data.dao.TransactionDao
import com.example.data.model.BudgetGoal
import com.example.data.model.RecurringTransactionEntity
import com.example.data.model.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

/**
 * Repository class that abstracts Room database operations for Transactions, Budget Goals,
 * and Recurring Subscriptions, providing a clean, thread-safe, and reactive API for ViewModels
 * to interact with local financial data.
 *
 * @param transactionDao The Room DAO for transaction records.
 * @param budgetGoalDao The Room DAO for budget goals.
 * @param recurringTransactionDao The Room DAO for scheduled recurring subscriptions.
 * @param ioDispatcher The coroutine dispatcher used for background operations (defaults to Dispatchers.IO).
 */
class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val budgetGoalDao: BudgetGoalDao? = null,
    private val recurringTransactionDao: RecurringTransactionDao? = null,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    // ==========================================
    // Reactive Transaction Streams (Room Flow)
    // ==========================================

    /**
     * Observable stream of all transaction records ordered chronologically (newest first).
     */
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    /**
     * Observable stream of total aggregated income.
     */
    val totalIncome: Flow<Double?> = transactionDao.getTotalIncome()

    /**
     * Observable stream of total aggregated expenses.
     */
    val totalExpense: Flow<Double?> = transactionDao.getTotalExpense()

    /**
     * Observable stream of the total count of transactions.
     */
    val transactionCount: Flow<Int> = transactionDao.getTransactionCount()

    // ==========================================
    // Transaction Query Operations
    // ==========================================

    /**
     * Retrieves transactions filtered by their type (e.g., "income" or "expense").
     */
    fun getTransactionsByType(type: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(type)
    }

    /**
     * Retrieves transactions filtered by category name.
     */
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(category)
    }

    /**
     * Retrieves transactions filtered within a specific date/timestamp range.
     */
    fun getTransactionsBetweenDates(startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsBetweenDates(startDate, endDate)
    }

    /**
     * Fetches a specific transaction by its unique ID.
     */
    suspend fun getTransactionById(id: Long): Transaction? = withContext(ioDispatcher) {
        transactionDao.getTransactionById(id)
    }

    // ==========================================
    // Transaction Write / Mutation Operations
    // ==========================================

    /**
     * Inserts or updates a single transaction.
     * @return The auto-generated row ID.
     */
    suspend fun insertTransaction(transaction: Transaction): Long = withContext(ioDispatcher) {
        transactionDao.insertTransaction(transaction)
    }

    /**
     * Inserts a list of transactions in a batch.
     */
    suspend fun insertTransactions(transactions: List<Transaction>): List<Long> = withContext(ioDispatcher) {
        transactionDao.insertTransactions(transactions)
    }

    /**
     * Updates an existing transaction record.
     */
    suspend fun updateTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.updateTransaction(transaction)
    }

    /**
     * Deletes a specific transaction record.
     */
    suspend fun deleteTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.deleteTransaction(transaction)
    }

    /**
     * Deletes a transaction record by its unique ID.
     */
    suspend fun deleteTransactionById(id: Long) = withContext(ioDispatcher) {
        transactionDao.deleteTransactionById(id)
    }

    /**
     * Deletes all transaction records.
     */
    suspend fun deleteAllTransactions() = withContext(ioDispatcher) {
        transactionDao.deleteAllTransactions()
    }

    // ==========================================
    // Reactive Budget Goal Streams & Operations
    // ==========================================

    /**
     * Observable stream of all user budget goals.
     */
    val allBudgetGoals: Flow<List<BudgetGoal>> =
        budgetGoalDao?.getAllBudgetGoals() ?: kotlinx.coroutines.flow.flowOf(emptyList())

    /**
     * Observes a budget goal for a specific category.
     */
    fun getBudgetGoalByCategory(category: String): Flow<BudgetGoal?> {
        return budgetGoalDao?.getBudgetGoalByCategory(category)
            ?: kotlinx.coroutines.flow.flowOf(null)
    }

    /**
     * Fetches a budget goal by ID.
     */
    suspend fun getBudgetGoalById(id: Long): BudgetGoal? = withContext(ioDispatcher) {
        budgetGoalDao?.getBudgetGoalById(id)
    }

    /**
     * Inserts or replaces a budget goal.
     */
    suspend fun insertBudgetGoal(budgetGoal: BudgetGoal): Long = withContext(ioDispatcher) {
        budgetGoalDao?.insertBudgetGoal(budgetGoal) ?: -1L
    }

    /**
     * Inserts a list of budget goals.
     */
    suspend fun insertBudgetGoals(budgetGoals: List<BudgetGoal>): List<Long> = withContext(ioDispatcher) {
        budgetGoalDao?.insertBudgetGoals(budgetGoals) ?: emptyList()
    }

    /**
     * Updates an existing budget goal.
     */
    suspend fun updateBudgetGoal(budgetGoal: BudgetGoal) = withContext(ioDispatcher) {
        budgetGoalDao?.updateBudgetGoal(budgetGoal)
    }

    /**
     * Deletes a budget goal.
     */
    suspend fun deleteBudgetGoal(budgetGoal: BudgetGoal) = withContext(ioDispatcher) {
        budgetGoalDao?.deleteBudgetGoal(budgetGoal)
    }

    /**
     * Deletes a budget goal by ID.
     */
    suspend fun deleteBudgetGoalById(id: Long) = withContext(ioDispatcher) {
        budgetGoalDao?.deleteBudgetGoalById(id)
    }

    /**
     * Deletes all budget goals.
     */
    suspend fun deleteAllBudgetGoals() = withContext(ioDispatcher) {
        budgetGoalDao?.deleteAllBudgetGoals()
    }

    /**
     * Increments progress towards a budget goal by a given monetary amount.
     */
    suspend fun addBudgetProgress(id: Long, amount: Double) = withContext(ioDispatcher) {
        budgetGoalDao?.addProgress(id, amount)
    }

    /**
     * Updates or increments progress towards a budget goal by a given monetary amount.
     */
    suspend fun updateBudgetProgress(id: Long, amount: Double) = addBudgetProgress(id, amount)

    // ==========================================
    // Reactive Recurring Subscriptions Streams
    // ==========================================

    /**
     * Observable stream of all scheduled recurring transactions/subscriptions.
     */
    val allRecurringTransactions: Flow<List<RecurringTransactionEntity>> =
        recurringTransactionDao?.getAllRecurringTransactions() ?: emptyFlow()

    /**
     * Observable stream of all active recurring transactions.
     */
    val activeRecurringTransactions: Flow<List<RecurringTransactionEntity>> =
        recurringTransactionDao?.getActiveRecurringTransactions() ?: emptyFlow()

    suspend fun getRecurringTransactionById(id: Long): RecurringTransactionEntity? = withContext(ioDispatcher) {
        recurringTransactionDao?.getRecurringTransactionById(id)
    }

    suspend fun insertRecurringTransaction(transaction: RecurringTransactionEntity): Long = withContext(ioDispatcher) {
        recurringTransactionDao?.insertRecurringTransaction(transaction) ?: -1L
    }

    suspend fun insertRecurringTransactions(transactions: List<RecurringTransactionEntity>): List<Long> = withContext(ioDispatcher) {
        recurringTransactionDao?.insertRecurringTransactions(transactions) ?: emptyList()
    }

    suspend fun updateRecurringTransaction(transaction: RecurringTransactionEntity) = withContext(ioDispatcher) {
        recurringTransactionDao?.updateRecurringTransaction(transaction)
    }

    suspend fun setRecurringTransactionActive(id: Long, isActive: Boolean) = withContext(ioDispatcher) {
        recurringTransactionDao?.setRecurringTransactionActive(id, isActive)
    }

    suspend fun deleteRecurringTransactionById(id: Long) = withContext(ioDispatcher) {
        recurringTransactionDao?.deleteRecurringTransactionById(id)
    }

    suspend fun deleteAllRecurringTransactions() = withContext(ioDispatcher) {
        recurringTransactionDao?.deleteAllRecurringTransactions()
    }
}
