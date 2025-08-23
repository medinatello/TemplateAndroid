# [ANDROID][MVP-02] Persistencia local (Room + DataStore) — Especificación detallada

> Proyecto: **Android base**  
> Módulo: **[ANDROID][MVP-02] Persistencia local**  
> Plataforma: **Android 14 (targetSdk 34)** · **minSdk 26** · **Kotlin**  
> Lineamientos: nativo primero, modular, sin dependencias de terceros salvo AndroidX/Google.

---

## 1) Propósito y alineación
**Objetivo**: incorporar **persistencia local** con dos mecanismos complementarios:
- **Room** para datos estructurados (tablas/relaciones/migraciones).
- **DataStore (Preferences)** para ajustes ligeros (flags, toggles, valores simples).  
  Dejamos ganchos para **DataStore Proto** si luego necesitamos contratos fuertemente tipados.

**Drivers de diseño**
- Módulos separados para aislar almacenamiento de UI.
- Repositorios con **interfaces públicas** para facilitar pruebas y futuros reemplazos (e.g., cambiar a otra DB).
- **Migraciones** desde el día 1 (schema versionado).
- **Pruebas instrumentadas** para DAO y **unit tests** para repositorios y DataStore.

**Fuera de alcance (MVP-02)**
- Sincronización remota ni cache híbrido (se verá en MVPs de red/sync).
- Cifrado de base de datos (dejamos extensión opcional).
- Telemetría avanzada (quedan interfaces).

---

## 2) Historias de usuario y criterios de aceptación (Gherkin)

### HU-101: Guardar y listar elementos locales (Room)
**Como** usuario  
**quiero** crear elementos y verlos listados aún sin conexión  
**para** mantener mi data disponible localmente.

**Criterios**
```
Dado que estoy en la app sin conexión
Cuando creo un Elemento con título "A"
Entonces la app lo persiste localmente
Y cuando ingreso al listado
Entonces veo "A" en la lista
```

### HU-102: Actualizar y eliminar elementos (Room)
```
Dado un Elemento existente con id X
Cuando edito su título a "B"
Entonces la base refleja el cambio
Y el listado muestra "B"

Dado un Elemento con id X
Cuando lo elimino
Entonces no aparece en el listado
```

### HU-103: Preferencias rápidas (DataStore)
```
Dado que abro la app por primera vez
Entonces la preferencia "temaOscuro" está en false por defecto

Cuando activo "temaOscuro"
Entonces el valor queda persistido
Y al reabrir la app el valor sigue en true
```

### HU-104: Migraciones seguras (Room)
```
Dado que el esquema de DB evoluciona de v1 a v2
Cuando actualizo la app
Entonces la migración se aplica sin perder datos
Y la app arranca sin crashes
```

---

## 3) Alcance técnico y arquitectura

### 3.1 Estructura de módulos (nuevos/ajustados)
- **:core:database** → Entities, DAO, `RoomDatabase`, `Migrations`, `TypeConverters`.
- **:core:datastore** → Wrappers de DataStore Preferences (+ ganchos para Proto).
- **:core:data** → Contratos (interfaces de repositorios), modelos de dominio simples (si aplica).
- **:data:local** → Implementaciones de repositorios locales que orquestan Room + DataStore.
- **:app** / **features** → Usan repositorios vía interfaces (no conocen Room/DataStore).

> Nota: mantenemos **:core:common** y **:core:ui** del MVP-01. No introducimos un framework de DI; usaremos **proveedores manuales** (object factories) para mantenerlo nativo y claro.

### 3.2 Modelo de ejemplo (MVP)
Dominio mínimo: **Elemento**
- `id: Long` (PK autogenerada)
- `title: String`
- `createdAt: Long` (epoch millis)
- `updatedAt: Long` (epoch millis, opcional)

Preferencias:
- `temaOscuro: Boolean` (default `false`)
- `ordenListado: String` (ej: `"CREATED_DESC"`, default)

### 3.3 Versionado de DB
- **DB_NAME**: `app.db`
- **DB_VERSION**: `1` (MVP)
- **Schema export**: habilitado a `schemas/` para versionar en VCS.

---

## 4) Plan de trabajo (tareas) — estilo Scrum (estimación ligera)

Épica: **[ANDROID][MVP-02] Persistencia local**

- **T1 — Version Catalog + Plugins** (2 pt)  
  Agregar dependencias Room, DataStore, Coroutines test, KSP.
- **T2 — Módulo :core:database** (3 pt)  
  Crear Entity/DAO/DB, TypeConverters, builder, export de schema.
- **T3 — Módulo :core:datastore** (2 pt)  
  Crear PreferencesDataStore, claves y wrapper de lectura/escritura (Flow).
