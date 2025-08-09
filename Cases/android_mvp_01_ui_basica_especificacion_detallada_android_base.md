# [ANDROID][MVP-01] UI básica — Especificación detallada

> Proyecto: **Android base**  
> Módulo: **[ANDROID][MVP-01] UI básica**  
> Plataforma: **Android 14 (targetSdk 34)** · **minSdk 26** · **Kotlin + Jetpack Compose**

---

## 1) Propósito y alineación
**Objetivo**: crear el esqueleto de una app Android modular con **UI mínima**, navegación básica y **sistema de diseño centralizado**, sin persistencia ni red. Este módulo sienta las bases (estándares, estructura y tooling interno nativo) para crecer de forma segura en los siguientes MVPs.

**Drivers de diseño**
- Nativo y moderno (Compose, Material 3, Navigation Compose).
- Modular desde el día 1 para facilitar **mantenimiento**, **pruebas**, **escalado** y **reemplazo de capas**.
- Dependencias externas: **cero** en este MVP (salvo librerías oficiales AndroidX/Google). Se prioriza lo nativo.
- Documentación y orden: definimos **plantilla** de historia de usuario, **desglose de tareas**, **criterios de aceptación**, **DoD** y **flujo de trabajo** para replicar en los siguientes módulos.

**Fuera de alcance (MVP-01)**
- Persistencia (DB/Datastore/Preferences).
- Llamadas a red o sincronización.
- Telemetría y DI avanzadas (dejamos los ganchos).
- Testing a profundidad (solo esqueleto mínimo de pruebas).

---

## 2) Historia(s) de usuario y criterios de aceptación

### HU-001: Navegación básica entre pantallas
**Como** usuario inicial de la app  
**quiero** ver una pantalla de inicio con un botón que me lleve a una segunda pantalla  
**para** validar que la app navega correctamente y respeta el diseño base.

**Criterios de aceptación (Gherkin)**
```
Dado que abro la app por primera vez
Cuando se renderiza la pantalla Home
Entonces veo un título de la app, un texto descriptivo y un botón primario "Continuar"

Dado que estoy en Home
Cuando toco el botón "Continuar"
Entonces navego a la pantalla Details y veo un texto estático y un botón "Volver"

Dado que estoy en Details
Cuando toco "Volver"
Entonces regreso a Home sin errores ni cierres inesperados
```

### HU-002: Sistema de diseño centralizado
**Como** desarrollador del proyecto  
**quiero** definir temas, colores y tipografía en un módulo **:core:designsystem**  
**para** reutilizar estilos y asegurar consistencia visual en todas las features.

**Criterios de aceptación (Gherkin)**
```
Dado el módulo :core:designsystem
Cuando se aplica el tema de la app
Entonces los colores, tipografía y formas vienen de un solo origen (Material 3)
Y los componentes de UI usan ese tema sin redefinir estilos locales
```

---

## 3) Alcance técnico del MVP-01

### 3.1 Estructura de módulos (Gradle)
- **:app** → configuración de aplicación, entrada, `NavHost` raíz.
- **:feature:home** → pantallas Home y Details + navegación interna.
- **:core:designsystem** → tema Material 3 (colores, tipografía, formas, dark/light).
- **:core:ui** → componentes atómicos reutilizables (e.g., `PrimaryButton`, `AppScaffold`).
- **:core:common** → utilidades básicas (constantes, helpers de navegación, annotations).

> Nota: La partición fina ahora evita refactors costosos luego.

### 3.2 Versiones y toolchain
- **Kotlin**: 2.0.x
- **Gradle**: 8.x
- **AGP (Android Gradle Plugin)**: 8.5.x
- **Compose BOM**: última estable compatible con AGP/Kotlin del proyecto
- **Navigation Compose**: última estable

> Usaremos **Version Catalog** (`libs.versions.toml`) para mantener versiones en un único archivo nativo de Gradle.

### 3.3 Convenciones de paquete
- Raíz de código: `com.androidbase` (ajustable por ambiente/org).
- Nombres de paquete por módulo, p. ej. `com.androidbase.core.ui`, `com.androidbase.feature.home`.

