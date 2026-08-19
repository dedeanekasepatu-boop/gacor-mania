package com.example.core.network

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for network connectivity and quality diagnostics.
 * Strictly used for MONITORING and DIAGNOSTIC, never claiming to boost radio signal.
 */
interface NetworkMonitor {
    val networkStatus: Flow<NetworkStatus>
    suspend fun measureLatency(): Long?
}
