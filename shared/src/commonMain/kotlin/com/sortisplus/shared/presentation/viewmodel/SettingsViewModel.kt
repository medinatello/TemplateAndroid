package com.sortisplus.shared.presentation.viewmodel

import com.sortisplus.shared.domain.usecase.settings.ObserveAppSettingsUseCase
import com.sortisplus.shared.domain.usecase.settings.UpdateDarkThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Settings screen state
 */
data class SettingsUiState(
    val isDarkTheme: Boolean = false,
    val listOrder: String = "CREATED_DESC",
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Shared SettingsViewModel for managing app settings
 * Uses domain use cases for business logic
 */
class SettingsViewModel(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val updateDarkThemeUseCase: UpdateDarkThemeUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    /**
     * Observes app settings and updates UI state
     */
    private fun observeSettings() {
        observeAppSettingsUseCase.execute()
            .onEach { appSettings ->
                _uiState.value = _uiState.value.copy(
                    isDarkTheme = appSettings.isDarkTheme,
                    listOrder = appSettings.listOrder,
                    isLoading = false,
                    error = null
                )
            }
            .launchIn(viewModelScope)
    }

    /**
     * Toggles dark theme setting
     */
    fun toggleDarkTheme() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val newValue = !_uiState.value.isDarkTheme
                updateDarkThemeUseCase.execute(newValue)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to update dark theme: ${e.message}"
                )
            }
        }
    }

    /**
     * Sets dark theme value
     */
    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                updateDarkThemeUseCase.execute(enabled)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to update dark theme: ${e.message}"
                )
            }
        }
    }

    /**
     * Clears any error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}