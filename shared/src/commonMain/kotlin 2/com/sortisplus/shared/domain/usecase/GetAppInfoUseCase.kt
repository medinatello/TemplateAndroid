package com.sortisplus.shared.domain.usecase

import com.sortisplus.shared.platform.AppClock
import com.sortisplus.shared.platform.KeyValueStore

data class AppInfo(
    val appName: String,
    val version: String,
    val currentTime: Long,
    val lastOpenTime: Long?
)

class GetAppInfoUseCase(
    private val keyValueStore: KeyValueStore,
    private val appClock: AppClock
) {
    
    companion object {
        private const val KEY_LAST_OPEN_TIME = "last_open_time"
        private const val APP_NAME = "Template Android KMP"
        private const val VERSION = "1.0.0"
    }
    
    fun execute(): AppInfo {
        val currentTime = appClock.currentTimeMillis()
        val lastOpenTime = keyValueStore.getString(KEY_LAST_OPEN_TIME)?.toLongOrNull()
        
        // Update last open time
        keyValueStore.putString(KEY_LAST_OPEN_TIME, currentTime.toString())
        
        return AppInfo(
            appName = APP_NAME,
            version = VERSION,
            currentTime = currentTime,
            lastOpenTime = lastOpenTime
        )
    }
}