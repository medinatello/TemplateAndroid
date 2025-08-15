# MVP-01 — Fundamentos y arquitectura (Android)

## Historias de usuario
- Como desarrollador, quiero una estructura modular (`app/`, `core/*`, `feature/*`) para escalar sin deuda.
- Como DevOps, quiero flavors `dev|stg|prd` para desplegar a ambientes separados.
- Como equipo, quiero DI raíz para aislar dependencias y facilitar pruebas.

## Criterios de aceptación
- Proyecto compila con módulos `core/ui`, `core/network`, `core/datastore` (placeholders).
- Flavors definidos exponen `BuildConfig.API_BASE_URL` y `FEATURE_FLAGS_JSON`.
- DI inicial registra al menos un servicio compartido (p. ej. `Clock`).

## Entregables
- Estructura de carpetas y módulos Gradle KTS.
- Config de flavors y `BuildConfig`.
- Módulo DI (`CoreModule`).

## Estimación
- 5–8 puntos. 1–2 días.

## DoR
- Nombres de paquete y dominios aprobados. Valores de API base por entorno.

## DoD
- Compila `debug` y `release` en local. CI pasa `lint` y `test` (aunque sean mínimos).

## Riesgos
- Config duplicada entre módulos → mitigar con versión de catálogo/convenciones.

## Métricas
- Build reproducible en 3 entornos. Tiempo de build inicial < 2 min en CI.

## Comandos
- `./gradlew :app:assembleDebug`

## Código de ejemplo
```kotlin
// settings.gradle.kts
include(":app", ":core:ui", ":core:network", ":core:datastore")

// App.kt
@HiltAndroidApp class App: Application()

// CoreModule.kt
@Module @InstallIn(SingletonComponent::class)
object CoreModule {
  @Provides fun clock(): Clock = Clock.systemUTC()
}
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Estructura base del proyecto
  Scenario: Compilación en entornos dev y prod
    Given que inicializo los módulos app y core
    And configuro flavors "dev" y "prd"
    When compilo el proyecto en ambos sabores
    Then la build finaliza sin errores
    And BuildConfig expone API_BASE_URL y FEATURE_FLAGS_JSON
```

## DoR extendido
- Paquete raíz definido y aprobado.
- URLs de API por entorno acordadas.
- Lista de módulos core a crear.

## DoD extendido
- CI compila `debug` y `release` con cache activa.
- Convenciones de nombre y DI documentadas en README del módulo.
