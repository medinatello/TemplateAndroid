# Guía Maestra de Desarrollo Android (Kotlin)

## 1. Introducción

Este documento es la guía fundamental para el desarrollo de la versión de Android de este proyecto. Su propósito es asegurar que todo el código siga los más altos estándares de calidad, consistencia y escalabilidad, siguiendo las recomendaciones de Google y JetBrains. Adherirse a estas reglas es crucial para la salud y mantenibilidad del código base.

---

## 2. Principios Fundamentales

### Idioma: Inglés (English-Only Policy)

Todo el código, comentarios, nombres de variables, funciones, commits y documentación deben estar **escritos en inglés**.
- **Razón:** Unifica el lenguaje del proyecto, facilita la colaboración internacional y es el estándar de la industria.

### Mensajes de Commit (Commit Messages)

Deben seguir el estándar de [Conventional Commits](https://www.conventionalcommits.org/).
- **Formato:** `<tipo>(<módulo>): <descripción>`
- **Ejemplos:**
  - `feat(auth): add google sign-in screen`
  - `fix(storage): correct race condition in user cache`
  - `docs(api): update swagger documentation link`
  - `refactor(network): switch to Ktor client`

---

## 3. Diseño de Código y API (Guías de Kotlin y Android)

La API pública de cada módulo (`public` o `internal`) debe ser clara, concisa y segura. Nos basamos en la [Guía de Estilo Oficial de Kotlin](https://kotlinlang.org/docs/coding-conventions.html) y la [Guía de Arquitectura de Android](https://developer.android.com/topic/architecture).

### 3.1. Claridad y Expresividad

El código debe ser auto-explicativo. Usa el poder de la librería estándar de Kotlin para mejorar la legibilidad.

```kotlin
// Prefiere funciones de alcance (scope functions) para claridad
user?.let {
    updateUI(it)
}

// Usa parámetros con nombre para mejorar la legibilidad en funciones con muchos argumentos
userService.updateUser(
    id = 123,
    name = "John Doe",
    isActive = true
)
```

### 3.2. Nomenclatura

- **Evita Abreviaturas:** `destinationAddress` es mejor que `destAddr`.
- **Nombres de Funciones:** Deben ser verbos o frases verbales. `fetchUserData()`, `processPayment()`.
- **Booleanos:** Deben tener prefijos como `is`, `has`, `can`. `isUserLoggedIn`, `hasActiveSubscription`.

### 3.3. Manejo de Nulabilidad (Null Safety)

- **Evita tipos nulables (`?`) tanto como sea posible.** Diseña tus clases y flujos de datos para que operen con valores no nulos.
- **Nunca uses el operador `!!` (not-null assertion).** Si crees que necesitas usarlo, es una señal de que hay un error en tu diseño. Usa `?.let`, `?:` (elvis operator), o rediseña.

---

## 4. Mejores Prácticas de Kotlin

### 4.1. Inmutabilidad

- **Prefiere `val` sobre `var` siempre.** Un estado inmutable reduce los efectos secundarios y es más seguro en entornos con concurrencia.

### 4.2. Clases vs. Data Classes

- **Usa `data class`** para cualquier clase que solo contenga datos. Automáticamente obtienes `equals()`, `hashCode()`, `toString()`, etc.
- **Usa `class`** regular para clases con lógica de negocio, estado complejo o que deban ser heredadas.

### 4.3. Concurrencia: Corrutinas (Coroutines)

- **Usa Corrutinas** para toda la programación asíncrona. Es el estándar moderno en Android.
- **Sigue la Concurrencia Estructurada:** Lanza las corrutinas desde un `CoroutineScope` atado a un ciclo de vida (ej. `viewModelScope`, `lifecycleScope`). Esto previene memory leaks.
- **Flujos (`Flow`):** Usa `Flow` para manejar flujos de datos asíncronos (ej. respuestas de una base de datos o API). Es el equivalente a Combine/Rx en el mundo reactivo.
- **`suspend fun`:** Marca todas las funciones de larga duración o I/O como `suspend`.

---

## 5. Guías para Jetpack Compose

### 5.1. Gestión de Estado y Flujo de Datos Unidireccional (UDF)

El estado debe fluir hacia abajo y los eventos hacia arriba.

- **`remember { mutableStateOf(...) }`:** Para estado **simple y local** que pertenece a un Composable. Se reinicia en la recomposición si no se usa `rememberSaveable`.
- **State Hoisting (Elevación de Estado):** Un Composable no debe tener su propio estado si este necesita ser compartido. El estado debe ser "elevado" a un ancestro común y pasado hacia abajo vía parámetros, y los eventos de cambio comunicados hacia arriba con lambdas.
- **`ViewModel` como Fuente de Verdad (Source of Truth):** La lógica de UI y el estado de la pantalla deben residir en un `ViewModel`. Las vistas observan el estado expuesto por el ViewModel (usualmente como un `StateFlow`) y le notifican eventos.

```kotlin
// En el ViewModel
private val _uiState = MutableStateFlow(UiState())
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

fun onButtonClicked() { /* ... */ }

// En el Composable
val uiState by viewModel.uiState.collectAsState()

Button(onClick = { viewModel.onButtonClicked() }) {
    Text(text = uiState.buttonText)
}
```

### 5.2. Composición y Performance

- **Composables Pequeños y Reutilizables:** Al igual que en SwiftUI, mantén tus Composables pequeños y enfocados.
- **Usa `key`:** Proporciona un `key` en listas (`LazyColumn`, `LazyRow`) para ayudar a Compose a optimizar la recomposición.
- **Evita cálculos costosos** directamente en un Composable. Si es necesario, usa `remember` para cachear el resultado.

---

## 6. Flujo de Trabajo: Desarrollo de un Módulo

El proyecto se organiza en módulos de Gradle (`:core`, `:feature:auth`, etc.).

1.  **Definir la API del Módulo:** Dentro de cada módulo, la capa de `api` o el código en `src/main` expone las clases e interfaces públicas. El resto es `internal`.
2.  **Implementar la Lógica:** La implementación concreta reside dentro del módulo.
3.  **Inyección de Dependencias:** Usaremos **Hilt** para la inyección de dependencias. Define los providers en un módulo de Hilt.
4.  **Escribir Tests Unitarios:** Usa **JUnit5** y **MockK/Mockito** para testear la lógica en `src/test`.
5.  **Añadir un `README.md`:** Cada módulo debe tener un `README.md` que explique su propósito y cómo usarlo.
6.  **Integrar en la App:** Añade la dependencia del módulo en el `build.gradle.kts` del módulo que lo necesita (ej. el módulo `:app` depende de `:feature:auth`).

---

## 7. Estrategia de Testing

- **Tests Unitarios (`/test`):** Para ViewModels, Casos de Uso, Repositorios. Rápidos y se ejecutan en la JVM.
- **Tests de Integración (`/androidTest`):** Para probar interacciones con la base de datos (Room), o APIs (con MockWebServer).
- **Tests de UI (`/androidTest`):** Usa la **Compose Test Rule** para verificar que tus Composables reaccionan correctamente a los cambios de estado y eventos.

---

## 8. Gestión de Dependencias (Gradle)

- **Catálogo de Versiones (Version Catalog):** Todas las dependencias deben ser declaradas en el archivo `libs.versions.toml`. Esto centraliza la gestión de versiones.
- **Aprobación Requerida:** No añadir nuevas dependencias sin aprobación del equipo.

---

## 9. Localización y Accesibilidad

### Localización

Todo el texto visible para el usuario **debe** usar recursos de string.
- **Mal:** `Text("Welcome!")`
- **Bien:** `Text(text = stringResource(R.string.welcome_title))`

### Accesibilidad

- **Contenido Descriptivo:** Usa el parámetro `contentDescription` en `Image` y `Icon` para usuarios de TalkBack.
- **Tamaño Táctil:** Asegúrate de que los elementos clicables tengan un tamaño mínimo de `48x48dp`.

---

## 10. Logging y Telemetría

- **Desarrollo:** Usa la librería **Timber**. Se integra con Logcat pero permite plantar árboles diferentes para las builds de release (ej. uno que envíe los logs a un servicio de telemetría).
- **Producción:** El módulo `:core:analytics` abstraerá la implementación de la telemetría. La implementación inicial usará Timber, pero puede ser reemplazada por un SDK de Firebase, Datadog, etc., sin cambiar el código de la app.
