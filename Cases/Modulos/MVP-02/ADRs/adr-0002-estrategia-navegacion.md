# ADR-0002: Navigation Strategy

## Status
Accepted

## Context
Con Jetpack Compose seleccionado como framework de UI (ADR-0001), necesitamos definir la estrategia de navegación para la aplicación. Las opciones principales son:

1. **Navigation Compose** - Solución oficial de Google para Compose
2. **Compose Destinations** - Librería de terceros con generación de código
3. **Custom Navigation** - Implementación propia basada en sealed classes
4. **Fragment-based Navigation** - Usar Fragments con Compose content
5. **Multi-Activity Architecture** - Actividades separadas para diferentes flows

## Decision
**Seleccionamos Navigation Compose con rutas tipadas usando Kotlin Serialization** como estrategia principal de navegación.

## Rationale

### Factores a favor de Navigation Compose:

#### Technical Benefits
- **Type Safety**: Rutas tipadas con Kotlin Serialization previenen errores
- **Official Support**: Mantenido directamente por Google
- **Deep Linking**: Soporte nativo para deep links
- **Back Stack Management**: Manejo automático del back stack
- **State Preservation**: SavedStateHandle integrado
- **Testing Support**: APIs para testing de navegación

#### Integration Benefits
- **Compose Native**: Diseñado específicamente para Jetpack Compose
- **ViewModel Integration**: Soporte nativo para ViewModel scoping
- **Animation Support**: Transiciones customizables entre pantallas
- **Lifecycle Aware**: Respeta el ciclo de vida de Android

#### Developer Experience
- **Declarative**: Define navegación de forma declarativa
- **IDE Support**: Autocompletado y refactoring en Android Studio
- **Documentation**: Extensa documentación oficial
- **Community**: Gran adopción en la comunidad Android

### Implementación con Rutas Tipadas:

#### Navigation Routes Definition
```kotlin
@Serializable
sealed class NavigationRoutes {
    @Serializable
    data object Home : NavigationRoutes()
    
    @Serializable
    data class Profile(val userId: String) : NavigationRoutes()
    
    @Serializable
    data class Details(
        val id: String,
        val category: String? = null
    ) : NavigationRoutes()
}
```

#### NavHost Implementation
```kotlin
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Home
    ) {
        composable<NavigationRoutes.Home> {
            HomeScreen(
                onNavigateToProfile = { userId ->
                    navController.navigate(NavigationRoutes.Profile(userId))
                }
            )
        }
        
        composable<NavigationRoutes.Profile> { backStackEntry ->
            val profile = backStackEntry.toRoute<NavigationRoutes.Profile>()
            ProfileScreen(
                userId = profile.userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
```

### Factores en contra de otras opciones:

#### Compose Destinations
- **Third-party Dependency**: No mantenido por Google
- **Code Generation**: Complejidad adicional en build process
- **Learning Curve**: Sintaxis específica de la librería
- **Debugging**: Más difícil debuggear código generado

#### Custom Navigation
- **Maintenance Overhead**: Necesitamos mantener nuestra propia implementación
- **Missing Features**: No tendríamos deep linking, state preservation, etc.
- **Testing**: Tendríamos que crear nuestros propios testing utilities
- **Documentation**: Sin documentación oficial

#### Fragment-based Navigation
- **Legacy Approach**: Fragments están siendo desplazados por Compose
- **Complexity**: Mezclar Fragments y Compose añade complejidad
- **Performance**: Overhead de Fragment lifecycle
- **Maintenance**: Código más complejo de mantener

## Consequences

### Positive Consequences
- **Type Safety**: Errores de navegación detectados en compile time
- **Maintainability**: Rutas centralizadas y fáciles de refactorizar
- **Testing**: Fácil testing de flows de navegación
- **Deep Linking**: Soporte automático para deep links
- **Performance**: Navegación optimizada para Compose
- **Future-proof**: Seguimos la dirección oficial de Google

### Negative Consequences
- **Learning Curve**: Equipo necesita aprender Navigation Compose
- **Serialization Overhead**: Pequeño overhead por serialización de parámetros
- **Beta Features**: Algunas features de type-safe navigation son recientes
- **Complex Flows**: Navegación muy compleja puede ser challenging

### Risks and Mitigations
- **Risk**: Performance con muchos parámetros complejos
  - **Mitigation**: Usar parámetros simples, pasar IDs en lugar de objetos completos
- **Risk**: Deep linking con parámetros complejos
  - **Mitigation**: Implementar custom NavType para casos especiales
- **Risk**: Back stack management en flows complejos
  - **Mitigation**: Usar popUpTo y saveState apropiadamente

## Implementation Guidelines

### Navigation Structure
```
navigation/
├── NavigationRoutes.kt          (Sealed classes para rutas)
├── AppNavigation.kt             (NavHost principal)
├── NavigationExtensions.kt      (Helper functions)
└── NavigationTesting.kt         (Testing utilities)
```

### Feature Module Navigation
```kotlin
// En cada feature module
fun NavGraphBuilder.featureNavigation(
    navController: NavHostController
) {
    composable<NavigationRoutes.FeatureScreen> { backStackEntry ->
        val args = backStackEntry.toRoute<NavigationRoutes.FeatureScreen>()
        FeatureScreen(
            // args mapping
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
```

### Testing Navigation
```kotlin
@Test
fun navigation_fromHomeToProfile_passesCorrectUserId() {
    val navController = TestNavHostController(LocalContext.current)
    navController.setGraph(R.navigation.test_navigation)
    
    composeTestRule.setContent {
        AppNavigation(navController = navController)
    }
    
    // Simulate navigation
    composeTestRule.onNodeWithText("Profile").performClick()
    
    // Verify destination
    assertEquals(
        NavigationRoutes.Profile("testUser"),
        navController.currentDestination?.route
    )
}
```

### Performance Considerations
- **Lazy Loading**: Usar navigation by feature modules
- **Parameter Optimization**: Pasar IDs simples, no objetos complejos
- **State Management**: Usar SavedStateHandle para persistencia
- **Memory Management**: Limpiar ViewModels correctamente

## Migration Strategy

### Phase 1: Setup Navigation Infrastructure
- Implementar NavigationRoutes sealed classes
- Configurar NavHost básico
- Migrar pantallas principales

### Phase 2: Feature-by-Feature Migration
- Migrar cada feature module independientemente
- Mantener compatibilidad durante transición
- Testing exhaustivo de cada migración

### Phase 3: Advanced Features
- Implementar deep linking
- Agregar animations customizadas
- Optimizar performance

## Related ADRs
- ADR-0001: UI Framework Selection (foundation for this decision)
- Future ADR: State Management Strategy
- Future ADR: Deep Linking Strategy

## Standards and Conventions

### Naming Conventions
- Routes: `NavigationRoutes.{FeatureName}`
- Screens: `{Feature}Screen.kt`
- Navigation functions: `{feature}Navigation()`

### Code Standards
- Todo parámetro de navegación debe ser serializable
- Usar data classes para parámetros complejos
- Implementar onNavigateBack en todas las pantallas
- Documentar rutas con KDoc

### Error Handling
- Manejar rutas inválidas gracefully
- Logging de errores de navegación
- Fallback routes para casos de error

## References
- [Navigation Compose Documentation](https://developer.android.com/jetpack/compose/navigation)
- [Type-safe Navigation](https://developer.android.com/guide/navigation/design/type-safety)
- [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)
- [Android Navigation Testing](https://developer.android.com/guide/navigation/navigation-testing)
