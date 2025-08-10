package com.sortisplus.templateandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sortisplus.core.common.Route
import com.sortisplus.core.designsystem.AppTheme
import com.sortisplus.feature.home.DetailsScreen
import com.sortisplus.feature.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This app uses edge-to-edge displays by default.
        enableEdgeToEdge()
        setContent { 
            TemplateAndroidApp()
        }
    }
}

@Composable
fun TemplateAndroidApp() {
    AppTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController, 
            startDestination = Route.Home
        ) {
            composable(Route.Home) { 
                HomeScreen(onContinue = { navController.navigate(Route.Details) })
            }
            composable(Route.Details) { 
                DetailsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}