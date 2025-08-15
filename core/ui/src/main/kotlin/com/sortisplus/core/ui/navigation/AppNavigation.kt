package com.sortisplus.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sortisplus.core.ui.screens.ComponentShowcaseScreen
import com.sortisplus.core.ui.screens.FormValidationDemoScreen
import com.sortisplus.core.ui.screens.HomeScreen
import com.sortisplus.core.ui.screens.LoginScreen
import com.sortisplus.core.ui.screens.ProfileScreen
import com.sortisplus.core.ui.screens.SettingsScreen
import com.sortisplus.core.ui.screens.ThemeToggleDemoScreen

/**
 * Main navigation composable for the application
 * Handles type-safe navigation between screens
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: AppRoute = AppRoute.Login
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable<AppRoute.Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoute.Home) {
                        // Clear login from back stack
                        popUpTo(AppRoute.Login) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable<AppRoute.Home> {
            HomeScreen(
                onNavigateToProfile = { userId ->
                    navController.navigate(AppRoute.Profile(userId))
                },
                onNavigateToSettings = {
                    navController.navigate(AppRoute.Settings)
                },
                onNavigateToDemo = { demoType ->
                    when (demoType) {
                        DemoType.COMPONENTS -> navController.navigate(AppRoute.ComponentShowcase)
                        DemoType.VALIDATION -> navController.navigate(AppRoute.FormValidationDemo)
                        DemoType.THEME -> navController.navigate(AppRoute.ThemeToggleDemo)
                    }
                }
            )
        }

        // Profile Screen
        composable<AppRoute.Profile> { backStackEntry ->
            val profile = backStackEntry.toRoute<AppRoute.Profile>()
            ProfileScreen(
                userId = profile.userId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Settings Screen
        composable<AppRoute.Settings> {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(AppRoute.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Demo Screens
        composable<AppRoute.ComponentShowcase> {
            ComponentShowcaseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<AppRoute.FormValidationDemo> {
            FormValidationDemoScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<AppRoute.ThemeToggleDemo> {
            ThemeToggleDemoScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Navigation extensions for easier usage
 */
class AppNavigator(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(AppRoute.Home) {
            popUpTo(AppRoute.Login) { inclusive = true }
        }
    }

    fun navigateToProfile(userId: String) {
        navController.navigate(AppRoute.Profile(userId))
    }

    fun navigateToSettings() {
        navController.navigate(AppRoute.Settings)
    }

    fun navigateToDemo(demoType: DemoType) {
        val route = when (demoType) {
            DemoType.COMPONENTS -> AppRoute.ComponentShowcase
            DemoType.VALIDATION -> AppRoute.FormValidationDemo
            DemoType.THEME -> AppRoute.ThemeToggleDemo
        }
        navController.navigate(route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToLogin() {
        navController.navigate(AppRoute.Login) {
            popUpTo(0) { inclusive = true }
        }
    }
    
    fun logout() {
        navController.navigate(AppRoute.Login) {
            popUpTo(0) { inclusive = true }
        }
    }
}

/**
 * Demo types for navigation
 */
enum class DemoType {
    COMPONENTS,
    VALIDATION,
    THEME
}

/**
 * Remember navigator instance
 */
@Composable
fun rememberAppNavigator(navController: NavHostController = rememberNavController()): AppNavigator {
    return remember(navController) {
        AppNavigator(navController)
    }
}
