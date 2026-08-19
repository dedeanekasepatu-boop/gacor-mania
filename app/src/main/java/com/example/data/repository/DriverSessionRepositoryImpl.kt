package com.example.data.repository

import com.example.core.util.GacorLogger
import com.example.data.local.dao.DriverSessionDao
import com.example.data.local.entity.DriverSessionEntity
import com.example.domain.model.DriverPlatform
import com.example.domain.model.DriverSession
import com.example.domain.repository.DriverSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DriverSessionRepositoryImpl(
    private val driverSessionDao: DriverSessionDao
) : DriverSessionRepository {

    override fun getActiveSession(): Flow<DriverSession?> {
        return driverSessionDao.getActiveSession().map { entity ->
            entity?.let {
                val platform = try {
                    DriverPlatform.valueOf(it.selectedPlatform)
                } catch (e: Exception) {
                    DriverPlatform.GRAB_CAR
                }
                DriverSession(
                    id = it.id,
                    selectedPlatform = platform,
                    startTime = it.startTime,
                    isActive = it.isActive
                )
            }
        }
    }

    override suspend fun setDriverPlatform(platform: DriverPlatform) {
        try {
            driverSessionDao.deactivateAllSessions()
            val entity = DriverSessionEntity(
                selectedPlatform = platform.name,
                startTime = System.currentTimeMillis(),
                isActive = true
            )
            driverSessionDao.insertSession(entity)
            GacorLogger.i("Driver platform switched to: ${platform.displayName}")
        } catch (e: Exception) {
            GacorLogger.e("Failed to set driver platform", e)
        }
    }
}
