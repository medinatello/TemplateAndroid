package com.sortisplus.shared.domain.usecase

import com.sortisplus.shared.platform.AppClock
import com.sortisplus.shared.platform.KeyValueStore
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class TestKeyValueStore : KeyValueStore {
    private val storage = mutableMapOf<String, Any>()
    
    override fun getString(key: String): String? = storage[key] as? String
    override fun putString(key: String, value: String) { storage[key] = value }
    override fun getInt(key: String, defaultValue: Int): Int = storage[key] as? Int ?: defaultValue
    override fun putInt(key: String, value: Int) { storage[key] = value }
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = storage[key] as? Boolean ?: defaultValue
    override fun putBoolean(key: String, value: Boolean) { storage[key] = value }
    override fun remove(key: String) { storage.remove(key) }
    override fun clear() { storage.clear() }
}

class TestAppClock(private val currentTime: Long = 1000L) : AppClock {
    override fun now(): Long = currentTime
    override fun currentTimeMillis(): Long = currentTime
    override fun formatDateTime(timestamp: Long, pattern: String): String = "formatted_$timestamp"
}

class GetAppInfoUseCaseTest {
    
    private lateinit var keyValueStore: KeyValueStore
    private lateinit var appClock: AppClock
    private lateinit var useCase: GetAppInfoUseCase
    
    @BeforeTest
    fun setup() {
        keyValueStore = TestKeyValueStore()
        appClock = TestAppClock(1234567890L)
        useCase = GetAppInfoUseCase(keyValueStore, appClock)
    }
    
    @Test
    fun testExecute_firstTime() = runTest {
        val result = useCase.execute()
        
        assertEquals("Template Android KMP", result.appName)
        assertEquals("1.0.0", result.version)
        assertEquals(1234567890L, result.currentTime)
        assertNull(result.lastOpenTime)
        
        // Verify that last open time was stored
        assertEquals("1234567890", keyValueStore.getString("last_open_time"))
    }
    
    @Test
    fun testExecute_subsequentTime() = runTest {
        // Set up a previous last open time
        keyValueStore.putString("last_open_time", "1000000000")
        
        val result = useCase.execute()
        
        assertEquals("Template Android KMP", result.appName)
        assertEquals("1.0.0", result.version)
        assertEquals(1234567890L, result.currentTime)
        assertEquals(1000000000L, result.lastOpenTime)
        
        // Verify that last open time was updated
        assertEquals("1234567890", keyValueStore.getString("last_open_time"))
    }
    
    @Test
    fun testExecute_invalidLastOpenTime() = runTest {
        // Set up an invalid last open time
        keyValueStore.putString("last_open_time", "invalid")
        
        val result = useCase.execute()
        
        assertEquals("Template Android KMP", result.appName)
        assertEquals("1.0.0", result.version)
        assertEquals(1234567890L, result.currentTime)
        assertNull(result.lastOpenTime)
        
        // Verify that last open time was updated
        assertEquals("1234567890", keyValueStore.getString("last_open_time"))
    }
}