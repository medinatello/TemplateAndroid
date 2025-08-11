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
import com.sortisplus.feature.home.CustomerMenuScreen
import com.sortisplus.feature.home.DetailsScreen
import com.sortisplus.feature.home.HomeScreen
import com.sortisplus.feature.home.MenuScreen
import com.sortisplus.feature.home.PersonCreateScreen
import com.sortisplus.feature.home.PersonDeleteScreen
import com.sortisplus.feature.home.PersonFindScreen
import com.sortisplus.feature.home.PersonListScreen
import com.sortisplus.feature.home.GreetingScreen

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
            startDestination = Route.Menu
        ) {
            // Keep original sample routes
            composable(Route.Home) {
                HomeScreen(onContinue = { navController.navigate(Route.Details) })
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
                PersonListScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.PersonCreate) {
                PersonCreateScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.PersonDelete) {
                PersonDeleteScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.PersonFind) {
                PersonFindScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
