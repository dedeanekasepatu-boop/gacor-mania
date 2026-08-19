package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.DriverSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverSessionDao {
    @Query("SELECT * FROM driver_sessions WHERE isActive = 1 ORDER BY startTime DESC LIMIT 1")
    fun getActiveSession(): Flow<DriverSessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: DriverSessionEntity): Long

    @Update
    suspend fun updateSession(session: DriverSessionEntity)

    @Query("UPDATE driver_sessions SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateAllSessions()
}
