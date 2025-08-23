package com.sortisplus.core.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using Kotlin serialization
 * Each route is a sealed class that can contain parameters
 */
sealed interface NavigationRoute

@Serializable
sealed class AppRoute : NavigationRoute {

    /**
     * Login screen route
     */
    @Serializable
    data object Login : AppRoute()

    /**
     * Home screen route
     */
    @Serializable
    data object Home : AppRoute()

    /**
     * Profile screen route with user ID parameter
     */
    @Serializable
    data class Profile(val userId: String) : AppRoute()

    /**
     * Settings screen route
     */
    @Serializable
    data object Settings : AppRoute()

    /**
     * Demo screens for showcasing components
     */
    @Serializable
    data object ComponentShowcase : AppRoute()

    @Serializable
    data object FormValidationDemo : AppRoute()

    @Serializable
    data object ThemeToggleDemo : AppRoute()
}

/**
 * Helper object for common navigation operations
 */
object NavigationHelper {

    /**
     * Creates a deep link string for the given route
     */
    fun createDeepLink(route: AppRoute): String {
        return when (route) {
            is AppRoute.Login -> "sortis://login"
            is AppRoute.Home -> "sortis://home"
            is AppRoute.Profile -> "sortis://profile/${route.userId}"
            is AppRoute.Settings -> "sortis://settings"
            is AppRoute.ComponentShowcase -> "sortis://demo/components"
            is AppRoute.FormValidationDemo -> "sortis://demo/validation"
            is AppRoute.ThemeToggleDemo -> "sortis://demo/theme"
        }
    }

    /**
     * Determines if authentication is required for the given route
     */
    fun requiresAuth(route: AppRoute): Boolean {
        return when (route) {
            is AppRoute.Login -> false
            is AppRoute.Home -> true
            is AppRoute.Profile -> true
            is AppRoute.Settings -> true
            is AppRoute.ComponentShowcase -> false
            is AppRoute.FormValidationDemo -> false
            is AppRoute.ThemeToggleDemo -> false
        }
    }
}
