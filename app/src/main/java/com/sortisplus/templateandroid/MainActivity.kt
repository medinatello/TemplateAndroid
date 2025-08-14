package com.sortisplus.templateandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
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
    // For now, we'll use system default themes until we integrate with DataStore
    val isDarkTheme = isSystemInDarkTheme()
    val useDynamicColor = true

    SortisTheme(
        darkTheme = isDarkTheme,
        dynamicColor = useDynamicColor
    ) {
        val navController = rememberNavController()

        AppNavigation(
            navController = navController,
            startDestination = AppRoute.Login
        )
    }
}
