package com.sortisplus.shared.domain.usecase.auth

import com.sortisplus.shared.domain.model.AuthState
import com.sortisplus.shared.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for observing authentication state
 */
class ObserveAuthStateUseCase(
    private val authRepository: AuthRepository
) {
    fun execute(): Flow<AuthState> {
        return authRepository.authState
    }
}