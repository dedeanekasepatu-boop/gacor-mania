package com.example.domain.usecase

import com.example.core.network.NetworkStatus
import com.example.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow

class GetNetworkStatusUseCase(
    private val networkRepository: NetworkRepository
) {
    operator fun invoke(): Flow<NetworkStatus> {
        return networkRepository.getNetworkStatus()
    }
}
