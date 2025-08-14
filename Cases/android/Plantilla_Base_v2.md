# Android — Plantilla Base v2

**Objetivo**: base para escalado, compatible con Android 14 y preparada para futuras versiones. Esta plantilla se alinea con 12 sprints (MVP-01..12) descritos en `docs/Mapa_de_modulos.md` y detallados en `docs/TechMap.md`.

## Estructura de módulos
```
app/ (arranque, navegación, DI raíz)
core/ui/ (tema, Design System Compose, adaptive)
core/data/ (contratos repos, models)
core/database/ (Room, migraciones, DAOs)
core/datastore/ (preferencias)
core/network/ (Retrofit/OkHttp o Ktor, interceptores)
core/work/ (WorkManager helpers)
core/auth/ (Credential Manager, Secure Storage)
core/telemetry/ (logs, métricas, trazas; fachada OTEL)
feature/<x>/ (funcionalidades)
build-logic/ (plugins y convenciones Gradle opcional)
```

Recomendado: separar `:core` (estable) de `:feature:*` (iterables por sprint). Mantener dependencias unidireccionales (feature → core), y contratos en `core/data`/`domain`.

## Wrappers por versión y preview
- `Build.VERSION.PREVIEW_SDK_INT > 0` para detectar previews
- Interfaces por capability y factories por `SDK_INT`

## Theming (Material 3 + Dynamic Color)
- `dynamicColorScheme` si SDK ≥ 31; fallback a paleta fija

## Red/Errores
- `Result<T>` sellado; interceptores Auth/Logging/Retry
- Taxonomía: Network/Serialization/Auth/Unknown

## Observabilidad
- Timber (debug) + exportador (OTLP/AI/Sentry) en prod
- Métricas: latencia, error rate, ANR, cold start

## CI/CD
 - Lint + Unit + androidTest + assemble + artefactos
 - Puertas: cobertura ≥80%, sin Lint crítico

## Planeación por sprint (resumen)
- MVP-01 Fundamentos: crear módulos, flavors (`dev|stg|prod`), DI raíz y `BuildConfig`.
- MVP-02 UI: Compose + Navigation, rutas tipadas, validación y a11y mínima.
- MVP-03 Config: DataStore/EncryptedPrefs, modelo `AppConfig` y migraciones.
- MVP-04 API: Retrofit/Ktor, interceptores, DTOs y `Result<T>`.
- MVP-05 Auth: OAuth2/OIDC + Credential Manager, tokens en Keystore.
- MVP-06 Sesión: `SavedStateHandle`, bloqueo por inactividad y restauración.
- MVP-07 Background: WorkManager con constraints, sync idempotente.
- MVP-08 Telemetría: Timber + OTel/Crashlytics, métricas clave.
- MVP-09 Archivos: upload/download con progreso y reanudación.
- MVP-10 Notificaciones: FCM, canales, deep links.
- MVP-11 i18n/a11y/theming: strings, Dynamic Type/Color.
- MVP-12 Pruebas/CI: pipelines, cobertura, firmas.

## Configuración de entornos (ejemplo)
- Flavors: `dev`, `stg`, `prd`. Variables en `build.gradle.kts` → `BuildConfig`.
- Secretos: nunca en VCS; usar `local.properties` o inyección por CI y Keystore.
- Ejemplo:
  - `BuildConfig.API_BASE_URL`
  - `BuildConfig.FEATURE_FLAGS_JSON`

## Nombres y paquetes
- Paquete raíz: `com.tuorg.tuapp` (único). Módulos `:core:*` y `:feature:*` con nombres descriptivos.
- Ramas: `feat/`, `fix/`, `chore/`, `docs/`. Commits convencionales.
