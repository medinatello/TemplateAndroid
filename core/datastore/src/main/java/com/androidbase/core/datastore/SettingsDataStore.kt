package com.androidbase.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DS_NAME = "settings"

private val Context.dataStore by preferencesDataStore(name = DS_NAME)

object SettingsKeys {
    val DarkTheme = booleanPreferencesKey("dark_theme")
    val ListOrder = stringPreferencesKey("list_order")
}

class SettingsDataStore(private val context: Context) {

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
}