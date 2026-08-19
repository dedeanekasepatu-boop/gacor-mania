package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core.common.AppContainer
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.dashboard.DashboardViewModel

@Composable
fun GacorNavGraph(
    navController: NavHostController,
    container: AppContainer,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            val viewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModel.provideFactory(
                    getLocationUpdatesUseCase = container.getLocationUpdatesUseCase,
                    getNetworkStatusUseCase = container.getNetworkStatusUseCase,
                    measureNetworkLatencyUseCase = container.measureNetworkLatencyUseCase,
                    getDriverSessionUseCase = container.getDriverSessionUseCase,
                    setDriverPlatformUseCase = container.setDriverPlatformUseCase
                )
            )
            DashboardScreen(viewModel = viewModel)
        }
    }
}
