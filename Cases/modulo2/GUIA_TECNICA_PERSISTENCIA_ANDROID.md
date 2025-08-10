# Guía Técnica: Persistencia Local en Android - Para Programadores de Otros Lenguajes

> **Audiencia**: Programadores experimentados en otros lenguajes (Java, C#, Python, etc.)  
> **Objetivo**: Entender cómo implementar persistencia local moderna en Android  
> **Stack**: Kotlin + Room + DataStore + Jetpack Compose

---

## 🎯 Conceptos Fundamentales

### **¿Qué es la Persistencia Local en Android?**

Similar a usar **SQLite + Preferences** en otras plataformas, pero con herramientas modernas:

- **Room** = ORM/Entity Framework para SQLite
- **DataStore** = Shared Preferences mejorado  
- **Flow** = Observables/Streams reactivos
- **Coroutines** = async/await de Kotlin

---

## 🏗️ Arquitectura Implementada

### **Patrón: Repository + Clean Architecture**

```
📱 UI Layer (Compose)
    ↓ usa
🏢 Domain Layer (Interfaces)  
    ↓ implementa
💾 Data Layer (Room + DataStore)
```

**Equivalencias en otros lenguajes:**
- **C#**: Entity Framework + Repository Pattern
- **Java Spring**: JPA + Service Layer  
- **Python**: SQLAlchemy + Repository
- **Node.js**: Prisma + Service Layer

---

## 📊 1. Configuración de Dependencias

### **gradle/libs.versions.toml** (equivalent to package.json/pom.xml)

```toml
[versions]
room = "2.6.1"           # ORM para SQLite
datastore = "1.1.1"      # Preferences modernas
ksp = "2.0.21-1.0.26"    # Procesador de anotaciones

[libraries]
# ORM SQLite (like Entity Framework)
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }

# Modern Preferences (like AppSettings)  
datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }

# Async/Reactive (like Task<T>, Observable)
kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }
```

### **Módulos build.gradle.kts** (like project structure)

```kotlin
// :core:database module
dependencies {
    api(libs.room.runtime)           // Expose Room to other modules
    implementation(libs.room.ktx)     // Kotlin extensions
    ksp(libs.room.compiler)          // Annotation processor
}
```

---

## 💾 2. Definición de Entidades (Database Models)

### **Entity = Table Definition**

```kotlin
// Como @Entity en JPA o [Table] en C#
@Entity(tableName = "personas")
data class PersonaEntity(
    @PrimaryKey(autoGenerate = true)  // AUTO_INCREMENT PRIMARY KEY
    val id: Long = 0,
    
    val nombre: String,               // VARCHAR NOT NULL
    val apellido: String,             // VARCHAR NOT NULL  
    val fechaNacimiento: Long,        // BIGINT (timestamp)
    val peso: Double,                 // DOUBLE
    val esZurdo: Boolean              // BOOLEAN/TINYINT
)
```

**Equivalencias:**
- **C# EF**: `[Key] public int Id { get; set; }`
- **Java JPA**: `@Id @GeneratedValue public Long id;`  
- **Python SQLAlchemy**: `id = Column(Integer, primary_key=True)`

---

## 🔍 3. Data Access Objects (DAOs)

### **DAO = Repository Interface**

```kotlin
@Dao
interface PersonaDao {
    // SELECT * FROM personas ORDER BY apellido ASC
    @Query("SELECT * FROM personas ORDER BY apellido ASC, nombre ASC")
    fun observeAll(): Flow<List<PersonaEntity>>    // Reactive stream
    
    // SELECT * FROM personas WHERE id = :id LIMIT 1
    @Query("SELECT * FROM personas WHERE id = :id")
    suspend fun getById(id: Long): PersonaEntity?   // Async method
    
    // INSERT OR REPLACE INTO personas (...)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PersonaEntity): Long
    
    // UPDATE personas SET ... WHERE id = ?
    @Update
    suspend fun update(entity: PersonaEntity): Int
    
    // DELETE FROM personas WHERE id = :id
    @Query("DELETE FROM personas WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
```

**Claves importantes:**
- `suspend` = `async` en C#, `async/await` en JavaScript
- `Flow<T>` = `IObservable<T>` en C#, `Observable` en RxJava
- Anotaciones generan SQL automáticamente

---

## 🗄️ 4. Database Configuration

### **Database = DbContext/Connection**

```kotlin
@Database(
    entities = [PersonaEntity::class],    // Tablas incluidas
    version = 1,                          // Schema version
    exportSchema = true                   // Genera SQL para migraciones
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personaDao(): PersonaDao  // Factory method
}

// Singleton Pattern (like DbContext registration)
object DatabaseProvider {
    @Volatile private var INSTANCE: AppDatabase? = null
    
    fun get(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app.db"                    // Database filename
            )
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
            .also { INSTANCE = it }
        }
}
```

**Equivalencias:**
- **C# EF**: `services.AddDbContext<AppDbContext>()`
- **Java Spring**: `@Configuration` + `DataSource`
- **Node.js**: Database connection pool

---

## 🔧 5. Preferences con DataStore

### **DataStore = Modern SharedPreferences**

```kotlin
// Settings keys (like appsettings.json keys)
object SettingsKeys {
    val DarkTheme = booleanPreferencesKey("dark_theme")
    val ListOrder = stringPreferencesKey("list_order")
}

class SettingsDataStore(private val context: Context) {
    
    // Reactive property (like INotifyPropertyChanged)
    val darkTheme: Flow<Boolean> =
        context.dataStore.data.map { prefs -> 
            prefs[SettingsKeys.DarkTheme] ?: false 
        }
    
    // Async setter
    suspend fun setDarkTheme(value: Boolean) {
        context.dataStore.edit { 
            it[SettingsKeys.DarkTheme] = value 
        }
    }
}
```

**Equivalencias:**
- **C#**: `Properties.Settings.Default.DarkTheme`
- **Java**: `SharedPreferences.edit().putBoolean()`
- **Python**: `configparser` or `json` config files

---

## 🏢 6. Repository Pattern (Business Logic Layer)

### **Interfaces = Contracts**

```kotlin
// Domain model (DTO/View Model)
data class Persona(
    val id: Long,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: Long,
    val peso: Double,
    val esZurdo: Boolean
) {
    // Calculated property (like computed property)
    val edad: Int
        get() {
            val ageInMillis = System.currentTimeMillis() - fechaNacimiento
            return (ageInMillis / (365.25 * 24 * 60 * 60 * 1000)).toInt()
        }
}

// Repository interface (like IRepository<T>)
interface PersonaRepository {
    fun observeAll(): Flow<List<Persona>>          // Reactive list
    suspend fun getById(id: Long): Persona?        // Async single
    suspend fun create(...): Long                  // Returns generated ID
    suspend fun update(...): Boolean               // Returns success
    suspend fun delete(id: Long): Boolean          // Returns success
}
```

### **Implementation = Service Class**

```kotlin
class LocalPersonaRepository(context: Context) : PersonaRepository {
    
    private val db = DatabaseProvider.get(context)
    private val dao = db.personaDao()
    
    // Stream transformation (like LINQ Select)
    override fun observeAll(): Flow<List<Persona>> =
        dao.observeAll().map { entities -> 
            entities.map { it.toDomain() } 
        }
    
    // Async operations with error handling
    override suspend fun create(nombre: String, apellido: String, ...): Long {
        return dao.insert(
            PersonaEntity(
                nombre = nombre,
                apellido = apellido,
                fechaNacimiento = fechaNacimiento,
                peso = peso,
                esZurdo = esZurdo
            )
        )
    }
    
    override suspend fun delete(id: Long): Boolean = 
        dao.deleteById(id) > 0
}

// Entity to Domain mapping (like AutoMapper)
private fun PersonaEntity.toDomain() = Persona(
    id = id,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    peso = peso,
    esZurdo = esZurdo
)
```

---

## 🎨 7. UI Integration (Jetpack Compose)

### **Reactive UI = Observables in View**

```kotlin
@Composable
fun PersonaListScreen() {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personaRepository(context) }
    
    // Reactive state (like Observable collections)
    val personasState = remember { mutableStateOf<List<Persona>>(emptyList()) }
    
    // Subscribe to changes (like binding in MVVM)
    LaunchedEffect(Unit) {
        repository.observeAll().collect { personas ->
            personasState.value = personas
        }
    }
    
    // UI rendering (like RecyclerView/ListView)
    LazyColumn {
        items(personasState.value) { persona ->
            Text(
                "#${persona.id} - ${persona.apellido}, ${persona.nombre} | " +
                "${persona.edad} años | Peso: ${persona.peso}kg | " +
                "Zurdo: ${if (persona.esZurdo) "Sí" else "No"}"
            )
        }
    }
}

@Composable  
fun PersonaCreateScreen() {
    val repository = remember { LocalProviders.personaRepository(LocalContext.current) }
    var nombre by remember { mutableStateOf("") }        // Two-way binding
    var apellido by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var esZurdo by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()                 // For async operations
    
    Column {
        // Form fields (like TextBox, CheckBox)
        OutlinedTextField(
            value = nombre, 
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )
        
        Checkbox(
            checked = esZurdo,
            onCheckedChange = { esZurdo = it }
        )
        
        // Async button click
        Button(onClick = {
            scope.launch {                               // Run in background
                val pesoDouble = peso.toDoubleOrNull() ?: 0.0
                repository.create(nombre, apellido, System.currentTimeMillis(), pesoDouble, esZurdo)
            }
        }) {
            Text("Guardar")
        }
    }
}
```

---

## 🔄 8. Dependency Injection (Manual)

### **Provider Pattern = IoC Container**

```kotlin
// Manual DI (like services.AddScoped<>)
object LocalProviders {
    fun personaRepository(context: Context): PersonaRepository =
        LocalPersonaRepository(context)
    
    fun settingsRepository(context: Context): SettingsRepository =
        LocalSettingsRepository(context)
}

// Usage in UI (like constructor injection)
@Composable
fun SomeScreen() {
    val context = LocalContext.current
    val personaRepo = remember { LocalProviders.personaRepository(context) }
    val settingsRepo = remember { LocalProviders.settingsRepository(context) }
}
```

---

## ⚡ 9. Conceptos Clave para Otros Lenguajes

### **Kotlin Coroutines = async/await**

```kotlin
// Kotlin
suspend fun loadData(): List<Persona> = dao.getAll()

// C# equivalent
async Task<List<Persona>> LoadDataAsync() => await dao.GetAll();

// Python equivalent  
async def load_data() -> List[Persona]: return await dao.get_all()
```

### **Flow = Reactive Streams**

```kotlin
// Kotlin
fun observe(): Flow<List<T>> = dao.observeAll()

// C# equivalent
IObservable<List<T>> Observe() => dao.ObserveAll();

// RxJava equivalent
Observable<List<T>> observe() { return dao.observeAll(); }
```

### **Jetpack Compose = Declarative UI**

```kotlin
// Kotlin Compose
@Composable fun PersonaItem(persona: Persona) {
    Text("${persona.nombre} ${persona.apellido}")
}

// React equivalent
function PersonaItem({ persona }) {
    return <span>{persona.nombre} {persona.apellido}</span>
}

// SwiftUI equivalent  
struct PersonaItem: View {
    let persona: Persona
    var body: some View {
        Text("\(persona.nombre) \(persona.apellido)")
    }
}
```

---

## 📁 10. Estructura de Módulos

```
app/                          # Main application module
├── MainActivity.kt           # Entry point (like Program.cs)
└── TemplateAndroidApp()      # Main UI composition

core/
├── common/                   # Shared utilities  
├── data/                     # Domain interfaces
├── database/                 # Room entities/DAOs
├── datastore/                # Preferences wrapper
└── ui/                       # Reusable UI components

data/
└── local/                    # Repository implementations  

feature/  
└── home/                     # Feature-specific screens
```

**Equivalencias:**
- **C# Solution**: Projects organized by layer
- **Java Maven**: Multi-module project
- **Python**: Packages with __init__.py
- **Node.js**: Separate npm packages/modules

---

## 🎯 Conclusiones para Programadores

### **Si vienes de...**

**C#/.NET**: 
- Room = Entity Framework  
- DataStore = Settings.settings
- Coroutines = Task/async-await
- Flow = IObservable/IAsyncEnumerable

**Java Spring**:
- Repository pattern es similar
- @Dao = @Repository interfaces  
- Room annotations = JPA annotations
- Dependency injection manual (no @Autowired)

**Python Django**:
- PersonaEntity = Model class
- DAO methods = QuerySet operations
- Repository = Service layer
- Flow = AsyncIterator

**React/Node.js**:
- Compose = JSX declarative UI
- Flow = RxJS Observables  
- suspend functions = async/await
- Room = Prisma/TypeORM

### **Puntos Clave a Recordar**

1. **Todo es reactivo**: UI se actualiza automáticamente cuando cambian los datos
2. **Async por defecto**: Operaciones DB siempre en background threads
3. **Inmutabilidad**: `data class` son inmutables como records
4. **Null safety**: Kotlin fuerza manejo de null values
5. **Modularidad**: Separación clara entre capas

Esta implementación te da una base sólida para expandir con networking, autenticación, y features más complejas siguiendo los mismos patrones arquitectónicos.