package com.sortisplus.shared.domain.usecase.auth

import com.sortisplus.shared.domain.model.AuthState
import com.sortisplus.shared.domain.repository.AuthRepository

/**
 * Use case for user login
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun execute(email: String, password: String): AuthState {
        // Basic validation
        if (email.isBlank() || password.isBlank()) {
            return AuthState.Error("Email and password are required")
        }
        
        if (password.length < 6) {
            return AuthState.Error("Password must be at least 6 characters")
        }
        
        if (!email.contains("@")) {
            return AuthState.Error("Please enter a valid email address")
        }
        
        return authRepository.login(email, password)
    }
}