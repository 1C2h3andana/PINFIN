package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FraudAlert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the [FraudAlert] entity.
 */
@Dao
interface FraudAlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFraudAlert(alert: FraudAlert): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFraudAlerts(alerts: List<FraudAlert>): List<Long>

    @Update
    suspend fun updateFraudAlert(alert: FraudAlert)

    @Delete
    suspend fun deleteFraudAlert(alert: FraudAlert)

    @Query("DELETE FROM fraud_alerts WHERE id = :id")
    suspend fun deleteFraudAlertById(id: Long)

    @Query("DELETE FROM fraud_alerts")
    suspend fun deleteAllFraudAlerts()

    @Query("SELECT * FROM fraud_alerts ORDER BY timestamp DESC")
    fun getAllFraudAlerts(): Flow<List<FraudAlert>>

    @Query("SELECT * FROM fraud_alerts WHERE id = :id LIMIT 1")
    suspend fun getFraudAlertById(id: Long): FraudAlert?

    @Query("SELECT * FROM fraud_alerts WHERE severityLevel = :severity ORDER BY timestamp DESC")
    fun getFraudAlertsBySeverity(severity: String): Flow<List<FraudAlert>>

    @Query("SELECT COUNT(*) FROM fraud_alerts WHERE severityLevel = 'CRITICAL' OR severityLevel = 'HIGH'")
    fun getCriticalAlertCount(): Flow<Int>
}
