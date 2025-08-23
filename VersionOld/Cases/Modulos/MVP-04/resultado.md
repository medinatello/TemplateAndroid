# resultado — MVP 04

## Resumen

En el MVP 04 se implementaron tres funcionalidades principales para mejorar la robustez y escalabilidad de la plantilla multiplataforma:

1. **Consolidación de Inyección de Dependencias con Koin**: Se refinó la configuración de Koin como el sistema de DI principal para el módulo `shared`, manteniendo la compatibilidad con Hilt en el módulo Android.

2. **Sistema de Caché Persistente con SQLDelight**: Se implementó un servicio de caché con estrategia TTL (Time-To-Live) utilizando SQLDelight para almacenar respuestas de red y mejorar el rendimiento offline.

3. **Arquitectura "Cache-then-Network"**: Se creó un repositorio de ejemplo (UserRepository) que implementa la estrategia de caché híbrida, priorizando datos en caché válidos y recurriendo a la red cuando es necesario.

Todas las funcionalidades fueron desarrolladas siguiendo la arquitectura limpia y los estándares definidos en `DEVELOPMENT_STANDARDS.md`, integrándose correctamente con el módulo `shared` y manteniendo la compatibilidad multiplataforma.

## Versiones utilizadas

- Kotlin: 2.0.0
- AGP (Android Gradle Plugin): 8.12.0
- Compose Multiplatform: 1.7.1
- Ktor: 2.3.12
- SQLDelight: 2.0.2
- Koin: 3.5.6
- Kotlinx Serialization: 1.6.3
- Kotlinx Coroutines: 1.8.1

## Estructura final del proyecto

```
shared/
├── src/
│   ├── commonMain/kotlin/com/sortisplus/shared/
│   │   ├── data/
│   │   │   ├── cache/
│   │   │   │   └── CacheService.kt              # Servicio de caché con TTL
│   │   │   └── repository/
│   │   │       ├── UserRepositoryImpl.kt        # Implementación cache-then-network
│   │   │       └── [otros repositorios existentes]
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   └── User.kt                      # Modelo de datos serializable
│   │   │   └── repository/
│   │   │       └── UserRepository.kt            # Interface del repositorio
│   │   └── di/
│   │       └── SharedModule.kt                  # Configuración Koin actualizada
│   ├── commonMain/sqldelight/com/sortisplus/shared/database/
│   │   └── AppDatabase.sq                       # Esquema SQLDelight con tabla de caché
│   └── commonTest/kotlin/com/sortisplus/shared/
│       └── data/cache/
│           └── CacheServiceTest.kt              # Pruebas unitarias del caché
```

## Decisiones y justificaciones

### ADR-002: Inyección de Dependencias con Koin
- **Decisión**: Mantener Koin como sistema de DI principal para el módulo `shared`
- **Justificación**: Koin es multiplataforma y se integra naturalmente con KMP, permitiendo compartir la configuración de dependencias entre Android y Desktop
- **Implementación**: Se consolidó la configuración en `SharedModule.kt` con módulos separados para dominio, datos y presentación

### ADR-003: Estrategia de Caché con SQLDelight
- **Decisión**: Implementar un servicio de caché persistente con TTL usando SQLDelight
- **Justificación**: SQLDelight proporciona type-safety y es multiplataforma, ideal para KMP
- **Implementación**: Tabla `CacheEntry` con campos `key`, `data`, `cachedAt`, `expiresAt`

### Estrategia Cache-then-Network
- **Decisión**: Implementar patrón híbrido de caché para repositorios de red
- **Justificación**: Mejora la UX con respuestas rápidas del caché mientras mantiene datos actualizados
- **Flujo**: Cache válido → Respuesta inmediata / Cache inválido → Red → Actualizar caché

## Ejemplos de código

### CacheService con TTL
```kotlin
suspend fun get(key: String): String? {
    val currentTime = appClock.currentTimeMillis()
    val cacheEntry = database.appDatabaseQueries.selectCacheByKey(
        key,
        currentTime
    ).executeAsOneOrNull()
    
    return cacheEntry?.data_
}

suspend fun put(key: String, data: String, ttl: Duration = 1.hours) {
    val currentTime = appClock.currentTimeMillis()
    val expiresAt = currentTime + ttl.inWholeMilliseconds
    
    database.appDatabaseQueries.insertOrReplaceCacheEntry(
        key, data, currentTime, expiresAt
    )
}
```

### Estrategia Cache-then-Network
```kotlin
suspend fun getAllUsers(): List<User> {
    // 1. Intentar caché
    val cachedData = cacheService.get("users_all")
    if (cachedData != null) {
        return Json.decodeFromString<List<User>>(cachedData)
    }
    
    // 2. Si no hay caché, solicitud de red
    try {
        val response = httpClient.get("$baseUrl/users")
        if (response.status == HttpStatusCode.OK) {
            val users: List<User> = response.body()
            
            // 3. Cachear la respuesta
            cacheService.put("users_all", Json.encodeToString(users), ttl = 1.hours)
            
            return users
        }
    } catch (e: Exception) {
        // 4. Fallback a base de datos local
        return getUsersFromDatabase()
    }
    
    return getUsersFromDatabase()
}
```

## Build & Test

### Comandos utilizados:
```bash
# Compilación completa
./gradlew assembleDebug

# Pruebas unitarias del módulo shared
./gradlew :shared:testDebugUnitTest

# Análisis estático
./gradlew ktlintCheck detekt
```

### Resultados:
- ✅ Build exitoso para Android Debug
- ✅ Módulo `shared` compila correctamente para Android y Desktop
- ✅ Integración de Koin funcional sin conflictos con Hilt
- ✅ Tablas SQLDelight creadas correctamente con migración automática
- ✅ CacheService con pruebas unitarias implementadas

### Cobertura de pruebas:
- CacheService: Pruebas de TTL, expiración, y operaciones CRUD
- Pruebas de integración pendientes para UserRepository (recomendado para MVP-05)

## Notas para futuros sprints

### Deuda técnica identificada:
1. **Sistema de Reintentos**: Ktor 2.3.12 no incluye `HttpRequestRetry` por defecto. Considerar actualizar a Ktor 3.x o implementar retry manual en MVP-05
2. **Pruebas de UserRepository**: Implementar pruebas de integración que validen la estrategia cache-then-network
3. **Limpieza automática de caché**: Considerar un job en background que limpie entradas expiradas

### Recomendaciones:
1. **MVP-05**: Implementar autenticación aprovechando el CacheService para tokens
2. **Monitoreo**: Agregar métricas de cache hit/miss ratio
3. **Configuración**: Hacer configurable el TTL por tipo de datos
4. **Testing**: Crear un `TestCacheService` para pruebas más robustas

### Riesgos mitigados:
- ✅ Conflictos entre Koin y Hilt resueltos manteniendo separación de responsabilidades
- ✅ Performance mejorada con caché persistente
- ✅ Estrategia de fallback implementada para escenarios offline