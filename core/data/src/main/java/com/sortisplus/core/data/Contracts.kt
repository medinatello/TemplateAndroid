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

// Persona domain model (Cliente)
 data class Persona(
    val id: Long,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: Long,
    val peso: Double,
    val esZurdo: Boolean
) {
    // Calculated property for age
    val edad: Int
        get() {
            val currentTime = System.currentTimeMillis()
            val ageInMillis = currentTime - fechaNacimiento
            val ageInYears = ageInMillis / (365.25 * 24 * 60 * 60 * 1000)
            return ageInYears.toInt()
        }
    
    companion object {
        fun validate(
            nombre: String,
            apellido: String, 
            fechaNacimiento: Long,
            peso: Double
        ): ValidationResult {
            val errors = mutableListOf<String>()
            
            if (nombre.isBlank()) errors.add("El nombre no puede estar vacío")
            if (nombre.length > 50) errors.add("El nombre no puede tener más de 50 caracteres")
            
            if (apellido.isBlank()) errors.add("El apellido no puede estar vacío")  
            if (apellido.length > 50) errors.add("El apellido no puede tener más de 50 caracteres")
            
            if (fechaNacimiento <= 0) errors.add("La fecha de nacimiento debe ser válida")
            if (fechaNacimiento > System.currentTimeMillis()) errors.add("La fecha de nacimiento no puede ser futura")
            
            if (peso <= 0) errors.add("El peso debe ser mayor a 0")
            if (peso > 1000) errors.add("El peso debe ser menor a 1000kg")
            
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

interface PersonaRepository {
    fun observeAll(): Flow<List<Persona>>
    suspend fun getById(id: Long): DatabaseResult<Persona?>
    suspend fun create(
        nombre: String,
        apellido: String,
        fechaNacimiento: Long,
        peso: Double,
        esZurdo: Boolean
    ): DatabaseResult<Long>
    suspend fun update(
        id: Long,
        nombre: String,
        apellido: String,
        fechaNacimiento: Long,
        peso: Double,
        esZurdo: Boolean
    ): DatabaseResult<Boolean>
    suspend fun delete(id: Long): DatabaseResult<Boolean>
}

interface SettingsRepository {
    val darkTheme: Flow<Boolean>
    val listOrder: Flow<String>
    suspend fun setDarkTheme(value: Boolean)
    suspend fun setListOrder(value: String)
}