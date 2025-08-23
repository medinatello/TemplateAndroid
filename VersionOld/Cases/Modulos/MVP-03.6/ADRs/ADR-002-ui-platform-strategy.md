# ADR-002: Estrategia de UI Multiplataforma para Desktop

## Estado
Aceptado

## Contexto
El MVP-03.6 requiere implementar funcionalidad UI real en Desktop que sea equivalente a la existente en Android. Necesitamos decidir cómo estructurar la UI multiplataforma para maximizar reutilización mientras optimizamos para cada plataforma.

### Desafíos
- Android UI usa Jetpack Compose con Material 3 y navegación compleja
- Desktop necesita equivalente funcional pero adaptado a patrones de escritorio
- Validación de formularios, design system y navegación deben funcionar en ambos
- Performance y UX deben ser óptimos para cada plataforma

## Decisión
Adoptamos una **estrategia de UI híbrida** con:
1. **Shared Business Components**: Lógica y estado en `commonMain`
2. **Platform-Adapted UI**: Compose components específicos pero estructuralmente similares
3. **Responsive Design System**: Componentes que se adaptan a cada plataforma

## Arquitectura UI Multiplataforma

### Estructura de Componentes
```
shared/src/commonMain/presentation/
├── viewmodel/              # ViewModels compartidos
├── state/                  # UI State definitions  
├── validation/             # Form validation logic
└── navigation/             # Navigation state & logic

app/src/main/ui/           # Android-specific UI
├── components/            # Android Compose components
├── screens/               # Android screen implementations
├── theme/                 # Android Material 3 theme
└── navigation/            # Android Navigation Compose

desktopApp/src/desktopMain/ui/  # Desktop-specific UI  
├── components/            # Desktop Compose components
├── screens/               # Desktop screen implementations
├── theme/                 # Desktop-adapted theme
└── navigation/            # Desktop navigation implementation
```

### Shared vs Platform-Specific

#### Shared (`commonMain`)
```kotlin
// ViewModels with business logic
class LoginViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    
    fun login(email: String, password: String) { /* shared logic */ }
}

// UI State definitions
data class LoginUiState(
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null
)

// Validation logic
object FormValidators {
    fun validateEmail(email: String): ValidationResult
    fun validatePassword(password: String): ValidationResult
}
```

#### Platform-Specific UI
```kotlin
// Android
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Android-optimized layout
        ValidatedTextField(
            value = email,
            onValueChange = { /* update */ },
            error = uiState.emailError,
            keyboardType = KeyboardType.Email
        )
        // ... rest of Android UI
    }
}

// Desktop  
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Card(modifier = Modifier.width(400.dp).padding(24.dp)) {
        // Desktop-optimized layout (windowed, different spacing)
        ValidatedTextField(
            value = email,
            onValueChange = { /* update */ },
            error = uiState.emailError,
            // Desktop-specific keyboard handling
        )
        // ... rest of Desktop UI
    }
}
```

## Design System Multiplataforma

### Shared Design Tokens
```kotlin
// commonMain
object DesignTokens {
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
    
    val CornerRadiusSmall = 4.dp
    val CornerRadiusMedium = 8.dp
}

// Android theme
@Composable
fun AndroidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = AndroidTypography,
        content = content
    )
}

// Desktop theme  
@Composable
fun DesktopTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = DesktopTypography, // Adjusted for desktop reading
        content = content
    )
}
```

### Component Adaptation Strategy

#### Shared Interface, Platform Implementation
```kotlin
// Shared component contract
interface ValidatedTextFieldComponent {
    @Composable
    fun ValidatedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        error: String?,
        label: String,
        modifier: Modifier = Modifier
    )
}

// Android implementation
class AndroidValidatedTextField : ValidatedTextFieldComponent {
    @Composable
    override fun ValidatedTextField(/* params */) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            // Android-specific styling
        )
    }
}

// Desktop implementation  
class DesktopValidatedTextField : ValidatedTextFieldComponent {
    @Composable
    override fun ValidatedTextField(/* params */) {
        Column {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                isError = error != null,
                // Desktop-specific styling (larger touch targets, etc.)
            )
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
```

## Navigation Multiplataforma

### Shared Navigation State
```kotlin
// commonMain
sealed class NavigationDestination {
    object Home : NavigationDestination()
    object Profile : NavigationDestination()
    data class UserDetails(val userId: String) : NavigationDestination()
}

class NavigationManager {
    private val _currentDestination = MutableStateFlow<NavigationDestination>(NavigationDestination.Home)
    val currentDestination = _currentDestination.asStateFlow()
    
    fun navigateTo(destination: NavigationDestination) {
        _currentDestination.value = destination
    }
}
```

