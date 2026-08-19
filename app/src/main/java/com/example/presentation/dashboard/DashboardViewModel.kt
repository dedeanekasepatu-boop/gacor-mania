package com.example.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.permissions.LocationPermissionStatus
import com.example.core.util.GacorLogger
import com.example.domain.model.DriverPlatform
import com.example.domain.usecase.GetDriverSessionUseCase
import com.example.domain.usecase.GetLocationUpdatesUseCase
import com.example.domain.usecase.GetNetworkStatusUseCase
import com.example.domain.usecase.MeasureNetworkLatencyUseCase
import com.example.domain.usecase.SetDriverPlatformUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getLocationUpdatesUseCase: GetLocationUpdatesUseCase,
    private val getNetworkStatusUseCase: GetNetworkStatusUseCase,
    private val measureNetworkLatencyUseCase: MeasureNetworkLatencyUseCase,
    private val getDriverSessionUseCase: GetDriverSessionUseCase,
    private val setDriverPlatformUseCase: SetDriverPlatformUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeNetwork()
        observeDriverSession()
        checkInitialLatency()
    }

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            GacorLogger.i("Location permission granted by user")
            _uiState.update { it.copy(permissionStatus = LocationPermissionStatus.Granted) }
            startLocationUpdates()
        } else {
            GacorLogger.w("Location permission denied by user")
            _uiState.update {
                it.copy(
                    permissionStatus = LocationPermissionStatus.Denied,
                    locationData = it.locationData.copy(
                        isLocationAvailable = false,
                        providerStatus = "Permission Denied"
                    )
                )
            }
        }
    }

    fun onPlatformSelected(platform: DriverPlatform) {
        viewModelScope.launch {
            GacorLogger.i("Selecting platform: ${platform.displayName}")
            setDriverPlatformUseCase(platform)
            _uiState.update { it.copy(selectedPlatform = platform) }
        }
    }

    fun checkInitialLatency() {
        viewModelScope.launch {
            _uiState.update { it.copy(isMeasuringLatency = true) }
            val latency = measureNetworkLatencyUseCase()
            _uiState.update {
                it.copy(
                    isMeasuringLatency = false,
                    lastLatencyResultMs = latency,
                    networkStatus = it.networkStatus.copy(latencyMs = latency)
                )
            }
        }
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            getLocationUpdatesUseCase()
                .catch { e ->
                    GacorLogger.e("Location updates error", e)
                    _uiState.update {
                        it.copy(
                            locationData = it.locationData.copy(
                                isLocationAvailable = false,
                                providerStatus = "Location Error: ${e.message ?: "Unknown"}"
                            )
                        )
                    }
                }
                .collect { locationData ->
                    _uiState.update { it.copy(locationData = locationData) }
                }
        }
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            getNetworkStatusUseCase()
                .catch { e ->
                    GacorLogger.e("Network status error", e)
                }
                .collect { networkStatus ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            networkStatus = networkStatus.copy(
                                latencyMs = currentState.lastLatencyResultMs ?: networkStatus.latencyMs
                            )
                        )
                    }
                }
        }
    }

    private fun observeDriverSession() {
        viewModelScope.launch {
            getDriverSessionUseCase().collect { session ->
                if (session != null) {
                    _uiState.update { it.copy(selectedPlatform = session.selectedPlatform) }
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            getLocationUpdatesUseCase: GetLocationUpdatesUseCase,
            getNetworkStatusUseCase: GetNetworkStatusUseCase,
            measureNetworkLatencyUseCase: MeasureNetworkLatencyUseCase,
            getDriverSessionUseCase: GetDriverSessionUseCase,
            setDriverPlatformUseCase: SetDriverPlatformUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DashboardViewModel(
                    getLocationUpdatesUseCase,
                    getNetworkStatusUseCase,
                    measureNetworkLatencyUseCase,
                    getDriverSessionUseCase,
                    setDriverPlatformUseCase
                ) as T
            }
        }
    }
}
