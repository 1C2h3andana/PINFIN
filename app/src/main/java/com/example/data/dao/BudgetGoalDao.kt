package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BudgetGoal
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the [BudgetGoal] entity.
 * Handles CRUD operations for local budget goal records.
 */
@Dao
interface BudgetGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetGoal(budgetGoal: BudgetGoal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetGoals(budgetGoals: List<BudgetGoal>): List<Long>

    @Update
    suspend fun updateBudgetGoal(budgetGoal: BudgetGoal)

    @Delete
    suspend fun deleteBudgetGoal(budgetGoal: BudgetGoal)

    @Query("DELETE FROM budget_goals WHERE id = :id")
    suspend fun deleteBudgetGoalById(id: Long)

    @Query("DELETE FROM budget_goals")
    suspend fun deleteAllBudgetGoals()

    @Query("SELECT * FROM budget_goals ORDER BY id ASC")
    fun getAllBudgetGoals(): Flow<List<BudgetGoal>>

    @Query("SELECT * FROM budget_goals WHERE id = :id LIMIT 1")
    suspend fun getBudgetGoalById(id: Long): BudgetGoal?

    @Query("SELECT * FROM budget_goals WHERE category = :category LIMIT 1")
    fun getBudgetGoalByCategory(category: String): Flow<BudgetGoal?>

    @Query("UPDATE budget_goals SET currentProgress = currentProgress + :amount WHERE id = :id")
    suspend fun addProgress(id: Long, amount: Double)
}
