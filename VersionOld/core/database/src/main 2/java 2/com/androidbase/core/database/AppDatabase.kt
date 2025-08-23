package com.androidbase.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androidbase.core.database.dao.ElementDao
import com.androidbase.core.database.dao.PersonaDao
import com.androidbase.core.database.model.ElementEntity
import com.androidbase.core.database.model.PersonaEntity

@Database(
  entities = [ElementEntity::class, PersonaEntity::class],
  version = 1,
  exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun elementDao(): ElementDao
  abstract fun personaDao(): PersonaDao
}