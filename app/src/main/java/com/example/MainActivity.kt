package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.presentation.navigation.GacorNavGraph
import com.example.presentation.theme.GacorDriverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as GacorApplication).container

        setContent {
            GacorDriverTheme {
                val navController = rememberNavController()
                GacorNavGraph(
                    navController = navController,
                    container = appContainer
                )
            }
        }
    }
}
