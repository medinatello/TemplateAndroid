# Core Network Module

Este módulo proporciona la configuración base para comunicación HTTP en la aplicación Android Template.

## Características

- **Retrofit**: Cliente HTTP configurado para APIs REST
- **OkHttp**: Cliente HTTP con interceptores de logging
- **NetworkResult**: Wrapper para manejo tipado de respuestas de red
- **Hilt Integration**: Módulo de DI para inyección de dependencias

## Configuración

### Dependencias principales
- Retrofit 2.9.0
- OkHttp 4.12.0
- Hilt (para DI)

### URLs de API por entorno

Las URLs se configuran automáticamente según el flavor de build:

- **dev**: `https://api-dev.sortisplus.com/`
- **stg**: `https://api-staging.sortisplus.com/`
- **prd**: `https://api.sortisplus.com/`

## Uso

### NetworkResult

```kotlin
// Ejemplo de uso del wrapper NetworkResult
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

// Extensiones para manejo fluido
result
    .onSuccess { data -> /* manejar éxito */ }
    .onError { exception -> /* manejar error */ }
    .onLoading { /* mostrar loading */ }
```

### Inyección de dependencias

El módulo `NetworkModule` registra automáticamente:
- `OkHttpClient` con logging interceptor
- `Retrofit` instance configurada
- HTTP logging interceptor (solo en DEBUG)

## Convenciones

- Todas las clases y funciones están en inglés
- Documentación KDoc para APIs públicas
- Manejo de errores tipado con NetworkResult
- Logging estructurado para debugging

## Próximos pasos

- Agregar interceptores de autenticación
- Implementar reintentos automáticos
- Agregar métricas de red para observabilidad
