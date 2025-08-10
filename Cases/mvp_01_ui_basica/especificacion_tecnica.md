# Especificación Técnica MVP-01

## Stack Tecnológico

- **Lenguaje**: Kotlin 2.1
- **SDK mínimo**: 23
- **SDK objetivo**: 34 (Android 14)
- **Jetpack Compose**: Implementación de UI
- **Material 3**: Diseño y componentes
- **Navigation Compose**: Navegación entre pantallas

## Componentes Principales

### Arquitectura

Para este MVP inicial, mantendremos una estructura simple:

```
app/
  ├─ ui/
  │   ├─ screens/
  │   │   ├─ home/
  │   │   └─ details/
  │   ├─ theme/
  │   └─ navigation/
  ├─ domain/
  ├─ di/
  └─ MainActivity.kt
```

### Sistema de Navegación

Utilizaremos Navigation Compose con un NavHost centralizado en MainActivity:

```kotlin
NavHost(
    navController = navController,
    startDestination = "home"
) {
    composable("home") { HomeScreen(navController) }
    composable("details") { DetailsScreen(navController) }
}
```

### Pruebas

Implementaremos pruebas básicas de UI utilizando los componentes de test de Compose:

- Pruebas de navegación
- Verificación de estado inicial de pantallas
- Comprobación de interacciones básicas
