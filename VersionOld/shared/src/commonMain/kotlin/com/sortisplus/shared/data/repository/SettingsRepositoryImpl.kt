package com.sortisplus.shared.data.repository

import com.sortisplus.shared.domain.repository.SettingsRepository
import com.sortisplus.shared.platform.KeyValueStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Settings repository implementation using KeyValueStore
 * Provides reactive access to app settings
 */
class SettingsRepositoryImpl(
    private val keyValueStore: KeyValueStore
) : SettingsRepository {

    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_LIST_ORDER = "list_order"
        private const val DEFAULT_LIST_ORDER = "CREATED_DESC"
    }

    // StateFlows for reactive UI updates
    private val _darkTheme = MutableStateFlow(keyValueStore.getBoolean(KEY_DARK_THEME, false))
    private val _listOrder = MutableStateFlow(keyValueStore.getString(KEY_LIST_ORDER) ?: DEFAULT_LIST_ORDER)

    override val darkTheme: Flow<Boolean> = _darkTheme.asStateFlow()
    override val listOrder: Flow<String> = _listOrder.asStateFlow()

    override suspend fun setDarkTheme(value: Boolean) {
        keyValueStore.putBoolean(KEY_DARK_THEME, value)
        _darkTheme.value = value
    }

    override suspend fun setListOrder(value: String) {
        keyValueStore.putString(KEY_LIST_ORDER, value)
        _listOrder.value = value
    }

    override suspend fun resetToDefaults() {
        setDarkTheme(false)
        setListOrder(DEFAULT_LIST_ORDER)
    }
}