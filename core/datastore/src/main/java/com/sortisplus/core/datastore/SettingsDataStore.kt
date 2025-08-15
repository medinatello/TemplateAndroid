package com.sortisplus.core.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DS_NAME = "settings"
private const val MIGRATION_KEY = "migration_completed"

private val Context.dataStore by preferencesDataStore(name = DS_NAME)

object SettingsKeys {
    val DarkTheme = booleanPreferencesKey("dark_theme")
    val ListOrder = stringPreferencesKey("list_order")
    val MigrationCompleted = booleanPreferencesKey(MIGRATION_KEY)
}

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val darkTheme: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[SettingsKeys.DarkTheme] ?: false }

    val listOrder: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[SettingsKeys.ListOrder] ?: "CREATED_DESC" }

    suspend fun setDarkTheme(value: Boolean) {
        context.dataStore.edit { it[SettingsKeys.DarkTheme] = value }
    }

    suspend fun setListOrder(value: String) {
        context.dataStore.edit { it[SettingsKeys.ListOrder] = value }
    }

    /**
     * Migra datos desde SharedPreferences a DataStore si no se ha hecho ya
     */
    suspend fun migrateFromSharedPreferences() {
        val migrationCompleted = context.dataStore.data.first()[SettingsKeys.MigrationCompleted] ?: false

        if (!migrationCompleted) {
            val sharedPrefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

            context.dataStore.edit { preferences ->
                // Migrar tema oscuro
                if (sharedPrefs.contains("theme_dark")) {
                    preferences[SettingsKeys.DarkTheme] = sharedPrefs.getBoolean("theme_dark", false)
                }

                // Migrar orden de lista
                if (sharedPrefs.contains("list_order")) {
                    preferences[SettingsKeys.ListOrder] = sharedPrefs.getString("list_order", "CREATED_DESC") ?: "CREATED_DESC"
                }

                // Marcar migración como completada
                preferences[SettingsKeys.MigrationCompleted] = true
            }

            // Limpiar SharedPreferences después de la migración
            sharedPrefs.edit().clear().apply()
        }
    }

    /**
     * Resetea todas las preferencias a sus valores por defecto
     */
    suspend fun resetToDefaults() {
        context.dataStore.edit { preferences ->
            preferences[SettingsKeys.DarkTheme] = false
            preferences[SettingsKeys.ListOrder] = "CREATED_DESC"
        }
    }
}