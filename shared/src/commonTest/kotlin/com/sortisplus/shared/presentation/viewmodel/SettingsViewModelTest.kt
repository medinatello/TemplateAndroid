package com.sortisplus.shared.presentation.viewmodel

import com.sortisplus.shared.domain.repository.SettingsRepository
import com.sortisplus.shared.domain.usecase.settings.AppSettings
import com.sortisplus.shared.domain.usecase.settings.ObserveAppSettingsUseCase
import com.sortisplus.shared.domain.usecase.settings.UpdateDarkThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsViewModelTest {

    private var darkThemeValue = false
    
    private val mockSettingsRepository = object : SettingsRepository {
        private val _darkTheme = MutableStateFlow(darkThemeValue)
        private val _listOrder = MutableStateFlow("CREATED_DESC")
        
        override val darkTheme = _darkTheme
        override val listOrder = _listOrder
        
        override suspend fun setDarkTheme(enabled: Boolean) {
            darkThemeValue = enabled
            _darkTheme.value = enabled
        }
        
        override suspend fun setListOrder(order: String) {
            _listOrder.value = order
        }
        
        override suspend fun resetToDefaults() {
            setDarkTheme(false)
            setListOrder("CREATED_DESC")
        }
    }
    
    private val mockObserveAppSettingsUseCase = ObserveAppSettingsUseCase(mockSettingsRepository)
    private val mockUpdateDarkThemeUseCase = UpdateDarkThemeUseCase(mockSettingsRepository)

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