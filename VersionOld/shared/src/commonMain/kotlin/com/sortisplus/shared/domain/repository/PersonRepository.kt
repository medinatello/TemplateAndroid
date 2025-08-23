package com.sortisplus.shared.domain.repository

import com.sortisplus.shared.domain.model.DatabaseResult
import com.sortisplus.shared.domain.model.Person
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Person operations
 */
interface PersonRepository {
    fun observeAll(): Flow<List<Person>>
    suspend fun getById(id: Long): DatabaseResult<Person?>
    suspend fun create(
        firstName: String,
        lastName: String,
        birthDateMillis: Long,
        weightKg: Double,
        isLeftHanded: Boolean
    ): DatabaseResult<Long>
    suspend fun update(
        id: Long,
        firstName: String,
        lastName: String,
        birthDateMillis: Long,
        weightKg: Double,
        isLeftHanded: Boolean
    ): DatabaseResult<Boolean>
    suspend fun delete(id: Long): DatabaseResult<Boolean>
}