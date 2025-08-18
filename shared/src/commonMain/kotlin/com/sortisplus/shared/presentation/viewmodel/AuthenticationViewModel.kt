package com.sortisplus.shared.presentation.viewmodel

import com.sortisplus.shared.domain.model.AuthState
import com.sortisplus.shared.domain.usecase.auth.LoginUseCase
import com.sortisplus.shared.domain.usecase.auth.LogoutUseCase
import com.sortisplus.shared.domain.usecase.auth.ObserveAuthStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Authentication UI state
 */
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val authState: AuthState = AuthState.Unauthenticated
)

/**
 * Shared AuthenticationViewModel for login/logout operations
 */
class AuthenticationViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    /**
     * Observes authentication state changes
     */
    private fun observeAuthState() {
        observeAuthStateUseCase.execute()
            .onEach { authState ->
                _uiState.value = _uiState.value.copy(
                    authState = authState,
                    isLoading = authState is AuthState.Loading
                )
            }
            .launchIn(viewModelScope)
    }

    /**
     * Updates email field and clears email error
     */
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }

    /**
     * Updates password field and clears password error
     */
    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null
        )
    }

    /**
     * Validates email field
     */
    private fun validateEmail(): Boolean {
        val email = _uiState.value.email
        return when {
            email.isBlank() -> {
                _uiState.value = _uiState.value.copy(emailError = "Email is required")
                false
            }
            !email.contains("@") -> {
                _uiState.value = _uiState.value.copy(emailError = "Please enter a valid email")
                false
            }
            else -> {
                _uiState.value = _uiState.value.copy(emailError = null)
                true
            }
        }
    }

    /**
     * Validates password field
     */
    private fun validatePassword(): Boolean {
        val password = _uiState.value.password
        return when {
            password.isBlank() -> {
                _uiState.value = _uiState.value.copy(passwordError = "Password is required")
                false
            }
            password.length < 6 -> {
                _uiState.value = _uiState.value.copy(passwordError = "Password must be at least 6 characters")
                false
            }
            else -> {
                _uiState.value = _uiState.value.copy(passwordError = null)
                true
            }
        }
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
     * Attempts to log in with current email and password
     */
    fun login() {
        if (!validateForm()) {
            return
        }

        viewModelScope.launch {
            val result = loginUseCase.execute(_uiState.value.email, _uiState.value.password)
            // AuthState is already updated through the observer
        }
    }

    /**
     * Logs out the current user
     */
    fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute()
        }
    }

    /**
     * Clears any error states
     */
    fun clearErrors() {
        _uiState.value = _uiState.value.copy(
            emailError = null,
            passwordError = null
        )
    }

    /**
     * Clears the form
     */
    fun clearForm() {
        _uiState.value = _uiState.value.copy(
            email = "",
            password = "",
            emailError = null,
            passwordError = null
        )
    }

    /**
     * Checks if login button should be enabled
     */
    fun isLoginEnabled(): Boolean {
        return _uiState.value.email.isNotEmpty() && 
               _uiState.value.password.isNotEmpty() && 
               !_uiState.value.isLoading
    }

    /**
     * Gets current error message from auth state if any
     */
    fun getAuthErrorMessage(): String? {
        return (_uiState.value.authState as? AuthState.Error)?.error
    }
}