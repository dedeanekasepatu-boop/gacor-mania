package com.example.core.location

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for location monitoring.
 * Strictly adheres to monitoring without mock generation, GPS spoofing, or boosting claims.
 */
interface LocationProvider {
    fun getLocationUpdates(): Flow<LocationData>
    fun isLocationServiceEnabled(): Boolean
    fun hasLocationPermission(): Boolean
}
