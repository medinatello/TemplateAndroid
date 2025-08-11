package com.sortisplus.core.data

import kotlinx.coroutines.flow.Flow

// Result wrapper for error handling
sealed class DatabaseResult<out T> {
    data class Success<T>(val data: T) : DatabaseResult<T>()
    data class Error(val exception: Exception) : DatabaseResult<Nothing>()
}

// Validation result
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
) {
    companion object {
        fun success() = ValidationResult(true)
        fun failure(vararg errors: String) = ValidationResult(false, errors.toList())
    }
}

// Domain model
data class Element(
    val id: Long,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long? = null
)

// Person domain model (Customer)
data class Person(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthDateMillis: Long,
    val weightKg: Double,
    val isLeftHanded: Boolean
) {
    // Calculated property for age
    val age: Int
        get() {
            val currentTime = System.currentTimeMillis()
            val ageInMillis = currentTime - birthDateMillis
            val ageInYears = ageInMillis / (365.25 * 24 * 60 * 60 * 1000)
            return ageInYears.toInt()
        }

    companion object {
        fun validate(
            firstName: String,
            lastName: String,
            birthDateMillis: Long,
            weightKg: Double
        ): ValidationResult {
            val errors = mutableListOf<String>()

            if (firstName.isBlank()) errors.add("First name cannot be empty")
            if (firstName.length > 50) errors.add("First name cannot have more than 50 characters")

            if (lastName.isBlank()) errors.add("Last name cannot be empty")
            if (lastName.length > 50) errors.add("Last name cannot have more than 50 characters")

            if (birthDateMillis <= 0) errors.add("Birth date must be valid")
            if (birthDateMillis > System.currentTimeMillis()) errors.add("Birth date cannot be in the future")

            if (weightKg <= 0) errors.add("Weight must be greater than 0")
            if (weightKg > 1000) errors.add("Weight must be less than 1000kg")

            return if (errors.isEmpty()) {
                ValidationResult.success()
            } else {
                ValidationResult.failure(*errors.toTypedArray())
            }
        }
    }
}

interface ElementRepository {
    fun observeAll(): Flow<List<Element>>
    suspend fun create(title: String): Long
    suspend fun update(id: Long, title: String): Boolean
    suspend fun delete(id: Long): Boolean
}

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

interface SettingsRepository {
    val darkTheme: Flow<Boolean>
    val listOrder: Flow<String>
    suspend fun setDarkTheme(value: Boolean)
    suspend fun setListOrder(value: String)
}
