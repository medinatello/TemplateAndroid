package com.sortisplus.core.datastore

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Inicializador que se ejecuta al inicio de la aplicación para configurar
 * el sistema de almacenamiento y realizar migraciones necesarias.
 */
@Singleton
class DataStoreInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val configurationManager: ConfigurationManager
) {

    private val initScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Inicializa el sistema de almacenamiento de datos
     */
    fun initialize() {
        initScope.launch {
            try {
                // Ejecutar migraciones y configuraciones iniciales
                configurationManager.initialize()

                // Log para debugging (en desarrollo)
                android.util.Log.d("DataStoreInitializer", "DataStore initialization completed successfully")
            } catch (e: Exception) {
                // Manejo de errores durante la inicialización
                android.util.Log.e("DataStoreInitializer", "Error during DataStore initialization", e)

                // En caso de error crítico, se podría ejecutar un reset de emergencia
                handleInitializationError(e)
            }
        }
    }

    private suspend fun handleInitializationError(error: Exception) {
        try {
            when (error) {
                is SecurityException -> {
                    // Error relacionado con el almacenamiento seguro
                    android.util.Log.w("DataStoreInitializer", "Security error, clearing secure storage")
                    configurationManager.clearAllData()
                }
                is java.io.IOException -> {
                    // Error de I/O, posiblemente corrupción de archivos
                    android.util.Log.w("DataStoreInitializer", "I/O error, performing emergency reset")
                    configurationManager.emergencyReset()
                }
                else -> {
                    // Otros errores, log pero continuar
                    android.util.Log.w("DataStoreInitializer", "Unexpected error during initialization: ${error.message}")
                }
            }
        } catch (resetError: Exception) {
            android.util.Log.e("DataStoreInitializer", "Failed to handle initialization error", resetError)
        }
    }
}
