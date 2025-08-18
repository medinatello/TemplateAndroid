package com.sortisplus.shared.presentation.viewmodel

import com.sortisplus.shared.domain.usecase.settings.AppSettings
import com.sortisplus.shared.domain.usecase.settings.ObserveAppSettingsUseCase
import com.sortisplus.shared.domain.usecase.settings.UpdateDarkThemeUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsViewModelTest {

    private var darkThemeValue = false
    
    private val mockObserveAppSettingsUseCase = object : ObserveAppSettingsUseCase {
        fun execute() = flowOf(AppSettings(isDarkTheme = darkThemeValue, listOrder = "CREATED_DESC"))
    }
    
    private val mockUpdateDarkThemeUseCase = object : UpdateDarkThemeUseCase {
        suspend fun execute(enabled: Boolean) {
            darkThemeValue = enabled
        }
    }

    @Test
    fun `initial state should have correct default values`() = runTest {
        val viewModel = SettingsViewModel(mockObserveAppSettingsUseCase, mockUpdateDarkThemeUseCase)
        
        val initialState = viewModel.uiState.value
        assertFalse(initialState.isDarkTheme)
        assertEquals("CREATED_DESC", initialState.listOrder)
        assertFalse(initialState.isLoading)
        assertEquals(null, initialState.error)
    }

    @Test
    fun `toggleDarkTheme should update the theme`() = runTest {
        val viewModel = SettingsViewModel(mockObserveAppSettingsUseCase, mockUpdateDarkThemeUseCase)
        
        // Initially false
        assertFalse(viewModel.uiState.value.isDarkTheme)
        
        // Toggle to true
        viewModel.toggleDarkTheme()
        
        // Value should be updated in mock
        assertTrue(darkThemeValue)
    }

    @Test
    fun `setDarkTheme should set specific value`() = runTest {
        val viewModel = SettingsViewModel(mockObserveAppSettingsUseCase, mockUpdateDarkThemeUseCase)
        
        // Set to true
        viewModel.setDarkTheme(true)
        assertTrue(darkThemeValue)
        
        // Set to false
        viewModel.setDarkTheme(false)
        assertFalse(darkThemeValue)
    }
}