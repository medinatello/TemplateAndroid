# Estructura del Proyecto CasesVersion2

## Arquitectura KMP moderna y limpia

```
CasesVersion2/
├── gradle/                           # Configuración Gradle
│   ├── libs.versions.toml           # Catálogo de versiones centralizado
│   └── wrapper/
├── shared/                          # 🎯 Módulo KMP principal
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/kotlin/       # Código compartido multiplataforma
│       │   └── com/sortisplus/shared/
│       │       ├── di/              # Dependency Injection (Koin)
│       │       ├── domain/          # Clean Architecture - Domain Layer
│       │       │   ├── model/       # Entities y Value Objects
│       │       │   ├── repository/  # Repository interfaces
│       │       │   └── usecase/     # Use Cases (Business Logic)
│       │       ├── data/            # Clean Architecture - Data Layer
│       │       │   ├── local/       # SQLDelight local database
│       │       │   ├── remote/      # Ktor network layer
│       │       │   └── repository/  # Repository implementations
│       │       ├── presentation/    # Clean Architecture - Presentation Layer
│       │       │   └── viewmodel/   # ViewModels compartidos
│       │       └── platform/        # Abstracciones de plataforma
│       ├── androidMain/kotlin/      # Implementaciones Android específicas
│       │   └── com/sortisplus/shared/
│       │       └── platform/        # Android-specific implementations
│       ├── desktopMain/kotlin/      # Implementaciones Desktop específicas
│       │   └── com/sortisplus/shared/
│       │       └── platform/        # Desktop-specific implementations
│       ├── commonTest/kotlin/       # Tests compartidos
│       ├── androidUnitTest/kotlin/  # Tests específicos Android
│       └── desktopTest/kotlin/      # Tests específicos Desktop
├── androidApp/                      # 📱 Aplicación Android
│   ├── build.gradle.kts
│   ├── src/main/
│   │   ├── kotlin/com/sortisplus/templateandroid/
│   │   │   ├── MainActivity.kt      # Actividad principal
│   │   │   ├── TemplateAndroidApplication.kt
│   │   │   └── ui/                  # UI específica Android (si aplica)
│   │   └── AndroidManifest.xml
│   └── src/test/                    # Tests específicos Android App
├── desktopApp/                      # 🖥️ Aplicación Desktop  
│   ├── build.gradle.kts
│   └── src/
│       ├── desktopMain/kotlin/
│       │   └── com/sortisplus/desktopapp/
│       │       └── DesktopApp.kt    # Punto de entrada Desktop
│       └── desktopTest/kotlin/
├── core/                            # 🔧 Módulos core (si necesarios)
│   └── design-system/               # Sistema de diseño compartido
│       ├── build.gradle.kts
│       └── src/commonMain/kotlin/
├── features/                        # 🎯 Features modulares (futuro)
│   ├── auth/                        # Feature: Authentication
│   ├── person-management/           # Feature: Person Management  
│   └── settings/                    # Feature: Settings
├── docs/                            # 📚 Documentación
│   ├── architecture/                # Documentación arquitectural
│   ├── setup/                       # Guías de setup
│   └── development/                 # Guías de desarrollo
├── scripts/                         # 🛠️ Scripts de utilidad
│   ├── setup.sh                     # Script de setup inicial
│   ├── build-all.sh                 # Build de todas las plataformas
│   └── tests.sh                     # Ejecutar todos los tests
├── build.gradle.kts                 # Build principal
├── settings.gradle.kts              # Configuración de módulos
├── gradle.properties               # Properties de Gradle
├── .gitignore
└── README.md
```

## Principios de organización

### 1. **Separación clara de responsabilidades**
- `shared/`: Lógica de negocio compartida
- `androidApp/`: UI y lógica específica Android
- `desktopApp/`: UI y lógica específica Desktop

### 2. **Clean Architecture en shared/**
```
presentation/ → domain/ → data/
     ↓            ↓         ↓
 ViewModels   Use Cases  Repositories
               Models    Data Sources
```

### 3. **Namespace unificado**
- ✅ Único namespace: `com.sortisplus.*`
- ✅ No mezclas con `androidbase`
- ✅ Estructura coherente en todas las plataformas

### 4. **Configuración centralizada**
- `gradle/libs.versions.toml`: Todas las versiones
- `gradle.properties`: Configuración global
- Dependencias organizadas por categorías

### 5. **Testing strategy**
```
commonTest/     → Tests de lógica compartida (JUnit, Kotest)
androidTest/    → Tests específicos Android (Espresso)
desktopTest/    → Tests específicos Desktop
```

## Ventajas sobre proyecto V1

| Aspecto | V1 (Actual) | V2 (Nuevo) |
|---------|-------------|------------|
| Arquitectura | Híbrida (Android + KMP) | KMP pura |
| Namespace | `androidbase` + `sortisplus` | `com.sortisplus` únicamente |
| DI | Hilt + Koin | Koin únicamente |
| Database | Room + SQLDelight | SQLDelight únicamente |
| Settings | DataStore + Multiplatform Settings | Multiplatform Settings únicamente |
| Duplicación | Archivos duplicados recurrentes | Sin duplicaciones |
| Mantenibilidad | Baja (deuda técnica) | Alta (código limpio) |
| Build time | ~5+ minutos | <2 minutos |
| Test coverage | Fragmentada | Unificada >80% |

## Migración de código existente

### ✅ Se migra (con adaptaciones):
- Domain models (`Person`, `Element`, etc.)
- Business logic (Use cases)
- Test cases (adaptados a KMP)
- UI components (convertidos a Compose Multiplatform)

### ❌ NO se migra:
- Configuraciones Android nativas
- Dependencias Hilt
- Archivos con namespace `androidbase`
- Código duplicado
- Configuraciones específicas de Room/DataStore Android

## Criterios de validación

### Build y compilación
```bash
✅ ./gradlew build                    # Sin errores
✅ ./gradlew shared:build            # Módulo compartido compila
✅ ./gradlew androidApp:assembleDebug # APK se genera
✅ ./gradlew desktopApp:createDistributable # Desktop executable
```

### Tests
```bash
✅ ./gradlew test                     # Todos los tests pasan
✅ ./gradlew shared:testDebugUnitTest # Tests compartidos
✅ Cobertura >80% en lógica de negocio
```

### Calidad de código
```bash
✅ ./gradlew detekt                   # Análisis estático
✅ ./gradlew ktlintCheck              # Style check
✅ Sin warnings críticos
```

Esta estructura garantiza un proyecto limpio, mantenible y escalable desde el primer día.