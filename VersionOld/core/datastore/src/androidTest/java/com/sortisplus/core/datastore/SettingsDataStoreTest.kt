package com.sortisplus.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.PreferencesKeys
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsDataStoreTest {

    private lateinit var context: Context
    private lateinit var settingsDataStore: SettingsDataStore

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        settingsDataStore = SettingsDataStore(context)
    }

    @After
    fun cleanup() = runTest {
        // Limpiar DataStore después de cada prueba
        context.dataStore.edit { it.clear() }
    }

    @Test
    fun testDefaultValues() = runTest {
        val darkTheme = settingsDataStore.darkTheme.first()
        val listOrder = settingsDataStore.listOrder.first()

        assertFalse("Dark theme should be false by default", darkTheme)
        assertEquals("List order should be CREATED_DESC by default", "CREATED_DESC", listOrder)
    }

    @Test
    fun testSetDarkTheme() = runTest {
        settingsDataStore.setDarkTheme(true)
        val darkTheme = settingsDataStore.darkTheme.first()
        assertTrue("Dark theme should be true after setting", darkTheme)

        settingsDataStore.setDarkTheme(false)
        val darkThemeUpdated = settingsDataStore.darkTheme.first()
        assertFalse("Dark theme should be false after updating", darkThemeUpdated)
    }

    @Test
    fun testSetListOrder() = runTest {
        val newOrder = "ALPHABETICAL_ASC"
        settingsDataStore.setListOrder(newOrder)
        val listOrder = settingsDataStore.listOrder.first()
        assertEquals("List order should match the set value", newOrder, listOrder)
    }

    @Test
    fun testMigrationFromSharedPreferences() = runTest {
        // Simular datos en SharedPreferences
        val sharedPrefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean("theme_dark", true)
            .putString("list_order", "ALPHABETICAL_DESC")
            .apply()

        // Ejecutar migración
        settingsDataStore.migrateFromSharedPreferences()

        // Verificar que los datos se migraron correctamente
        val darkTheme = settingsDataStore.darkTheme.first()
        val listOrder = settingsDataStore.listOrder.first()

        assertTrue("Dark theme should be migrated as true", darkTheme)
        assertEquals("List order should be migrated correctly", "ALPHABETICAL_DESC", listOrder)

        // Verificar que SharedPreferences se limpió
        assertFalse("SharedPreferences should be cleared after migration",
            sharedPrefs.contains("theme_dark"))
    }

    @Test
    fun testResetToDefaults() = runTest {
        // Establecer valores no predeterminados
        settingsDataStore.setDarkTheme(true)
        settingsDataStore.setListOrder("ALPHABETICAL_ASC")

        // Resetear a valores predeterminados
        settingsDataStore.resetToDefaults()

        val darkTheme = settingsDataStore.darkTheme.first()
        val listOrder = settingsDataStore.listOrder.first()

        assertFalse("Dark theme should be reset to false", darkTheme)
        assertEquals("List order should be reset to default", "CREATED_DESC", listOrder)
    }
}

private val Context.dataStore by preferencesDataStore(name = "settings")
