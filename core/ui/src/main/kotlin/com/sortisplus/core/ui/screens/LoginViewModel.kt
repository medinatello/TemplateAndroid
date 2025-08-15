package com.sortisplus.core.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sortisplus.core.datastore.AuthState
import com.sortisplus.core.datastore.AuthenticationManager
import com.sortisplus.core.ui.validation.ValidationResult
import com.sortisplus.core.ui.validation.Validators
import com.sortisplus.core.ui.validation.and
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Login Screen that handles authentication logic
 * and form validation.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationManager: AuthenticationManager
) : ViewModel() {

    // Form state
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // Validation state
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    // UI state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Authentication state from manager
    val authState: StateFlow<AuthState> = authenticationManager.authState as StateFlow<AuthState>

    // Validators
    private val emailValidator = Validators.required() and Validators.email()
    private val passwordValidator = Validators.required() and Validators.minLength(6)

    /**
     * Updates the email field and clears errors if validation passes
     */
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        if (_emailError.value != null) {
            validateEmail()
        }
    }

    /**
     * Updates the password field and clears errors if validation passes
     */
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        if (_passwordError.value != null) {
            validatePassword()
        }
    }

    /**
     * Validates the email field
     */
    fun validateEmail(): Boolean {
        val result = emailValidator.validate(_email.value)
        _emailError.value = if (result is ValidationResult.Invalid) result.errorMessage else null
        return result.isValid
    }

    /**
     * Validates the password field
     */
    fun validatePassword(): Boolean {
        val result = passwordValidator.validate(_password.value)
        _passwordError.value = if (result is ValidationResult.Invalid) result.errorMessage else null
        return result.isValid
    }

    /**
     * Validates the entire form
     */
    private fun validateForm(): Boolean {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        return isEmailValid && isPasswordValid
    }

    /**
     * Attempts to log in with the current email and password
     */
    fun login() {
        if (!validateForm()) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val result = authenticationManager.login(_email.value, _password.value)
                
                // Handle different results if needed
                when (result) {
                    is AuthState.Authenticated -> {
                        // Success - navigation will be handled by the UI observing authState
                    }
                    is AuthState.Error -> {
                        // Error is already set in authState, UI will display it
                    }
                    else -> {
                        // Other states handled by authState observer
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Clears any error states (useful for retry scenarios)
     */
    fun clearErrors() {
        _emailError.value = null
        _passwordError.value = null
    }

    /**
     * Clears the form (useful for logout scenarios)
     */
    fun clearForm() {
        _email.value = ""
        _password.value = ""
        clearErrors()
    }

    /**
     * Checks if the login button should be enabled
     */
    fun isLoginEnabled(): Boolean {
        return _email.value.isNotEmpty() && 
               _password.value.isNotEmpty() && 
               !_isLoading.value &&
               authState.value !is AuthState.Loading
    }

    /**
     * Gets the current error message from auth state if any
     */
    fun getAuthErrorMessage(): String? {
        return (authState.value as? AuthState.Error)?.error
    }
}