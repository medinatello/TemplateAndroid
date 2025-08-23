# Resumen ejecutivo
Este informe detalla una revisión exhaustiva de la plantilla de proyecto Android. El hallazgo más crítico es una **severa desconexión entre la arquitectura documentada y la implementación real**. Mientras que la documentación y la estructura del proyecto sugieren una aplicación moderna, multiplataforma (KMP) y con Arquitectura Limpia, el código funcional es una aplicación Android nativa, simple y con importantes fallos arquitectónicos.

-   **Hallazgo Crítico**: El proyecto mantiene dos arquitecturas en paralelo. Una es la arquitectura KMP (módulo `:shared`) que está bien diseñada según la documentación del MVP-04 (Koin, Ktor, SQLDelight) pero **no está integrada y es código muerto**. La otra es la arquitectura real y funcional, que es una app Android nativa que **no sigue los principios de Arquitectura Limpia**.
-   **Deuda Arquitectónica Grave**: La aplicación funcional viola principios clave: la capa de presentación (`:app`, `:core:ui`) está fuertemente acoplada a la capa de datos (`:data:local`, `:core:database` con Room), y existe un conflicto de inyección de dependencias en el módulo `:app` al incluir tanto Hilt como Koin.
-   **Ausencia de Calidad de Código**: No existen `gates` de calidad. Las herramientas de análisis estático (`ktlint`, `detekt`) no están implementadas, y el entorno de ejecución de pruebas está roto (falta el SDK de Android), impidiendo cualquier validación automática.
-   **Documentación Engañosa**: La documentación de los sprints (especialmente MVP-04) describe funcionalidades y arquitecturas que no existen en la aplicación funcional, lo cual es peligrosamente engañoso para el equipo.
-   **Riesgo de Estabilidad**: La verificación de migraciones de la base de datos de SQLDelight (en el módulo `:shared`) está desactivada, lo cual es una práctica de alto riesgo.

**Dirección Recomendada**:
Es imperativo **detener el desarrollo de nuevas funcionalidades** y tomar una decisión estratégica fundamental:
1.  **Decidir la arquitectura**: ¿Será este un proyecto KMP o nativo de Android?
2.  **Pagar la deuda**: Eliminar la arquitectura no utilizada, resolver el conflicto de DI, arreglar el acoplamiento de capas y establecer las herramientas de calidad de código y CI/CD.

**Semáforo General**:
-   Arquitectura: 🔴
-   Código: 🟠
-   Pruebas: 🔴
-   Seguridad: 🟠
-   Observabilidad: 🟠
-   DevEx: 🔴

# Cumplimiento de estándares del proyecto
Evaluación basada en `Cases/Documentation Main/DEVELOPMENT_STANDARDS.md`.

| Estándar | Evidencia (archivo:línea) | Cumple (Sí/Parcial/No) | Nota |
| :--- | :--- | :--- | :--- |
| Nomenclatura y estilo | `HomeScreen.kt` | Parcial | El código inspeccionado sigue las convenciones, pero la falta de `ktlint` impide una validación automática. |
| Arquitectura limpia | `app/build.gradle.kts` | No | Violación directa de la regla de dependencias (Presentación → Datos). Módulos de dominio y feature huérfanos. |
| Gestión de dependencias | `gradle/libs.versions.toml` | Parcial | Se usa un catálogo centralizado, pero las ADRs en `MVP-04` no reflejan la realidad del código funcional. |
| Commits y ramas | N/A | No Verificable | Requiere acceso al historial de Git, que no tengo. |
| Pruebas y cobertura | `build.gradle.kts` | No | No se encontró un plugin de cobertura. La ejecución de pruebas está bloqueada por el entorno. |
| Logging y telemetría | `shared/build.gradle.kts` vs `core/datastore/...` | No | No hay una estrategia de logging unificada. Se usa `Napier` en `:shared` y `android.util.Log` en `:core:datastore`. |
| CI/CD y calidad | `build.gradle.kts` (todos) | No | No hay fichero de CI. `ktlint` y `detekt` no están aplicados en ningún módulo. |
| Documentación | `Cases/Modulos/MVP-04/resultado.md` | No | La documentación de los sprints es inexacta y no refleja el estado real del código. |

# SOLID
-   **S - Principio de Responsabilidad Única (SRP)**
    -   **Diagnóstico general**: Se cumple en componentes de UI puros, pero se viola en componentes de ciclo de vida de Android.
    -   **NO CUMPLIMIENTOS**:
        -   `app/src/main/java/com/sortisplus/templateandroid/MainActivity.kt` → La Activity contiene lógica para determinar la ruta de navegación inicial basada en el estado de autenticación. Esta lógica debería estar en un ViewModel. **Riesgo**: Bajo. **Recomendación**: Mover la lógica de decisión de navegación a un `MainViewModel`.

