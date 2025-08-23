# Core DataStore Module - MVP-03

Este módulo implementa el sistema de configuración y almacenamiento seguro para la aplicación Android, cumpliendo con los requerimientos del MVP-03.

## Características implementadas

### ✅ DataStore para Preferencias
- **SettingsDataStore**: Manejo reactivo de preferencias no sensibles
- **PreferencesRepository**: Wrapper que proporciona una interfaz limpia
- **Migración automática**: Desde SharedPreferences a DataStore
- **Valores por defecto**: Configuración consistente y predecible

### ✅ Almacenamiento Seguro
- **SecureStorage**: Cifrado de tokens y credenciales con Android Keystore
- **EncryptedSharedPreferences**: Persistencia segura de datos sensibles
- **Flujos reactivos**: Observación del estado de autenticación
- **Gestión de sesiones**: Login/logout automático

### ✅ Gestor Unificado
- **ConfigurationManager**: Interfaz única para toda la configuración
- **AppConfig**: Modelo de datos centralizado
- **Inicialización automática**: Migraciones y configuración al inicio
- **Manejo de errores**: Recuperación automática y reset de emergencia

## Uso básico

### Inyección de dependencias
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val configurationManager: ConfigurationManager
) : ViewModel()
```

### Observar configuración
```kotlin
// Observar configuración completa
configurationManager.appConfig.collect { config ->
    updateUI(config.isDarkTheme, config.listOrder)
}

// Observar estado de autenticación
configurationManager.isUserAuthenticated.collect { isAuth ->
    if (isAuth) navigateToHome() else navigateToLogin()
}
```

### Actualizar preferencias
```kotlin
// Cambiar tema
configurationManager.setDarkTheme(true)

// Cambiar orden de lista
configurationManager.setListOrder("ALPHABETICAL_ASC")
```

### Almacenamiento seguro
```kotlin
// Guardar tokens de autenticación
configurationManager.saveAuthTokens(authToken, refreshToken)

// Obtener token para API calls
val token = configurationManager.getAuthToken()

// Logout
configurationManager.logout()
```

## Migración desde SharedPreferences

El sistema detecta automáticamente datos existentes en SharedPreferences y los migra a DataStore:

```kotlin
// Datos migrados automáticamente:
// - theme_dark -> DataStore
// - list_order -> DataStore
// - SharedPreferences se limpia después de la migración
```

## Configuración disponible

### Preferencias (DataStore)
- `isDarkTheme: Boolean` - Tema oscuro activado
- `listOrder: String` - Orden de lista (CREATED_DESC, ALPHABETICAL_ASC, etc.)

### Almacenamiento seguro (EncryptedSharedPreferences)
- Tokens de autenticación
- Credenciales de usuario
- Claves API
- Cualquier dato sensible

## Pruebas

```bash
# Ejecutar pruebas unitarias
./gradlew :core:datastore:test

# Ejecutar pruebas de instrumentación
./gradlew :core:datastore:connectedAndroidTest
```

## Arquitectura

```
ConfigurationManager
├── PreferencesRepository
│   └── SettingsDataStore (DataStore)
└── SecureStorage (EncryptedSharedPreferences + Keystore)
```

## Consideraciones de seguridad

1. **Datos sensibles**: Nunca almacenar en DataStore, usar siempre SecureStorage
2. **Logging**: Los datos seguros nunca aparecen en logs
3. **Keystore**: Aprovecha el hardware de seguridad del dispositivo
4. **Cifrado**: AES256_GCM para valores, AES256_SIV para claves

## Migraciones futuras

Para agregar nuevas preferencias:

1. Añadir clave en `SettingsKeys`
2. Actualizar `AppConfig` con el nuevo campo
3. Implementar getter/setter en `SettingsDataStore`
4. Actualizar `PreferencesRepository` si es necesario

## Manejo de errores

El sistema incluye manejo robusto de errores:
- **Corrupción de datos**: Reset automático a valores por defecto
- **Errores de seguridad**: Limpieza de almacenamiento cifrado
- **Fallos de I/O**: Recuperación automática

## ADRs relevantes

- [ADR-0001](../../../Cases/Modulos/MVP-03/ADRs/adr-0001-almacenamiento-preferencias.md): Decisión de usar DataStore
- [ADR-0002](../../../Cases/Modulos/MVP-03/ADRs/adr-0002-seguros-y-secrets.md): Almacenamiento seguro con Keystore
