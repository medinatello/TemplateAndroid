# Reglas de Desarrollo - Plantilla Android

> **Propósito**: Establecer estándares de calidad y consistencia para el desarrollo de la plantilla Android Base.  
> **Alcance**: Aplicable a todos los módulos MVP y futuros desarrollos.  
> **Versión**: 1.0 (Módulo 2.5)

---

## 1. Convenciones de Nomenclatura

### 1.1 Idioma Obligatorio
- **TODO el código debe estar en inglés**: variables, funciones, clases, archivos, namespaces, packages, etc.
- **Excepción**: Comentarios y documentación pueden estar en español para facilitar la comprensión del equipo.
- **UI/UX**: Textos mostrados al usuario deben ser dinámicos usando recursos (strings.xml) en español, con soporte futuro para internacionalización.

### 1.2 Nombres Descriptivos
```kotlin
// ✅ CORRECTO - Descriptivo y en inglés
class PersonRepository
fun getUserById(userId: Long): User?
val isNetworkAvailable: Boolean
```

```kotlin
// ❌ INCORRECTO - Español o poco descriptivo
class RepositorioPersona
fun obtenerUsuario(id: Long): Usuario?
val conexion: Boolean
```

### 1.3 Estándares Android
- **Packages**: `com.sortisplus.{module}.{feature}`
- **Activities**: `{Feature}Activity` (ej: `MainActivity`, `PersonActivity`)
- **Fragments**: `{Feature}Fragment` 
- **ViewModels**: `{Feature}ViewModel`
- **Repositories**: `{Entity}Repository`
- **DAOs**: `{Entity}Dao`
- **Entities**: `{Name}Entity` para Room
- **Resources**: usar `snake_case` para IDs, layouts, strings

---

## 2. Gestión de Strings y Recursos

### 2.1 Localización Obligatoria
- **Prohibido**: Hardcodear strings en el código Kotlin/Compose
- **Obligatorio**: Usar `stringResource()` en Compose o `getString()` en Activities/Fragments
- **Estructura de recursos**:
  ```
  res/
  ├── values/strings.xml           (español - idioma por defecto)
  ├── values-en/strings.xml        (inglés)
  └── values-{lang}/strings.xml    (futuros idiomas)
  ```

### 2.2 Convenciones de Strings
```xml
<!-- ✅ CORRECTO - Descriptivo y organizado -->
<string name="home_welcome_message">Bienvenido a la aplicación</string>
<string name="person_list_empty_state">No hay personas registradas</string>
<string name="error_network_connection">Error de conexión a internet</string>

<!-- ❌ INCORRECTO - Genérico o mal estructurado -->
<string name="message">Mensaje</string>
<string name="error">Error</string>
```

---

## 3. Arquitectura y Diseño

### 3.1 Estándares Google/AndroidX
- **UI**: Jetpack Compose + Material 3
- **Navegación**: Navigation Compose
- **Estado**: ViewModel + SavedStateHandle + StateFlow/Flow
- **Persistencia**: Room + DataStore
- **Inyección**: Hilt (cuando sea necesario) o proveedores manuales simples
- **Concurrencia**: Coroutines + Flow
- **Testing**: JUnit 5 + Compose Testing + MockK

### 3.2 Patrón de Capas
```
presentation/ (UI + ViewModel)
    ↓
domain/ (Use Cases + Models + Contracts)
    ↓
data/ (Repositories + DataSources)
```

---

## 4. Manejo de Errores

### 4.1 Principio de No-Propagación
- **Prohibido**: Lanzar excepciones que atraviesen múltiples capas sin control
- **Obligatorio**: Capturar errores en cada capa y transformarlos en respuestas controladas

### 4.2 Patrón Result
```kotlin
// ✅ CORRECTO - Manejo controlado de errores
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

suspend fun getPersonById(id: Long): Result<Person> {
    return try {
        val person = personDao.getById(id)
        if (person != null) {
            Result.Success(person)
        } else {
            Result.Error(PersonNotFoundException("Person with ID $id not found"))
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error getting person by ID: $id", e)
        Result.Error(e)
    }
}
```

### 4.3 Logging Estructurado
- **Obligatorio**: Usar Android Log con tags descriptivos
- **Niveles**: VERBOSE, DEBUG, INFO, WARN, ERROR
- **Preparación**: Estructura compatible con OpenTelemetry (futuro)

