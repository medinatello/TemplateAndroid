package com.sortisplus.core.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecureStorageTest {

    private lateinit var context: Context
    private lateinit var secureStorage: SecureStorage

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        secureStorage = SecureStorage(context)
    }

    @After
    fun cleanup() {
        secureStorage.clearAll()
    }

    @Test
    fun testSaveAndGetAuthToken() {
        val testToken = "test_auth_token_12345"

        secureStorage.saveAuthToken(testToken)
        val retrievedToken = secureStorage.getAuthToken()

        assertEquals("Auth token should match saved value", testToken, retrievedToken)
        assertTrue("Should have auth token", secureStorage.hasAuthToken())
    }

    @Test
    fun testSaveAndGetRefreshToken() {
        val testRefreshToken = "test_refresh_token_67890"

        secureStorage.saveRefreshToken(testRefreshToken)
        val retrievedToken = secureStorage.getRefreshToken()

        assertEquals("Refresh token should match saved value", testRefreshToken, retrievedToken)
    }

    @Test
    fun testSaveAndGetUserCredentials() {
        val username = "testuser@example.com"
        val encryptedPassword = "encrypted_password_hash"

        secureStorage.saveUserCredentials(username, encryptedPassword)

        assertEquals("Username should match", username, secureStorage.getUsername())
        assertEquals("Password should match", encryptedPassword, secureStorage.getEncryptedPassword())
    }

    @Test
    fun testSaveAndGetGenericSecret() {
        val key = "api_key"
        val value = "secret_api_key_value"

        secureStorage.saveSecret(key, value)
        val retrievedValue = secureStorage.getSecret(key)

        assertEquals("Secret should match saved value", value, retrievedValue)
    }

    @Test
    fun testRemoveSecret() {
        val key = "temp_secret"
        val value = "temporary_value"

        secureStorage.saveSecret(key, value)
        assertNotNull("Secret should exist", secureStorage.getSecret(key))

        secureStorage.removeSecret(key)
        assertNull("Secret should be removed", secureStorage.getSecret(key))
    }

    @Test
    fun testLogout() = runTest {
        val authToken = "auth_token_123"
        val refreshToken = "refresh_token_456"

        secureStorage.saveAuthToken(authToken)
        secureStorage.saveRefreshToken(refreshToken)

        assertTrue("Should be logged in", secureStorage.isUserLoggedIn.first())

        secureStorage.logout()

        assertNull("Auth token should be removed", secureStorage.getAuthToken())
        assertNull("Refresh token should be removed", secureStorage.getRefreshToken())
        assertFalse("Should not be logged in", secureStorage.isUserLoggedIn.first())
    }

    @Test
    fun testClearAll() {
        val authToken = "auth_token_123"
        val username = "testuser"
        val apiKey = "api_key_value"

        secureStorage.saveAuthToken(authToken)
        secureStorage.saveUserCredentials(username, "password")
        secureStorage.saveSecret("api_key", apiKey)

        secureStorage.clearAll()

        assertNull("Auth token should be cleared", secureStorage.getAuthToken())
        assertNull("Username should be cleared", secureStorage.getUsername())
        assertNull("API key should be cleared", secureStorage.getSecret("api_key"))
        assertFalse("Should not have auth token", secureStorage.hasAuthToken())
    }

    @Test
    fun testUserLoggedInStateFlow() = runTest {
        // Inicialmente sin token
        assertFalse("Should not be logged in initially", secureStorage.isUserLoggedIn.first())

        // Guardar token
        secureStorage.saveAuthToken("test_token")
        assertTrue("Should be logged in after saving token", secureStorage.isUserLoggedIn.first())

        // Remover token
        secureStorage.removeSecret("auth_token")
        assertFalse("Should not be logged in after removing token", secureStorage.isUserLoggedIn.first())
    }
}
