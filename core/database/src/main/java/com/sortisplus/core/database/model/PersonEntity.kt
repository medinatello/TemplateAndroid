package com.sortisplus.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Desarrollo: tabla en inglés para nuevo esquema limpio
@Entity(tableName = "people")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val birthDateMillis: Long,
    val weightKg: Double,
    val isLeftHanded: Boolean
)
