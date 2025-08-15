package com.sortisplus.core.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages authentication state and operations throughout the application.
 * Integrates with ConfigurationManager and SecureStorage for persistent authentication.
 */
@Singleton
class AuthenticationManager @Inject constructor(
    private val configurationManager: ConfigurationManager
) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: Flow<AuthState> = _authState.asStateFlow()

    /**
     * Convenience flow to check if user is authenticated
     */
    val isAuthenticated: Flow<Boolean> = combine(
        authState,
        configurationManager.isUserAuthenticated
    ) { state, hasToken ->
        state is AuthState.Authenticated && hasToken
    }

    /**
     * Current user information if authenticated
     */
    val currentUser: UserInfo?
        get() = (_authState.value as? AuthState.Authenticated)?.userInfo

    init {
        checkInitialAuthenticationState()
    }

    /**
     * Checks the initial authentication state when the app starts
     */
    private fun checkInitialAuthenticationState() {
        val hasAuthToken = configurationManager.getAuthToken() != null
        val username = configurationManager.getUserCredentials().first
        
        if (hasAuthToken && !username.isNullOrEmpty()) {
            // User was previously authenticated
            val userInfo = UserInfo.fromEmail(username)
            _authState.value = AuthState.Authenticated(userInfo)
        } else {
            // No previous authentication found
            _authState.value = AuthState.Unauthenticated
        }
    }

    /**
     * Attempts to authenticate the user with email and password
     * For demo purposes, this simulates a login process
     */
    suspend fun login(email: String, password: String): AuthState {
        _authState.value = AuthState.Loading
        
        try {
            // Simulate network delay
            delay(1500)
            
            // For demo purposes, validate basic requirements
            if (email.isBlank() || password.isBlank()) {
                val errorState = AuthState.Error("Email and password are required")
                _authState.value = errorState
                return errorState
            }
            
            if (password.length < 6) {
                val errorState = AuthState.Error("Password must be at least 6 characters")
                _authState.value = errorState
                return errorState
            }
            
            if (!email.contains("@")) {
                val errorState = AuthState.Error("Please enter a valid email address")
                _authState.value = errorState
                return errorState
            }
            
            // Simulate successful authentication
            val userInfo = UserInfo.fromEmail(email)
            
            // Save authentication data
            configurationManager.saveUserCredentials(email, password) // In real app, password should be hashed
            configurationManager.saveAuthTokens(
                authToken = "demo_auth_token_${System.currentTimeMillis()}",
                refreshToken = "demo_refresh_token_${System.currentTimeMillis()}"
            )
            
            val authenticatedState = AuthState.Authenticated(userInfo)
            _authState.value = authenticatedState
            
            return authenticatedState
            
        } catch (e: Exception) {
            val errorState = AuthState.Error("Login failed: ${e.message}")
            _authState.value = errorState
            return errorState
        }
    }

    /**
     * Logs out the current user and clears all authentication data
     */
    suspend fun logout() {
        _authState.value = AuthState.Loading
        
        try {
            // Clear all authentication data
            configurationManager.logout()
            
            // Update state
            _authState.value = AuthState.Unauthenticated
            
        } catch (e: Exception) {
            // Even if logout fails, we should consider the user logged out locally
            _authState.value = AuthState.Unauthenticated
        }
    }

    /**
     * Refreshes the authentication token if needed
     * For demo purposes, this always succeeds
     */
    suspend fun refreshAuthToken(): Boolean {
        return try {
            val refreshToken = configurationManager.getRefreshToken()
            if (refreshToken != null) {
                // Simulate token refresh
                delay(500)
                configurationManager.saveAuthTokens(
                    authToken = "refreshed_auth_token_${System.currentTimeMillis()}",
                    refreshToken = refreshToken
                )
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Performs emergency logout and data clearing
     */
    suspend fun emergencyLogout() {
        _authState.value = AuthState.Loading
        configurationManager.emergencyReset()
        _authState.value = AuthState.Unauthenticated
    }

    /**
     * Checks if the current session is still valid
     */
    fun isSessionValid(): Boolean {
        return _authState.value is AuthState.Authenticated && 
               configurationManager.getAuthToken() != null
    }
}