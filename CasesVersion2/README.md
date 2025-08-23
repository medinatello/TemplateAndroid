# CasesVersion2 - TemplateAndroid KMP

**Versión**: 2.0  
**Fecha de creación**: 23 de Agosto, 2025  
**Arquitectura**: Kotlin Multiplatform (KMP) pura  

## Descripción

Este es el proyecto **TemplateAndroid Version 2** - una reescritura completa con arquitectura **KMP pura** desde el inicio, diseñada para resolver los problemas arquitectónicos y de deuda técnica del proyecto original.

## Problemas resueltos del V1

❌ **Problemas del proyecto original**:
- Arquitectura híbrida (Android nativo + KMP)
- Namespace pollution (`androidbase` vs `sortisplus`)
- Archivos duplicados recurrentes
- Conflictos de DI (Hilt vs Koin)
- Deuda técnica acumulada de 4+ sprints

✅ **Soluciones en V2**:
- Arquitectura KMP 100% desde día 1
- Namespace unificado: `com.sortisplus.*`
- Código limpio sin duplicaciones
- DI unificado con Koin
- Stack moderno y coherente

## Arquitectura objetivo

### Stack tecnológico unificado
```
📱 Plataformas soportadas:
- Android (API 29+)
- Desktop (Windows, macOS, Linux)
- iOS (preparado para futuro)

🏗️ Arquitectura:
- Clean Architecture
- MVVM + MVI
- Repository Pattern

🛠️ Stack técnico:
- Kotlin Multiplatform 2.0+
- Compose Multiplatform
- Koin (DI)
- SQLDelight (Database)
- Ktor (Network)
- Multiplatform Settings
- Napier (Logging)
```

### Estructura de módulos
```
CasesVersion2/
├── shared/                 # Lógica KMP compartida
│   ├── commonMain/         # Código compartido
│   ├── androidMain/        # Android específico
│   ├── desktopMain/        # Desktop específico
│   └── commonTest/         # Tests compartidos
├── androidApp/             # App Android
├── desktopApp/             # App Desktop
├── core/                   # Módulos core compartidos
└── features/               # Features modulares
```

## Migración desde V1

### Características a migrar:
1. ✅ Domain models (Person, Element, etc.)
2. ✅ Use cases definidos
3. ✅ Business logic
4. ✅ Test cases
5. ✅ UI components (adaptados a KMP)

### NO se migra:
- ❌ Código Android nativo duplicado
- ❌ Configuraciones Hilt
- ❌ Namespace `androidbase`
- ❌ Archivos duplicados
- ❌ Deuda técnica acumulada

## Planificación

### Fase 1: Base KMP (Día 1)
- [x] Estructura de proyecto KMP
- [ ] Configuración base de Gradle
- [ ] Módulo shared con domain layer
- [ ] Configuración Koin
- [ ] Configuración SQLDelight

### Fase 2: Core Features (Días 2-3)
- [ ] Migrar domain models
- [ ] Implementar repositories
- [ ] Configurar networking con Ktor
- [ ] ViewModels compartidos

### Fase 3: UI multiplataforma (Días 3-4)
- [ ] UI base con Compose Multiplatform
- [ ] Navegación
- [ ] Theme system
- [ ] Componentes reutilizables

### Fase 4: Features (Días 5+)
- [ ] Feature: Authentication
- [ ] Feature: Person management
- [ ] Feature: Settings
- [ ] Testing completo

## Criterios de éxito

1. ✅ **Build limpio**: Sin warnings ni errores
2. ✅ **Tests**: Cobertura >80%
3. ✅ **Multiplataforma**: Android + Desktop funcionando
4. ✅ **Arquitectura**: Clean Architecture aplicada
5. ✅ **Performance**: Tiempo de build <2 minutos
6. ✅ **Mantenibilidad**: Código limpio y documentado

## Comandos básicos

```bash
# Build completo
./gradlew build

# Run Android
./gradlew :androidApp:installDebug

# Run Desktop
./gradlew :desktopApp:run

# Tests
./gradlew test

# Análisis estático
./gradlew detekt ktlintCheck
```

---

**IMPORTANTE**: Este proyecto V2 reemplaza completamente al proyecto original. Una vez validado, se debe archivar el proyecto V1 y continuar desarrollo únicamente en V2.