### 3.4 Navegación
- `NavHost` central en **:app**.
- Rutas como constantes en **:core:common** (`Route.Home`, `Route.Details`).
- `HomeScreen` → botón → `DetailsScreen`. Back stack estándar.

### 3.5 Sistema de diseño
- Material 3 con esquemas **light/dark**.
- Tipografías: por ahora **system fonts**; ganchos para custom fonts.
- `AppTheme { ... }` aplicado desde :app.

### 3.6 Componentes UI
- `AppScaffold(title, content)`
- `PrimaryButton(text, onClick)`
- `ScreenContainer(modifier)` para paddings/responsiveness básicos.

### 3.7 Calidad y linting
- **Android Lint** habilitado (sin dependencias externas).
- `-Xexplicit-api=strict` (en módulos de librería) si aplicable.
- **Warnings as errors** en módulos core (opcional, parametrizable por buildType).

### 3.8 Testing (esqueleto mínimo)
- Unit test placeholder en **:feature:home** para validar navegación (lógica de rutas).
- UI test placeholder con `compose-ui-test` (Smoke test de Home → Details).

---

## 4) Plan de trabajo (tareas)

> **Metodología**: trabajaremos en **tándems** de tareas atómicas, cortas, con PRs pequeños. Cada tarea produce un incremento visible (UI, config o tooling). Estimación ligera en **puntos** (1, 2, 3, 5).

### Épica: [ANDROID][MVP-01] UI básica

#### T1 — Inicializar repositorio y Gradle (2 pt)
- Crear estructura raíz del proyecto y `.gitignore` Android/Kotlin.
- Configurar `settings.gradle.kts` con módulos vacíos.
- Crear **Version Catalog** (`gradle/libs.versions.toml`).
- Definir `build.gradle.kts` raíz con repos y opciones comunes.

**Resultado**: proyecto abre y sincroniza en Android Studio sin módulos Android aún.

#### T2 — Crear módulos base (3 pt)
- Añadir módulos Android Library: `:core:designsystem`, `:core:ui`, `:core:common`.
- Añadir módulo Android Application: `:app`.
- Añadir feature module `:feature:home` (Android Library + Compose).

**Resultado**: compila vació; todos los módulos con `namespace` y Compose habilitado donde corresponda.

#### T3 — Configurar Compose + Material 3 (2 pt)
- Activar Compose en módulos UI/feature.
- Añadir BOM de Compose y dependencias base.
- Crear `AppTheme` en `:core:designsystem` con light/dark.

**Resultado**: `setContent { AppTheme { ... } }` disponible en `:app`.

#### T4 — Navegación mínima (2 pt)
- Agregar Navigation Compose.
- Definir rutas en `:core:common`.
- Crear `NavHost` en `:app` con `Home` y `Details`.

**Resultado**: botón en Home lleva a Details; back devuelve a Home.

#### T5 — Componentes UI reutilizables (2 pt)
- Implementar `PrimaryButton`, `AppScaffold`, `ScreenContainer` en `:core:ui`.
- Home/Details usan estos componentes; cero estilos locales ad-hoc.

**Resultado**: UI coherente y centralizada.

#### T6 — Lint y normas mínimas (1 pt)
- Activar Android Lint, revisar baseline si es necesario.
- `ktOptions`/`kotlinOptions` unificados (JVM target, freeCompilerArgs).

**Resultado**: build falla ante problemas críticos de lint.

#### T7 — Tests placeholder (1 pt)
- Unit test mínimo para rutas.
- UI test mínimo para flujo Home → Details.

**Resultado**: pipeline de pruebas básico ejecuta con éxito localmente.

#### T8 — Documentación del módulo (2 pt)
- Este documento + README de cada módulo con propósito y API pública.

**Resultado**: documentación visible, repetible para próximos MVPs.

> **Total estimado**: 15 pt

---

## 5) Especificación de configuración (paso a paso)

> A continuación, la **receta exacta** para replicar el setup.

