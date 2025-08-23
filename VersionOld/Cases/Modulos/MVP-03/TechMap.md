# ANDROID — TechMap

Este documento resume el stack tecnológico utilizado para la gestión de red y almacenamiento en Android y otros entornos. Aquí se detallan las tecnologías recomendadas y buenas prácticas para cada plataforma, con especial énfasis en la seguridad y la persistencia de datos.

## Android
- **Red:** Retrofit + OkHttp/Ktor para llamadas HTTP.
- **Serialización:** Moshi/kotlinx.serialization para manejo de JSON.
- **Almacenamiento de preferencias:** DataStore (Preferences/Proto) para datos no sensibles, con ejemplos de migración desde SharedPreferences.
- **Almacenamiento seguro:** Android Keystore + EncryptedSharedPreferences para secretos y tokens sensibles.

### Ejemplo de uso de DataStore
```kotlin
val THEME = booleanPreferencesKey("theme_dark")
val ds = context.dataStore
suspend fun setDark(v:Boolean) = ds.edit { it[THEME] = v }
val isDark = ds.data.map { it[THEME] ?: false }
```

### Ejemplo de uso de EncryptedSharedPreferences
```kotlin
val prefs = EncryptedSharedPreferences.create(
    "secret_prefs",
    masterKeyAlias,
    context,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
prefs.edit().putString("token", "valor_cifrado").apply()
```

## Otras plataformas
- **.NET:** HttpClient + System.Text.Json para red y serialización.
- **Go:** net/http + encoding/json para red y serialización.
- **Node:** undici/axios para red y manejo de datos.

**Nota:** Para almacenamiento seguro en Android, se recomienda DataStore para preferencias y Keystore/EncryptedSharedPreferences para secretos. Documentar migraciones y políticas de reseteo.