- **T4 — Contratos en :core:data** (2 pt)  
  Definir interfaces de repositorio (`ElementRepository`, `SettingsRepository`) y modelos.
- **T5 — Implementación en :data:local** (3 pt)  
  Repositorios concretos usando Room + DataStore. Proveedores manuales.
- **T6 — Pruebas DAO (androidTest)** (3 pt)  
  InMemory/Temporary DB, tests CRUD y consultas específicas.
- **T7 — Pruebas de Repositorios (test JVM)** (2 pt)  
  Fakes/Rules para coroutines; validación de flujos.
- **T8 — Pruebas DataStore (test JVM)** (2 pt)  
  Defaults, set/get, persistencia en disco temporal.
- **T9 — Demo mínima de consumo en feature** (2 pt)  
  Botones invisibles a producción (debug) o sample screen para validar E2E.
- **T10 — Documentación + DoD** (1 pt)  
  README por módulo, guía de migraciones, checklist de validación manual.

> **Total estimado**: 20 pt

---

## 5) Configuración (Gradle y catálogo de versiones)

### 5.1 `gradle/libs.versions.toml` (añadidos)
```toml
[versions]
room = "2.6.1"
datastore = "1.1.1"
ksp = "2.0.0-1.0.22"
coroutines = "1.8.1"

[libraries]
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
room-ktx     = { module = "androidx.room:room-ktx",     version.ref = "room" }
room-compiler= { module = "androidx.room:room-compiler", version.ref = "room" }

datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }

kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }
kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "coroutines" }

[plugins]
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

### 5.2 `build.gradle.kts` por módulo

**:core:database**
```kotlin
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.androidbase.core.database"
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    javaCompileOptions {
      annotationProcessorOptions {
        arguments += mapOf(
          "room.schemaLocation" to "$projectDir/schemas",
          "room.incremental" to "true"
        )
      }
    }
  }
  kotlinOptions { jvmTarget = "17" }
  sourceSets.configureEach {
    resources.srcDir("schemas") // para que el esquema esté disponible en el artefacto
  }
}

dependencies {
  api(libs.room-runtime)
  implementation(libs.room-ktx)
  ksp(libs.room-compiler)
}
```

**:core:datastore**
```kotlin
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.androidbase.core.datastore"
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
  kotlinOptions { jvmTarget = "17" }
}

dependencies {
  api(libs.datastore.preferences)
  implementation(libs.kotlinx.coroutines.core)
}
```

**:core:data**
```kotlin
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.androidbase.core.data"
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
  kotlinOptions { jvmTarget = "17" }
}

dependencies { /* solo contratos: sin impl */ }
```

**:data:local**
```kotlin
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.androidbase.data.local"
  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig { minSdk = libs.versions.minSdk.get().toInt() }
  kotlinOptions { jvmTarget = "17" }
}

dependencies {
  implementation(project(":core:data"))
  implementation(project(":core:database"))
  implementation(project(":core:datastore"))
  implementation(libs.kotlinx.coroutines.core)
}
```

---

## 6) Código clave (Room + DataStore)

### 6.1 Room

**Entity `ElementEntity.kt`**
```kotlin
package com.androidbase.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "elements")
data class ElementEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val createdAt: Long,
  val updatedAt: Long? = null
)
```

**DAO `ElementDao.kt`**
```kotlin
package com.androidbase.core.database.dao

import androidx.room.*
import com.androidbase.core.database.model.ElementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementDao {

  @Query("SELECT * FROM elements ORDER BY createdAt DESC")
  fun observeAll(): Flow<List<ElementEntity>>

  @Query("SELECT * FROM elements WHERE id = :id")
  suspend fun getById(id: Long): ElementEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: ElementEntity): Long

  @Update
  suspend fun update(entity: ElementEntity): Int

  @Delete
  suspend fun delete(entity: ElementEntity): Int

  @Query("DELETE FROM elements WHERE id = :id")
  suspend fun deleteById(id: Long): Int
}
```

**DB `AppDatabase.kt`**
```kotlin
package com.androidbase.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androidbase.core.database.dao.ElementDao
import com.androidbase.core.database.model.ElementEntity

@Database(
  entities = [ElementEntity::class],
  version = 1,
  exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun elementDao(): ElementDao
}
```

**Builder `DatabaseProvider.kt` (proveedor manual)**
```kotlin
package com.androidbase.core.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
  @Volatile private var INSTANCE: AppDatabase? = null

  fun get(context: Context): AppDatabase =
    INSTANCE ?: synchronized(this) {
      INSTANCE ?: Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app.db"
      )
      .fallbackToDestructiveMigrationOnDowngrade()
      .build()
        .also { INSTANCE = it }
    }
}
```

**(Ganchos) Migraciones `Migrations.kt`**  
_Este MVP arranca en v1. Aquí quedará la plantilla para futuros cambios._
```kotlin
package com.androidbase.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Ejemplo cuando pasemos a v2:
val MIGRATION_1_2 = object : Migration(1, 2) {
  override fun migrate(db: SupportSQLiteDatabase) {
    // db.execSQL("ALTER TABLE elements ADD COLUMN ...")
  }
}
```

### 6.2 DataStore (Preferences)

**Keys y wrapper `SettingsDataStore.kt`**
```kotlin
package com.androidbase.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DS_NAME = "settings"