### 5.1 `settings.gradle.kts`
- Activar versión de features necesarias y declarar módulos:
```kotlin
pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "android-base"
include(":app")
include(":core:designsystem")
include(":core:ui")
include(":core:common")
include(":feature:home")
```

### 5.2 `gradle/libs.versions.toml`
> Mantener aquí versiones oficiales sin libs de terceros.
```toml
[versions]
agp = "8.5.0"
kotlin = "2.0.0"
compileSdk = "34"
minSdk = "26"

authorsNote = "Solo librerías AndroidX/Google"

[libraries]
androidx-core-ktx = { module = "androidx.core:core-ktx", version = "1.13.1" }
material3 = { module = "androidx.compose.material3:material3", version = "1.2.1" }
compose-bom = { module = "androidx.compose:compose-bom", version = "2024.06.00" }
compose-ui = { module = "androidx.compose.ui:ui" }
compose-ui-tooling = { module = "androidx.compose.ui:ui-tooling" }
compose-ui-tooling-preview = { module = "androidx.compose.ui:ui-tooling-preview" }
compose-foundation = { module = "androidx.compose.foundation:foundation" }
compose-runtime = { module = "androidx.compose.runtime:runtime" }
activity-compose = { module = "androidx.activity:activity-compose", version = "1.9.0" }
navigation-compose = { module = "androidx.navigation:navigation-compose", version = "2.7.7" }
compose-ui-test-junit4 = { module = "androidx.compose.ui:ui-test-junit4" }
compose-ui-test-manifest = { module = "androidx.compose.ui:ui-test-manifest" }

[bundles]
compose = ["compose-ui", "compose-foundation", "compose-runtime", "compose-ui-tooling-preview", "material3"]

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
android-library = { id = "com.android.library", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

### 5.3 `build.gradle.kts` (raíz)
```kotlin
plugins {
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
}
```

### 5.4 Plantillas de `build.gradle.kts` por tipo de módulo

**Aplicación (`:app`)**
```kotlin
plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.androidbase.app"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.androidbase"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.compileSdk.get().toInt()
    versionCode = 1
    versionName = "1.0.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  buildFeatures { compose = true }

  composeOptions { kotlinCompilerExtensionVersion = "1.5.11" }

  kotlinOptions { jvmTarget = "17" }
}

dependencies {
  implementation(platform(libs.compose.bom))
  implementation(libs.bundles.compose)
  implementation(libs.activity.compose)
  implementation(libs.navigation.compose)
}
```

**Librería (Compose) — para `:feature:home`, `:core:ui`**
```kotlin
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.androidbase.feature.home" // ajustar por módulo
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }

  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.11" }
  kotlinOptions { jvmTarget = "17" }
  publishing { singleVariant("release") }
}

dependencies {
  implementation(platform(libs.compose.bom))
  implementation(libs.bundles.compose)
  implementation(libs.activity.compose)
}
```

**Librería (no-Compose) — `:core:common`**
```kotlin
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.androidbase.core.common"
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
  kotlinOptions { jvmTarget = "17" }
}

dependencies { /* vacío por ahora */ }
```

### 5.5 Código mínimo por módulo

**:core:designsystem — `AppTheme.kt`**
```kotlin
@file:Suppress("SameParameterValue")
package com.androidbase.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun AppTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = if (darkTheme) DarkColors else LightColors,
    content = content
  )
}
```

**:core:ui — componentes**
```kotlin
package com.androidbase.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppScaffold(title: String, content: @Composable (PaddingValues) -> Unit) {
  Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { inner ->
    content(inner)
  }
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
  Button(onClick = onClick, modifier = modifier) { Text(text) }
}

@Composable
fun ScreenContainer(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
  Surface(modifier = modifier.fillMaxSize()) { content() }
}
```

**:core:common — rutas**
```kotlin
package com.androidbase.core.common

object Route {
  const val Home = "home"
  const val Details = "details"
}
```

**:feature:home — pantallas**
```kotlin
package com.androidbase.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidbase.core.ui.AppScaffold
import com.androidbase.core.ui.PrimaryButton

