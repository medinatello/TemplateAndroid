package com.sortisplus.shared.domain.usecase.auth

import com.sortisplus.shared.domain.repository.AuthRepository

/**
 * Use case for user logout
 */
class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun execute() {
        authRepository.logout()
    }
}