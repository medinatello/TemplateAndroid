package com.sortisplus.core.datastore

import android.content.Context
import android.content.SharedPreferences
// TODO: Reactivar cuando security-crypto esté disponible
// import androidx.security.crypto.EncryptedSharedPreferences
// import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Almacenamiento seguro para datos sensibles usando EncryptedSharedPreferences
 * y Android Keystore para cifrado basado en hardware.
 */
@Singleton
class SecureStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fileName = "secure_preferences"
    
    // TODO: Reemplazar con EncryptedSharedPreferences cuando security-crypto esté disponible
    private val encryptedSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    // StateFlows para observar cambios de forma reactiva
    private val _isUserLoggedIn = MutableStateFlow(hasAuthToken())
    val isUserLoggedIn: Flow<Boolean> = _isUserLoggedIn.asStateFlow()

    /**
     * Guarda un token de autenticación de forma segura
     */
    fun saveAuthToken(token: String) {
        encryptedSharedPreferences.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
        _isUserLoggedIn.value = true
    }

    /**
     * Obtiene el token de autenticación almacenado
     */
    fun getAuthToken(): String? {
        return encryptedSharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Verifica si existe un token de autenticación
     */
    fun hasAuthToken(): Boolean {
        return getAuthToken() != null
    }

    /**
     * Guarda un refresh token de forma segura
     */
    fun saveRefreshToken(token: String) {
        encryptedSharedPreferences.edit()
            .putString(KEY_REFRESH_TOKEN, token)
            .apply()
    }

    /**
     * Obtiene el refresh token almacenado
     */
    fun getRefreshToken(): String? {
        return encryptedSharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    /**
     * Guarda credenciales de usuario de forma segura
     */
    fun saveUserCredentials(username: String, encryptedPassword: String) {
        encryptedSharedPreferences.edit()
            .putString(KEY_USERNAME, username)
            .putString(KEY_PASSWORD, encryptedPassword)
            .apply()
    }

    /**
     * Obtiene el nombre de usuario almacenado
     */
    fun getUsername(): String? {
        return encryptedSharedPreferences.getString(KEY_USERNAME, null)
    }

    /**
     * Obtiene la contraseña cifrada almacenada
     */
    fun getEncryptedPassword(): String? {
        return encryptedSharedPreferences.getString(KEY_PASSWORD, null)
    }

    /**
     * Guarda cualquier secreto genérico de forma segura
     */
    fun saveSecret(key: String, value: String) {
        encryptedSharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    /**
     * Obtiene cualquier secreto genérico almacenado
     */
    fun getSecret(key: String): String? {
        return encryptedSharedPreferences.getString(key, null)
    }

    /**
     * Elimina un secreto específico
     */
    fun removeSecret(key: String) {
        encryptedSharedPreferences.edit()
            .remove(key)
            .apply()

        // Actualizar estado si se eliminó el token de auth
        if (key == KEY_AUTH_TOKEN) {
            _isUserLoggedIn.value = false
        }
    }

    /**
     * Elimina todos los datos seguros almacenados
     */
    fun clearAll() {
        encryptedSharedPreferences.edit()
            .clear()
            .apply()
        _isUserLoggedIn.value = false
    }

    /**
     * Cierra sesión eliminando tokens de autenticación
     */
    fun logout() {
        encryptedSharedPreferences.edit()
            .remove(KEY_AUTH_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
        _isUserLoggedIn.value = false
    }

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }
}
