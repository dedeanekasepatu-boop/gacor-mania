package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_sessions")
data class DriverSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val selectedPlatform: String,
    val startTime: Long,
    val isActive: Boolean
)