### Platform Navigation Implementation
```kotlin
// Android - Navigation Compose
@Composable
fun AppNavigation(navigationManager: NavigationManager) {
    val navController = rememberNavController()
    val destination by navigationManager.currentDestination.collectAsState()
    
    LaunchedEffect(destination) {
        when (destination) {
            is NavigationDestination.Home -> navController.navigate("home")
            is NavigationDestination.Profile -> navController.navigate("profile")
            is NavigationDestination.UserDetails -> navController.navigate("user/${destination.userId}")
        }
    }
    
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable("profile") { ProfileScreen() }
        composable("user/{userId}") { UserDetailsScreen() }
    }
}

// Desktop - Window-based navigation
@Composable
fun DesktopAppNavigation(navigationManager: NavigationManager) {
    val destination by navigationManager.currentDestination.collectAsState()
    
    Row(modifier = Modifier.fillMaxSize()) {
        // Desktop sidebar navigation
        NavigationSidebar(
            currentDestination = destination,
            onNavigate = navigationManager::navigateTo
        )
        
        // Main content area
        Box(modifier = Modifier.weight(1f)) {
            when (destination) {
                is NavigationDestination.Home -> HomeScreen()
                is NavigationDestination.Profile -> ProfileScreen()
                is NavigationDestination.UserDetails -> UserDetailsScreen(destination.userId)
            }
        }
    }
}
```

## Platform-Specific Optimizations

### Android Optimizations
- Material 3 Dynamic Color support
- Navigation Compose with animations
- Android-specific keyboard handling
- Touch-optimized component sizing
- Android accessibility services integration

### Desktop Optimizations  
- Mouse hover states
- Keyboard navigation and shortcuts
- Window resizing behavior
- Desktop context menus
- OS-native file dialogs
- Desktop-appropriate component sizes

## Form Validation Multiplataforma

### Shared Validation Logic
```kotlin
// commonMain
class FormValidator {
    fun validateLoginForm(email: String, password: String): LoginValidationResult {
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        
        return LoginValidationResult(
            emailError = emailError,
            passwordError = passwordError,
            isValid = emailError == null && passwordError == null
        )
    }
}

data class LoginValidationResult(
    val emailError: String?,
    val passwordError: String?,
    val isValid: Boolean
)
```

### Platform UI Integration
```kotlin
// Both platforms use same validation, different UI feedback
@Composable
fun LoginForm(
    viewModel: LoginViewModel,
    platformComponent: ValidatedTextFieldComponent
) {
    val uiState by viewModel.uiState.collectAsState()
    
    with(platformComponent) {
        ValidatedTextField(
            value = email,
            onValueChange = viewModel::updateEmail,
            error = uiState.emailError,
            label = "Email"
        )
        
        ValidatedTextField(
            value = password,
            onValueChange = viewModel::updatePassword,
            error = uiState.passwordError,
            label = "Password"
        )
    }
}
```

## Testing Strategy

### Shared UI Logic Tests
```kotlin
// Test shared ViewModels
@Test
fun `login validation works correctly`() = runTest {
    val viewModel = LoginViewModel(mockAuthUseCase)
    
    viewModel.updateEmail("invalid-email")
    viewModel.updatePassword("123")
    
    val uiState = viewModel.uiState.value
    assertThat(uiState.emailError).isNotNull()
    assertThat(uiState.passwordError).isNotNull()
}
```

### Platform UI Tests
```kotlin
// Android UI tests
@Test
fun loginScreen_showsErrorsCorrectly() {
    composeTestRule.setContent {
        LoginScreen(viewModel = testViewModel)
    }
    
    composeTestRule.onNodeWithText("Email").performTextInput("invalid")
    composeTestRule.onNodeWithText("Invalid email format").assertIsDisplayed()
}

// Desktop UI tests (similar structure)
@Test
fun desktopLoginScreen_showsErrorsCorrectly() {
    // Desktop-specific test implementation
}
```

## Performance Considerations

### Lazy Loading
- Load heavy components only when needed
- Platform-specific resource loading
- Image loading optimized per platform

### Memory Management
- Shared ViewModels with proper lifecycle
- Platform-specific disposal patterns
- Efficient state management

## Implementación Gradual

### Fase 1: Shared Foundation
- ViewModels y state management
- Validation logic
- Navigation state management

### Fase 2: Component Adaptation
- Design system adaptation
- Basic component implementations
- Theme configuration

### Fase 3: Full Feature Parity
- Complex UI components
- Platform-specific optimizations
- Performance tuning

## Criterios de Éxito
- [ ] ≥ 90% feature parity entre Android y Desktop
- [ ] Shared business logic ≥ 85%
- [ ] Performance desktop acceptable (UI responsive)
- [ ] UX optimizada para cada plataforma
- [ ] Design consistency maintained

## Alternativas Consideradas

### Alternativa 1: 100% Shared UI
**Rechazada**: No permite optimizaciones específicas de plataforma

### Alternativa 2: Separate UI Stacks  
**Rechazada**: Duplicaría demasiado código y esfuerzo de mantenimiento

### Alternativa 3: Web-based Desktop
**Rechazada**: Performance inferior y UX no nativa

---

**Fecha**: 2025-01-17  
**Autor**: UI Lead + KMP Developer  
**Reviewers**: Tech Lead, UX Designer  
**Estado**: Aceptado para implementación en MVP-03.6