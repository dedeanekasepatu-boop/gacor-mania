package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_samples")
data class NetworkSampleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val networkType: String,
    val isConnected: Boolean,
    val isMetered: Boolean,
    val latencyMs: Long?,
    val timestamp: Long
)
