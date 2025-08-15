package com.sortisplus.core.datastore.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sortisplus.core.datastore.AppConfig
import com.sortisplus.core.datastore.ConfigurationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Ejemplo de ViewModel que muestra cómo usar el sistema de configuración y almacenamiento
 * implementado en el MVP-03.
 */
@HiltViewModel
class ExampleConfigurationViewModel @Inject constructor(
    private val configurationManager: ConfigurationManager
) : ViewModel() {

    // Estado de la configuración de la app
    val appConfig: StateFlow<AppConfig?> = configurationManager.appConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Estado de autenticación
    val isAuthenticated: StateFlow<Boolean> = configurationManager.isUserAuthenticated
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Ejemplo: Cambiar tema oscuro
     */
    fun toggleDarkTheme() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentConfig = appConfig.value
                val newThemeState = !(currentConfig?.isDarkTheme ?: false)
                configurationManager.setDarkTheme(newThemeState)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Ejemplo: Cambiar orden de lista
     */
    fun setListOrder(order: String) {
        viewModelScope.launch {
            configurationManager.setListOrder(order)
        }
    }

    /**
     * Ejemplo: Simular login
     */
    fun login(username: String, password: String, authToken: String, refreshToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Guardar credenciales de forma segura
                configurationManager.saveUserCredentials(username, password)

                // Guardar tokens de autenticación
                configurationManager.saveAuthTokens(authToken, refreshToken)

                // En una app real, aquí harías la llamada al servidor
                // networkRepository.login(username, password)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Ejemplo: Logout
     */
    fun logout() {
        viewModelScope.launch {
            configurationManager.logout()
        }
    }

    /**
     * Ejemplo: Reset completo de configuración
     */
    fun resetConfiguration() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                configurationManager.emergencyReset()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Ejemplo: Obtener token para hacer llamadas API
     */
    fun getAuthTokenForApiCall(): String? {
        return configurationManager.getAuthToken()
    }
}
