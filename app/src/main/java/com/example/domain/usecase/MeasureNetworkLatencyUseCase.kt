package com.example.domain.usecase

import com.example.domain.repository.NetworkRepository

class MeasureNetworkLatencyUseCase(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(): Long? {
        return networkRepository.measureLatency()
    }
}
