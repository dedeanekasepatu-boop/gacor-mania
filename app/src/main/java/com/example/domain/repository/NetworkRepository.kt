package com.example.domain.repository

import com.example.core.network.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getNetworkStatus(): Flow<NetworkStatus>
    suspend fun measureLatency(): Long?
    suspend fun saveNetworkSample(networkStatus: NetworkStatus)
}
