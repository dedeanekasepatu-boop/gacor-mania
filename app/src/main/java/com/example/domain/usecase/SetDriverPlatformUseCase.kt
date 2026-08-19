package com.example.domain.usecase

import com.example.domain.model.DriverPlatform
import com.example.domain.repository.DriverSessionRepository

class SetDriverPlatformUseCase(
    private val driverSessionRepository: DriverSessionRepository
) {
    suspend operator fun invoke(platform: DriverPlatform) {
        driverSessionRepository.setDriverPlatform(platform)
    }
}
