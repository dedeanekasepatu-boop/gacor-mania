package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.LocationSampleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location_samples ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLocationSamples(): Flow<List<LocationSampleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationSample(sample: LocationSampleEntity): Long

    @Query("DELETE FROM location_samples WHERE timestamp < :cutoffTimestamp")
    suspend fun pruneOldSamples(cutoffTimestamp: Long)
}
