package com.sortisplus.core.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sortisplus.core.datastore.AppConfig
import com.sortisplus.core.datastore.AuthenticationManager
import com.sortisplus.core.datastore.AuthState
import com.sortisplus.core.datastore.ConfigurationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings Screen that manages user preferences
 * and app configuration using DataStore.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val configurationManager: ConfigurationManager,
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    /**
     * Current app configuration as StateFlow for reactive UI updates
     */
    val appConfig: StateFlow<AppConfig?> = configurationManager.appConfig
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    /**
     * Current authentication state
     */
    val authState: StateFlow<AuthState> = authenticationManager.authState as StateFlow<AuthState>
    
    val isAuthenticated: StateFlow<Boolean> = configurationManager.isUserAuthenticated
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
        
    /**
     * Current user information if authenticated
     */
    val currentUser = authenticationManager.currentUser

    /**
     * Toggles the dark theme setting
     */
    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            configurationManager.setDarkTheme(enabled)
        }
    }

    /**
     * Updates the list order preference
     */
    fun setListOrder(order: String) {
        viewModelScope.launch {
            configurationManager.setListOrder(order)
        }
    }

    /**
     * Resets all preferences to default values
     */
    fun resetToDefaults() {
        viewModelScope.launch {
            configurationManager.resetPreferencesToDefaults()
        }
    }

    /**
     * Performs emergency reset of all data
     */
    fun emergencyReset() {
        viewModelScope.launch {
            configurationManager.emergencyReset()
        }
    }

    /**
     * Logs out the current user
     */
    fun logout() {
        viewModelScope.launch {
            authenticationManager.logout()
        }
    }
    
    /**
     * Gets the display name for the current user
     */
    fun getUserDisplayName(): String {
        return currentUser?.name ?: "User"
    }
    
    /**
     * Gets the email for the current user
     */
    fun getUserEmail(): String {
        return currentUser?.email ?: ""
    }
}