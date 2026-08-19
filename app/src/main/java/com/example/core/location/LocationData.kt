package com.example.core.location

enum class GpsSignalQuality {
    UNKNOWN,
    EXCELLENT, // < 5m
    GOOD,      // 5m - 15m
    FAIR,      // 15m - 30m
    POOR       // > 30m
}

data class LocationData(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val accuracy: Float? = null,
    val altitude: Double? = null,
    val speed: Float? = null,
    val bearing: Float? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isLocationAvailable: Boolean = false,
    val providerStatus: String = "Waiting for permission"
) {
    val signalQuality: GpsSignalQuality
        get() = when {
            accuracy == null || !isLocationAvailable -> GpsSignalQuality.UNKNOWN
            accuracy < 5.0f -> GpsSignalQuality.EXCELLENT
            accuracy <= 15.0f -> GpsSignalQuality.GOOD
            accuracy <= 30.0f -> GpsSignalQuality.FAIR
            else -> GpsSignalQuality.POOR
        }
}
