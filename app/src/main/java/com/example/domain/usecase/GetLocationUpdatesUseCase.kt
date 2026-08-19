package com.example.domain.usecase

import com.example.core.location.LocationData
import com.example.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class GetLocationUpdatesUseCase(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<LocationData> {
        return locationRepository.getLocationUpdates()
    }
}
