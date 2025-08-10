# Proyecto Android Base (API 34 – Android 14)
**Enfoque:** nativo-primero, modular progresivo, fácil mantenimiento, y listo para migrar a 15/16 sin reescribir el mundo.  
**Meta:** plantilla escalable a partir de **MVPs pequeños** y funcionales (micro-proyectos), que se van encadenando.

---

## 0) Principios de diseño
- **Target/Compile:** API 34 (Android 14). `minSdk` ajustable al mercado, sugerido 23–26.
- **Nativo primero:** prioriza **AndroidX/Jetpack** (Compose, Room, WorkManager, DataStore, Hilt, Navigation, Security Crypto).
- **Excepciones justificadas:** usar terceros sólo si el **beneficio real** > costo de dependencia (p. ej., **OkHttp/Retrofit** para HTTP fiable; **Coil** si manejo de imágenes lo amerita).
- **Modularidad progresiva:** inicia simple (1 módulo) y extrae a `core/*` y `feature/*` conforme crece.
- **Compatibilidad futura:** encapsula APIs ligadas a versión (permisos, FGS, pickers) detrás de interfaces propias.
- **Ciclos cortos:** MVP incrementales; cada uno agrega un aspecto (UI → persistencia → red → sync → notifs).

---

## 1) Estructura de trabajo del proyecto (carpetas del “espacio”)
- **/arquitectura**: decisiones, diagramas, convenciones de código, guidelines de revisión.
- **/ui-ux**: temas, componentes Compose reutilizables, accesibilidad, validaciones.
- **/navegacion-estado**: Navigation-Compose, contratos de rutas, paso de datos, state-hoisting.
- **/networking**: cliente HTTP, serialización, interceptores, manejo de errores.
- **/persistencia**: Room/SQLDelight, DataStore, migraciones, repos.
- **/auth-seguridad**: Credential Manager, Keystore/Security, políticas de permisos.
- **/background-sync**: WorkManager, políticas de backoff, FGS (si aplica), transferencias.
- **/notificaciones**: FCM, canales, permisos POST_NOTIFICATIONS.
- **/compat-actualizaciones**: wrappers por versión, plan de migración a 15/16.
- **/testing-calidad**: unit/UI tests, cobertura, ktlint/detekt, estrategias QA.
- **/observabilidad**: logging, Crashlytics/Sentry, métricas de perf.
- **/devops-entrega**: build logic, versionado, firmas, CI/CD, flavors.
- **/mvp-roadmap**: especificaciones y checklists de cada MVP.
- **/snippets**: trozos de código autocontenidos (recetas).

> Recomendación: mantener aquí notas Markdown, diagramas y enlaces a PRs/commits relevantes.

---

## 2) Ruta de MVPs (micro-proyectos encadenados)
Cada MVP es **entregable funcional** y vive en una rama o carpeta separada para aislar complejidad.

### MVP-01 · UI básica (data entry sin persistencia)
- **Objetivo:** Formularios simples con Compose + validación local.
- **Incluye:** Theme M3, TextField/Dropdown/Checkbox/DatePicker, navegación mínima.
- **No incluye:** Red, DB, notifs.
- **Deliverables:** pantallas + navegación + validación + pruebas UI básicas.

### MVP-02 · Persistencia local (Room + DataStore)
- **Objetivo:** Guardar y leer entidades, migraciones simples.
- **Incluye:** Room (DAO/Entities), DataStore (preferencias), repos + ViewModel.
- **Entregables:** CRUD local, lista paginada con Paging 3 (opcional).

### MVP-03 · Networking (HTTP + serialización)
- **Objetivo:** Consumir un servicio REST (GET/POST), manejo de errores y timeouts.
- **Incluye:** Retrofit (+OkHttp) **o** Ktor Client (justificar elección), kotlinx.serialization/Moshi.
- **Entregables:** capa `core:network`, interceptores (auth/log), Result wrapper, pruebas unitarias.

### MVP-04 · Offline-first & Sync
- **Objetivo:** Estrategia cache-then-network, sync periódico/one-off.
- **Incluye:** WorkManager (backoff, constraints), RemoteMediator (si Paging), políticas de conflicto.
- **Entregables:** flujo offline-first con pruebas y métricas de éxito (latencia, tasa de error).

### MVP-05 · Autenticación y seguridad
- **Objetivo:** Login con Credential Manager (password/passkeys/Google), almacenamiento seguro.
- **Incluye:** Security Crypto/Keystore para tokens, refresco de sesión, políticas de expiración.
- **Entregables:** flujo auth completo + pruebas de integración.

### MVP-06 · Notificaciones
- **Objetivo:** Notificaciones locales y push (FCM) con canales y permiso runtime (13+).
- **Incluye:** canal por tipo de evento, deep-links, navegación desde notif.
- **Entregables:** mensajes push end-to-end (token, recepción, navegación).

### MVP-07 · Subida/Descarga de archivos
- **Objetivo:** Upload multipart con progreso y cancelación; descargas confiables.
- **Incluye:** WorkManager/DownloadManager, SAF/FileProvider para compartir URIs.
- **Entregables:** demo de adjuntos y manejo de errores (reintentos/cancel).

