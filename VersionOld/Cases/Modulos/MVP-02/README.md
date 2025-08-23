# MVP-02: UI Framework y Navegación

## Objetivo
Implementar el sistema de UI basado en Jetpack Compose + Material 3 con navegación tipada, validación de formularios y accesibilidad básica.

## Alcance del Sprint
- ✅ Configuración de Jetpack Compose + Material 3
- ✅ Sistema de navegación con rutas tipadas
- ✅ Design System básico (colores, tipografía, componentes)
- ✅ Validación de formularios
- ✅ Accesibilidad mínima (a11y)
- ✅ Theming con Dynamic Color (Android 12+)
- ✅ Preview y testing básico de UI

## Entregables

### 1. Configuración Base de Compose
- Actualización de `build.gradle.kts` con dependencias de Compose
- Configuración de `CompileSDK` y `targetSDK` compatibles
- Theme principal con Material 3

### 2. Design System
- Esquema de colores (light/dark + dynamic)
- Tipografía siguiendo Material 3
- Componentes reutilizables básicos
- Espaciado y dimensiones consistentes

### 3. Sistema de Navegación
- Navigation Compose con rutas tipadas
- Navegación por parámetros type-safe
- Back stack management
- Deep linking básico

### 4. Validación de Formularios
- Validators reutilizables
- Estados de error consistentes
- Feedback visual inmediato
- Integración con accessibility

### 5. Accesibilidad
- Content descriptions
- Semantic properties
- Screen reader support
- Touch target compliance (48dp mínimo)

## Criterios de Aceptación

### Funcionales
- [ ] La app usa Compose como único framework de UI
- [ ] Navegación funciona correctamente entre pantallas
- [ ] Formularios validan datos en tiempo real
- [ ] Soporte para modo claro/oscuro
- [ ] Dynamic Color funciona en Android 12+

### Técnicos
- [ ] Todo el código en inglés
- [ ] Sin hardcoded strings (usar stringResource)
- [ ] Componentes siguiendo UDF (Unidirectional Data Flow)
- [ ] Tests de UI con Compose Testing
- [ ] Preview functions para todos los componentes

### Calidad
- [ ] Lint sin errores críticos
- [ ] Cobertura de tests ≥ 80%
- [ ] Documentación de componentes públicos
- [ ] Accesibilidad validada con TalkBack

## Dependencias Técnicas
- Jetpack Compose BOM
- Material 3
- Navigation Compose
- ViewModel Compose
- Activity Compose
- Compose Testing

## Consideraciones de Implementación

### Arquitectura UI
```
presentation/
├── theme/          (Material 3 theme, colors, typography)
├── components/     (Reusable UI components)
├── screens/        (Screen composables)
├── navigation/     (Navigation setup, routes)
└── utils/          (UI utilities, extensions)
```

### State Management
- ViewModel para lógica de negocio
- State hoisting para componentes reutilizables
- `remember` para estado local simple
- `collectAsState()` para observar flows

### Testing Strategy
- Unit tests para ViewModels
- Compose tests para UI components
- Screenshot tests para regression
- Accessibility tests con Espresso

## Riesgos y Mitigaciones

### Riesgo: Complejidad de navegación
**Mitigación**: Usar Navigation Compose con rutas simples, evitar deep nesting

### Riesgo: Performance en recomposiciones
**Mitigación**: Usar `derivedStateOf`, `LaunchedEffect` apropiadamente

### Riesgo: Inconsistencia visual
**Mitigación**: Design System centralizado, usar tokens de diseño

## Notas de Implementación
- Seguir estrictamente las reglas de nomenclatura en inglés
- Usar `stringResource()` para todos los textos
- Implementar previews para todos los componentes
- Validar accesibilidad en cada pantalla
- Mantener componentes pequeños y reutilizables
