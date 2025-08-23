# ADR-001: Estrategia de Migración de Funcionalidades a KMP

## Estado
Aceptado

## Contexto
El MVP-03.5 estableció exitosamente la base técnica de Kotlin Multiplatform (KMP) con módulos `shared` y `desktopApp`. Sin embargo, las aplicaciones resultantes son demos básicas que no incluyen las funcionalidades reales implementadas en MVPs 01-03 (fundamentos, UI/navegación, almacenamiento).

### Situación Actual
- ✅ **Base KMP**: Funcional con Android + Desktop targets
- ✅ **Stack técnico**: Ktor, SQLDelight, Koin, Compose Multiplatform
- ❌ **Funcionalidad real**: Solo demos básicos, no aplicaciones completas
- ❌ **Business logic**: Casos de uso reales solo en Android
- ❌ **UI avanzada**: Design system y navegación solo en Android

### Objetivo del MVP-03.6
Migrar todas las funcionalidades existentes de Android al módulo `shared` para lograr aplicaciones reales y funcionales en ambas plataformas.

## Decisión
Adoptamos una **estrategia de migración incremental por capas** siguiendo el principio de "shared-first" mientras mantenemos compatibilidad total con la aplicación Android existente.

### Enfoque por Capas
1. **Domain Layer**: Casos de uso y entidades → `commonMain`
2. **Data Layer**: Repositorios e interfaces → `commonMain` 
3. **Presentation Layer**: ViewModels y state management → `commonMain`
4. **UI Layer**: Componentes y navegación → plataforma específica pero reutilizable

### Principios de Migración

#### 1. Zero Breaking Changes
- La aplicación Android debe mantener 100% de funcionalidad durante y después de la migración
- No regresiones en performance o UX
- API contracts existentes se mantienen

#### 2. Shared-First Approach
- Maximizar código compartido en `commonMain`
- Platform-specific code solo cuando sea técnicamente necesario
- Prefer composition over inheritance para diferencias de plataforma

#### 3. Incremental Migration
- Migrar módulo por módulo, no toda la aplicación de una vez
- Mantener build funcional en cada step
- Feature flags para rollback rápido si es necesario

#### 4. Data Preservation
- Migrar datos existentes de Android sin pérdida
- DataStore → KeyValueStore migration automatizada
- Backward compatibility durante período de transición

## Arquitectura Objetivo

```
shared/
├── src/commonMain/kotlin/
│   ├── domain/
│   │   ├── usecase/         # Casos de uso migrados de MVPs 01-03
│   │   ├── repository/      # Interfaces de repositorio
│   │   └── model/           # Entidades de dominio
│   ├── data/
│   │   ├── repository/      # Implementaciones de repositorio
│   │   ├── source/          # Data sources locales/remotos
│   │   └── storage/         # Almacenamiento multiplataforma
│   ├── presentation/
│   │   ├── viewmodel/       # ViewModels compartidos
│   │   ├── state/           # UI State management
│   │   └── navigation/      # Lógica de navegación
│   └── di/
│       ├── DomainModule.kt  # DI para domain layer
│       ├── DataModule.kt    # DI para data layer
│       └── PresentationModule.kt
├── src/androidMain/kotlin/
│   ├── data/source/         # Android-specific data sources
│   ├── storage/             # Android storage implementations
│   └── platform/            # Android platform abstractions
└── src/desktopMain/kotlin/
    ├── data/source/         # Desktop-specific data sources
    ├── storage/             # Desktop storage implementations
    └── platform/            # Desktop platform abstractions

app/                         # Android UI + shared integration
desktopApp/                  # Desktop UI + shared integration
```

## Estrategia de UI Multiplataforma

### Android App
- Mantiene UI existente (Jetpack Compose)
- Integra gradualmente con shared ViewModels
- Conserva Android-specific optimizations

### Desktop App  
- Compose Multiplatform con design adapted para Desktop
- Reutiliza maximum código de componentes Android
- Desktop-specific adaptations (menus, shortcuts, window management)

