# Gemini Project: TemplateAndroid

Comunicate siempre en español, incluso los logs que emites cuando vas realizando las tareas.


## Project Overview

This is a multi-module Android application template. It serves as a foundation for new Android projects, providing a clear and scalable architecture.

The project is written in Kotlin and uses modern Android development practices, including:

*   **Jetpack Compose:** For building the user interface.
*   **Kotlin DSL:** For Gradle build scripts.
*   **Multi-module architecture:** To separate concerns and improve maintainability.
*   **Product Flavors:** To manage different build variants (dev, stg, prd).
*   **Dependency Management:** Using a centralized `libs.versions.toml` file.

### Modu

*   **`:app`**: The main application module.
*   **`:core`**: Contains core functionalities shared across the application.
    *   **`:core:common`**: Common utilities and extensions.
    *   **`:core:data`**: Data layer components.
    *   **`:core:database`**: Room database implementation.
    *   **`:core:datastore`**: Jetpack DataStore implementation.
    *   **`:core:designsystem`**: UI components and themes.
    *   **`:core:network`**: Networking layer (e.g., Retrofit).
    *   **`:core:ui`**: Base UI components and utilities.
*   **`:data`**: Data modules.
    *   **`:data:local`**: Local data source implementation.
*   **`:feature`**: Feature modules.
    *   **`:feature:home`**: Home screen feature.

## Building and Running

### Building the project

To build the project, you can use the following Gradle command:

```bash
./gradlew build
```

### Running the application

To install and run the application on a connected device or emulator, use:

```bash
./gradlew installDebug runApp
```

### Running tests

To run unit tests, use:
```bash
./gradlew test
```

To run instrumented tests, use:
```bash
./gradlew connectedAndroidTest
```

## Development Conventions

### Git

This project uses a squash merge strategy for merging feature branches into `dev`. A helper script is provided at `scripts/git-squash-merge.sh`.

### Diagnostics

The project includes two diagnostic tasks to help troubleshoot common issues:

*   `./gradlew diagnoseRunConfiguration`: To diagnose why the application may not be runnable in Android Studio.
*   `./gradlew diagnoseModuleVisibility`: To diagnose why a module may not be visible in Android Studio.

# Prompts Gemini - TemplateAndroid

Este archivo contiene prompts estáticos reutilizables para trabajar con Gemini Code en el proyecto TemplateAndroid. Cada prompt está diseñado para ser eficiente y seguir los estándares de desarrollo definidos en `Cases/Documentation Main/`.

## Índice de Prompts

