package com.example.core.network

data class NetworkStatus(
    val networkType: String = "No Connection",
    val isConnected: Boolean = false,
    val isMetered: Boolean = false,
    val signalStrength: String? = null,
    val latencyMs: Long? = null,
    val timestamp: Long = System.currentTimeMillis()
)
