package com.sortisplus.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personas")
data class PersonaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: Long,
    val peso: Double,
    val esZurdo: Boolean
)
