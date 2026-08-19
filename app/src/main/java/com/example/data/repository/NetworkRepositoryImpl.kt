package com.example.data.repository

import com.example.core.network.NetworkMonitor
import com.example.core.network.NetworkStatus
import com.example.core.util.GacorLogger
import com.example.data.local.dao.NetworkDao
import com.example.data.local.entity.NetworkSampleEntity
import com.example.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class NetworkRepositoryImpl(
    private val networkMonitor: NetworkMonitor,
    private val networkDao: NetworkDao
) : NetworkRepository {

    override fun getNetworkStatus(): Flow<NetworkStatus> {
        return networkMonitor.networkStatus.onEach { status ->
            if (status.isConnected) {
                saveNetworkSample(status)
            }
        }
    }

    override suspend fun measureLatency(): Long? {
        val latency = networkMonitor.measureLatency()
        return latency
    }

    override suspend fun saveNetworkSample(networkStatus: NetworkStatus) {
        try {
            val entity = NetworkSampleEntity(
                networkType = networkStatus.networkType,
                isConnected = networkStatus.isConnected,
                isMetered = networkStatus.isMetered,
                latencyMs = networkStatus.latencyMs,
                timestamp = networkStatus.timestamp
            )
            networkDao.insertNetworkSample(entity)
        } catch (e: Exception) {
            GacorLogger.w("Failed to save network sample: ${e.message}")
        }
    }
}
