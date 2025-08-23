package com.sortisplus.shared

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.sortisplus.shared.database.AppDatabase
import org.robolectric.RuntimeEnvironment

object TestDatabaseHelper {
    fun createInMemoryDatabase(): AppDatabase {
        val driver = AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = RuntimeEnvironment.getApplication(),
            name = null // null means in-memory
        )
        return AppDatabase(driver)
    }
}