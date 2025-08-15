package com.sortisplus.templateandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.sortisplus.core.datastore.AuthState
import com.sortisplus.core.datastore.AuthenticationManager
import com.sortisplus.core.ui.navigation.AppNavigation
import com.sortisplus.core.ui.navigation.AppRoute
import com.sortisplus.core.ui.theme.SortisTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.Clock
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var clock: Clock
    
    @Inject
    lateinit var authenticationManager: AuthenticationManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This app uses edge-to-edge displays by default.
        enableEdgeToEdge()
        setContent {
            TemplateAndroidApp(authenticationManager)
        }
    }
}

@Composable
fun TemplateAndroidApp(authenticationManager: AuthenticationManager) {
    // For now, we'll use system default themes until we integrate with DataStore
    val isDarkTheme = isSystemInDarkTheme()
    val useDynamicColor = true
    
    // Observe authentication state to determine start destination
    val authState by authenticationManager.authState.collectAsState(initial = AuthState.Loading)
    
    // Determine initial destination based on authentication state
    val startDestination = when (authState) {
        is AuthState.Authenticated -> AppRoute.Home
        is AuthState.Loading -> AppRoute.Login // Show login while loading
        else -> AppRoute.Login
    }

    SortisTheme(
        darkTheme = isDarkTheme,
        dynamicColor = useDynamicColor
    ) {
        val navController = rememberNavController()

        AppNavigation(
            navController = navController,
            startDestination = startDestination
        )
    }
}
