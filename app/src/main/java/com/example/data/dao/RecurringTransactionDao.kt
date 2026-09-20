package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Room table `recurring_transactions`.
 */
@Dao
interface RecurringTransactionDao {

    @Query("SELECT * FROM recurring_transactions ORDER BY nextDueTimestamp ASC")
    fun getAllRecurringTransactions(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE isActive = 1 ORDER BY nextDueTimestamp ASC")
    fun getActiveRecurringTransactions(): Flow<List<RecurringTransactionEntity>>

    @Query("SELECT * FROM recurring_transactions WHERE id = :id LIMIT 1")
    suspend fun getRecurringTransactionById(id: Long): RecurringTransactionEntity?

    @Query("SELECT * FROM recurring_transactions WHERE isActive = 1 AND nextDueTimestamp <= :currentTimestamp ORDER BY nextDueTimestamp ASC")
    suspend fun getDueRecurringTransactions(currentTimestamp: Long): List<RecurringTransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurringTransaction(transaction: RecurringTransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurringTransactions(transactions: List<RecurringTransactionEntity>): List<Long>

    @Update
    suspend fun updateRecurringTransaction(transaction: RecurringTransactionEntity)

    @Query("UPDATE recurring_transactions SET isActive = :isActive WHERE id = :id")
    suspend fun setRecurringTransactionActive(id: Long, isActive: Boolean)

    @Query("""
        UPDATE recurring_transactions 
        SET lastExecutedTimestamp = :executedAt, 
            nextDueTimestamp = :nextDue, 
            executionCount = executionCount + 1 
        WHERE id = :id
    """)
    suspend fun markExecuted(id: Long, executedAt: Long, nextDue: Long)

    @Query("DELETE FROM recurring_transactions WHERE id = :id")
    suspend fun deleteRecurringTransactionById(id: Long)

    @Delete
    suspend fun deleteRecurringTransaction(transaction: RecurringTransactionEntity)

    @Query("DELETE FROM recurring_transactions")
    suspend fun deleteAllRecurringTransactions()
}
