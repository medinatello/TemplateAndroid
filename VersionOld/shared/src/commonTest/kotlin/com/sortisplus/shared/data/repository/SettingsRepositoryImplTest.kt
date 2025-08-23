package com.sortisplus.shared.data.repository

import com.sortisplus.shared.platform.KeyValueStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsRepositoryImplTest {

    private val mockKeyValueStore = object : KeyValueStore {
        private val data = mutableMapOf<String, Any>()
        
        override fun getString(key: String): String? = data[key] as? String
        override fun putString(key: String, value: String) { data[key] = value }
        override fun getInt(key: String, defaultValue: Int): Int = data[key] as? Int ?: defaultValue
        override fun putInt(key: String, value: Int) { data[key] = value }
        override fun getBoolean(key: String, defaultValue: Boolean): Boolean = data[key] as? Boolean ?: defaultValue
        override fun putBoolean(key: String, value: Boolean) { data[key] = value }
        override fun remove(key: String) { data.remove(key) }
        override fun clear() { data.clear() }
    }

    private val settingsRepository = SettingsRepositoryImpl(mockKeyValueStore)

    @Test
    fun `initial dark theme should be false`() = runTest {
        val darkTheme = settingsRepository.darkTheme.first()
        assertFalse(darkTheme)
    }

    @Test
    fun `setDarkTheme should update the flow`() = runTest {
        settingsRepository.setDarkTheme(true)
        val darkTheme = settingsRepository.darkTheme.first()
        assertTrue(darkTheme)
    }

    @Test
    fun `initial list order should be default`() = runTest {
        val listOrder = settingsRepository.listOrder.first()
        assertEquals("CREATED_DESC", listOrder)
    }

    @Test
    fun `setListOrder should update the flow`() = runTest {
        settingsRepository.setListOrder("NAME_ASC")
        val listOrder = settingsRepository.listOrder.first()
        assertEquals("NAME_ASC", listOrder)
    }

    @Test
    fun `resetToDefaults should restore default values`() = runTest {
        // First set some non-default values
        settingsRepository.setDarkTheme(true)
        settingsRepository.setListOrder("CUSTOM_ORDER")
        
        // Reset to defaults
        settingsRepository.resetToDefaults()
        
        // Verify defaults are restored
        val darkTheme = settingsRepository.darkTheme.first()
        val listOrder = settingsRepository.listOrder.first()
        
        assertFalse(darkTheme)
        assertEquals("CREATED_DESC", listOrder)
    }
}