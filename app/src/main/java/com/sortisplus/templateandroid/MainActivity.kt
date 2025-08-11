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
import com.sortisplus.data.local.LocalProviders
import com.sortisplus.feature.home.ui.CustomerMenuScreen
import com.sortisplus.feature.home.ui.DetailsScreen
import com.sortisplus.feature.home.ui.HomeScreen
import com.sortisplus.feature.home.ui.MenuScreen
import com.sortisplus.feature.home.ui.PersonCreateScreen
import com.sortisplus.feature.home.ui.PersonDeleteScreen
import com.sortisplus.feature.home.ui.PersonFindScreen
import com.sortisplus.feature.home.ui.PersonListScreen
import com.sortisplus.feature.home.ui.GreetingScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

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
    val context = androidx.compose.ui.platform.LocalContext.current
    val settings = remember { LocalProviders.settingsRepository(context) }
    val isDark by settings.darkTheme.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    AppTheme(darkTheme = isDark) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.Menu
        ) {
            // Keep original sample routes
            composable(Route.Home) {
                HomeScreen(
                    onContinue = { navController.navigate(Route.Details) }
                )
            }
            composable(Route.Details) {
                DetailsScreen(onBack = { navController.popBackStack() })
            }

            // New menu and flows
            composable(Route.Menu) {
                MenuScreen(
                    onGreeting = { navController.navigate(Route.Greeting) },
                    onCustomer = { navController.navigate(Route.CustomerMenu) }
                )
            }
            composable(Route.Greeting) {
                GreetingScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.CustomerMenu) {
                CustomerMenuScreen(
                    onList = { navController.navigate(Route.PersonList) },
                    onCreate = { navController.navigate(Route.PersonCreate) },
                    onDelete = { navController.navigate(Route.PersonDelete) },
                    onFind = { navController.navigate(Route.PersonFind) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Route.PersonList) {
                PersonListScreen(
                    onBack = { navController.popBackStack() },
                    isDarkTheme = isDark,
                    onToggleTheme = { scope.launch { settings.setDarkTheme(!isDark) } }
                )
            }
            composable(Route.PersonCreate) {
                PersonCreateScreen(
                    onBack = { navController.popBackStack() },
                    isDarkTheme = isDark,
                    onToggleTheme = { scope.launch { settings.setDarkTheme(!isDark) } }
                )
            }
            composable(Route.PersonDelete) {
                PersonDeleteScreen(
                    onBack = { navController.popBackStack() },
                    isDarkTheme = isDark,
                    onToggleTheme = { scope.launch { settings.setDarkTheme(!isDark) } }
                )
            }
            composable(Route.PersonFind) {
                PersonFindScreen(
                    onBack = { navController.popBackStack() },
                    isDarkTheme = isDark,
                    onToggleTheme = { scope.launch { settings.setDarkTheme(!isDark) } }
                )
            }
        }
    }
}
