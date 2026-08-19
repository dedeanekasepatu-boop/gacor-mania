package com.example.core.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.core.common.Constants
import com.example.core.util.GacorLogger
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DefaultLocationProvider(
    private val context: Context,
    private val fusedClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
) : LocationProvider {

    private val locationManager: LocationManager? by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    }

    override fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocation || coarseLocation
    }

    override fun isLocationServiceEnabled(): Boolean {
        val lm = locationManager ?: return false
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun getLocationUpdates(): Flow<LocationData> = callbackFlow {
        if (!hasLocationPermission()) {
            GacorLogger.w("Location updates requested without granted permission")
            trySend(
                LocationData(
                    isLocationAvailable = false,
                    providerStatus = "Waiting for permission"
                )
            )
            close()
            return@callbackFlow
        }

        if (!isLocationServiceEnabled()) {
            GacorLogger.w("GPS / Location provider is disabled on device")
            trySend(
                LocationData(
                    isLocationAvailable = false,
                    providerStatus = "GPS Disabled in Settings"
                )
            )
        } else {
            trySend(
                LocationData(
                    isLocationAvailable = false,
                    providerStatus = "Acquiring GPS Signal..."
                )
            )
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            Constants.LOCATION_UPDATE_INTERVAL_MS
        ).apply {
            setMinUpdateIntervalMillis(Constants.LOCATION_FASTEST_INTERVAL_MS)
            setWaitForAccurateLocation(false)
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location != null) {
                    val locationData = location.toLocationData(status = "Active (Online)")
                    GacorLogger.d("Received location: lat=${location.latitude}, lng=${location.longitude}, acc=${location.accuracy}")
                    trySend(locationData)
                }
            }

            override fun onLocationAvailability(availability: com.google.android.gms.location.LocationAvailability) {
                if (!availability.isLocationAvailable) {
                    GacorLogger.w("Location is currently unavailable")
                    trySend(
                        LocationData(
                            isLocationAvailable = false,
                            providerStatus = "Searching for satellites..."
                        )
                    )
                }
            }
        }

        try {
            fusedClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            
            // Also try to query last known location immediately
            fusedClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                if (lastLoc != null) {
                    trySend(lastLoc.toLocationData(status = "Active (Cached)"))
                }
            }
        } catch (e: SecurityException) {
            GacorLogger.e("SecurityException during requestLocationUpdates", e)
            trySend(
                LocationData(
                    isLocationAvailable = false,
                    providerStatus = "Waiting for permission"
                )
            )
        } catch (e: Exception) {
            GacorLogger.e("Exception during requestLocationUpdates", e)
            trySend(
                LocationData(
                    isLocationAvailable = false,
                    providerStatus = "Location Error: ${e.localizedMessage ?: "Unknown"}"
                )
            )
        }

        awaitClose {
            GacorLogger.d("Removing location updates")
            fusedClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun Location.toLocationData(status: String): LocationData {
        return LocationData(
            latitude = this.latitude,
            longitude = this.longitude,
            accuracy = if (this.hasAccuracy()) this.accuracy else null,
            altitude = if (this.hasAltitude()) this.altitude else null,
            speed = if (this.hasSpeed()) this.speed else null,
            bearing = if (this.hasBearing()) this.bearing else null,
            timestamp = this.time,
            isLocationAvailable = true,
            providerStatus = status
        )
    }
}
