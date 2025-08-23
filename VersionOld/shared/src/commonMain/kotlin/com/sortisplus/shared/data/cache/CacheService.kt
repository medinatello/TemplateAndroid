package com.sortisplus.shared.data.cache

import com.sortisplus.shared.database.AppDatabase
import com.sortisplus.shared.platform.AppClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

interface CacheService {
    suspend fun get(key: String): String?
    suspend fun put(key: String, data: String, ttl: Duration = 1.hours)
    suspend fun delete(key: String)
    suspend fun clearExpired()
    suspend fun clearAll()
}

class CacheServiceImpl(
    private val database: AppDatabase,
    private val appClock: AppClock
) : CacheService {
    
    override suspend fun get(key: String): String? = withContext(Dispatchers.Default) {
        val currentTime = appClock.currentTimeMillis()
        val cacheEntry = database.appDatabaseQueries.selectCacheByKey(
            key,
            currentTime
        ).executeAsOneOrNull()
        
        cacheEntry?.data_
    }
    
    override suspend fun put(key: String, data: String, ttl: Duration) = withContext(Dispatchers.Default) {
        val currentTime = appClock.currentTimeMillis()
        val expiresAt = currentTime + ttl.inWholeMilliseconds
        
        database.appDatabaseQueries.insertOrReplaceCacheEntry(
            key,
            data,
            currentTime,
            expiresAt
        )
    }
    
    override suspend fun delete(key: String) = withContext(Dispatchers.Default) {
        database.appDatabaseQueries.deleteCacheByKey(key)
    }
    
    override suspend fun clearExpired() = withContext(Dispatchers.Default) {
        val currentTime = appClock.currentTimeMillis()
        database.appDatabaseQueries.deleteExpiredCache(currentTime)
    }
    
    override suspend fun clearAll() = withContext(Dispatchers.Default) {
        database.appDatabaseQueries.clearAllCache()
    }
}