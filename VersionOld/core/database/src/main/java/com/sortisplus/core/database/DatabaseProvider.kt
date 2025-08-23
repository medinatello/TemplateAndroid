package com.sortisplus.core.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_dev_v2.db"
            )
                .fallbackToDestructiveMigrationOnDowngrade()
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
}
