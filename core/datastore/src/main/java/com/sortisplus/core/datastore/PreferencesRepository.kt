package com.sortisplus.core.datastore

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio para gestionar las preferencias del usuario de forma reactiva.
 * Actúa como wrapper sobre SettingsDataStore para proporcionar una interfaz limpia.
 */
@Singleton
class PreferencesRepository @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) {

    /**
     * Flow reactivo para observar el estado del tema oscuro
     */
    val isDarkTheme: Flow<Boolean> = settingsDataStore.darkTheme

    /**
     * Flow reactivo para observar el orden de la lista
     */
    val listOrder: Flow<String> = settingsDataStore.listOrder

    /**
     * Establece si el tema oscuro está activado
     */
    suspend fun setDarkTheme(isDark: Boolean) {
        settingsDataStore.setDarkTheme(isDark)
    }

    /**
     * Establece el orden de la lista
     */
    suspend fun setListOrder(order: String) {
        settingsDataStore.setListOrder(order)
    }

    /**
     * Inicializa el repositorio realizando migraciones necesarias
     */
    suspend fun initialize() {
        settingsDataStore.migrateFromSharedPreferences()
    }

    /**
     * Resetea todas las preferencias a sus valores por defecto
     */
    suspend fun resetToDefaults() {
        settingsDataStore.resetToDefaults()
    }
}