-   **O - Principio Abierto/Cerrado (OCP)**
    -   **Diagnóstico general**: Difícil de evaluar sin una lógica de negocio más compleja. La estructura de navegación basada en `when` en `AppNavigation.kt` es una violación menor, pero aceptable para la navegación.

-   **L - Principio de Sustitución de Liskov (LSP)**
    -   **Diagnóstico general**: No se observó suficiente uso de herencia para evaluar este principio de forma significativa.

-   **I - Principio de Segregación de Interfaces (ISP)**
    -   **Diagnóstico general**: La arquitectura general lo viola. Los módulos de presentación (`:app`, `:core:ui`) dependen de módulos de datos concretos, forzándolos a conocer detalles de implementación que no necesitan.

-   **D - Principio de Inversión de Dependencias (DIP)**
    -   **Diagnóstico general**: Violado sistemáticamente.
    -   **NO CUMPLIMIENTOS**:
        -   `app/build.gradle.kts` → Depende directamente de `:data:local` y `:core:data`.
        -   `feature/home/build.gradle.kts` → Depende directamente de `:data:local` y `:core:data`.
        -   **Recomendación**: La capa de presentación debería depender de interfaces de repositorio definidas en una capa de dominio (idealmente en `:shared`), y la capa de datos debería implementar esas interfaces.

# Arquitectura limpia
-   **Diagrama textual de módulos/capas y dependencias (REAL)**:
    ```
    :app (Hilt, Koin)
    ├── :core:ui (Screens)
    │   └── :core:designsystem
    ├── :data:local
    │   └── :core:database (Room)
    ├── :core:datastore
    └── :core:common

    :feature:home (HUERFANO)
    ├── :data:local
    └── ...

    :shared (HUERFANO) (Koin, Ktor, SQLDelight)
    └── ...
    ```

-   **Reglas de acoplamiento y violaciones identificadas**:
    -   **Violación 1**: `app` → `data:local`. La capa de aplicación no debe depender de una implementación concreta de datos. **Impacto**: Alto. Acoplamiento fuerte, dificulta el testing y la reutilización. **Propuesta**: `app` debe depender de `feature` modules, y `feature` modules de interfaces de dominio.
    -   **Violación 2**: `feature:home` → `data:local`. Idem.
    -   **Violación 3**: Módulos `:shared` y `:feature:home` completamente huérfanos. **Impacto**: Crítico. Esfuerzo de desarrollo desperdiciado y confusión masiva sobre la dirección del proyecto. **Propuesta**: Decidir si se va a usar KMP. Si es así, refactorizar la app para usar `:shared`. Si no, eliminar `:shared` y `:feature:home`.

# Hallazgos y recomendaciones (issue cards)
## Arquitectura Real vs. Documentada
-   **Ubicación**: Todo el proyecto, comparar `Cases/Modulos/MVP-04/resultado.md` con el código fuente.
-   **Síntoma**: La documentación describe una app KMP funcional con caché y DI, pero el código es una app nativa simple con una arquitectura diferente y con fallos.
-   **Impacto**: Crítico. Imposibilita la incorporación de nuevos desarrolladores, genera desconfianza en la documentación y oculta problemas reales.
-   **Prioridad**: P0
-   **Riesgo de cambio**: Alto
-   **Propuesta**: Pausar el desarrollo. Realizar una reunión de alineación arquitectónica para decidir el futuro del proyecto (KMP vs. Nativo). Actualizar o eliminar la documentación para que refleje la realidad.
-   **Efecto colateral**: Requiere una inversión de tiempo significativa en refactorización o eliminación de código.
-   **Métrica de verificación posterior**: La documentación del proyecto describe con precisión el código fuente y la arquitectura.

## Conflicto de Inyección de Dependencias (Hilt y Koin)
-   **Ubicación**: `app/build.gradle.kts`
-   **Síntoma**: El módulo `:app` incluye las dependencias de `com.google.dagger:hilt-android` y `io.insert-koin:koin-android`.
-   **Impacto**: Crítico. Aumenta el tamaño de la app, la complejidad del build, y el riesgo de conflictos en tiempo de ejecución. Hace que el grafo de dependencias sea impredecible.
-   **Prioridad**: P0
-   **Riesgo de cambio**: Medio
-   **Propuesta**: Decidir sobre un único framework de DI para la capa de Android (Hilt es el estándar de facto recomendado por Google) y eliminar el otro.
-   **Efecto colateral**: Requiere refactorizar cualquier código que pudiera estar usando el framework de DI eliminado.
-   **Métrica de verificación posterior**: El proyecto solo tiene un framework de DI en la capa de Android.

