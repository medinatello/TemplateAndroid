package com.sortisplus.shared

import io.ktor.client.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

@Serializable
data class TestData(val message: String)

class LibraryTest {
    
    @Test
    fun testKotlinxSerialization() {
        val data = TestData("Hello KMP")
        val json = Json.encodeToString(TestData.serializer(), data)
        val decoded = Json.decodeFromString(TestData.serializer(), json)
        assertEquals("Hello KMP", decoded.message)
    }
    
    @Test
    fun testKtor() = runTest {
        val client = HttpClient()
        assertNotNull(client)
        client.close()
    }
    
    @Test
    fun testKoin() {
        startKoin {
            modules(
                module {
                    single { "Test Service" }
                }
            )
        }
        
        val koinApp = org.koin.core.context.GlobalContext.get()
        assertNotNull(koinApp)
        
        stopKoin()
    }
}