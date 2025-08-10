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
import com.sortisplus.feature.home.ClienteMenuScreen
import com.sortisplus.feature.home.DetailsScreen
import com.sortisplus.feature.home.HomeScreen
import com.sortisplus.feature.home.MenuScreen
import com.sortisplus.feature.home.PersonaCreateScreen
import com.sortisplus.feature.home.PersonaDeleteScreen
import com.sortisplus.feature.home.PersonaFindScreen
import com.sortisplus.feature.home.PersonaListScreen
import com.sortisplus.feature.home.SaludoScreen

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
                    onSaludo = { navController.navigate(Route.Saludo) },
                    onCliente = { navController.navigate(Route.ClienteMenu) }
                )
            }
            composable(Route.Saludo) {
                SaludoScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.ClienteMenu) {
                ClienteMenuScreen(
                    onLista = { navController.navigate(Route.PersonaList) },
                    onCrear = { navController.navigate(Route.PersonaCreate) },
                    onEliminar = { navController.navigate(Route.PersonaDelete) },
                    onBuscar = { navController.navigate(Route.PersonaFind) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Route.PersonaList) {
                PersonaListScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.PersonaCreate) {
                PersonaCreateScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.PersonaDelete) {
                PersonaDeleteScreen(onBack = { navController.popBackStack() })
            }
            composable(Route.PersonaFind) {
                PersonaFindScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}