# Guía de Desarrollo con Kotlin Multiplatform (KMP)

Esta guía sintetiza las buenas prácticas para desarrollar aplicaciones multiplataforma con Kotlin Multiplatform (KMP). Complementa las guías existentes de desarrollo Android y proporciona un marco común para compartir código entre Android, escritorio y otros futuros targets (iOS, web).

## Principios generales

1. **Separación de responsabilidades**: coloca la lógica de dominio, modelos de datos, casos de uso y validadores en `shared` (`commonMain`). La interfaz de usuario y cualquier código dependiente de la plataforma deben residir en los módulos específicos (`androidMain`, `desktopMain`, etc.).
2. **Abstracción mediante expect/actual**: para cualquier funcionalidad que requiera APIs de plataforma (almacenamiento, redes, notificaciones, etc.), define una interfaz `expect` en `commonMain` y provee implementaciones `actual` por plataforma. Esto mantiene la portabilidad y simplifica las pruebas.
3. **Librerías multiplataforma**: preferir dependencias compatibles con KMP (por ejemplo, Ktor para redes, SQLDelight para bases de datos, Multiplatform‑Settings para preferencias, Koin/Napier para DI y logging). Evalúa continuamente el soporte multiplataforma antes de agregar nuevas librerías.
4. **Gestión de versiones y targets**: sincroniza las versiones de Kotlin, Gradle y demás plugins a lo largo de los módulos. Declara explícitamente los targets soportados en el bloque `kotlin {}` y documenta cualquier plataforma futura en los planes de MVP.
5. **Pruebas unificadas**: ubica las pruebas de lógica de negocio en `commonTest` y mantén una cobertura mínima del 80 %. Añade pruebas específicas de plataforma solo cuando la lógica lo requiera (por ejemplo, pruebas de UI en Android).
6. **Estilo y nomenclatura**: usa inglés para nombres de archivos, clases, funciones y variables. Sigue las convenciones de Kotlin (camelCase para métodos y variables, PascalCase para clases). Documenta cada módulo y expón ejemplos en `resultado.md`.

## Estructura recomendada

```
/androidApp        # Aplicación Android (UI y wiring específico)
/desktopApp        # Aplicación de escritorio (Windows/Linux/macOS)
/shared            # Módulo común con la lógica de dominio
  /src/commonMain
  /src/commonTest
  /src/androidMain
  /src/androidUnitTest
  /src/desktopMain
  /src/desktopTest
settings.gradle.kts
build.gradle.kts   # Configuración raíz y de módulos
```

## Trabajo incremental por MVP

Cada MVP debe:
* Definir claramente sus objetivos y alcances.
* Documentar las decisiones de arquitectura en un ADR (`ADRs/ADR‑NNN‑<tema>.md`).
* Proponer tareas con criterios de aceptación medibles en `TAREAS.md`.
* Al finalizar, generar un `resultado.md` con un resumen técnico, árbol de módulos, versiones usadas, ejemplos de código y métricas de cobertura.

Esta guía sirve como referencia para todos los proyectos basados en KMP (Android, Swift, Go u otros). Adáptala cuando incorpores nuevas plataformas o librerías.