## Ausencia de Quality Gates (Lint, Detekt, Tests)
-   **Ubicación**: `build.gradle.kts` (todos los módulos), entorno de ejecución.
-   **Síntoma**: `ktlint` y `detekt` no están aplicados. La ejecución de tests está bloqueada por la falta del SDK de Android en el entorno. No hay plugin de cobertura.
-   **Impacto**: Alto. Permite la introducción de bugs, código de baja calidad y deuda técnica sin control.
-   **Prioridad**: P1
-   **Riesgo de cambio**: Bajo
-   **Propuesta**:
    1.  Arreglar el entorno de ejecución para que las pruebas puedan correr.
    2.  Aplicar los plugins de `detekt` y `ktlint` a todos los módulos.
    3.  Añadir un plugin de cobertura (`kover` es la opción recomendada para KMP).
    4.  Integrar estas tareas en un pipeline de CI.
-   **Efecto colateral**: Probablemente revelará cientos de issues de lint/detekt que necesitarán ser corregidos.
-   **Métrica de verificación posterior**: `./gradlew check` y `./gradlew koverReport` se ejecutan con éxito.

## Verificación de Migración de Base de Datos Desactivada
-   **Ubicación**: `shared/build.gradle.kts`
-   **Síntoma**: `verifyMigrations.set(false)` para la base de datos SQLDelight.
-   **Impacto**: Alto. Si hay un cambio en el esquema `.sq` que no se corresponde con un fichero de migración, la app puede crashear en producción para usuarios que actualizan.
-   **Prioridad**: P1
-   **Riesgo de cambio**: Bajo
-   **Propuesta**: Habilitar la verificación de migraciones y crear los ficheros `.sqm` necesarios para cualquier cambio de esquema.
-   **Efecto colateral**: Ninguno. Es una medida de seguridad.
-   **Métrica de verificación posterior**: `verifySqlDelightMigration` se ejecuta con éxito.

# Calidad de pruebas
-   **Resultados de ejecución de tests**: **FALLIDO**. La ejecución de cualquier tarea de Gradle dependiente de Android falla por la ausencia del SDK de Android en el entorno.
-   **Cobertura**: **N/A**. No se pudo ejecutar las pruebas y no hay un plugin de cobertura de código configurado.
-   **Tests triviales detectados**: N/A.
-   **Gaps**: La totalidad del proyecto carece de pruebas verificables en este momento. La documentación del MVP-04 menciona pruebas para `CacheService`, pero no pudieron ser ejecutadas.
-   **Recomendaciones**: El primer paso es arreglar el entorno de ejecución. Una vez hecho, se debe establecer una cultura de TDD y asegurar que la cobertura mínima del 80% (definida en los estándares) se cumpla, empezando por la lógica de negocio en el módulo de dominio.

# UI/Accesibilidad
-   **Cumplimiento Material Design**: El código en `HomeScreen.kt` usa componentes de Material 3 y sigue las guías de layout, lo cual es bueno.
-   **TalkBack, etiquetas, contrastes**: Se encontró uso de `contentDescription` en `NavigationCard`, lo cual es positivo para la accesibilidad. Sin embargo, los `Icon`s tienen `contentDescription = null`, lo que podría ser un problema si el texto adyacente no es suficiente. Se recomienda una revisión más profunda con las herramientas de accesibilidad de Android.

# Seguridad y privacidad
-   **Permisos declarados en manifest vs usados**: El `AndroidManifest.xml` no declara ningún permiso, lo cual es excelente desde el punto de vista de la privacidad.
-   **Manejo seguro de datos locales y claves**:
    -   Se usan URLs de API en `buildConfigField`, lo cual es una práctica estándar.
    -   Se usa `DataStore` para el estado de autenticación, lo cual es bueno. No pude verificar si se usa `EncryptedSharedPreferences`.
    -   **Riesgo Principal**: La desactivación de la verificación de migraciones de SQLDelight en `:shared` es un riesgo de estabilidad y potencial pérdida de datos.
-   **Dependencias con CVEs**: No se realizó un escaneo de CVEs. Se recomienda añadir una tarea de Gradle para esto (ej. `dependency-check-gradle`).

# Observabilidad
-   **Logging**: No hay una estrategia de logging unificada. `Napier` se usa en `:shared`, y `android.util.Log` se usa esporádicamente en `:core:datastore`. La aplicación principal carece de logging estructurado. Se recomienda usar `Timber` para la capa de Android y `Napier` para la capa KMP, con una configuración adecuada para producción.
-   **Crash reporting, analíticas y métricas**: No se encontró ninguna implementación (Firebase Crashlytics, etc.).

