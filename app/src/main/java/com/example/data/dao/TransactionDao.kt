package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the [Transaction] entity.
 * Handles CRUD operations for local transaction records.
 */
@Dao
interface TransactionDao {

    /**
     * Inserts a transaction into the database, replacing on conflict.
     * @return The row ID of the inserted transaction.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    /**
     * Inserts a list of transactions into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<Transaction>): List<Long>

    /**
     * Updates an existing transaction.
     */
    @Update
    suspend fun updateTransaction(transaction: Transaction)

    /**
     * Deletes a transaction record.
     */
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    /**
     * Deletes a transaction by its primary key ID.
     */
    @Query("DELETE FROM transaction_records WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)

    /**
     * Deletes all transaction records.
     */
    @Query("DELETE FROM transaction_records")
    suspend fun deleteAllTransactions()

    /**
     * Retrieves all transaction records ordered by date descending.
     */
    @Query("SELECT * FROM transaction_records ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    /**
     * Retrieves a single transaction record by its ID.
     */
    @Query("SELECT * FROM transaction_records WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Long): Transaction?

    /**
     * Retrieves transactions filtered by type ("income" or "expense").
     */
    @Query("SELECT * FROM transaction_records WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<Transaction>>

    /**
     * Retrieves transactions filtered by category.
     */
    @Query("SELECT * FROM transaction_records WHERE category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>

    /**
     * Retrieves total sum of income transactions.
     */
    @Query("SELECT SUM(amount) FROM transaction_records WHERE type = 'income'")
    fun getTotalIncome(): Flow<Double?>

    /**
     * Retrieves total sum of expense transactions.
     */
    @Query("SELECT SUM(amount) FROM transaction_records WHERE type = 'expense'")
    fun getTotalExpense(): Flow<Double?>

    /**
     * Retrieves transactions between two timestamp ranges.
     */
    @Query("SELECT * FROM transaction_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsBetweenDates(startDate: Long, endDate: Long): Flow<List<Transaction>>

    /**
     * Retrieves expense transactions since a given date timestamp.
     */
    @Query("SELECT * FROM transaction_records WHERE date >= :sinceDate AND type = 'expense' ORDER BY date ASC")
    fun getExpenseTransactionsSince(sinceDate: Long): Flow<List<Transaction>>

    /**
     * Counts the total number of transactions recorded.
     */
    @Query("SELECT COUNT(*) FROM transaction_records")
    fun getTransactionCount(): Flow<Int>
}
