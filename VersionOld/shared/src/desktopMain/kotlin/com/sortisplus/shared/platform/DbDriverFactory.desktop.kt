package com.sortisplus.shared.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.sortisplus.shared.database.AppDatabase
import java.io.File

actual class DbDriverFactory {
    actual fun createDriver(): SqlDriver {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".templateandroid")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        
        val databasePath = File(appDir, "app_database.db").absolutePath
        val driver = JdbcSqliteDriver("jdbc:sqlite:$databasePath")
        AppDatabase.Schema.create(driver)
        return driver
    }
}