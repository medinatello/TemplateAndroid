package com.sortisplus.shared.data.repository

import com.sortisplus.shared.domain.model.AuthState
import com.sortisplus.shared.domain.model.UserInfo
import com.sortisplus.shared.domain.repository.AuthRepository
import com.sortisplus.shared.platform.KeyValueStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Authentication repository implementation using KeyValueStore
 */
class AuthRepositoryImpl(
    private val keyValueStore: KeyValueStore
) : AuthRepository {

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PASSWORD = "user_password" // In real app, this should be encrypted/hashed
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    override val isAuthenticated: Flow<Boolean> = authState.map { state ->
        state is AuthState.Authenticated
    }

    override val currentUser: UserInfo?
        get() = (_authState.value as? AuthState.Authenticated)?.userInfo

    init {
        checkInitialAuthenticationState()
    }

    override fun checkInitialAuthenticationState() {
        val hasAuthToken = keyValueStore.getString(KEY_AUTH_TOKEN) != null
        val userEmail = keyValueStore.getString(KEY_USER_EMAIL)
        
        if (hasAuthToken && !userEmail.isNullOrEmpty()) {
            // User was previously authenticated
            val userInfo = UserInfo.fromEmail(userEmail)
            _authState.value = AuthState.Authenticated(userInfo)
        } else {
            // No previous authentication found
            _authState.value = AuthState.Unauthenticated
        }
    }

    override suspend fun login(email: String, password: String): AuthState {
        _authState.value = AuthState.Loading
        
        try {
            // Simulate network delay
            delay(1500)
            
            // Simulate successful authentication
            val userInfo = UserInfo.fromEmail(email)
            
            // Save authentication data
            keyValueStore.putString(KEY_USER_EMAIL, email)
            keyValueStore.putString(KEY_USER_PASSWORD, password) // In real app, this should be encrypted
            keyValueStore.putString(KEY_AUTH_TOKEN, "demo_auth_token_${System.currentTimeMillis()}")
            keyValueStore.putString(KEY_REFRESH_TOKEN, "demo_refresh_token_${System.currentTimeMillis()}")
            
            val authenticatedState = AuthState.Authenticated(userInfo)
            _authState.value = authenticatedState
            
            return authenticatedState
            
        } catch (e: Exception) {
            val errorState = AuthState.Error("Login failed: ${e.message}")
            _authState.value = errorState
            return errorState
        }
    }

    override suspend fun logout() {
        _authState.value = AuthState.Loading
        
        try {
            // Clear all authentication data
            keyValueStore.remove(KEY_AUTH_TOKEN)
            keyValueStore.remove(KEY_REFRESH_TOKEN)
            keyValueStore.remove(KEY_USER_EMAIL)
            keyValueStore.remove(KEY_USER_PASSWORD)
            
            // Update state
            _authState.value = AuthState.Unauthenticated
            
        } catch (e: Exception) {
            // Even if logout fails, we should consider the user logged out locally
            _authState.value = AuthState.Unauthenticated
        }
    }

    override suspend fun refreshAuthToken(): Boolean {
        return try {
            val refreshToken = keyValueStore.getString(KEY_REFRESH_TOKEN)
            if (refreshToken != null) {
                // Simulate token refresh
                delay(500)
                keyValueStore.putString(KEY_AUTH_TOKEN, "refreshed_auth_token_${System.currentTimeMillis()}")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun emergencyLogout() {
        _authState.value = AuthState.Loading
        keyValueStore.clear()
        _authState.value = AuthState.Unauthenticated
    }

    override suspend fun isSessionValid(): Boolean {
        return _authState.value is AuthState.Authenticated && 
               keyValueStore.getString(KEY_AUTH_TOKEN) != null
    }
}