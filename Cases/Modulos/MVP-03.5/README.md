# MVP-03.5 — Fundamentos de Kotlin Multiplatform (KMP)

Este MVP es un paso intermedio para transformar una app Android en un proyecto Kotlin Multiplatform (KMP). El objetivo es crear la base compartida (módulo `shared`), definir targets, fijar versiones, preparar DI, almacenamiento, red y pruebas para Android y Escritorio (Windows/Linux/macOS).

> Este MVP no introduce funcionalidades visibles para el usuario final. Es de arquitectura.

## Versiones y targets (obligatorias)

- **Kotlin**: 2.0.x
- **Gradle**: 8.x
- **AGP (Android Gradle Plugin)**: compatible con Kotlin 2.0.x
- **Java**: 17
- **Compose Multiplatform**: versión estable compatible con Kotlin 2.0.x
- **Ktor Client**: 2.x
- **SQLDelight**: 2.x
- **Multiplatform-Settings**: 1.x
- **DI**: Koin 3.x

**Targets iniciales**

- `androidTarget()`
- `jvm("desktop")` (Windows/Linux/macOS)

> iOS queda fuera de este MVP; se documenta como target futuro.

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
build.gradle.kts (raíz y por módulo)
```

## Superficies `expect/actual` que se establecen en 03.5

1) **KeyValueStore**
   - `expect interface KeyValueStore { fun getString(key: String): String?; fun putString(key: String, value: String); }`
   - `actual` en Android: Multiplatform‑Settings respaldado por SharedPreferences/DataStore según configuración.
   - `actual` en Desktop: Multiplatform‑Settings en archivo local.

2) **AppClock**
   - `expect interface AppClock { fun now(): Long }`
   - `actual` por plataforma usando utilidades estándar.

3) **DbDriverFactory** (SQLDelight)
   - `expect class DbDriverFactory { fun createDriver(schema: SqlSchema): SqlDriver }`
   - `actual` Android: `AndroidSqliteDriver`; Desktop: `JdbcSqliteDriver`.

4) **HttpClientFactory** (Ktor)
   - `expect fun httpClient(): HttpClient`
   - `actual` Android: motor Android u OkHttp; Desktop: CIO.

## Librerías KMP y decisiones

- **Networking**: Ktor Client en `commonMain`. Configurar timeouts, logging y reintentos básicos.
- **Base de datos**: SQLDelight. El esquema se define en `commonMain` y los drivers específicos en cada plataforma.
- **Preferencias**: Multiplatform‑Settings.
- **DI**: Koin solo en `shared`. En Android, Hilt puede seguir en la capa de UI; se deberá conectar a Koin mediante un adaptador.
- **UI Desktop**: Compose Multiplatform (aplicación mínima “Hola Mundo”) para validar compilación y ejecución.

Se registrará un ADR que justifique la sustitución de Room/DataStore/Hilt por SQLDelight/Settings/Koin.

## Criterios de aceptación (Definition of Done del MVP 03.5)

1. El proyecto **compila** para Android y Desktop.
2. `androidApp` consume al menos **un caso de uso** desde `shared`.
3. `desktopApp` ejecuta una ventana mínima con un texto de verificación.
4. Ktor, SQLDelight, Multiplatform‑Settings y Koin están **configurados en `commonMain`** y accesibles.
5. **Pruebas**:
   - `shared`: pruebas unitarias con **80%+ de cobertura** en lógica pura.
   - `androidApp`: smoke test de arranque.
   - `desktopApp`: test simple de inicialización.
6. **CI**:
   - Pipeline que ejecute `./gradlew :shared:allTests :androidApp:assembleDebug :desktopApp:test`.
   - Publicación de reportes de pruebas y cobertura.
7. **Estilo/Calidad**: ktlint y Detekt configurados al menos con reglas básicas.

## Pasos de implementación (resumen ejecutivo)

1. **Reestructuración**: crear módulo `shared` y `desktopApp`; configurar `settings.gradle.kts` y los targets `androidTarget()` y `jvm("desktop")`.
2. **Dependencias**: añadir Ktor, SQLDelight, Multiplatform‑Settings y Koin en el módulo `shared`.
3. **expect/actual**: crear `KeyValueStore`, `AppClock`, `DbDriverFactory` y `httpClient` como expect en `commonMain` e implementar sus versiones `actual` para Android y Desktop.
4. **DI**: definir módulos de Koin en `shared` (para casos de uso, repositorios, clientes). En Android, inicializar Koin y establecer el puente con Hilt si la UI sigue con Hilt.
5. **Desktop**: implementar una app mínima con Compose Desktop que invoque un caso de uso de `shared`.
6. **Pruebas**: agregar pruebas unitarias en `commonTest`; pruebas de integración de inicio en Android y Desktop; asegurar cobertura mínima de 80% en `shared`.
7. **CI y Estándares**: añadir tareas de ktlint y Detekt y configurar un workflow mínimo de CI.
8. **Docs**: al finalizar, crear un archivo `resultado.md` resumiendo lo realizado, versiones usadas y árbol de módulos resultante.

## Comandos útiles

- Construir la app de Android: `./gradlew :androidApp:assembleDebug`
- Ejecutar la app de escritorio: `./gradlew :desktopApp:run`
- Ejecutar pruebas de `shared`: `./gradlew :shared:allTests`
- Ejecutar linters: `./gradlew ktlintCheck detekt`
- Ejecutar el pipeline localmente: `./gradlew :shared:allTests :androidApp:assembleDebug :desktopApp:test`

## Riesgos y mitigaciones

- **Conflictos entre Hilt y Koin**: se debe aislar Hilt a la capa de UI de Android y usar Koin en el código compartido. Documentar el adaptador en un ADR.
- **Deuda técnica Android‑only**: si se detectan APIs demasiado acopladas a Android, se deben encapsular mediante `expect/actual` o retrasar su migración a un MVP posterior.
- **Complejidad de KMP**: la configuración multiplataforma puede ser compleja. Mitigación: comenzar con un caso mínimo y seguir la documentación oficial.
- **Curva de aprendizaje**: el equipo debe familiarizarse con SQLDelight, Ktor y Koin multiplataforma. Se recomienda crear PoCs antes de integrarlos.

## Entregable

Al finalizar este MVP, se debe crear el archivo `Cases/Modulos/MVP-03.5/resultado.md` que incluya:

- Un resumen técnico de lo implementado (módulos creados, targets configurados, librerías añadidas).
- Las versiones finales utilizadas.
- El árbol de módulos y sourceSets resultante.
- Las decisiones técnicas (enlazando ADRs correspondientes).
- Instrucciones de build/run y las métricas de cobertura obtenidas.
- Notas y riesgos pendientes para futuros sprints.