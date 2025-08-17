# TAREAS — MVP-03.5 KMP

> Todas las tareas deben respetar estándares de `Cases/Documentation Main`.

## T-01 — Configuración de módulos y targets
**Objetivo:** Crear `shared` y `desktopApp`; configurar `androidTarget()` y `jvm("desktop")`.

**Definición de Hecho:**
- Los módulos `shared` y `desktopApp` están declarados en `settings.gradle.kts`.
- Existen los source sets `commonMain`, `commonTest`, `androidMain` y `desktopMain`.
- Los comandos `./gradlew :androidApp:assembleDebug` y `./gradlew :desktopApp:run` se ejecutan sin errores.

## T-02 — Dependencias base KMP
**Objetivo:** Añadir Ktor, SQLDelight, Multiplatform‑Settings y Koin al módulo `shared`.

**Definición de Hecho:**
- El archivo `build.gradle.kts` de `shared` incluye las dependencias y plugins necesarios (incluido el plugin de SQLDelight).
- El comando `./gradlew :shared:assemble` se ejecuta sin errores.
- Hay una prueba simple que referencia cada librería y compila correctamente.

## T-03 — Expect/actual inicial
**Objetivo:** Definir interfaces `expect` e implementar `actual` para las superficies clave.

**Definición de Hecho:**
- Se declaran `KeyValueStore`, `AppClock`, `DbDriverFactory` y `httpClient` en `commonMain`.
- Se implementan las versiones `actual` en `androidMain` y `desktopMain`.
- Las pruebas en `commonTest` validan el funcionamiento de `KeyValueStore` y `AppClock` con cobertura ≥ 80 % en estas clases.

## T-04 — Inyección de dependencias en `shared` y puente en Android
**Objetivo:** Configurar Koin en `shared` y conectar con Hilt en la UI de Android.

**Definición de Hecho:**
- Los módulos de Koin que exponen repositorios, casos de uso y clientes están definidos.
- Koin se inicializa adecuadamente (por ejemplo, en la clase `Application`).
- Existe un documento breve que explica cómo coexistir con Hilt en la capa de presentación.
- Un smoke test confirma el arranque exitoso de la app Android (`./gradlew :androidApp:assembleDebug`).

## T-05 — Aplicación de escritorio mínima
**Objetivo:** Crear un módulo `desktopApp` con Compose Desktop que consuma un caso de uso de `shared`.

**Definición de Hecho:**
- El comando `./gradlew :desktopApp:run` abre una ventana con texto.
- La aplicación de escritorio invoca un caso de uso de `shared` y muestra el resultado.

## T-06 — Calidad y CI
**Objetivo:** Garantizar la calidad del código y configurar la integración continua.

**Definición de Hecho:**
- Se agregan las tareas `ktlintCheck` y `detekt` al proceso de build.
- El pipeline de CI ejecuta `:shared:allTests`, `:androidApp:assembleDebug`, `:desktopApp:test` y publica los informes de tests y cobertura.
- La cobertura de código en el módulo `shared` es al menos del 80 %.
