# Revisión de Estado — Migración a Esquema Modular v2

Este documento compara el estado actual del proyecto con las tareas definidas en los MVPs bajo el nuevo esquema modular. Se revisa cada MVP, se listan las tareas, se indica si están cubiertas en el código actual, y se identifican pendientes o incompletos. Al final, se listan tareas que estaban en el modelo anterior y no se han migrado al nuevo.

---

## Referencias

- **Nuevo esquema:** [Plantilla_Base_v2.md](../Cases/android/Plantilla_Base_v2.md)
- **Reglas de desarrollo:** [reglas_desarrollo.md](../Cases/reglas_desarrollo.md), [Reglas_Desarrollo_v2.md](../Cases/android/Reglas_Desarrollo_v2.md)
- **Tareas por módulo:** [modulos/](../Cases/android/modulos/)

---

## Estado por MVP

### MVP-01 · Fundamentos
**Tareas:**
- Crear módulos base (`app`, `core/*`, `feature/*`)
- Configurar flavors (`dev|stg|prod`)
- DI raíz y `BuildConfig`

**Estado:**
- Módulos base presentes (`app`, `core/*`, `feature/home`)
- Flavors: revisar configuración en `build.gradle.kts` y `BuildConfig`
- DI raíz: verificar uso de Hilt o alternativa

**Pendientes/Incompletos:**
- Validar flavors en todos los módulos
- Documentar DI raíz según nueva estructura

---

### MVP-02 · UI: Compose + Navegación
**Tareas:**
- Compose + Material 3
- Rutas tipadas y navegación
- Validación y accesibilidad mínima

**Estado:**
- Uso de Compose y Material 3 en `core/ui`
- Navegación tipada: revisar implementación en `app` y `feature/home`
- Validación y accesibilidad: verificar uso de recursos y targets ≥48dp

**Pendientes/Incompletos:**
- Validar rutas tipadas en todos los features
- Revisar cobertura de accesibilidad

---

### MVP-03 · Configuración: DataStore/EncryptedPrefs
**Tareas:**
- DataStore para preferencias
- EncryptedSharedPreferences para datos sensibles
- Modelo `AppConfig` y migraciones

**Estado:**
- DataStore presente en `core/datastore`
- EncryptedPrefs: revisar implementación
- Migraciones: verificar en `core/database`

**Pendientes/Incompletos:**
- Implementar modelo `AppConfig`
- Documentar migraciones

---

### MVP-04 · API: Networking
**Tareas:**
- Retrofit/Ktor, interceptores
- DTOs y `Result<T>`
- Pruebas unitarias

**Estado:**
- Networking en `core/network` (verificar Retrofit/Ktor)
- Interceptores y DTOs: revisar código
- Result wrapper: presente en reglas

**Pendientes/Incompletos:**
- Pruebas unitarias de capa de red

---

### MVP-05 · Auth: OAuth2/OIDC + Credential Manager
**Tareas:**
- Login con Credential Manager
- Tokens en Keystore
- Refresco de sesión

**Estado:**
- Revisar implementación en `core/auth`
- Keystore: verificar uso
- Refresco de sesión: revisar en `core/auth` y `app`

**Pendientes/Incompletos:**
- Pruebas de integración de flujo auth

---

### MVP-06 · Sesión: SavedStateHandle, bloqueo y restauración
**Tareas:**
- SavedStateHandle en ViewModels
- Bloqueo por inactividad
- Restauración de sesión

**Estado:**
- Uso de SavedStateHandle: revisar en ViewModels
- Bloqueo/restauración: verificar en `app` y `core/auth`

**Pendientes/Incompletos:**
- Implementar lógica de restauración y bloqueo

---

### MVP-07 · Background: WorkManager y Sync
**Tareas:**
- WorkManager con constraints
- Sync idempotente

**Estado:**
- WorkManager presente en `core/work`
- Idempotencia: revisar implementación

**Pendientes/Incompletos:**
- Pruebas de sincronización

---

### MVP-08 · Telemetría: Timber + OTel/Crashlytics
**Tareas:**
- Logging estructurado
- Exportador de logs
- Métricas clave

**Estado:**
- Timber presente
- Exportador: revisar integración con OTel/Crashlytics

**Pendientes/Incompletos:**
- Métricas de latencia, error rate, ANR

---

### MVP-09 · Archivos: Upload/Download
**Tareas:**
- Subida/descarga con progreso
- Reanudación y manejo de errores

**Estado:**
- Revisar implementación en `feature` correspondiente

**Pendientes/Incompletos:**
- Demo de adjuntos y manejo de errores

---

### MVP-10 · Notificaciones: FCM, canales, deep links
**Tareas:**
- FCM y canales
- Deep links y navegación desde notificación

**Estado:**
- Revisar integración FCM y canales en `app`

**Pendientes/Incompletos:**
- Estructura de deep links

---

### MVP-11 · i18n/a11y/theming
**Tareas:**
- Strings en recursos
- Dynamic Type/Color
- Accesibilidad

**Estado:**
- Strings en recursos: presente
- Dynamic Color: revisar en theming
- Accesibilidad: verificar cumplimiento WCAG

**Pendientes/Incompletos:**
- Validar accesibilidad en pantallas

---

### MVP-12 · Pruebas/CI
**Tareas:**
- Pipelines CI
- Cobertura y firmas

**Estado:**
- CI configurado (verificar scripts y reportes)
- Cobertura: revisar umbral y reportes

**Pendientes/Incompletos:**
- Validar bloqueo por lint/tests en CI

---

## Tareas del modelo anterior no migradas al nuevo esquema

- Revisión de `/arquitectura`, `/ui-ux`, `/navegacion-estado`, etc. como carpetas de documentación y decisiones (ver si se mantienen en el nuevo modelo).
- Estrategias de compatibilidad futura (wrappers por versión) — revisar si se han implementado según [template_base.md].
- Plantillas de checklist por MVP — verificar si se usan en los nuevos módulos.
- Documentación de decisiones técnicas y riesgos por MVP.

---

## Resumen de pendientes

- Validar y documentar flavors y DI raíz en todos los módulos.
- Revisar rutas tipadas y accesibilidad en UI.
- Implementar y documentar modelo `AppConfig` y migraciones.
- Pruebas unitarias e integración en networking y auth.
- Lógica de restauración y bloqueo de sesión.
- Pruebas de sincronización y manejo de errores en background y archivos.
- Integración completa de telemetría y métricas.
- Estructura de deep links y canales de notificación.
- Validar accesibilidad y dynamic color en UI.
- Validar CI/CD y cobertura en todos los módulos.
- Migrar documentación y checklists del modelo anterior si no están presentes.

---

**Última revisión:** 13 de agosto de 2025  
**Próxima acción:** Completar pendientes y actualizar documentación por módulo.
