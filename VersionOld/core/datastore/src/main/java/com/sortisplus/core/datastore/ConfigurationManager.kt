package com.sortisplus.core.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor centralizado de configuración que combina preferencias públicas y almacenamiento seguro.
 * Proporciona una interfaz unificada para toda la configuración de la aplicación.
 */
@Singleton
class ConfigurationManager @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val secureStorage: SecureStorage
) {

    /**
     * Configuración completa de la aplicación como Flow reactivo
     */
    val appConfig: Flow<AppConfig> = combine(
        preferencesRepository.isDarkTheme,
        preferencesRepository.listOrder
    ) { isDarkTheme, listOrder ->
        AppConfig(
            isDarkTheme = isDarkTheme,
            listOrder = listOrder
        )
    }

    /**
     * Estado de autenticación del usuario
     */
    val isUserAuthenticated: Flow<Boolean> = secureStorage.isUserLoggedIn

    /**
     * Inicializa el gestor de configuración
     * Ejecuta migraciones necesarias y configuraciones iniciales
     */
    suspend fun initialize() {
        preferencesRepository.initialize()
    }

    // Métodos de preferencias públicas
    suspend fun setDarkTheme(enabled: Boolean) {
        preferencesRepository.setDarkTheme(enabled)
    }

    suspend fun setListOrder(order: String) {
        preferencesRepository.setListOrder(order)
    }

    suspend fun resetPreferencesToDefaults() {
        preferencesRepository.resetToDefaults()
    }

    // Métodos de almacenamiento seguro
    fun saveAuthTokens(authToken: String, refreshToken: String? = null) {
        secureStorage.saveAuthToken(authToken)
        refreshToken?.let { secureStorage.saveRefreshToken(it) }
    }

    fun getAuthToken(): String? = secureStorage.getAuthToken()

    fun getRefreshToken(): String? = secureStorage.getRefreshToken()

    fun saveUserCredentials(username: String, encryptedPassword: String) {
        secureStorage.saveUserCredentials(username, encryptedPassword)
    }

    fun getUserCredentials(): Pair<String?, String?> {
        return Pair(secureStorage.getUsername(), secureStorage.getEncryptedPassword())
    }

    fun logout() {
        secureStorage.logout()
    }

    fun clearAllData() {
        secureStorage.clearAll()
    }

    /**
     * Configuración de emergencia que resetea todo a valores por defecto
     */
    suspend fun emergencyReset() {
        resetPreferencesToDefaults()
        clearAllData()
    }

    /**
     * Verifica si la aplicación está configurada correctamente
     */
    suspend fun isAppConfigured(): Boolean {
        // Verificar que las migraciones se hayan completado
        // y que existan las configuraciones mínimas necesarias
        return true // Implementar lógica específica según necesidades
    }
}
