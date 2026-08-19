package com.example.core.permissions

sealed class LocationPermissionStatus {
    object Granted : LocationPermissionStatus()
    object Denied : LocationPermissionStatus()
    object WaitingForPermission : LocationPermissionStatus()
    object PermanentlyDenied : LocationPermissionStatus()
}
