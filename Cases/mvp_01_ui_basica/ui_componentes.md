# Componentes UI para MVP-01

## Pantallas Principales

### HomeScreen

Pantalla inicial que muestra al usuario cuando abre la aplicación por primera vez.

**Componentes:**
- Título de la aplicación (Text)
- Texto descriptivo sobre la funcionalidad (Text)
- Botón primario "Continuar" (Button)

**Ejemplo de implementación:**

```kotlin
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Aplicación",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esta es una descripción de la aplicación que muestra su funcionalidad principal.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("details") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Continuar")
        }
    }
}
```

### DetailsScreen

Pantalla secundaria a la que se navega desde Home.

**Componentes:**
- Texto estático con información (Text)
- Botón "Volver" para regresar a Home (Button)

**Ejemplo de implementación:**

```kotlin
@Composable
fun DetailsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Detalles",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esta es la pantalla de detalles que muestra información adicional sobre la aplicación.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Volver")
        }
    }
}
```

## Componentes Reutilizables

### PrimaryButton

Botón primario con estilo consistente en toda la aplicación.

```kotlin
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(text)
    }
}
```

### AppTextField

Campo de texto con estilo consistente.

```kotlin
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
```

## Tema y Estilos

### MaterialTheme

Definición del tema de Material 3 para la aplicación.

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
```

### Definición de Colores

```kotlin
private val LightColorScheme = lightColorScheme(
    primary = Blue80,
    onPrimary = Blue20,
    secondary = DarkBlue80,
    onSecondary = DarkBlue20,
    tertiary = Yellow80,
    onTertiary = Yellow20,
    background = Grey10,
    onBackground = Grey90,
    surface = Grey10,
    onSurface = Grey90
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue30,
    onPrimary = Blue90,
    secondary = DarkBlue30,
    onSecondary = DarkBlue90,
    tertiary = Yellow30,
    onTertiary = Yellow90,
    background = Grey90,
    onBackground = Grey10,
    surface = Grey90,
    onSurface = Grey10
)
```
