# TAREAS — MVP-03.5 KMP

> Todas las tareas deben respetar los estándares definidos en `Cases/Documentation Main`.

## T-01 — Configuración de módulos y targets
**Objetivo**: Crear el módulo `shared` y `desktopApp`, e incluir los targets `androidTarget()` y `jvm("desktop")`.

**Definición de Hecho**:
- Los módulos `shared` y `desktopApp` aparecen en `settings.gradle.kts`.
- Se definen los source sets `commonMain/commonTest/androidMain/desktopMain`.
- Los comandos `./gradlew :androidApp:assembleDebug` y `./gradlew :desktopApp:run` se ejecutan sin errores.

## T-02 — Dependencias base multiplataforma
**Objetivo**: Añadir Ktor, SQLDelight, Multiplatform‑Settings y Koin al módulo `shared`.

**Definición de Hecho**:
- El `build.gradle.kts` de `shared` incluye las dependencias y plugins necesarios.
- `./gradlew :shared:assemble` se ejecuta sin errores.
- Prueba simple que referencia cada librería compilando correctamente.

## T-03 — Implementar `expect/actual` inicial
**Objetivo**: Definir y concretar las abstracciones `KeyValueStore`, `AppClock`, `DbDriverFactory` y `httpClient`.

**Definición de Hecho**:
- Las interfaces `expect` están declaradas en `commonMain`.
- Las implementaciones `actual` están presentes en `androidMain` y `desktopMain`.
- Existen pruebas unitarias en `commonTest` que validan el comportamiento de `KeyValueStore` y `AppClock`.
- La cobertura de código del módulo `shared` es igual o superior al 80%.

## T-04 — Inyección de dependencias (DI) en `shared` y puente con Hilt
**Objetivo**: Configurar Koin en el módulo `shared` y establecer el puente con Hilt para la UI de Android.

**Definición de Hecho**:
- Se define un módulo de Koin con casos de uso, repositorios y clientes.
- Se inicializa Koin en el punto de entrada de Android (`Application`).
- Existe documentación breve o ADR que explique el puente entre Hilt y Koin.
- La aplicación Android arranca correctamente (`assembleDebug` + ejecución).

## T-05 — Aplicación de escritorio mínima
**Objetivo**: Crear una app de escritorio con Compose que utilice código de `shared`.

**Definición de Hecho**:
- El módulo `desktopApp` compila y se ejecuta con `./gradlew :desktopApp:run`.
- Se muestra una ventana con un texto estático.
- La app de escritorio llama a un caso de uso definido en `shared` y muestra su resultado en la consola o UI.

## T-06 — Calidad de código y CI
**Objetivo**: Garantizar estándares de calidad y configurar un pipeline de integración continua.

**Definición de Hecho**:
- Las tareas `ktlintCheck` y `detekt` están configuradas y pasan sin errores.
- Existen scripts de CI (por ejemplo, GitHub Actions) que ejecutan: `:shared:allTests`, `:androidApp:assembleDebug` y `:desktopApp:test`.
- Los reportes de pruebas y cobertura se generan y publican como artefactos del pipeline.