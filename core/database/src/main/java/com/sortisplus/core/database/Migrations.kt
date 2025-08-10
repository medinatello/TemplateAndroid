package com.sortisplus.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Placeholder for future migrations
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // db.execSQL("ALTER TABLE elements ADD COLUMN ...")
    }
}