### MVP-08 · Observabilidad y hardening
- **Objetivo:** Crashlytics/Sentry, logging estructurado, StrictMode en debug.
- **Incluye:** reportes de fallas, trazas clave de latencia/red/DB.
- **Entregables:** panel mínimo de salud de la app.

> Extra (opcional): **MVP-09** grandes pantallas (layouts adaptativos) y **MVP-10** internacionalización/Regional Preferences (compat wrappers para 14→15).

---

## 3) Arquitectura y módulos (progresivo, no complicado)
**Fase A (inicial, simple – 1 módulo):**
```
app/
  ├─ ui/…
  ├─ data/…
  ├─ domain/…
  ├─ di/…
  └─ build.gradle.kts
```
- Todo en uno para mover rápido en MVP-01/02.
- DI: **Hilt** (AndroidX-first) o “Service Locator” muy simple si prefieres cero anotaciones al inicio.

**Fase B (extraer core reutilizable):**
```
app/                      (arranque, navegación, DI raíz)
core/ui/                  (tema, componentes Compose)
core/database/            (Room, migraciones, DAOs base)
core/datastore/           (preferencias)
core/network/             (Retrofit/OkHttp o Ktor, interceptores)
core/work/                (WorkManager helpers)
core/auth/                (credenciales, tokens seguros)
feature/<x>/              (módulos por funcionalidad)
build-logic/              (plugins y convenciones Gradle opcional)
```
- Regla: extrae sólo cuando el código **se repita** o lo **necesites en otra app**.

---

## 4) Decisiones “nativo primero” y excepciones
- **UI:** Jetpack Compose + Material 3 (**nativo/AndroidX**).
- **Navegación:** Navigation-Compose (**AndroidX**).
- **Estado:** ViewModel + SavedStateHandle (**AndroidX**).
- **Persistencia:** Room (**AndroidX**); **SQLDelight** (tercero) sólo si necesitas KMP o SQL estricto.
- **Preferencias:** DataStore (**AndroidX**).
- **Background:** WorkManager (**AndroidX**).
- **Auth:** Credential Manager + Security Crypto/Keystore (**Google/AndroidX**).
- **HTTP:**
    - *Opción nativa mínima:* `HttpURLConnection` (para MVP-03 “sencillo”).
    - *Opción recomendada:* **OkHttp + Retrofit** por fiabilidad, ecosistema, interceptores, cancelación y HTTP/2.
- **Imágenes:** evita dependencia al inicio; si necesitas, **Coil** (liviano y Compose-ready).
- **Logs/Crash:** Android Log + StrictMode; añadir **Crashlytics** cuando toque.

---

## 5) Wrappers por versión (compatibilidad futura)
Crea interfaces en `core/*` y dos implementaciones según API cuando aplique:
- **Permisos** (`PermissionManager`): POST_NOTIFICATIONS (13+), pickers de media.
- **FGS/Background** (`BackgroundExecutor`): en 14 vs 15/16 cambian restricciones.
- **Regional Preferences** (`LocalePreferences`): API 14 vs 15.
- **Photo/Media Picker** (`MediaPicker`): usa Photo Picker (13+) con fallback si algún OEM falla.

---

## 6) Estándares de código y utilidades
- **Kotlin + Gradle Kotlin DSL**.
- **Formateo/Lint:** ktlint/Spotless, detekt (fase futura).
- **Pruebas:** JUnit5, Turbine/Coroutines Test, Compose UI testing.
- **Convenciones:** `libs.versions.toml`, paquetes coherentes, reglas de nullability y Result wrappers.

---

## 7) Convención para chats/vías de trabajo (organización)
- **[ANDROID][MVP-01] UI básica**
- **[ANDROID][MVP-02] Persistencia local**
- **[ANDROID][MVP-03] Networking**
- **[ANDROID][MVP-04] Offline & Sync**
- **[ANDROID][MVP-05] Autenticación**
- **[ANDROID][MVP-06] Notificaciones**
- **[ANDROID][MVP-07] Archivos**
- **[ANDROID][ARQ] Módulos & DI**
- **[ANDROID][OPS] Testing & CI/CD**
- **[ANDROID][COMPAT] Wrappers de versión**

> Cada hilo enfocado y corto. Enlazamos decisiones en este `README` para no perder el hilo.

---

## 8) Próximos pasos (sugeridos)
1. **Crear repositorio** con Fase A (1 módulo) y `README` inicial (este documento).
2. **Ejecutar MVP-01**: UI básica con validación local y navegación.
3. **Plan de extracción**: definir qué pasará a `core/*` tras MVP-02 y MVP-03.
4. **Plantillas de PR** y checklists de definición de listo (DoD) por MVP.

---

## 9) Plantilla de checklist por MVP (copiar/pegar)
- Objetivo y alcance claro
- Criterios de aceptación / demo
- Decisiones técnicas (y porqué)
- Riesgos y mitigaciones
- Pruebas (unit/UI)
- Métricas (si aplica)
- Documentación (en esta carpeta)