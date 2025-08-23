package com.sortisplus.shared.domain.repository

import com.sortisplus.shared.domain.model.AuthState
import com.sortisplus.shared.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    val authState: Flow<AuthState>
    val isAuthenticated: Flow<Boolean>
    val currentUser: UserInfo?
    
    suspend fun login(email: String, password: String): AuthState
    suspend fun logout()
    suspend fun refreshAuthToken(): Boolean
    suspend fun isSessionValid(): Boolean
    suspend fun emergencyLogout()
    fun checkInitialAuthenticationState()
}