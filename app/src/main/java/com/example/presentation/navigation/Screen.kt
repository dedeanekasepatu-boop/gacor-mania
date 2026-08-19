package com.example.presentation.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
}
