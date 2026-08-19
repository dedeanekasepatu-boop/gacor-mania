package com.example.presentation.dashboard

import com.example.core.location.LocationData
import com.example.core.network.NetworkStatus
import com.example.core.permissions.LocationPermissionStatus
import com.example.domain.model.DriverPlatform

data class DashboardUiState(
    val locationData: LocationData = LocationData(),
    val networkStatus: NetworkStatus = NetworkStatus(),
    val selectedPlatform: DriverPlatform = DriverPlatform.GRAB_CAR,
    val permissionStatus: LocationPermissionStatus = LocationPermissionStatus.WaitingForPermission,
    val isMeasuringLatency: Boolean = false,
    val lastLatencyResultMs: Long? = null,
    val appVersion: String = "Foundation V1.0"
)
