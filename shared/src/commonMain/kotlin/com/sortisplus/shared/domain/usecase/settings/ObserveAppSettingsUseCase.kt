package com.sortisplus.shared.domain.usecase.settings

import com.sortisplus.shared.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Combined app settings data
 */
data class AppSettings(
    val isDarkTheme: Boolean,
    val listOrder: String
)

/**
 * Use case for observing all app settings reactively
 */
class ObserveAppSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    fun execute(): Flow<AppSettings> {
        return combine(
            settingsRepository.darkTheme,
            settingsRepository.listOrder
        ) { isDarkTheme, listOrder ->
            AppSettings(
                isDarkTheme = isDarkTheme,
                listOrder = listOrder
            )
        }
    }
}