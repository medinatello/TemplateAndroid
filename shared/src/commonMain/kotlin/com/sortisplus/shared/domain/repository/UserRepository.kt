package com.sortisplus.shared.domain.repository

import com.sortisplus.shared.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: Long): User?
    suspend fun createUser(name: String, email: String): User
    suspend fun updateUser(user: User): User
    suspend fun deleteUser(id: Long)
    fun observeUsers(): Flow<List<User>>
}