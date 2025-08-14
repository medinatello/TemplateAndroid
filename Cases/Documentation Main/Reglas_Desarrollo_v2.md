# Android — Reglas de Desarrollo v2

- Código y commits en inglés
- Arquitectura: Presentation → Domain → Data
- Kotlin conventions, evitar `!!`, preferir `val`
- Errores tipados; nada de excepciones crudas a UI
- UI: Compose + navegación tipada; estados explícitos
- i18n/a11y: strings en recursos; hit targets ≥48dp
- Testing: unit/integration/UI; meta 80%
- Análisis: ktlint/Detekt/Android Lint; macrobench si aplica
- Git: ramas por feature; Conventional Commits; PR template
- Observabilidad: Timber + exportador (OTLP/Crashlytics/Sentry)

## Estilo y naming
- Paquetes: `lowercase.with.dots`; clases `PascalCase`; funciones/props `camelCase`.
- Estados UI: `UiState` inmutable + `copy`; eventos `UiEvent` sellados.
- Use-cases: `VerbNounUseCase` en Domain.

## Manejo de errores
- Jerarquía: `NetworkError`, `SerializationError`, `AuthError`, `UnknownError`.
- Retries con backoff exponencial y cancelación cooperativa.
- No atrapar `Throwable` sin re-lanzar tipificado.

## Seguridad y secretos
- NUNCA hardcodear secretos. Keystore + EncryptedSharedPreferences. Revisar fugas en logs.
- Revisar exported components y permisos en manifiesto.

## PRs y revisión
- Checklist: build, lint, tests, screenshots/logs, impacto de rendimiento.
- Revisión por 2 devs o codeowner. Comentarios resolubles antes de merge.

## Cobertura y CI
- Umbral 80% líneas/branches en módulos core. Reportes almacenados como artefactos.
- Bloqueo de merge si fallan lint/tests.
