package com.example.domain.usecase

import com.example.domain.model.DriverSession
import com.example.domain.repository.DriverSessionRepository
import kotlinx.coroutines.flow.Flow

class GetDriverSessionUseCase(
    private val driverSessionRepository: DriverSessionRepository
) {
    operator fun invoke(): Flow<DriverSession?> {
        return driverSessionRepository.getActiveSession()
    }
}
