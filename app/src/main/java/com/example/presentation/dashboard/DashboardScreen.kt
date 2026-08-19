package com.example.presentation.dashboard

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.location.GpsSignalQuality
import com.example.core.permissions.LocationPermissionStatus
import com.example.core.util.Formatters
import com.example.presentation.components.MetricItem
import com.example.presentation.components.PermissionCard
import com.example.presentation.components.PlatformSelector
import com.example.presentation.components.StatusCard
import com.example.presentation.theme.GacorAmber
import com.example.presentation.theme.GacorCyan
import com.example.presentation.theme.GacorEmerald
import com.example.presentation.theme.GacorRose
import com.example.presentation.theme.Slate700
import com.example.presentation.theme.Slate800
import com.example.presentation.theme.Slate850
import com.example.presentation.theme.Slate950

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Permission launcher for fine and coarse location
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        viewModel.onPermissionResult(fineGranted || coarseGranted)
    }

    // Check initial permission on startup
    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            viewModel.onPermissionResult(true)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header: GACOR DRIVER AI
                GacorHeader(
                    onRefreshDiagnostics = { viewModel.checkInitialLatency() }
                )

                // Permission Banner (if waiting or denied)
                if (uiState.permissionStatus !is LocationPermissionStatus.Granted) {
                    PermissionCard(
                        onRequestPermission = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    )
                }

                // 1. GPS STATUS Card
                GpsStatusSection(uiState = uiState)

                // 2. NETWORK STATUS Card
                NetworkStatusSection(
                    uiState = uiState,
                    onRefreshLatency = { viewModel.checkInitialLatency() }
                )

                // 3. DRIVER MODE Card
                DriverModeSection(
                    uiState = uiState,
                    onPlatformSelected = { viewModel.onPlatformSelected(it) }
                )

                // 4. DEMAND INTELLIGENCE Card
                DemandIntelligenceSection()

                // 5. APP STATUS Card
                AppStatusSection(appVersion = uiState.appVersion)

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun GacorHeader(
    onRefreshDiagnostics: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("gacor_header"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = "Gacor Driver Logo",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column {
                Text(
                    text = "GACOR DRIVER AI",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Driver Cockpit Assistant",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        IconButton(
            onClick = onRefreshDiagnostics,
            modifier = Modifier.testTag("refresh_diagnostics_button")
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh Diagnostics",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun GpsStatusSection(
    uiState: DashboardUiState,
    modifier: Modifier = Modifier
) {
    val location = uiState.locationData

    val statusBadgeColor = when (location.signalQuality) {
        GpsSignalQuality.EXCELLENT, GpsSignalQuality.GOOD -> GacorEmerald
        GpsSignalQuality.FAIR -> GacorAmber
        GpsSignalQuality.POOR -> GacorRose
        GpsSignalQuality.UNKNOWN -> Slate700
    }

    StatusCard(
        title = "GPS STATUS",
        icon = Icons.Default.MyLocation,
        iconTint = GacorEmerald,
        modifier = modifier,
        testTag = "gps_status_card",
        trailingBadge = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusBadgeColor)
                )
                Text(
                    text = if (location.isLocationAvailable) "ONLINE" else "STANDBY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = statusBadgeColor
                )
            }
        }
    ) {
        MetricItem(
            label = "Status:",
            value = location.providerStatus,
            valueColor = if (location.isLocationAvailable) GacorEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
            testTag = "gps_status_value"
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        MetricItem(
            label = "Accuracy:",
            value = Formatters.formatAccuracy(location.accuracy),
            testTag = "gps_accuracy_value",
            isMonospace = true
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        MetricItem(
            label = "Latitude:",
            value = Formatters.formatCoordinate(location.latitude),
            testTag = "gps_latitude_value",
            isMonospace = true
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        MetricItem(
            label = "Longitude:",
            value = Formatters.formatCoordinate(location.longitude),
            testTag = "gps_longitude_value",
            isMonospace = true
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        MetricItem(
            label = "Speed:",
            value = Formatters.formatSpeed(location.speed),
            testTag = "gps_speed_value",
            isMonospace = true
        )
    }
}

@Composable
private fun NetworkStatusSection(
    uiState: DashboardUiState,
    onRefreshLatency: () -> Unit,
    modifier: Modifier = Modifier
) {
    val network = uiState.networkStatus
    val isConnected = network.isConnected

    StatusCard(
        title = "NETWORK STATUS",
        icon = Icons.Default.CellTower,
        iconTint = GacorCyan,
        modifier = modifier,
        testTag = "network_status_card",
        trailingBadge = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isConnected) GacorCyan else GacorRose)
                )
                Text(
                    text = if (isConnected) "CONNECTED" else "OFFLINE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isConnected) GacorCyan else GacorRose
                )
            }
        }
    ) {
        MetricItem(
            label = "Network:",
            value = network.networkType,
            valueColor = if (isConnected) GacorCyan else MaterialTheme.colorScheme.onSurfaceVariant,
            testTag = "network_type_value"
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        MetricItem(
            label = "Signal:",
            value = network.signalStrength ?: "--",
            testTag = "network_signal_value"
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        MetricItem(
            label = "Latency:",
            value = if (uiState.isMeasuringLatency) "Checking..." else Formatters.formatLatency(network.latencyMs),
            valueColor = when {
                network.latencyMs == null -> MaterialTheme.colorScheme.onSurfaceVariant
                network.latencyMs < 60 -> GacorEmerald
                network.latencyMs < 120 -> GacorAmber
                else -> GacorRose
            },
            testTag = "network_latency_value",
            isMonospace = true
        )
    }
}

@Composable
private fun DriverModeSection(
    uiState: DashboardUiState,
    onPlatformSelected: (com.example.domain.model.DriverPlatform) -> Unit,
    modifier: Modifier = Modifier
) {
    StatusCard(
        title = "DRIVER MODE",
        icon = Icons.Default.DirectionsCar,
        iconTint = GacorAmber,
        modifier = modifier,
        testTag = "driver_mode_card"
    ) {
        Text(
            text = "Platform:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(10.dp))
        PlatformSelector(
            selectedPlatform = uiState.selectedPlatform,
            onPlatformSelected = onPlatformSelected
        )
    }
}

@Composable
private fun DemandIntelligenceSection(
    modifier: Modifier = Modifier
) {
    StatusCard(
        title = "DEMAND INTELLIGENCE",
        icon = Icons.Default.Psychology,
        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
        testTag = "demand_intelligence_card"
    ) {
        MetricItem(
            label = "Status:",
            value = "Coming Soon",
            valueColor = MaterialTheme.colorScheme.onSurfaceVariant,
            testTag = "demand_status_value"
        )
    }
}

@Composable
private fun AppStatusSection(
    appVersion: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_status_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Foundation Status Icon",
                    tint = GacorEmerald,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "APP STATUS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = appVersion,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
