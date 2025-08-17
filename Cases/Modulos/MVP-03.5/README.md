# MVP-03.5 — Fundamentos de Kotlin Multiplatform (KMP)

Este MVP es un paso intermedio y fundamental para transformar la base de código de una aplicación exclusiva de Android a un proyecto Kotlin Multiplatform (KMP). El objetivo es reestructurar el proyecto, introducir las dependencias necesarias y establecer las bases para compartir código entre Android, Windows, Linux y macOS.

**Este MVP no introduce nuevas funcionalidades de cara al usuario.** Su propósito es puramente técnico y de arquitectura.

## Objetivos Principales

1.  **Reestructuración del Proyecto:**
    *   Crear un módulo `shared` que contenga la lógica de negocio común.
    *   Configurar los `sourceSets` de Gradle para `commonMain`, `androidMain`, y `desktopMain`.

2.  **Migración de Código Compartido:**
    *   Mover modelos de datos, validadores y lógica de negocio no dependiente de Android al módulo `shared` en `commonMain`.

3.  **Introducción de Librerías KMP:**
    *   Reemplazar librerías específicas de Android por sus alternativas KMP:
        *   **Base de Datos:** Room → **SQLDelight**
        *   **Preferencias:** DataStore → **Multiplatform-Settings**
        *   **Inyección de Dependencias:** Hilt → **Koin** (en el código compartido)
    *   Añadir **Ktor** como cliente HTTP para `commonMain`.

4.  **Configuración de Plataformas:**
    *   **Android (`androidMain`):** Adaptar la app de Android para que consuma la lógica del módulo `shared`. Hilt puede seguir utilizándose en la capa de UI de Android.
    *   **Escritorio (`desktopMain`):** Crear una aplicación de escritorio mínima (una ventana con un "Hola Mundo") para verificar que la compilación y ejecución en Windows, Linux y macOS es correcta.

## Criterios de Aceptación

*   El proyecto compila exitosamente para los targets de Android y Escritorio.
*   La aplicación de Android se ejecuta y consume al menos un componente del módulo `shared`.
*   La aplicación de escritorio se lanza y muestra una ventana simple.
*   Las nuevas dependencias KMP (Ktor, SQLDelight, Koin, Multiplatform-Settings) están configuradas y son accesibles desde `commonMain`.
*   La estructura de directorios y módulos sigue las convenciones de KMP.