private val Context.dataStore by preferencesDataStore(name = DS_NAME)

object SettingsKeys {
  val DarkTheme = booleanPreferencesKey("dark_theme")
  val ListOrder = stringPreferencesKey("list_order")
}

class SettingsDataStore(private val context: Context) {

  val darkTheme: Flow<Boolean> =
    context.dataStore.data.map { prefs -> prefs[SettingsKeys.DarkTheme] ?: false }

  val listOrder: Flow<String> =
    context.dataStore.data.map { prefs -> prefs[SettingsKeys.ListOrder] ?: "CREATED_DESC" }

  suspend fun setDarkTheme(value: Boolean) {
    context.dataStore.edit { it[SettingsKeys.DarkTheme] = value }
  }

  suspend fun setListOrder(value: String) {
    context.dataStore.edit { it[SettingsKeys.ListOrder] = value }
  }
}
```

### 6.3 Contratos (repositorios) — `:core:data`

```kotlin
package com.androidbase.core.data

import kotlinx.coroutines.flow.Flow

data class Element(
  val id: Long,
  val title: String,
  val createdAt: Long,
  val updatedAt: Long? = null
)

interface ElementRepository {
  fun observeAll(): Flow<List<Element>>
  suspend fun create(title: String): Long
  suspend fun update(id: Long, title: String): Boolean
  suspend fun delete(id: Long): Boolean
}

interface SettingsRepository {
  val darkTheme: Flow<Boolean>
  val listOrder: Flow<String>
  suspend fun setDarkTheme(value: Boolean)
  suspend fun setListOrder(value: String)
}
```

### 6.4 Implementaciones locales — `:data:local`

```kotlin
package com.androidbase.data.local

import android.content.Context
import com.androidbase.core.data.Element
import com.androidbase.core.data.ElementRepository
import com.androidbase.core.data.SettingsRepository
import com.androidbase.core.database.DatabaseProvider
import com.androidbase.core.database.model.ElementEntity
import com.androidbase.core.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.system.*

class LocalElementRepository(context: Context) : ElementRepository {
  private val db = DatabaseProvider.get(context)
  private val dao = db.elementDao()

  override fun observeAll(): Flow<List<Element>> =
    dao.observeAll().map { list -> list.map { it.toDomain() } }

  override suspend fun create(title: String): Long {
    val now = System.currentTimeMillis()
    return dao.insert(ElementEntity(title = title, createdAt = now))
  }

  override suspend fun update(id: Long, title: String): Boolean {
    val current = dao.getById(id) ?: return false
    val changed = current.copy(title = title, updatedAt = System.currentTimeMillis())
    return dao.update(changed) > 0
  }

  override suspend fun delete(id: Long): Boolean =
    dao.deleteById(id) > 0
}

private fun ElementEntity.toDomain() = Element(
  id = id,
  title = title,
  createdAt = createdAt,
  updatedAt = updatedAt
)

class LocalSettingsRepository(context: Context) : SettingsRepository {
  private val ds = SettingsDataStore(context)
  override val darkTheme = ds.darkTheme
  override val listOrder = ds.listOrder
  override suspend fun setDarkTheme(value: Boolean) = ds.setDarkTheme(value)
  override suspend fun setListOrder(value: String) = ds.setListOrder(value)
}
```

**Proveedor manual para app/feature — `LocalProviders.kt` (en :data:local)**
```kotlin
package com.androidbase.data.local

import android.content.Context
import com.androidbase.core.data.ElementRepository
  import com.androidbase.core.data.SettingsRepository

object LocalProviders {
  fun elementRepository(context: Context): ElementRepository =
    LocalElementRepository(context)

  fun settingsRepository(context: Context): SettingsRepository =
    LocalSettingsRepository(context)
}
```

---

## 7) Pruebas y validación

### 7.1 Dependencias de test (añadir en módulos que prueban repos)
```kotlin
testImplementation(libs.kotlinx.coroutines.test)
androidTestImplementation(libs.kotlinx.coroutines.test)
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test:core:1.5.0")
androidTestImplementation("androidx.test:runner:1.5.2")
```

### 7.2 Tests DAO (androidTest en :core:database)
**`ElementDaoTest.kt`**
```kotlin
@RunWith(AndroidJUnit4::class)
class ElementDaoTest {

