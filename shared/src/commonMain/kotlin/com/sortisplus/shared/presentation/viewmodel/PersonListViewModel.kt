package com.sortisplus.shared.presentation.viewmodel

import com.sortisplus.shared.domain.model.Person
import com.sortisplus.shared.domain.usecase.person.GetAllPersonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Person list screen state
 */
data class PersonListUiState(
    val persons: List<Person> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Shared PersonListViewModel for displaying persons
 */
class PersonListViewModel(
    private val getAllPersonsUseCase: GetAllPersonsUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(PersonListUiState(isLoading = true))
    val uiState: StateFlow<PersonListUiState> = _uiState.asStateFlow()

    init {
        observePersons()
    }

    /**
     * Observes persons from the repository
     */
    private fun observePersons() {
        getAllPersonsUseCase.execute()
            .onEach { persons ->
                _uiState.value = _uiState.value.copy(
                    persons = persons,
                    isLoading = false,
                    error = null
                )
            }
            .launchIn(viewModelScope)
    }

    /**
     * Refreshes the person list
     */
    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        // The flow will automatically update with new data
    }

    /**
     * Clears any error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}