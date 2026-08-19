package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.NetworkSampleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkDao {
    @Query("SELECT * FROM network_samples ORDER BY timestamp DESC LIMIT 50")
    fun getRecentNetworkSamples(): Flow<List<NetworkSampleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNetworkSample(sample: NetworkSampleEntity): Long

    @Query("DELETE FROM network_samples WHERE timestamp < :cutoffTimestamp")
    suspend fun pruneOldSamples(cutoffTimestamp: Long)
}
