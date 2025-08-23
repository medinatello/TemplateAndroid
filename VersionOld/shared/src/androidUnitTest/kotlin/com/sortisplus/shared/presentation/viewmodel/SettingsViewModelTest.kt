package com.sortisplus.shared.presentation.viewmodel

import com.sortisplus.shared.domain.repository.SettingsRepository
import com.sortisplus.shared.domain.usecase.settings.ObserveAppSettingsUseCase
import com.sortisplus.shared.domain.usecase.settings.UpdateDarkThemeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {

    private val mockSettingsRepository = object : SettingsRepository {
        private val _darkTheme = MutableStateFlow(false)
        private val _listOrder = MutableStateFlow("CREATED_DESC")
        
        override val darkTheme = _darkTheme
        override val listOrder = _listOrder
        
        override suspend fun setDarkTheme(enabled: Boolean) {
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
        
        // Wait for initialization to complete
        advanceUntilIdle()
        
        val initialState = viewModel.uiState.value
        assertFalse(initialState.isDarkTheme)
        assertEquals("CREATED_DESC", initialState.listOrder)
        assertFalse(initialState.isLoading)
        assertEquals(null, initialState.error)
    }

    @Test
    fun `viewModel methods should not throw exceptions`() = runTest {
        val viewModel = SettingsViewModel(mockObserveAppSettingsUseCase, mockUpdateDarkThemeUseCase)
        
        // Wait for initialization to complete
        advanceUntilIdle()
        
        // These should not throw exceptions
        viewModel.toggleDarkTheme()
        advanceUntilIdle()
        
        viewModel.setDarkTheme(true)
        advanceUntilIdle()
        
        viewModel.setDarkTheme(false)
        advanceUntilIdle()
        
        // Basic assertion to verify the test ran
        assertTrue(true)
    }
}