### Shared UI Components
- Componentes base reutilizables en `commonMain`
- Platform-specific theming y adaptations
- Design system compartido con platform variations

## Migración de Storage (MVP-03)

### DataStore → KeyValueStore
```kotlin
// Before (Android-only)
class PreferencesRepository(private val dataStore: DataStore<Preferences>)

// After (Multiplatform)
class PreferencesRepository(private val keyValueStore: KeyValueStore)
```

### Migration Strategy
1. Implementar KeyValueStore wrapper que lea de DataStore existente
2. Migrar datos gradualmente a nuevo formato
3. Reemplazar completamente DataStore una vez verificada migración

### Secure Storage
- Android: EncryptedSharedPreferences + Keystore  
- Desktop: OS-specific secure storage (Keychain en macOS, Credential Manager en Windows)

## Consideraciones de Performance

### Shared Module Optimization
- Lazy initialization para componentes pesados
- Platform-specific optimizations en `actual` implementations
- Memory management cuidadoso en shared objects

### Build Optimization
- Gradle configuration optimizada para KMP
- Parallel compilation habilitada
- Dependency caching mejorado

## Testing Strategy

### Shared Tests
- Unit tests para shared business logic
- Integration tests para shared data layer
- Contract tests para platform abstractions

### Platform Tests  
- Android: Mantener tests existentes + integration con shared
- Desktop: Equivalente funcional a Android tests
- E2E tests comparando comportamiento entre plataformas

## Riesgos y Mitigaciones

### Riesgo: Complejidad de migración
**Mitigación**: Migración incremental con rollback points
```kotlin
// Feature flag approach
object FeatureFlags {
    const val USE_SHARED_REPOSITORY = true
    const val USE_SHARED_VIEWMODELS = false // Migrate gradually
}
```

### Riesgo: Performance degradation
**Mitigación**: Continuous benchmarking
```bash
# Performance gates en CI
./gradlew :app:benchmarkDebug
./gradlew :desktopApp:performanceTest
```

### Riesgo: Platform differences
**Mitigación**: Platform-specific abstractions
```kotlin
// Platform abstraction approach
expect class PlatformStorageManager {
    suspend fun saveSecurely(key: String, value: String)
    suspend fun loadSecurely(key: String): String?
}
```

## Implementación

### Fase 1: Foundation (Week 1)
- Migrar domain layer básico
- Setup shared DI infrastructure
- Platform abstractions básicas

### Fase 2: Data Migration (Week 2)  
- Migrar repositories a shared
- Implement storage migration
- Data layer integration testing

### Fase 3: Presentation + UI (Week 3)
- Migrar ViewModels a shared
- Desktop UI implementation
- End-to-end functionality testing

## Criterios de Éxito
- [ ] Android app mantiene 100% funcionalidad sin regresiones
- [ ] Desktop app logra ≥ 90% feature parity con Android
- [ ] ≥ 85% de business logic compartido
- [ ] Performance Android maintained or improved
- [ ] Tests coverage ≥ 85% en shared module

## Alternativas Consideradas

### Alternativa 1: Big Bang Migration
**Rechazada**: Muy riesgoso, difícil de rollback, alto impacto en development velocity

### Alternativa 2: Duplicate Everything
**Rechazada**: Va contra los principios de KMP, mantenimiento duplicado

### Alternativa 3: UI-Only Sharing
**Rechazada**: No aprovecha todo el potencial de KMP

## Referencias
- [Kotlin Multiplatform Best Practices](https://kotlinlang.org/docs/multiplatform-mobile-best-practices.html)
- [Compose Multiplatform Documentation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-getting-started.html)
- MVP-03.5 ADR-001: KMP Stack Selection

---

**Fecha**: 2025-01-17  
**Autor**: Tech Lead  
**Reviewers**: Senior KMP Developer, Android Team Lead  
**Estado**: Aceptado para implementación en MVP-03.6