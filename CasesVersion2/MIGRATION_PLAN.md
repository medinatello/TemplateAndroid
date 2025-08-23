# Plan de Migración V1 → V2

## 🎯 Objetivo: Proyecto KMP limpio y moderno

**Tiempo estimado**: 2-3 días  
**Estrategia**: Nueva implementación con migración selectiva de código válido

---

## 📋 FASE 1: Análisis y Rescate (Día 1 - Mañana)

### ✅ **Código a rescatar del proyecto V1**

#### 🔍 **Domain Models** (Alta prioridad)
```bash
# Ubicación V1: shared/src/commonMain/kotlin/com/sortisplus/shared/domain/model/
```
- ✅ `Person.kt` - Modelo Person con validación
- ✅ `Element.kt` - Modelo Element
- ✅ `ValidationResult.kt` - Result pattern para validaciones
- ✅ `AuthState.kt` - Estados de autenticación
- ❓ `DatabaseResult.kt` - (Verificar si está corrupto)

#### 🔍 **Use Cases** (Alta prioridad)
```bash
# Ubicación V1: shared/src/commonMain/kotlin/com/sortisplus/shared/domain/usecase/
```
- ✅ `GetAppInfoUseCase.kt` - App info logic
- ✅ `CreatePersonUseCase.kt` - Crear persona
- ✅ `GetAllPersonsUseCase.kt` - Obtener personas
- ✅ `LoginUseCase.kt` - Login logic
- ✅ `LogoutUseCase.kt` - Logout logic
- ✅ `UpdateDarkThemeUseCase.kt` - Theme logic

#### 🔍 **Repository Interfaces** (Media prioridad)
```bash
# Ubicación V1: shared/src/commonMain/kotlin/com/sortisplus/shared/domain/repository/
```
- ✅ `PersonRepository.kt` - Interface para Person operations
- ✅ `AuthRepository.kt` - Interface para Authentication
- ✅ `SettingsRepository.kt` - Interface para Settings
- ✅ `ElementRepository.kt` - Interface para Element operations

#### 🔍 **Tests** (Media prioridad)
```bash
# Ubicación V1: shared/src/commonTest/kotlin/
```
- ✅ Tests de Use Cases
- ✅ Tests de Domain Models
- ✅ Tests de Repository implementations

#### 🔍 **UI Components** (Baja prioridad - adaptar)
```bash
# Ubicación V1: core/ui/src/
```
- ⚠️ Screens (requieren adaptación a Compose Multiplatform)
- ⚠️ Components (requieren adaptación)
- ⚠️ Theme system (adaptar a Material3)

### ❌ **Código a NO migrar**

- ❌ Cualquier archivo con namespace `androidbase`
- ❌ Configuraciones Hilt (`@HiltViewModel`, `@Inject`, etc.)
- ❌ Código específico Room/DataStore Android
- ❌ Archivos duplicados (con sufijo " 2", " 3", etc.)
- ❌ Módulos core Android (`core/datastore`, `core/data`)
- ❌ Feature modules híbridos (`feature/home`)

---

## 🏗️ FASE 2: Setup Proyecto V2 (Día 1 - Tarde)

### 2.1 **Estructura base**
```bash
mkdir -p CasesVersion2/{shared,androidApp,desktopApp,docs,scripts}
mkdir -p CasesVersion2/shared/src/{commonMain,commonTest,androidMain,desktopMain}
mkdir -p CasesVersion2/shared/src/commonMain/kotlin/com/sortisplus/shared/{domain,data,presentation,di,platform}
```

### 2.2 **Configuración Gradle**
- `settings.gradle.kts` - Configuración de módulos
- `build.gradle.kts` - Build principal
- `gradle/libs.versions.toml` - Catálogo de versiones
- `shared/build.gradle.kts` - Configuración KMP
- `androidApp/build.gradle.kts` - App Android
- `desktopApp/build.gradle.kts` - App Desktop

### 2.3 **Dependencies setup**
```toml
# gradle/libs.versions.toml
[versions]
kotlin = "2.0.0"
compose-multiplatform = "1.7.1"
koin = "3.5.6"
sqldelight = "2.0.2"
ktor = "2.3.12"
napier = "2.7.1"
```

### 2.4 **Configuración inicial**
- Koin modules setup
- SQLDelight database schema
- Basic navigation structure
- Theme system base

---

## 🚚 FASE 3: Migración Domain Layer (Día 2 - Mañana)

### 3.1 **Domain Models**
```kotlin
// V2: shared/src/commonMain/kotlin/com/sortisplus/shared/domain/model/
// Migrar desde V1 con adaptaciones menores
```

**Checklist**:
- [ ] Migrar `Person.kt` (validar que no tenga corruption)
- [ ] Migrar `Element.kt`
- [ ] Migrar `ValidationResult.kt`
- [ ] Migrar `AuthState.kt`
- [ ] Revisar y corregir `DatabaseResult.kt` si es necesario
- [ ] Actualizar namespaces a `com.sortisplus.shared.domain.model`

### 3.2 **Repository Interfaces**
```kotlin
// V2: shared/src/commonMain/kotlin/com/sortisplus/shared/domain/repository/
```

**Checklist**:
- [ ] Migrar interfaces de repository
- [ ] Adaptar signatures si es necesario para KMP
- [ ] Actualizar imports y namespaces

### 3.3 **Use Cases**
```kotlin
// V2: shared/src/commonMain/kotlin/com/sortisplus/shared/domain/usecase/
```

**Checklist**:
- [ ] Migrar Use Cases existentes
- [ ] Remover dependencias a Hilt
- [ ] Actualizar a Koin si es necesario
- [ ] Verificar lógica de negocio

