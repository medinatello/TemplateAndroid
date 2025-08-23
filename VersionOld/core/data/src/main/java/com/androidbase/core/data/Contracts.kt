package com.androidbase.core.data

import kotlinx.coroutines.flow.Flow

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
}

interface ElementRepository {
    fun observeAll(): Flow<List<Element>>
    suspend fun create(title: String): Long
    suspend fun update(id: Long, title: String): Boolean
    suspend fun delete(id: Long): Boolean
}

interface PersonaRepository {
    fun observeAll(): Flow<List<Persona>>
    suspend fun getById(id: Long): Persona?
    suspend fun create(
        nombre: String,
        apellido: String,
        fechaNacimiento: Long,
        peso: Double,
        esZurdo: Boolean
    ): Long
    suspend fun update(
        id: Long,
        nombre: String,
        apellido: String,
        fechaNacimiento: Long,
        peso: Double,
        esZurdo: Boolean
    ): Boolean
    suspend fun delete(id: Long): Boolean
}

interface SettingsRepository {
    val darkTheme: Flow<Boolean>
    val listOrder: Flow<String>
    suspend fun setDarkTheme(value: Boolean)
    suspend fun setListOrder(value: String)
}