@Composable
fun HomeScreen(onContinue: () -> Unit) {
  AppScaffold(title = "Android base") { padding ->
    Column(
      modifier = Modifier.padding(padding).padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text("Bienvenido a la base del proyecto")
      PrimaryButton(text = "Continuar", onClick = onContinue)
    }
  }
}

@Composable
fun DetailsScreen(onBack: () -> Unit) {
  AppScaffold(title = "Details") { padding ->
    Column(
      modifier = Modifier.padding(padding).padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text("Segunda pantalla")
      PrimaryButton(text = "Volver", onClick = onBack)
    }
  }
}
```

**:app — `MainActivity` + `NavHost`**
```kotlin
package com.androidbase.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.androidbase.core.common.Route
import com.androidbase.core.designsystem.AppTheme
import com.androidbase.feature.home.DetailsScreen
import com.androidbase.feature.home.HomeScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { App() }
  }
}

@Composable
fun App() {
  AppTheme {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Route.Home) {
      composable(Route.Home) { HomeScreen(onContinue = { nav.navigate(Route.Details) }) }
      composable(Route.Details) { DetailsScreen(onBack = { nav.popBackStack() }) }
    }
  }
}
```

### 5.6 Pruebas placeholder

**Unit test simple (rutas)**
```kotlin
package com.androidbase.feature.home

import org.junit.Assert.assertEquals
import org.junit.Test
import com.androidbase.core.common.Route

class RouteTest {
  @Test fun `home and details route are stable`() {
    assertEquals("home", Route.Home)
    assertEquals("details", Route.Details)
  }
}
```

**UI test simple (Smoke)**
```kotlin
// Por implementar en MVP-01. Mantener placeholder para no arrastrar deuda.
```

---

## 6) Definición de Hecho (DoD)
- Proyecto abre y compila sin errores en Android Studio.
- App arranca en emulador/disp. físico Android 14.
- Home y Details visibles; navegación funciona; back retorna sin crash.
- Tema Material 3 aplicado desde :core:designsystem.
- Componentes UI consumidos desde :core:ui.
- Lint sin issues críticos.
- Tests placeholder ejecutan/compilan (aunque mínimos).
- Documento (este) publicado; README por módulo creado.

---

## 7) Riesgos y supuestos
- **Compatibilidad de versiones**: mantener BOM/AGP alineados para evitar incompatibilidades del compiler de Compose.
- **Modularidad temprana**: ligera sobrecarga inicial a cambio de escalabilidad futura.
- **Sin dependencias externas**: reduce features listas, pero mantiene control total.

**Mitigaciones**
- Usar Version Catalog y BOM para actualizar de forma controlada.
- PRs pequeños y CI local (build + lint + tests).

---

## 8) Plantilla reutilizable para próximos módulos
> Copiar y adaptar en cada MVP/feature.

```
# [ANDROID][MVP-XX] <Nombre> — Especificación detallada

## 1) Propósito
<Resumen del objetivo y límites>

## 2) Historias de usuario
- HU-XXX: <Como/quiero/para>
  - Criterios (Gherkin)

## 3) Alcance técnico
- Módulos afectados
- APIs/Contratos
- Navegación/DI/Almacenamiento/Telemetría (según aplique)

## 4) Plan de trabajo (tareas)
- T1 — <Descripción> (pts)
- T2 — ...

## 5) Configuración y código
- Cambios en Gradle/Manifiestos
- Snippets clave

## 6) DoD
- Lista verificable

## 7) Riesgos y supuestos
- Lista + mitigaciones
```

---

## 9) Backlog inicial en tablero (sugerido)
- Lista de T1–T8 con estados: **To Do / In Progress / Review / Done**.
- Columnas adicionales (opcional): **Blocked**, **Tech Debt**.
- Política de PR: máximo ~300 líneas, 1–2 revisores.

---

## 10) Próximos pasos
1. Crear repo y subir el esqueleto.
2. Abrir issues por cada tarea T1–T8 con esta especificación.
3. Ejecutar T1→T3 y validar arranque en emulador.
4. Cerrar MVP-01 con demo interna (video corto o GIF del flujo Home↔Details).

