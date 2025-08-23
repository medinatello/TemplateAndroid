package com.sortisplus.core.datastore

/**
 * Modelo de datos para la configuración de la aplicación.
 * Centraliza todas las preferencias y configuraciones del usuario.
 */
data class AppConfig(
    val isDarkTheme: Boolean = false,
    val listOrder: String = "CREATED_DESC",
    val language: String = "es",
    val notificationsEnabled: Boolean = true,
    val autoBackupEnabled: Boolean = false,
    val cacheSize: Int = 50, // MB
    val sessionTimeout: Long = 30 * 60 * 1000L // 30 minutos en ms
) {
    companion object {
        /**
         * Configuración por defecto de la aplicación
         */
        fun defaultConfig() = AppConfig()

        /**
         * Opciones válidas para el orden de lista
         */
        object ListOrderOptions {
            const val CREATED_ASC = "CREATED_ASC"
            const val CREATED_DESC = "CREATED_DESC"
            const val ALPHABETICAL_ASC = "ALPHABETICAL_ASC"
            const val ALPHABETICAL_DESC = "ALPHABETICAL_DESC"
            const val PRIORITY_HIGH_FIRST = "PRIORITY_HIGH_FIRST"
            const val PRIORITY_LOW_FIRST = "PRIORITY_LOW_FIRST"

            fun getAllOptions() = listOf(
                CREATED_ASC,
                CREATED_DESC,
                ALPHABETICAL_ASC,
                ALPHABETICAL_DESC,
                PRIORITY_HIGH_FIRST,
                PRIORITY_LOW_FIRST
            )
        }

        /**
         * Idiomas soportados
         */
        object SupportedLanguages {
            const val SPANISH = "es"
            const val ENGLISH = "en"
            const val PORTUGUESE = "pt"

            fun getAllLanguages() = listOf(SPANISH, ENGLISH, PORTUGUESE)
        }
    }
}
