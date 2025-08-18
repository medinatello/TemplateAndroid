package com.sortisplus.shared.domain.usecase.settings

import com.sortisplus.shared.domain.repository.SettingsRepository

/**
 * Use case for updating dark theme preference
 */
class UpdateDarkThemeUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend fun execute(enabled: Boolean) {
        settingsRepository.setDarkTheme(enabled)
    }
}