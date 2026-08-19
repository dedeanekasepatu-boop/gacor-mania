package com.example.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.example.core.common.Constants
import com.example.core.util.GacorLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

class ConnectivityManagerNetworkMonitor(
    private val context: Context
) : NetworkMonitor {

    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    override val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val cm = connectivityManager
        if (cm == null) {
            trySend(NetworkStatus(networkType = "Unavailable", isConnected = false))
            close()
            return@callbackFlow
        }

        fun emitCurrentStatus(latency: Long? = null) {
            val activeNetwork = cm.activeNetwork
            val capabilities = cm.getNetworkCapabilities(activeNetwork)
            val isConnected = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            
            val type = when {
                capabilities == null -> "No Connection"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular (Mobile Data)"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN Active"
                else -> "Connected"
            }

            val isMetered = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) == false

            val signalCategory: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && capabilities != null) {
                val signal = capabilities.signalStrength
                if (signal != NetworkCapabilities.SIGNAL_STRENGTH_UNSPECIFIED) {
                    when {
                        signal > -65 -> "Excellent ($signal dBm)"
                        signal > -75 -> "Good ($signal dBm)"
                        signal > -85 -> "Fair ($signal dBm)"
                        else -> "Poor ($signal dBm)"
                    }
                } else {
                    if (isConnected) "Good (Active)" else "No Signal"
                }
            } else {
                if (isConnected) "Active" else "Disconnected"
            }

            val status = NetworkStatus(
                networkType = type,
                isConnected = isConnected,
                isMetered = isMetered,
                signalStrength = signalCategory,
                latencyMs = latency,
                timestamp = System.currentTimeMillis()
            )

            GacorLogger.d("Network status updated: type=$type, connected=$isConnected, signal=$signalCategory")
            trySend(status)
        }

        // Initial emission
        emitCurrentStatus()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                GacorLogger.i("Network available: $network")
                emitCurrentStatus()
            }

            override fun onLost(network: Network) {
                GacorLogger.w("Network lost: $network")
                emitCurrentStatus()
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                emitCurrentStatus()
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            cm.registerNetworkCallback(request, callback)
        } catch (e: Exception) {
            GacorLogger.e("Failed to register NetworkCallback", e)
        }

        awaitClose {
            try {
                cm.unregisterNetworkCallback(callback)
            } catch (e: Exception) {
                GacorLogger.e("Error unregistering NetworkCallback", e)
            }
        }
    }

    override suspend fun measureLatency(): Long? = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress("8.8.8.8", 53), Constants.NETWORK_PING_TIMEOUT_MS)
                val duration = System.currentTimeMillis() - startTime
                GacorLogger.d("Diagnostic ping latency measured: ${duration}ms")
                duration
            }
        } catch (e: Exception) {
            GacorLogger.w("Latency measurement error: ${e.message}")
            null
        }
    }
}
