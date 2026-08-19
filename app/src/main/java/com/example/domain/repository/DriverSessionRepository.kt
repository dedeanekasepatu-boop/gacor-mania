package com.example.domain.repository

import com.example.domain.model.DriverPlatform
import com.example.domain.model.DriverSession
import kotlinx.coroutines.flow.Flow

interface DriverSessionRepository {
    fun getActiveSession(): Flow<DriverSession?>
    suspend fun setDriverPlatform(platform: DriverPlatform)
}