  @get:Rule val instant = InstantTaskExecutorRule()

  private lateinit var db: AppDatabase
  private lateinit var dao: ElementDao

  @Before fun setUp() {
    val ctx = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    dao = db.elementDao()
  }

  @After fun tearDown() { db.close() }

  @Test fun insert_and_query() = runTest {
    val id = dao.insert(ElementEntity(title = "A", createdAt = 1L))
    assertTrue(id > 0)
    val got = dao.getById(id)!!
    assertEquals("A", got.title)
  }

  @Test fun flow_observeAll() = runTest {
    val values = mutableListOf<List<ElementEntity>>()
    val job = launch { dao.observeAll().take(2).toList(values) }
    dao.insert(ElementEntity(title = "A", createdAt = 1L))
    dao.insert(ElementEntity(title = "B", createdAt = 2L))
    job.join()
    assertEquals(2, values.last().size)
  }
}
```

### 7.3 Tests Repos (test JVM en :data:local)
_Nos apoyamos en fakes simples del DAO, o usamos una DB en memoria con Robolectric si luego lo añadimos. En este MVP validamos lógica básica con integración real en androidTest y unit tests de mapping._
```kotlin
class LocalElementRepositoryTest {
  // Sugerencia: test de mapping y reglas de negocio sin Android framework.
}
```

### 7.4 Tests DataStore (test JVM en :core:datastore)
Usando un **Context** de prueba con carpeta temporal o `TestScope`:
```kotlin
class SettingsDataStoreTest {
  // Crear un context de prueba con Robolectric en siguiente MVP si se desea.
  // Aquí incluimos pruebas instrumentadas básicas si es necesario tocar disco real.
}
```

### 7.5 Validación manual (Checklist)
- Ejecutar app, crear 3 elementos, cerrar y reabrir → persisten.
- Editar y borrar un elemento → UI refleja cambios.
- Activar tema oscuro en una pantalla de ajustes (debug) → reabrir app → persiste.
- Database Inspector: ver tabla `elements` y filas esperadas.
- Tamaño de DB < 2MB en escenario inicial.
- Sin ANR: operaciones CRUD en corrutinas/suspend.

---

## 8) Integración con UI (demo mínima para QA interno)
En **:feature:home** (o `:feature:sandbox` si preferimos), agregar una pantalla *debug-only* que consuma:
- `LocalProviders.elementRepository(context)` para crear/listar/editar/borrar.
- `LocalProviders.settingsRepository(context)` para setear/leer `darkTheme`.
  La UI de producción sigue simple; la demo puede ocultarse detrás de un flag de build `DEBUG`.

---

## 9) Definición de Hecho (DoD)
- Módulos **:core:database**, **:core:datastore**, **:core:data**, **:data:local** creados y compilando.
- Room funcionando con entity/dao/db y **schema exportado** en `schemas/`.
- DataStore Preferences entregando **Flows** con defaults y setters.
- Repositorios locales disponibles vía proveedores manuales.
- Pruebas DAO **androidTest** pasando en emulador físico/virtual.
- Pruebas básicas de repos/DS consolidadas (mínimo: happy paths).
- Documentación (este MD + READMEs por módulo).
- Demo interna (video/GIF) mostrando CRUD y toggle persistidos.

---

## 10) Riesgos y mitigaciones
- **Migraciones**: riesgo de ruptura al subir versión → mantener `schemas/` y pruebas de migración cuando exista v2.
- **Bloqueos de UI**: operaciones mal orquestadas → usar `suspend`/DAO `Flow` y validar con ANR Watch en desarrollo.
- **Crecimiento de módulos**: posible sobre-modularización → monitorear dependencias y tiempos de build, fusionar si conviene.
- **Test de DataStore en JVM**: puede requerir Robolectric para tests 100% herméticos → pospuesto a MVP futuro si hace falta.

---

## 11) Roadmap de evolución
- v2 DB: nueva columna `isDone: Boolean` en `elements` con migración 1→2.
- DataStore Proto para tipos fuertes (ej. `UserSettings.proto`).
- Cifrado en repositorio (SQLCipher o EncryptedFile + transformaciones) si entra en alcance.
- Integración con una capa de red (cache + source of truth) en MVP posterior.

---

## 12) Guía rápida de “cómo trabajaremos” (repetible)
1. Crear issues por cada T1–T10 con esta especificación.
2. PRs chicos, cada uno con **Checklist de validación**.
3. Antes de merge: correr `./gradlew lintDebug testDebug connectedDebugAndroidTest`.
4. Subir **schemas/** al repo para trackear el historial de DB.
5. Cerrar el MVP con demo y notas de lecciones aprendidas.