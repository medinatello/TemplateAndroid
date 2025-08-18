package com.sortisplus.shared.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for application settings and preferences
 */
interface SettingsRepository {
    val darkTheme: Flow<Boolean>
    val listOrder: Flow<String>
    suspend fun setDarkTheme(value: Boolean)
    suspend fun setListOrder(value: String)
    suspend fun resetToDefaults()
}