```kotlin
// ✅ CORRECTO - Log estructurado
companion object {
    private const val TAG = "PersonRepository"
}

Log.d(TAG, "Fetching person with ID: $id")
Log.e(TAG, "Failed to save person: ${person.name}", exception)
```

---

## 5. Testing y Cobertura

### 5.1 Cobertura Mínima
- **Objetivo**: >80% de cobertura de código
- **Obligatorio**: Tests unitarios para:
  - ViewModels y lógica de presentación
  - Repositories y lógica de datos
  - Use Cases y lógica de dominio
  - Mappers y transformaciones

### 5.2 Tipos de Tests
- **Unit Tests**: Lógica de negocio, ViewModels, Repositories (sin Android)
- **Integration Tests**: Room DAOs, DataStore, APIs
- **UI Tests**: Compose screens, navegación, interacciones

### 5.3 Estructura de Tests
```kotlin
// ✅ CORRECTO - Estructura descriptiva
class PersonRepositoryTest {
    @Test
    fun `when person exists, should return success with person data`() {
        // Given
        val expectedPerson = createTestPerson()
        
        // When
        val result = repository.getPersonById(1L)
        
        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedPerson)
    }
}
```

---

## 6. Documentación de Código

### 6.1 KDoc Obligatorio
- **Clases públicas**: Documentar propósito y responsabilidad
- **Funciones públicas**: Documentar parámetros, retorno y efectos secundarios
- **Propiedades públicas**: Documentar significado y restricciones

```kotlin
/**
 * Repository for managing person data from local database
 * 
 * Provides CRUD operations for Person entities and maintains data consistency
 * between the local Room database and the application layer.
 */
class PersonRepository @Inject constructor(
    private val personDao: PersonDao
) {
    
    /**
     * Retrieves a person by their unique identifier
     * 
     * @param id The unique identifier of the person to retrieve
     * @return Result containing the Person if found, or Error if not found or database error occurs
     */
    suspend fun getPersonById(id: Long): Result<Person> {
        // Implementation
    }
}
```

---

## 7. Build y Entrega

### 7.1 Validación Obligatoria
Antes de cada entrega/commit, ejecutar:
```bash
./gradlew clean
./gradlew lintDebug
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest  # si hay tests instrumentados
./gradlew assembleDebug
```

### 7.2 Criterios de Aceptación
- ✅ Build limpio sin warnings críticos
- ✅ Todos los tests pasando
- ✅ Cobertura >80%
- ✅ Lint sin errores críticos
- ✅ Documentación actualizada

---

## 8. Herramientas de Calidad

### 8.1 Análisis Estático (Configurar en futuro módulo)
- **Detekt**: Análisis de código Kotlin
- **ktlint**: Formato de código
- **Android Lint**: Análisis específico de Android

### 8.2 Métricas de Calidad
- Complejidad ciclomática
- Duplicación de código
- Cobertura de tests
- Deuda técnica

---

## 9. Estándares UI/UX

### 9.1 Material Design 3
- **Obligatorio**: Usar componentes Material 3
- **Temas**: Soporte para modo claro/oscuro
- **Accesibilidad**: Seguir pautas WCAG 2.1

### 9.2 Navegación
- **Patrón**: Navigation Compose con rutas tipadas
- **Estado**: Manejar back stack correctamente
- **Deep links**: Preparar estructura para enlaces profundos

### 9.3 Estados de UI
```kotlin
// ✅ CORRECTO - Estados UI claros
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}
```

---

## 10. Versionado y Git

### 10.1 Commits
- **Formato**: `[MVP-XX-Y] Feature description`
- **Idioma**: Inglés para mensajes de commit
- **Atomicidad**: Un cambio lógico por commit

### 10.2 Branches
- `main`: Código estable
- `feature/mvp-xx-description`: Features en desarrollo
- `hotfix/description`: Correcciones críticas

---

## 11. Aplicación de Estas Reglas

### 11.1 Revisión de Código
- **Obligatorio**: Code review antes de merge
- **Checklist**: Verificar cumplimiento de estas reglas
- **Herramientas**: Usar CI/CD para validación automática

### 11.2 Refactoring
- Aplicar estas reglas de forma gradual
- Priorizar módulos críticos
- Mantener backward compatibility cuando sea posible

---

## 12. Evolución de las Reglas

Este documento es vivo y debe evolucionar con:
- Nuevas versiones de Android/Kotlin
- Feedback del equipo
- Mejores prácticas emergentes
- Lecciones aprendidas de cada MVP

**Última actualización**: Módulo 2.5  
**Próxima revisión**: Módulo 3.0