- [mvp-sprint](#mvp-sprint) - Ejecutar tareas de un MVP específico
- [code-review](#code-review) - Revisión de código para un MVP
- [build-test](#build-test) - Compilar y probar un módulo específico
- [architecture-analysis](#architecture-analysis) - Analizar arquitectura del proyecto
- [migration-guide](#migration-guide) - Crear guía de migración entre versiones

## Prompts Disponibles

### mvp-sprint
**Descripción**: Ejecuta las tareas definidas para un MVP específico siguiendo los estándares del proyecto.

**Uso**: `ejecuta el prompt mvp-sprint con MVP-05`

**Variables**:
- `MVP_NUMBER`: Número del MVP (ej: MVP-05, MVP-06)

**Prompt**:
```
En la carpeta Cases/Modulos/{MVP_NUMBER}/ están los documentos necesarios para trabajar este sprint. Incluye el detalle y todas las especificaciones. En la carpeta Cases/Documentation Main/ hay archivos .md que indican los estándares que debes seguir para cumplir este sprint.

Tu tarea principal es:
1. Revisar y entender los documentos en Cases/Modulos/{MVP_NUMBER}/
2. Revisar los estándares en Cases/Documentation Main/
3. Implementar todas las tareas especificadas en Tareas.md
4. Al finalizar, completar el archivo Cases/Modulos/{MVP_NUMBER}/resultado.md

Sigue estos principios:
- Utiliza la arquitectura limpia definida en DEVELOPMENT_STANDARDS.md
- Mantén compatibilidad con Kotlin Multiplatform (KMP)
- Integra correctamente con el módulo shared
- Escribe pruebas unitarias con cobertura mínima del 80%
- Documenta todas las decisiones arquitectónicas

Cuando termines todas las tareas, actualiza el archivo resultado.md con:
- Resumen de funcionalidades implementadas
- Versiones utilizadas
- Estructura final del proyecto
- Decisiones y justificaciones
- Ejemplos de código relevantes
- Resultados de build y tests
- Notas para futuros sprints
```

### code-review
**Descripción**: Realiza una revisión completa del código implementado en un MVP.

**Uso**: `ejecuta el prompt code-review con MVP-04`

**Variables**:
- `MVP_NUMBER`: Número del MVP a revisar

**Prompt**:
```
Realiza una revisión completa del código implementado para {MVP_NUMBER}. 

Analiza los siguientes aspectos:
1. **Arquitectura**: Verificar que sigue Clean Architecture y principios SOLID
2. **KMP Compliance**: Validar compatibilidad multiplataforma
3. **Estándares**: Cumplimiento de DEVELOPMENT_STANDARDS.md
4. **Testing**: Cobertura y calidad de pruebas
5. **Performance**: Identificar posibles cuellos de botella
6. **Security**: Revisar exposición de datos sensibles
7. **Maintainability**: Legibilidad y documentación del código

Genera un reporte con:
- ✅ Aspectos que cumplen con los estándares
- ⚠️ Mejoras sugeridas
- ❌ Problemas críticos que deben solucionarse
- 📝 Recomendaciones para futuros desarrollos

Enfócate en los archivos creados o modificados en el {MVP_NUMBER}.
```

### build-test
**Descripción**: Compila y prueba módulos específicos del proyecto.

**Uso**: `ejecuta el prompt build-test con shared`

**Variables**:
- `MODULE_NAME`: Nombre del módulo (shared, app, core:database, etc.)

**Prompt**:
```
Ejecuta una compilación y prueba completa del módulo {MODULE_NAME}.

Realiza las siguientes tareas en orden:
1. Compilar el módulo: ./gradlew :{MODULE_NAME}:assembleDebug
2. Ejecutar pruebas unitarias: ./gradlew :{MODULE_NAME}:testDebugUnitTest
3. Análisis estático: ./gradlew ktlintCheck detekt
4. Verificar que no hay warnings críticos

Si hay errores:
- Analizar y corregir problemas de compilación
- Resolver fallos en tests
- Arreglar violaciones de lint/detekt

Proporciona un resumen final con:
- ✅ Estado de compilación
- 📊 Resultados de tests (número de tests ejecutados/pasados/fallidos)
- 🔍 Problemas de análisis estático encontrados y corregidos
- 📈 Métricas de cobertura si están disponibles
```

### architecture-analysis
**Descripción**: Analiza la arquitectura actual del proyecto y sugiere mejoras.

**Uso**: `ejecuta el prompt architecture-analysis`

**Prompt**:
```
Realiza un análisis completo de la arquitectura del proyecto TemplateAndroid.

Analiza los siguientes aspectos:
1. **Modularización**: Estructura de módulos y dependencias
2. **Clean Architecture**: Separación de capas (Presentation, Domain, Data)
3. **KMP Integration**: Uso de expect/actual y commonMain/androidMain/desktopMain
4. **Dependency Injection**: Configuración de Koin y Hilt
5. **Database Layer**: Uso de SQLDelight y Room
6. **Network Layer**: Configuración de Ktor
7. **State Management**: ViewModels y manejo de estado
8. **Testing Strategy**: Estructura de tests y mocks

Genera un reporte con:
📋 **Estado Actual**:
- Descripción de la arquitectura actual
- Diagrama de dependencias entre módulos

🎯 **Fortalezas Identificadas**:
- Aspectos bien implementados
- Patrones correctamente aplicados

⚠️ **Áreas de Mejora**:
- Problemas arquitectónicos
- Violaciones de principios SOLID
- Acoplamiento excesivo

🚀 **Recomendaciones**:
- Refactorizaciones sugeridas
- Nuevos patrones a implementar
- Plan de mejora por fases
```

### migration-guide
**Descripción**: Crea una guía de migración entre versiones de dependencias.

**Uso**: `ejecuta el prompt migration-guide de Kotlin-1.9 a Kotlin-2.0`

**Variables**:
- `FROM_VERSION`: Versión origen
- `TO_VERSION`: Versión destino

**Prompt**:
```
Crea una guía de migración para actualizar de {FROM_VERSION} a {TO_VERSION} en el proyecto TemplateAndroid.

La guía debe incluir:

1. **Análisis de Compatibilidad**:
   - Cambios breaking
   - Nuevas features disponibles
   - Dependencias afectadas

2. **Pasos de Migración**:
   - Actualización de gradle/libs.versions.toml
   - Cambios en build.gradle.kts
   - Modificaciones de código necesarias
   - Actualizaciones en configuración

3. **Testing Plan**:
   - Tests que ejecutar después de la migración
   - Validaciones de compatibilidad KMP
   - Verificaciones de build en todas las plataformas

4. **Rollback Plan**:
   - Cómo revertir los cambios si hay problemas
   - Puntos de verificación durante la migración

5. **Documentación**:
   - Actualizar README y documentación técnica
   - Crear entrada en CHANGELOG.md

Ejecuta la migración paso a paso y documenta cualquier problema encontrado.
```

## Uso de los Prompts

Para usar cualquier prompt, simplemente escribe:
```
ejecuta el prompt [nombre-del-prompt] con [parámetros]
```

Ejemplos:
- `ejecuta el prompt mvp-sprint con MVP-06`
- `ejecuta el prompt code-review con MVP-05`
- `ejecuta el prompt build-test con app`
- `ejecuta el prompt architecture-analysis`
- `ejecuta el prompt migration-guide de AGP-8.1 a AGP-8.2`

## Añadir Nuevos Prompts

Para añadir un nuevo prompt:

1. Crea una nueva sección en este archivo
2. Define claramente el propósito y uso del prompt
3. Especifica las variables necesarias
4. Escribe el prompt siguiendo las mejores prácticas
5. Añade un ejemplo de uso
6. Actualiza el índice

**Template para nuevos prompts**:
```markdown
### nombre-del-prompt
**Descripción**: [Descripción breve del prompt]

**Uso**: `ejecuta el prompt nombre-del-prompt con [parámetros]`

**Variables**:
- `VARIABLE_NAME`: Descripción de la variable

**Prompt**:
```
[Contenido del prompt aquí]
```

## Notas

- Los prompts están diseñados para ser eficientes y minimizar tokens
- Siguen las convenciones y estándares definidos en `Documentation Main/`
- Se actualizan según la evolución del proyecto
- Mantén la coherencia con la arquitectura KMP establecida
