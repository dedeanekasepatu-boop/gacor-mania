package com.example.domain.model

data class DriverSession(
    val id: Long = 0,
    val selectedPlatform: DriverPlatform = DriverPlatform.GRAB_CAR,
    val startTime: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
