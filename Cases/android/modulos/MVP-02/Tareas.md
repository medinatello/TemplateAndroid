# MVP-02 — UI base y navegación (Android)

## Historias de usuario
- Como usuario, quiero navegar desde Login a Home tras validación local.
- Como usuario, quiero ver un tema consistente (Material 3, claro/oscuro).

## Criterios de aceptación
- `NavHost` con rutas tipadas (`login`, `home`).
- Validación básica en formulario (email requerido, password ≥ 6).
- Soporte de tema claro/oscuro y a11y mínima (contentDescription).

## Entregables
- Pantallas `LoginScreen`, `HomeScreen` en Compose.
- `Theme.kt` con esquema Material 3.

## Estimación
- 5 puntos. 1 día.

## DoR
- Reglas de validación aprobadas.

## DoD
- UI tests de navegación y validación.

## Comandos
- `./gradlew :app:connectedDebugAndroidTest`

## Código de ejemplo
```kotlin
@Composable fun AppNav() {
  val nav = rememberNavController()
  NavHost(nav, startDestination = "login") {
    composable("login") { Login { nav.navigate("home") } }
    composable("home") { Home() }
  }
}
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Navegación básica
  Scenario: Login exitoso
    Given que ingreso email y contraseña válidos
    When toco el botón Ingresar
    Then navego a Home
    And el botón Ingresar queda deshabilitado durante el envío
```

## DoR extendido
- Reglas de validación acordadas (email, longitud mínima).
- Paleta de colores y tipografía definidas.

## DoD extendido
- Pruebas UI cubren navegación y estados de error.
- A11y básica verificada (labels, contentDescription, tamaños táctiles).
