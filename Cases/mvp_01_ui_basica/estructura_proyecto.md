# Estructura del Proyecto (MVP-01)

Para el MVP-01, mantendremos una estructura simple con un único módulo. A medida que el proyecto crezca, extraeremos componentes a módulos separados.

## Fase A (MVP-01): Estructura Inicial

```
app/
  ├─ ui/
  │   ├─ screens/
  │   │   ├─ home/
  │   │   │   ├─ HomeScreen.kt
  │   │   │   └─ HomeViewModel.kt (opcional)
  │   │   └─ details/
  │   │       ├─ DetailsScreen.kt
  │   │       └─ DetailsViewModel.kt (opcional)
  │   ├─ theme/
  │   │   ├─ Color.kt
  │   │   ├─ Theme.kt
  │   │   └─ Type.kt
  │   ├─ components/
  │   │   └─ Buttons.kt
  │   └─ navigation/
  │       └─ AppNavigation.kt
  ├─ domain/
  │   └─ model/
  ├─ di/
  │   └─ AppModule.kt (si se usa Hilt)
  └─ MainActivity.kt
```

## Convenciones de Nombres

- **Pantallas**: `NombreScreen.kt`
- **ViewModels**: `NombreViewModel.kt`
- **Componentes UI**: Descriptivo y específico (ej. `PrimaryButton.kt`, `FormTextField.kt`)
- **Temas**: Separados por función (`Color.kt`, `Type.kt`, `Theme.kt`)

## Planificación para Modularización Futura

Cuando avancemos a MVP-02 y MVP-03, consideraremos extraer estos componentes a módulos separados:

- `:core:ui` - Temas y componentes base
- `:core:navigation` - Contratos de navegación y rutas

Para MVP-01, mantendremos todo en un único módulo para facilitar el desarrollo inicial.
