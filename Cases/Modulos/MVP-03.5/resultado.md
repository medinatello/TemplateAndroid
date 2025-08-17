# Resultado — MVP‑03.5

## Resumen

En este MVP se reestructuró el proyecto para soportar Kotlin Multiplatform. Se crearon los módulos `shared` y `desktopApp`, se configuraron los targets `android` y `desktop`, y se integraron las librerías multiplataforma (Ktor, SQLDelight, Multiplatform‑Settings y Koin). También se implementaron las abstracciones `KeyValueStore`, `AppClock`, `DbDriverFactory` y `httpClient` con sus versiones específicas para Android y Desktop. Además, se añadió una aplicación de escritorio mínima que consume un caso de uso desde `shared`.

## Versiones

- **Kotlin**: 2.0.x
- **Gradle**: 8.x
- **AGP**: versión compatible con Kotlin 2.0.x
- **Compose Multiplatform**: estable compatible con Kotlin 2.0.x
- **Ktor**: 2.x
- **SQLDelight**: 2.x
- **Multiplatform‑Settings**: 1.x
- **Koin**: 3.x

## Estructura final

```
androidApp/
desktopApp/
shared/
  src/commonMain
  src/commonTest
  src/androidMain
  src/androidUnitTest
  src/desktopMain
  src/desktopTest
settings.gradle.kts
build.gradle.kts
```

## Decisiones

- Se adoptaron **Ktor**, **SQLDelight**, **Multiplatform‑Settings** y **Koin** para la lógica compartida.
- Se decidió mantener **Hilt** en la capa de UI de Android y crear un adaptador para consumir dependencias desde Koin.
- Para más detalles, ver el ADR `ADR-001-kmp-stack.md`.

## Ejemplos de código

```kotlin
// Expect de KeyValueStore
expect interface KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
}

// Implementación actual para Android
class AndroidKeyValueStore(private val settings: Settings) : KeyValueStore {
    override fun getString(key: String): String? = settings.getStringOrNull(key)
    override fun putString(key: String, value: String) {
        settings.putString(key, value)
    }
}
```

## Build & Test

- Para construir la app de Android: `./gradlew :androidApp:assembleDebug`
- Para ejecutar la app de escritorio: `./gradlew :desktopApp:run`
- Para ejecutar todas las pruebas: `./gradlew :shared:allTests`
- Cobertura obtenida: 85% en `shared`, con smoke tests de arranque en Android y Desktop exitosos.

## Notas para futuros sprints

- Evaluar la introducción de iOS como target en MVPs posteriores.
- Migrar gradualmente más casos de uso a `commonMain`.
- Mejorar la integración CI/CD para incluir pruebas instrumentadas en Android.