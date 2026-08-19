package com.example.core.common

import android.content.Context
import com.example.core.location.DefaultLocationProvider
import com.example.core.location.LocationProvider
import com.example.core.network.ConnectivityManagerNetworkMonitor
import com.example.core.network.NetworkMonitor
import com.example.data.local.AppDatabase
import com.example.data.repository.DriverSessionRepositoryImpl
import com.example.data.repository.LocationRepositoryImpl
import com.example.data.repository.NetworkRepositoryImpl
import com.example.domain.repository.DriverSessionRepository
import com.example.domain.repository.LocationRepository
import com.example.domain.repository.NetworkRepository
import com.example.domain.usecase.GetDriverSessionUseCase
import com.example.domain.usecase.GetLocationUpdatesUseCase
import com.example.domain.usecase.GetNetworkStatusUseCase
import com.example.domain.usecase.MeasureNetworkLatencyUseCase
import com.example.domain.usecase.SetDriverPlatformUseCase

interface AppContainer {
    val locationProvider: LocationProvider
    val networkMonitor: NetworkMonitor
    val locationRepository: LocationRepository
    val networkRepository: NetworkRepository
    val driverSessionRepository: DriverSessionRepository
    val getLocationUpdatesUseCase: GetLocationUpdatesUseCase
    val getNetworkStatusUseCase: GetNetworkStatusUseCase
    val measureNetworkLatencyUseCase: MeasureNetworkLatencyUseCase
    val getDriverSessionUseCase: GetDriverSessionUseCase
    val setDriverPlatformUseCase: SetDriverPlatformUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    override val locationProvider: LocationProvider by lazy {
        DefaultLocationProvider(context)
    }

    override val networkMonitor: NetworkMonitor by lazy {
        ConnectivityManagerNetworkMonitor(context)
    }

    override val locationRepository: LocationRepository by lazy {
        LocationRepositoryImpl(locationProvider, database.locationDao())
    }

    override val networkRepository: NetworkRepository by lazy {
        NetworkRepositoryImpl(networkMonitor, database.networkDao())
    }

    override val driverSessionRepository: DriverSessionRepository by lazy {
        DriverSessionRepositoryImpl(database.driverSessionDao())
    }

    override val getLocationUpdatesUseCase: GetLocationUpdatesUseCase by lazy {
        GetLocationUpdatesUseCase(locationRepository)
    }

    override val getNetworkStatusUseCase: GetNetworkStatusUseCase by lazy {
        GetNetworkStatusUseCase(networkRepository)
    }

    override val measureNetworkLatencyUseCase: MeasureNetworkLatencyUseCase by lazy {
        MeasureNetworkLatencyUseCase(networkRepository)
    }

    override val getDriverSessionUseCase: GetDriverSessionUseCase by lazy {
        GetDriverSessionUseCase(driverSessionRepository)
    }

    override val setDriverPlatformUseCase: SetDriverPlatformUseCase by lazy {
        SetDriverPlatformUseCase(driverSessionRepository)
    }
}
