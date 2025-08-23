# Development Standards

Este documento define los estándares de desarrollo que deben seguirse en
**TemplateAndroid**.  Su objetivo es garantizar consistencia, calidad y
previsibilidad en todas las entregas.  Estas normas aplican tanto a las
aplicaciones Android como a los módulos multiplataforma (Kotlin Multiplatform
Project, o KMP) y deben ser observadas en todos los MVPs.

## Nomenclatura y estilo de código

- Escribe **todos los identificadores en inglés**.  Las variables y
  funciones usan _camelCase_; los tipos (clases, interfaces, `object`s) usan
  _PascalCase_.
- Los nombres de constantes se escriben en mayúsculas separadas por
  guiones bajos (`VALOR_POR_DEFECTO`).
- Evita abreviaturas no estandarizadas. Prefiere nombres descriptivos y
  explícitos.
- Sigue las recomendaciones oficiales de Kotlin para formato de código y
  aplica **ktlint** para asegurar consistencia automática.

## Arquitectura limpia

El proyecto adopta la **Clean Architecture**, separando capas y
responsabilidades:

1. **Presentación** – UI y lógica específica de plataforma.  En Android
   puede usar frameworks como **Jetpack Compose** y DI con **Hilt**
   únicamente en esta capa.  En Desktop se usa **Compose Multiplatform**.
2. **Dominio** – Casos de uso y modelos del negocio.  Esta capa debe ser
   agnóstica de la plataforma y residir en el módulo `shared` (`commonMain`).
3. **Datos** – Repositorios, fuentes de datos (HTTP, SQLDelight, sistemas de
   almacenamiento de preferencias).  La lógica compartida reside en
   `commonMain` y los detalles de plataforma en los submódulos
   (`androidMain`, `desktopMain`) mediante **expect/actual**.

Mantén las dependencias unidireccionales (Presentación → Dominio → Datos) y
evita referencias cruzadas.  Utiliza **interfaces y abstracciones** para
aislar la lógica de los detalles de implementación.

## Gestión de dependencias

- Las versiones de Kotlin, Gradle y librerías clave (Ktor, SQLDelight,
  Multiplatform‑Settings, Koin, Compose Multiplatform, etc.) deben declararse
  explícitamente en los `build.gradle.kts` o en variables centralizadas.
- Toda adición o actualización de dependencia debe documentarse en el
  archivo del MVP correspondiente y, si implica un cambio de criterio
  arquitectónico, registrarse mediante un **ADR** (Architecture Decision
  Record).

## Commits y ramas

- Utiliza la convención **Conventional Commits** (`feat:`, `fix:`, `docs:`,
  etc.) para nombrar tus mensajes de commit.
- Mantén los cambios **pequeños y atómicos**; cada commit debe tener una
  motivación clara y ser reversible sin romper la base de código.
- Las ramas de trabajo deben derivar de la rama de la funcionalidad en
  curso (por ejemplo, `feature/mvp_03_5`). Usa prefijos como
  `feat/mvp_04_xxx` o `fix/mvp_05_bug`.

## Pruebas y cobertura

- Todos los módulos deben incluir **pruebas unitarias** y, cuando
  corresponda, **pruebas de integración**.  Las pruebas de la lógica
  compartida (en `shared`) van en `commonTest`.  Para Android se usan
  `androidTest` y para Desktop `desktopTest`.
- La meta de cobertura mínima es del **80 %** en el código de negocio y del
  70 % en las clases de plataforma.  Los MVPs que introduzcan lógica
  significativa deben especificar cómo alcanzar este umbral.
- Utiliza _fixtures_ y _mocks_ para aislar dependencias externas.

## Logging y telemetría

- Implementa **logging estructurado**.  En Android se puede usar _Logcat_
  solamente en la capa de UI; para lógica compartida, Ktor y KMP
  proporcionan interceptores de logging que escriben en el medio
  correspondiente.
- Incluye métricas de uso y rendimiento cuando corresponda.  Exponer
  contadores y temporizadores ayuda a detectar cuellos de botella.

## CI/CD y calidad

- Todo cambio debe pasar por el **pipeline de CI**, que incluye:
  - Compilación de todas las plataformas afectadas.
  - Ejecución de pruebas automáticas y reporte de cobertura.
  - Análisis estático (ktlint y Detekt) para asegurar el cumplimiento de
    estilo y encontrar problemas comunes.
- El proyecto utiliza **semver** para la numeración de versiones; los
  incrementos de versión deben documentarse en los `CHANGELOG.md` y
  acompañarse con notas de lanzamiento.

## Documentación

- Cada MVP debe crear un archivo `resultado.md` que resuma lo logrado,
  explique decisiones técnicas, liste las versiones empleadas y proporcione
  ejemplos de código relevantes.
- Mantén actualizados los documentos en `Documentation Main`.  Cuando
  encuentres una mejora o descubras un error, actualiza las guías para que
  beneficien a todo el equipo.

## Revisión de código

- Todo cambio debe pasar por **code review**.  Al menos una persona distinta
  del autor debe aprobar el pull request.
- Las discusiones de diseño y solicitudes de cambio deben registrarse en la
  revisión; evita discusiones informales que puedan perderse.

Adherirse a estas normas asegura que el proyecto evolucione de manera
ordenada, facilite la colaboración y permita escalar la plantilla a nuevos
proyectos sin sufrir deuda técnica.