# Rendimiento
-   **Benchmarks ejecutados**: No se encontraron módulos de benchmark ni se pudieron ejecutar.
-   **Hotspots y plan de perfiles**:
    -   La regla de lint deshabilitada (`CoroutineCreationDuringComposition`) en `:feature:home` apunta a un posible anti-patrón que puede causar `jank` y recomposiciones innecesarias.
    -   La estrategia de "Cache-then-Network" descrita en la documentación (aunque no implementada funcionalmente) es una excelente idea para mejorar el rendimiento.

# DevEx y CI/CD
-   **Config mínima de ktlint/detekt**: Ausente. Los plugins no se aplican.
-   **Mejoras en Gradle**: El uso de un catálogo de versiones (`libs.versions.toml`) es una buena práctica. Sin embargo, la estructura de dependencias rotas y los módulos huérfanos hacen que el proyecto sea muy difícil de entender.
-   **CI**: No se encontró un pipeline de CI. Es una ausencia crítica para un proyecto que aspira a ser una plantilla.

# Trazabilidad
La trazabilidad entre requisitos, código y pruebas está rota debido a la documentación inexacta.

**Mini-matriz para MVP-04**:
| Requisito (Tarea) | Código | Test | Doc (`resultado.md`) |
| :--- | :--- | :--- | :--- |
| T-06: DI con Koin | Implementado en `:shared` (huérfano) | No ejecutado | Incorrecta (afirma que no hay conflictos) |
| T-07: Retry en Ktor | No implementado | No ejecutado | Incorrecta (dice que se pospone) |
| T-08: Caché SQLDelight| Implementado en `:shared` (huérfano) | No ejecutado | Incorrecta (no menciona que es código no funcional) |

# Estado de sprints 1–4
El análisis del **MVP-04** es suficiente para determinar que el estado documentado de los sprints no es fiable. La documentación describe un proyecto idealizado que no se corresponde con la base de código funcional. Es probable que los sprints anteriores sufran de la misma desconexión. Recomiendo una auditoría completa del código entregado contra la documentación de todos los sprints.

# Backlog priorizado
| Id | Título | Área | Prioridad | Esfuerzo (S/M/L) | Due sugerido |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | Decidir y alinear la visión arquitectónica (KMP vs. Nativo) | Arquitectura | P0 | M | Equipo completo |
| 2 | Eliminar el framework de DI conflictivo (Hilt o Koin) de :app | Arquitectura | P0 | M | Tech Lead |
| 3 | Eliminar o integrar los módulos huérfanos (:shared, :feature:home) | Arquitectura | P0 | L | Tech Lead |
| 4 | Arreglar el entorno de ejecución de pruebas (SDK de Android) | DevEx | P1 | S | DevOps/Dev |
| 5 | Aplicar y configurar ktlint y detekt en todos los módulos | Calidad de Código | P1 | M | Dev |
| 6 | Habilitar la verificación de migraciones de SQLDelight | Seguridad | P1 | S | Dev |
| 7 | Romper el acoplamiento entre la capa de Presentación y Datos | Arquitectura | P1 | L | Tech Lead |
| 8 | Añadir un plugin de cobertura de código (kover) | Calidad de Pruebas | P2 | S | Dev |
| 9 | Implementar una estrategia de logging unificada (Timber/Napier) | Observabilidad | P2 | M | Dev |
| 10 | Refactorizar SRP en MainActivity | Código | P2 | S | Dev |

# Preguntas abiertas
-   ¿Cuál es la visión arquitectónica real y acordada para este proyecto? ¿Es una plantilla KMP o una plantilla nativa de Android?
-   ¿Cuál es el propósito actual del módulo `:shared`? ¿Es un POC, un desarrollo futuro o código abandonado?
-   ¿Por qué se abandonaron o nunca se implementaron las herramientas de calidad de código (`ktlint`, `detekt`) y los tests, a pesar de estar en los estándares?
-   ¿Quién y por qué desactivó la verificación de migraciones de la base de datos en el módulo `:shared`?

# Anexos
-   **Comandos ejecutados y resultados (resumen)**:
    -   Estilo: `./gradlew detekt` / `ktlintCheck` → **FALLIDO** (tareas no encontradas, plugins no aplicados).
    -   Pruebas unitarias: `./gradlew testDebugUnitTest` → **FALLIDO** (SDK de Android no encontrado).
    -   UI tests: `./gradlew connectedDebugAndroidTest` → **FALLIDO** (SDK de Android no encontrado).
    -   Cobertura: N/A (plugin no encontrado).
    -   Benchmarks: N/A (módulo no encontrado).
    -   Leak detection: No se encontró `LeakCanary`.
