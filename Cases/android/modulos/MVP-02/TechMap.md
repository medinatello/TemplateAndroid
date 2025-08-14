# TechMap MVP-02: UI Framework y Navegación

## Dependencias y Versiones

### Compose BOM
```kotlin
// build.gradle.kts (Module: app)
implementation(platform("androidx.compose:compose-bom:2024.04.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose:1.9.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
```

### Navigation
```kotlin
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // Si usamos Hilt
```

### Testing
```kotlin
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
debugImplementation("androidx.compose.ui:ui-tooling")
debugImplementation("androidx.compose.ui:ui-test-manifest")
```

## Arquitectura de Módulos

### Core UI Module (:core:ui)
```
core/ui/
├── src/main/kotlin/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   ├── Type.kt
│   │   └── Dimension.kt
│   ├── components/
│   │   ├── buttons/
│   │   ├── inputs/
│   │   ├── cards/
│   │   └── dialogs/
│   └── utils/
│       ├── Extensions.kt
│       └── Previews.kt
└── src/test/kotlin/
```

### Feature Module Structure
```
feature/home/
├── src/main/kotlin/
│   ├── navigation/
│   │   └── HomeNavigation.kt
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   └── components/
│       └── HomeComponents.kt
```

## Implementación Técnica

### 1. Theme Setup (Material 3)

#### Color.kt
```kotlin
@file:Suppress("MagicNumber")

package com.sortisplus.templateandroid.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary Colors
private val md_theme_light_primary = Color(0xFF6750A4)
private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
private val md_theme_light_primaryContainer = Color(0xFFEADDFF)
private val md_theme_light_onPrimaryContainer = Color(0xFF21005D)

// Secondary Colors
private val md_theme_light_secondary = Color(0xFF625B71)
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_secondaryContainer = Color(0xFFE8DEF8)
private val md_theme_light_onSecondaryContainer = Color(0xFF1D192B)

// Error Colors
private val md_theme_light_error = Color(0xFFBA1A1A)
private val md_theme_light_errorContainer = Color(0xFFFFDAD6)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_onErrorContainer = Color(0xFF410002)

// Surface Colors
private val md_theme_light_background = Color(0xFFFFFBFE)
private val md_theme_light_onBackground = Color(0xFF1C1B1F)
private val md_theme_light_surface = Color(0xFFFFFBFE)
private val md_theme_light_onSurface = Color(0xFF1C1B1F)

val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
)

// Dark theme colors (similar structure)
val DarkColorScheme = darkColorScheme(
    // ... implement dark colors
)
```

#### Type.kt
```kotlin
package com.sortisplus.templateandroid.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```

### 2. Navegación Tipada

#### NavigationRoutes.kt
```kotlin
package com.sortisplus.templateandroid.navigation

import kotlinx.serialization.Serializable

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

#### AppNavigation.kt
```kotlin
package com.sortisplus.templateandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sortisplus.templateandroid.feature.home.navigation.homeNavigation
import com.sortisplus.templateandroid.feature.profile.screens.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: NavigationRoutes = NavigationRoutes.Home
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeNavigation(navController)
        
        composable<NavigationRoutes.Profile> { backStackEntry ->
            val profile = backStackEntry.toRoute<NavigationRoutes.Profile>()
            ProfileScreen(
                userId = profile.userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable<NavigationRoutes.Details> { backStackEntry ->
            val details = backStackEntry.toRoute<NavigationRoutes.Details>()
            DetailsScreen(
                id = details.id,
                category = details.category,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
```

### 3. Validación de Formularios

#### Validator.kt
```kotlin
package com.sortisplus.templateandroid.core.ui.validation

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

interface Validator<T> {
    fun validate(value: T): ValidationResult
}

class EmailValidator : Validator<String> {
    override fun validate(value: String): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult.Error("Email is required")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            ValidationResult.Error("Invalid email format")
        } else {
            ValidationResult.Success
        }
    }
}

class PasswordValidator : Validator<String> {
    override fun validate(value: String): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult.Error("Password is required")
        } else if (value.length < MIN_PASSWORD_LENGTH) {
            ValidationResult.Error("Password must be at least $MIN_PASSWORD_LENGTH characters")
        } else {
            ValidationResult.Success
        }
    }
    
    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}
```

#### ValidatedTextField.kt
```kotlin
package com.sortisplus.templateandroid.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.sortisplus.templateandroid.core.ui.validation.ValidationResult
import com.sortisplus.templateandroid.core.ui.validation.Validator

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    validator: Validator<String>,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true
) {
    var validationResult by remember { mutableStateOf<ValidationResult?>(null) }
    val isError = validationResult is ValidationResult.Error
    
    LaunchedEffect(value) {
        if (value.isNotBlank()) {
            validationResult = validator.validate(value)
        }
    }
    
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                validationResult = validator.validate(newValue)
            },
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            isError = isError,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = if (isError) {
                        "$label with error: ${(validationResult as? ValidationResult.Error)?.message}"
                    } else {
                        label
                    }
                }
        )
        
        if (isError) {
            Text(
                text = (validationResult as ValidationResult.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.semantics {
                    contentDescription = "Error: ${(validationResult as ValidationResult.Error).message}"
                }
            )
        }
    }
}
```

### 4. Accesibilidad

#### AccessibilityExtensions.kt
```kotlin
package com.sortisplus.templateandroid.core.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

fun Modifier.accessibleButton(
    contentDescription: String,
    enabled: Boolean = true
): Modifier = this.semantics {
    this.contentDescription = contentDescription
    role = Role.Button
}

fun Modifier.accessibleClickable(
    contentDescription: String,
    role: Role = Role.Button
): Modifier = this.semantics {
    this.contentDescription = contentDescription
    this.role = role
}
```

## Testing Strategy

### Compose Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun homeScreen_displaysWelcomeMessage() {
        composeTestRule.setContent {
            TemplateAndroidTheme {
                HomeScreen(
                    uiState = HomeUiState(welcomeMessage = "Welcome"),
                    onNavigateToProfile = {}
                )
            }
        }
        
        composeTestRule
            .onNodeWithText("Welcome")
            .assertIsDisplayed()
    }
    
    @Test
    fun homeScreen_navigationButton_isAccessible() {
        composeTestRule.setContent {
            TemplateAndroidTheme {
                HomeScreen(
                    uiState = HomeUiState(),
                    onNavigateToProfile = {}
                )
            }
        }
        
        composeTestRule
            .onNodeWithContentDescription("Navigate to profile")
            .assertHasClickAction()
            .assertIsEnabled()
    }
}
```

## Configuración Build

### build.gradle.kts (app module)
```kotlin
android {
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.04.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    
    // Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("kotlinx-serialization-json:1.6.3")
    
    // Testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

## Patrones y Convenciones

### Naming Conventions
- Screens: `{Feature}Screen.kt`
- ViewModels: `{Feature}ViewModel.kt`
- Components: `{Feature}Components.kt`
- Navigation: `{Feature}Navigation.kt`

### State Management Pattern
```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList(),
    val error: String? = null
)

class FeatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun handleIntent(intent: UiIntent) {
        // Handle user actions
    }
}
```

## Métricas de Calidad
- Cobertura de tests: ≥ 80%
- Lint warnings: 0 críticos
- Accessibility score: ≥ 95% (usando Accessibility Scanner)
- Performance: < 16ms per frame en dispositivos objetivo
