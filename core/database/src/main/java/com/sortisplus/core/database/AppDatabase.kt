package com.sortisplus.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sortisplus.core.database.dao.ElementDao
import com.sortisplus.core.database.dao.PersonDao
import com.sortisplus.core.database.model.ElementEntity
import com.sortisplus.core.database.model.PersonEntity

@Database(
  entities = [ElementEntity::class, PersonEntity::class],
  version = 2,
  exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun elementDao(): ElementDao
  abstract fun personDao(): PersonDao
}
