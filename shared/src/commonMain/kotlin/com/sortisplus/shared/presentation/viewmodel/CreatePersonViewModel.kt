package com.sortisplus.shared.presentation.viewmodel

import com.sortisplus.shared.domain.model.DatabaseResult
import com.sortisplus.shared.domain.usecase.person.CreatePersonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Create person form state
 */
data class CreatePersonUiState(
    val firstName: String = "",
    val lastName: String = "",
    val birthDateMillis: Long = 0L,
    val weightKg: String = "",
    val isLeftHanded: Boolean = false,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val birthDateError: String? = null,
    val weightError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

/**
 * Shared CreatePersonViewModel for creating new persons
 */
class CreatePersonViewModel(
    private val createPersonUseCase: CreatePersonUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CreatePersonUiState())
    val uiState: StateFlow<CreatePersonUiState> = _uiState.asStateFlow()

    /**
     * Updates first name field
     */
    fun updateFirstName(firstName: String) {
        _uiState.value = _uiState.value.copy(
            firstName = firstName,
            firstNameError = null
        )
    }

    /**
     * Updates last name field
     */
    fun updateLastName(lastName: String) {
        _uiState.value = _uiState.value.copy(
            lastName = lastName,
            lastNameError = null
        )
    }

    /**
     * Updates birth date
     */
    fun updateBirthDate(birthDateMillis: Long) {
        _uiState.value = _uiState.value.copy(
            birthDateMillis = birthDateMillis,
            birthDateError = null
        )
    }

    /**
     * Updates weight field
     */
    fun updateWeight(weight: String) {
        _uiState.value = _uiState.value.copy(
            weightKg = weight,
            weightError = null
        )
    }

    /**
     * Toggles left-handed status
     */
    fun toggleLeftHanded() {
        _uiState.value = _uiState.value.copy(
            isLeftHanded = !_uiState.value.isLeftHanded
        )
    }

    /**
     * Validates the form and creates the person
     */
    fun createPerson() {
        val currentState = _uiState.value
        
        // Basic validation
        val errors = mutableMapOf<String, String>()
        
        if (currentState.firstName.isBlank()) {
            errors["firstName"] = "First name is required"
        }
        
        if (currentState.lastName.isBlank()) {
            errors["lastName"] = "Last name is required"
        }
        
        if (currentState.birthDateMillis <= 0) {
            errors["birthDate"] = "Please select a valid birth date"
        }
        
        val weight = currentState.weightKg.toDoubleOrNull()
        if (weight == null || weight <= 0) {
            errors["weight"] = "Please enter a valid weight"
        }
        
        if (errors.isNotEmpty()) {
            _uiState.value = currentState.copy(
                firstNameError = errors["firstName"],
                lastNameError = errors["lastName"],
                birthDateError = errors["birthDate"],
                weightError = errors["weight"]
            )
            return
        }
        
        // Create person
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, error = null)
            
            val result = createPersonUseCase.execute(
                firstName = currentState.firstName,
                lastName = currentState.lastName,
                birthDateMillis = currentState.birthDateMillis,
                weightKg = weight!!,
                isLeftHanded = currentState.isLeftHanded
            )
            
            when (result) {
                is DatabaseResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                is DatabaseResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Failed to create person"
                    )
                }
            }
        }
    }

    /**
     * Resets the form after successful creation
     */
    fun resetForm() {
        _uiState.value = CreatePersonUiState()
    }

    /**
     * Clears any error state
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}