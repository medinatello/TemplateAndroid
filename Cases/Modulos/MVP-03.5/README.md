# MVP-03.5 — Fundamentos de Kotlin Multiplatform (KMP)

Este MVP es un paso intermedio para transformar una app Android en un proyecto Kotlin Multiplatform (KMP). El objetivo es crear la base compartida (módulo `shared`), definir targets, fijar versiones, preparar la inyección de dependencias (DI), el almacenamiento, la red y las pruebas para Android y Escritorio (Windows/Linux/macOS).

> Este MVP no introduce funcionalidades visibles para el usuario final. Es de arquitectura.

---

## Versiones y targets (obligatorias)

- **Kotlin**: 2.0.x
- **Gradle**: 8.x
- **AGP (Android Gradle Plugin)**: compatible con Kotlin 2.0.x
- **Java**: 17
- **Compose Multiplatform**: versión estable compatible con Kotlin 2.0.x
- **Ktor Client**: 2.x
- **SQLDelight**: 2.x
- **Multiplatform-Settings**: 1.x
- **DI**: Koin 3.x

**Targets iniciales**
- `androidTarget()`
- `jvm("desktop")` (para Windows/Linux/macOS)

---

## Estructura del proyecto (resultado esperado)

```
/androidApp
/desktopApp
/shared
  /src/commonMain
  /src/commonTest
  /src/androidMain
  /src/androidUnitTest
  /src/desktopMain
  /src/desktopTest
settings.gradle.kts
build.gradle.kts (root y por módulo)
```

---

## Superficies `expect/actual` que se establecen en 03.5

1) **KeyValueStore**  
`expect interface KeyValueStore { fun getString(key: String): String?; fun putString(key: String, value: String) }`  
`actual` Android: Multiplatform‑Settings respaldado por SharedPreferences/DataStore.  
`actual` Desktop: Multiplatform‑Settings en archivo local.

2) **AppClock**  
`expect interface AppClock { fun now(): Long }`  
`actual` por plataforma usando utilidades estándar.

3) **DbDriverFactory** (SQLDelight)  
`expect class DbDriverFactory { fun createDriver(schema: SqlSchema): SqlDriver }`  
`actual` Android: `AndroidSqliteDriver`; Desktop: `JdbcSqliteDriver`.

4) **HttpEngineFactory** (Ktor)  
`expect fun httpClient(): HttpClient`  
`actual` Android: motor Android u OkHttp; Desktop: CIO.

---

## Librerías KMP y decisiones

- **Networking**: Ktor Client en `commonMain`. Incluye serialización, logging y reintentos básicos.  
- **Base de datos**: SQLDelight. El esquema se define en `commonMain` y los drivers se generan por plataforma.  
- **Preferencias**: Multiplatform‑Settings.  
- **Inyección de dependencias**: Koin en el módulo `shared`. Hilt puede coexistir en la UI de Android como wiring.  
- **UI Desktop**: Compose Multiplatform para una prueba mínima “Hola Mundo”.

> Las decisiones de sustitución de Room/DataStore/Hilt se documentan en los ADRs.

---

## Criterios de aceptación (Definition of Done del MVP 03.5)

1. El proyecto compila para Android y Desktop.  
2. `androidApp` consume al menos un caso de uso desde `shared`.  
3. `desktopApp` ejecuta una ventana mínima “Hola Mundo”.  
4. Ktor, SQLDelight, Multiplatform‑Settings y Koin están configurados en `commonMain` y accesibles.  
5. **Pruebas**:  
    - `shared`: pruebas unitarias con cobertura **≥ 80 %**.  
    - `androidApp`: smoke test de arranque.  
    - `desktopApp`: test simple de inicialización.  
6. **CI**:  
    - Pipeline ejecuta `./gradlew :shared:allTests :androidApp:assembleDebug :desktopApp:test`.  
    - Publica reportes de tests y cobertura.  
7. **Estilo/Calidad**: ktlint y Detekt configurados.

---

## Pasos de implementación (resumen ejecutivo)

1. **Reestructuración**: crear módulos `shared` y `desktopApp`; configurar `sourceSets` y targets.  
2. **Dependencias**: añadir Ktor, SQLDelight, Multiplatform‑Settings y Koin en `shared`.  
3. **expect/actual**: implementar `KeyValueStore`, `AppClock`, `DbDriverFactory` y `httpClient`.  
4. **Inyección**: definir módulos de Koin en `shared`; inicializar Koin en Android y proporcionar un puente con Hilt si se mantiene.  
5. **Aplicación de escritorio**: crear app mínima con Compose Desktop que invoque un caso de uso de `shared`.  
6. **Pruebas**: unitarios en `commonTest`; smoke en Android y Desktop; cobertura ≥ 80 % en `shared`.  
7. **CI y estándares**: integrar tareas de ktlint/Detekt y el workflow de CI mínimo.  
8. **Documentación**: actualizar `resultado.md` al final con detalles de lo logrado.

---

## Comandos útiles

- **Android**: `./gradlew :androidApp:assembleDebug`  
- **Desktop**: `./gradlew :desktopApp:run`  
- **Pruebas `shared`**: `./gradlew :shared:allTests`  
- **Lint**: `./gradlew ktlintCheck detekt`  
- **CI local**: `./gradlew :shared:allTests :androidApp:assembleDebug :desktopApp:test`

---

## Riesgos y mitigaciones

- **Conflicto Hilt/Koin**: limitar Koin al código compartido y Hilt a la UI de Android; documentado en ADR‑001.  
- **Deuda Android‑only**: si un módulo bloquea KMP, abstraer mediante `expect`/`actual` o replanificar su migración a un MVP posterior.  
- **Curva de aprendizaje**: reforzar con PoCs y documentación antes de la integración completa.  
- **Soporte multiplataforma**: inicialmente priorizar Desktop y posponer iOS para un MVP futuro.

---

## Entregable

Al finalizar este sprint se debe crear el archivo `resultado.md` con:  
- Resumen de lo implementado.  
- Versiones finales de las dependencias.  
- Árbol de módulos resultante.  
- Decisiones técnicas (enlazar ADRs).  
- Instrucciones de build/run y pruebas.  
- Métricas de cobertura.