### 3.4 **Domain Tests**
```kotlin
// V2: shared/src/commonTest/kotlin/com/sortisplus/shared/domain/
```

**Checklist**:
- [ ] Migrar tests de domain models
- [ ] Migrar tests de use cases
- [ ] Adaptar mocks para Koin
- [ ] Verificar que todos los tests pasan

---

## 💾 FASE 4: Data Layer (Día 2 - Tarde)

### 4.1 **Database Schema**
```sql
-- V2: shared/src/commonMain/sqldelight/com/sortisplus/shared/database/
CREATE TABLE Person (
  id INTEGER PRIMARY KEY,
  firstName TEXT NOT NULL,
  lastName TEXT NOT NULL,
  birthDateMillis INTEGER NOT NULL,
  weightKg REAL NOT NULL,
  isLeftHanded INTEGER NOT NULL
);
```

### 4.2 **Repository Implementations**
```kotlin
// V2: shared/src/commonMain/kotlin/com/sortisplus/shared/data/repository/
```

**Adaptaciones necesarias**:
- Cambiar Room → SQLDelight
- Cambiar DataStore → Multiplatform Settings
- Usar Ktor en lugar de Retrofit
- Integrar con Koin DI

### 4.3 **Platform-specific implementations**
```kotlin
// Android: shared/src/androidMain/kotlin/com/sortisplus/shared/platform/
// Desktop: shared/src/desktopMain/kotlin/com/sortisplus/shared/platform/
```

---

## 🎨 FASE 5: Presentation Layer (Día 3)

### 5.1 **ViewModels**
```kotlin
// V2: shared/src/commonMain/kotlin/com/sortisplus/shared/presentation/viewmodel/
```

**Migrar desde V1 con adaptaciones**:
- Remover anotaciones Hilt
- Adaptar a Koin
- Mantener lógica de estado

### 5.2 **UI Multiplataforma**
```kotlin
// Android: androidApp/src/main/kotlin/
// Desktop: desktopApp/src/desktopMain/kotlin/
```

**Nueva implementación**:
- Compose Multiplatform screens
- Navigation compartida
- Theme system unificado

---

## ✅ FASE 6: Testing y Validación (Día 3 - Final)

### 6.1 **Testing completo**
```bash
./gradlew test                    # Todos los tests
./gradlew shared:testDebugUnitTest
./gradlew androidApp:testDebug
./gradlew desktopApp:test
```

### 6.2 **Build verification**
```bash
./gradlew build                   # Build completo
./gradlew androidApp:assembleDebug # APK Android
./gradlew desktopApp:createDistributable # Executable Desktop
```

### 6.3 **Quality gates**
```bash
./gradlew detekt                  # Static analysis
./gradlew ktlintCheck             # Code style
```

---

## 📊 Criterios de Éxito

### ✅ **Build y Compilación**
- [ ] `./gradlew build` ejecuta sin errores
- [ ] APK Android se genera correctamente
- [ ] Executable Desktop se genera correctamente
- [ ] Sin warnings críticos de compilación

### ✅ **Funcionalidad**
- [ ] App Android inicia y funciona
- [ ] App Desktop inicia y funciona  
- [ ] Navegación funciona en ambas plataformas
- [ ] Persistencia de datos funciona
- [ ] Theme switching funciona

### ✅ **Testing**
- [ ] Todos los tests unitarios pasan
- [ ] Cobertura de tests >80% en domain layer
- [ ] Tests de integración básicos pasan

### ✅ **Calidad de Código**
- [ ] Detekt pasa sin errores críticos
- [ ] KtLint pasa
- [ ] Arquitectura Clean Architecture correcta
- [ ] Namespace unificado: `com.sortisplus.*`

---

## 🚨 Contingencias

### **Si aparecen problemas durante migración**:

1. **Domain models corruptos**: Reescribir desde cero usando specs del proyecto
2. **Tests fallan**: Adaptar progresivamente, priorizar funcionalidad
3. **Build problems**: Verificar version catalog compatibility
4. **UI issues**: Implementar screens básicos primero, polish después

### **Fallback plan**:
Si la migración toma más tiempo del esperado, implementar MVP mínimo funcional:
- [ ] Pantalla principal con navegación básica
- [ ] Una feature completamente funcional (Person management)
- [ ] Tests básicos funcionando
- [ ] Build estable en ambas plataformas

---

## 📈 Comparación Final

| Aspecto | V1 (Actual) | V2 (Objetivo) |
|---------|-------------|---------------|
| **Build time** | ~5+ minutos | <2 minutos |
| **Lines of code** | ~15,000+ (con duplicados) | ~8,000 (sin duplicados) |
| **Módulos** | 12+ módulos (híbridos) | 6 módulos (KMP limpios) |
| **Dependency conflicts** | 5+ conflictos conocidos | 0 conflictos |
| **Platform support** | Android + Desktop (problemático) | Android + Desktop (nativo) |
| **Maintainability** | Baja | Alta |
| **Team velocity** | Lenta (debugging constante) | Alta (desarrollo fluido) |

## 🎯 Resultado Esperado

Al final de los 3 días tendremos:
✅ Proyecto KMP completamente funcional  
✅ Sin deuda técnica del proyecto V1  
✅ Stack tecnológico moderno y unificado  
✅ Base sólida para desarrollo de features  
✅ Compatibilidad real con proyecto Swift  
✅ Arquitectura escalable para futuros desarrollos  

Este proyecto V2 será la **base definitiva** para todo el desarrollo futuro.