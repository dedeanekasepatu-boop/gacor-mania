package com.example.data.repository

import com.example.core.location.LocationData
import com.example.core.location.LocationProvider
import com.example.core.util.GacorLogger
import com.example.data.local.dao.LocationDao
import com.example.data.local.entity.LocationSampleEntity
import com.example.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class LocationRepositoryImpl(
    private val locationProvider: LocationProvider,
    private val locationDao: LocationDao
) : LocationRepository {

    override fun getLocationUpdates(): Flow<LocationData> {
        return locationProvider.getLocationUpdates().onEach { locationData ->
            if (locationData.isLocationAvailable && locationData.latitude != null && locationData.longitude != null) {
                saveLocationSample(locationData)
            }
        }
    }

    override fun hasLocationPermission(): Boolean {
        return locationProvider.hasLocationPermission()
    }

    override fun isLocationServiceEnabled(): Boolean {
        return locationProvider.isLocationServiceEnabled()
    }

    override suspend fun saveLocationSample(locationData: LocationData) {
        val lat = locationData.latitude ?: return
        val lng = locationData.longitude ?: return
        try {
            val entity = LocationSampleEntity(
                latitude = lat,
                longitude = lng,
                accuracy = locationData.accuracy,
                speed = locationData.speed,
                bearing = locationData.bearing,
                timestamp = locationData.timestamp
            )
            locationDao.insertLocationSample(entity)
        } catch (e: Exception) {
            GacorLogger.w("Failed to save location sample: ${e.message}")
        }
    }
}
