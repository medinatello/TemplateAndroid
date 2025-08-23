package com.sortisplus.shared.data.cache

import com.sortisplus.shared.TestDatabaseHelper
import com.sortisplus.shared.database.AppDatabase
import com.sortisplus.shared.platform.AppClock
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

@RunWith(RobolectricTestRunner::class)
class CacheServiceTest {
    
    private lateinit var database: AppDatabase
    private lateinit var cacheService: CacheService
    private lateinit var mockClock: MockAppClock
    
    @Before
    fun setup() {
        database = TestDatabaseHelper.createInMemoryDatabase()
        mockClock = MockAppClock()
        cacheService = CacheServiceImpl(database, mockClock)
    }
    
    @Test
    fun testPutAndGet() = runTest {
        val key = "test_key"
        val data = "test_data"
        
        cacheService.put(key, data)
        val result = cacheService.get(key)
        
        assertEquals(data, result)
    }
    
    @Test
    fun testGetExpiredData() = runTest {
        val key = "test_key"
        val data = "test_data"
        val ttl = 100.milliseconds
        
        // Put data with short TTL
        cacheService.put(key, data, ttl)
        
        // Advance time beyond TTL
        mockClock.advanceTimeBy(200.milliseconds)
        
        // Should return null because data is expired
        val result = cacheService.get(key)
        assertNull(result)
    }
    
    @Test
    fun testClearExpired() = runTest {
        val key1 = "key1"
        val key2 = "key2"
        val data = "test_data"
        
        // Put data with different TTLs
        cacheService.put(key1, data, 100.milliseconds)
        cacheService.put(key2, data, 1.hours)
        
        // Advance time to expire only key1
        mockClock.advanceTimeBy(200.milliseconds)
        
        // Clear expired entries
        cacheService.clearExpired()
        
        // key1 should be gone, key2 should still exist
        assertNull(cacheService.get(key1))
        assertEquals(data, cacheService.get(key2))
    }
    
    @Test
    fun testDelete() = runTest {
        val key = "test_key"
        val data = "test_data"
        
        cacheService.put(key, data)
        assertEquals(data, cacheService.get(key))
        
        cacheService.delete(key)
        assertNull(cacheService.get(key))
    }
    
    @Test
    fun testClearAll() = runTest {
        val key1 = "key1"
        val key2 = "key2"
        val data = "test_data"
        
        cacheService.put(key1, data)
        cacheService.put(key2, data)
        
        assertEquals(data, cacheService.get(key1))
        assertEquals(data, cacheService.get(key2))
        
        cacheService.clearAll()
        
        assertNull(cacheService.get(key1))
        assertNull(cacheService.get(key2))
    }
    
    private class MockAppClock : AppClock {
        private var currentTime = 1000000L
        
        override fun now(): Long = currentTime
        
        override fun currentTimeMillis(): Long = currentTime
        
        override fun formatDateTime(timestamp: Long, pattern: String): String = ""
        
        fun advanceTimeBy(duration: kotlin.time.Duration) {
            currentTime += duration.inWholeMilliseconds
        }
    }
}