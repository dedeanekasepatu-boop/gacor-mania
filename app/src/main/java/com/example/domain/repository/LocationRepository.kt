package com.example.domain.repository

import com.example.core.location.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<LocationData>
    fun hasLocationPermission(): Boolean
    fun isLocationServiceEnabled(): Boolean
    suspend fun saveLocationSample(locationData: LocationData)
}
