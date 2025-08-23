# [ANDROID][MVP-01] UI básica — Guía Técnica de Implementación

---

## 1. Stack Tecnológico y Versiones

- **Lenguaje**: Kotlin 2.0.x
- **Gradle**: 8.x
- **Android Gradle Plugin (AGP)**: 8.5.x
- **SDK mínimo**: 26
- **SDK objetivo**: 34 (Android 14)
- **UI**: Jetpack Compose con Material 3
- **Navegación**: Navigation Compose
- **Dependencias**: Se utiliza **Version Catalog** (`gradle/libs.versions.toml`) para una gestión centralizada.

---

## 2. Arquitectura y Estructura de Módulos

Se establece una arquitectura modular desde el inicio para promover la escalabilidad y separación de responsabilidades.

```
TemplateAndroid/
├── app/                    # Módulo principal de la aplicación y NavHost
├── core/                   # Módulos núcleo reutilizables
│   ├── common/             # Constantes (rutas de navegación) y utilidades
│   ├── designsystem/       # Sistema de diseño (AppTheme, colores, tipografía)
│   └── ui/                 # Componentes de UI reutilizables (botones, scaffolds)
└── feature/                # Módulos de funcionalidades
    └── home/               # Feature con HomeScreen y DetailsScreen
```

### Dependencias entre Módulos
- `app` implementa `feature:home` y todos los módulos `core`.
- `feature:home` implementa `core:ui` y `core:common`.
- `core:ui` implementa `core:designsystem`.

---

## 3. Implementación de Código Clave

A continuación se muestran los fragmentos de código esenciales para entender la implementación de este módulo.

### 3.1. Navegación (Navigation Compose)

**Rutas centralizadas (`:core:common`)**
```kotlin
package com.androidbase.core.common

object Route {
  const val Home = "home"
  const val Details = "details"
}
```

**Host de Navegación (`:app`)**
```kotlin
// En MainActivity.kt dentro de setContent
val nav = rememberNavController()
NavHost(navController = nav, startDestination = Route.Home) {
  composable(Route.Home) {
    HomeScreen(onContinue = { nav.navigate(Route.Details) })
  }
  composable(Route.Details) {
    DetailsScreen(onBack = { nav.popBackStack() })
  }
}
```

### 3.2. Sistema de Diseño (`:core:designsystem`)

Se define un `AppTheme` que encapsula `MaterialTheme` y provee los esquemas de color para modo claro y oscuro.

```kotlin
// En AppTheme.kt
@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  val colorScheme = if (darkTheme) {
    darkColorScheme() // Colores para modo oscuro
  } else {
    lightColorScheme() // Colores para modo claro
  }

  MaterialTheme(
    colorScheme = colorScheme,
    // tipografía y formas
    content = content
  )
}
```

### 3.3. Componentes de UI Reutilizables (`:core:ui`)

Se crean componentes genéricos que consumen el tema definido en `:core:designsystem`.

**Botón Primario:**
```kotlin
@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
  Button(onClick = onClick, modifier = modifier) {
    Text(text)
  }
}
```

**Scaffold base de la App:**
```kotlin
@Composable
fun AppScaffold(title: String, content: @Composable (PaddingValues) -> Unit) {
  Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { innerPadding ->
    content(innerPadding)
  }
}
```

### 3.4. Pantallas de la Feature (`:feature:home`)

Las pantallas utilizan los componentes de `:core:ui` y la lógica de navegación se pasa como lambdas.

**HomeScreen:**
```kotlin
@Composable
fun HomeScreen(onContinue: () -> Unit) {
  AppScaffold(title = "Android Base") { padding ->
    Column(
      modifier = Modifier.padding(padding).fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text("Bienvenido al proyecto base")
      PrimaryButton(text = "Continuar", onClick = onContinue)
    }
  }
}
```

**DetailsScreen:**
```kotlin
@Composable
fun DetailsScreen(onBack: () -> Unit) {
  AppScaffold(title = "Details") { padding ->
    Column(
      modifier = Modifier.padding(padding).fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text("Esta es la pantalla de detalles")
      PrimaryButton(text = "Volver", onClick = onBack)
    }
  }
}
```

---

## 4. Configuración de Build (Gradle)

Se utiliza `libs.versions.toml` para gestionar las versiones de las dependencias de forma centralizada.

**Ejemplo de `build.gradle.kts` para un módulo de feature:**
```kotlin
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.androidbase.feature.home"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
  }

  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.11" // Sincronizado con la versión de Kotlin
  }
}

dependencies {
  implementation(platform(libs.compose.bom))
  implementation(libs.bundles.compose)
  implementation(libs.navigation.compose)

  // Dependencias a otros módulos del proyecto
  implementation(project(":core:ui"))
  implementation(project(":core:common"))
}
```
