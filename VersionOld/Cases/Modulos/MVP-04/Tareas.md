# Tareas — MVP 04

En este archivo se desglosan las tareas necesarias para completar el **MVP 04**.  Cada
tarea cuenta con una **Definición de Hecho** (DoD) que sirve como criterio
objetivo de cierre.  Ajusta las subtareas según las historias de usuario
específicas de este sprint.

## T‑01 — Revisión de requisitos y planificación
**Objetivo**: Entender las historias de usuario del sprint 4 y definir el
alcance del trabajo.

**Definición de Hecho**:

- Se han leído y comprendido las historias de usuario asignadas.
- `DEVELOPMENT_STANDARDS.md` y el README general se han revisado para
  recordar los estándares.
- El equipo ha acordado las tareas a realizar y las ha documentado en este
  archivo.

## T‑02 — Implementación de funcionalidades
**Objetivo**: Desarrollar la lógica correspondiente a las historias de usuario
de este sprint.

**Definición de Hecho**:

- Los archivos necesarios han sido modificados o creados.
- El código respeta la arquitectura limpia y las convenciones del proyecto.
- La aplicación compila sin errores en la(s) plataforma(s) afectadas usando
  `./gradlew assembleDebug` o equivalente.

## T‑03 — Integración con módulo `shared` y expect/actual
**Objetivo**: Compartir la lógica común en `shared` y crear implementaciones
por plataforma cuando sea necesario.

**Definición de Hecho**:

- La lógica multiplataforma reside en `shared/commonMain` y se han creado
  las interfaces `expect` pertinentes.
- Las implementaciones `actual` para Android y Desktop están ubicadas en
  `androidMain` y `desktopMain` respectivamente.
- Las pruebas en `commonTest` verifican el comportamiento de la lógica común.

## T‑04 — Pruebas y cobertura
**Objetivo**: Validar el funcionamiento del código mediante pruebas
automáticas.

**Definición de Hecho**:

- Existen pruebas unitarias (y de integración si corresponde) que cubren
  al menos el 80 % de la lógica añadida.
- Los tests se ejecutan exitosamente con `./gradlew :shared:allTests` y los
  respectivos targets (`:androidApp:assembleDebug`, `:desktopApp:test`).
- Los casos límite y posibles errores se prueban explícitamente.

## T‑05 — Documentación y CI
**Objetivo**: Documentar los cambios y garantizar el correcto funcionamiento del
pipeline de CI.

**Definición de Hecho**:

- Se ha actualizado este archivo y el `README.md` con información precisa y
  se ha creado/actualizado `resultado.md` con el resumen del sprint.
- Se ha corrido localmente `./gradlew ktlintCheck detekt` y se han
  corregido los problemas detectados.
- Se ha verificado que la configuración de CI ejecuta todas las tareas de
  build, pruebas y análisis estático.

---
## Tareas Adicionales (Inspiradas por Mejoras del Proyecto Swift)

### T-06 — Configurar Inyección de Dependencias con Koin
**Objetivo**: Integrar Koin para gestionar las dependencias del proyecto de forma centralizada y escalable.
**Referencia**: `ADR-002-mvp04-dependency-injection.md`

**Definición de Hecho**:
- Las dependencias de Koin (`koin-core`, `koin-android`) están añadidas en los `build.gradle.kts` correspondientes.
- Se ha creado un módulo de Koin en `shared/commonMain` que define cómo construir `ApiClient`, repositorios y casos de uso.
- Koin se inicializa correctamente en el `Application` de Android y en el `main` de Desktop.
- Las dependencias en los ViewModels se obtienen a través de Koin (`by viewModel()` o `by inject()`).
- La creación manual de instancias en el código de la aplicación ha sido eliminada.

### T-07 — Implementar Sistema de Reintentos (Retry) en Ktor
**Objetivo**: Aumentar la resiliencia de la red implementando una política de reintentos automáticos para peticiones fallidas.

**Definición de Hecho**:
- Se ha añadido el plugin `HttpRequestRetry` de Ktor a la configuración del `HttpClient`.
- La política de reintentos está configurada para reintentar solo en errores de red o de servidor (códigos 5xx).
- Se ha configurado un `exponentialDelay` para evitar sobrecargar el servidor con reintentos inmediatos.
- Se ha establecido un número máximo de reintentos (ej. 3 intentos).
- Se han añadido pruebas unitarias que verifican que el cliente reintenta las peticiones cuando el mock server devuelve un error 503.

### T-08 — Implementar Capa de Caché Persistente con SQLDelight
**Objetivo**: Crear un servicio de caché para almacenar respuestas de la red en una base de datos local, mejorando el rendimiento y la capacidad offline.
**Referencia**: `ADR-003-mvp04-caching-strategy.md`

**Definición de Hecho**:
- El plugin y las dependencias de SQLDelight están configurados en el módulo `shared`.
- Se han creado los archivos `.sq` con el esquema de las tablas para el caché (ej. `UserEntity`). Las tablas incluyen una columna `cached_at` (timestamp).
- Se han definido las `queries` para insertar, seleccionar por ID, seleccionar todo y limpiar el caché.
- Se ha implementado el `expect/actual` para el `SqlDriver` en `androidMain` y `desktopMain`.
- Se ha creado un `CacheService` que implementa la lógica de TTL (Time-To-Live), decidiendo si un dato es válido o ha expirado.
- El `UserRepository` ha sido modificado para seguir la estrategia "Cache then Network":
    1. Intenta obtener datos del `CacheService`.
    2. Si hay datos válidos, los devuelve.
    3. Si no, realiza la petición de red.
    4. Al recibir la respuesta, la guarda en el caché y la devuelve.
- Existen pruebas unitarias para el `CacheService` que validan la lógica de expiración.

---

**Nota**: Ajusta o agrega tareas según lo exijan las historias de usuario del
sprint.  La claridad en la definición de tareas y sus criterios de cierre
facilita el trabajo colaborativo y la revisión posterior.