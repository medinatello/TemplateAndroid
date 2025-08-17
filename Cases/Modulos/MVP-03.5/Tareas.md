# Tareas del MVP-03.5: Fundamentos KMP

## Historias de Usuario (Técnicas)

-   **Como desarrollador,** quiero una estructura de proyecto KMP con un módulo `shared` para poder escribir lógica de negocio una sola vez y compartirla entre Android y Escritorio.
-   **Como arquitecto,** quiero que las dependencias de base de datos, preferencias y networking sean multiplataforma para asegurar la consistencia entre diferentes plataformas.

## Entregables

-   Nueva estructura de módulos (`shared`, `androidApp`, `desktopApp`).
-   Configuración de Gradle (`build.gradle.kts`) para KMP.
-   Código migrado a `commonMain`.
-   Aplicación de escritorio "Hola Mundo" funcional.

---

## Task Breakdown

### T-01: Reestructuración del Proyecto a KMP
**Estimación:** 2 días
**Prioridad:** Crítica

-   [ ] Crear un nuevo módulo `shared`.
-   [ ] Mover la lógica de los módulos `core` existentes al nuevo módulo `shared`.
-   [ ] Renombrar el módulo `app` a `androidApp` para mayor claridad.
-   [ ] Configurar `settings.gradle.kts` para incluir los nuevos módulos.
-   [ ] Actualizar el `build.gradle.kts` del módulo `shared` para usar el plugin `kotlin-multiplatform`.
-   [ ] Definir los `sourceSets` para `commonMain`, `androidMain`, y `desktopMain`.

### T-02: Introducir Dependencias KMP
**Estimación:** 2 días
**Prioridad:** Alta

-   [ ] Añadir **Ktor** al `commonMain` para networking.
-   [ ] Añadir **SQLDelight** al `commonMain` para la base de datos.
    -   Configurar el plugin de Gradle para SQLDelight.
    -   Crear un archivo `.sq` de ejemplo.
-   [ ] Añadir **Multiplatform-Settings** al `commonMain` para las preferencias.
-   [ ] Añadir **Koin** al `commonMain` como solución de inyección de dependencias.

### T-03: Migrar Lógica a `commonMain`
**Estimación:** 3 días
**Prioridad:** Alta

-   [ ] Mover los modelos de datos (DTOs, entidades) a `commonMain`.
-   [ ] Mover los repositorios (abstracciones) a `commonMain`.
-   [ ] Adaptar los casos de uso para que residan en `commonMain`.
-   [ ] Reemplazar las dependencias específicas de Android (Room, DataStore, Hilt) en el código migrado por las nuevas abstracciones KMP.

### T-04: Adaptar la App de Android
**Estimación:** 2 días
**Prioridad:** Media

-   [ ] Hacer que el módulo `androidApp` dependa del módulo `shared`.
-   [ ] Crear implementaciones (`actual`) en `androidMain` para las declaraciones (`expect`) de `commonMain` si fuera necesario.
-   [ ] Conectar la UI de Jetpack Compose en Android con los ViewModels que ahora usan la lógica de `shared`.
-   [ ] Asegurarse de que Hilt y Koin coexisten (Hilt para la capa de UI de Android, Koin para la lógica compartida).

### T-05: Crear la App de Escritorio
**Estimación:** 1 día
**Prioridad:** Media

-   [ ] Crear un nuevo módulo `desktopApp`.
-   [ ] Configurar el `build.gradle.kts` para usar `compose.desktop`.
-   [ ] Crear una función `main` que inicie una ventana de Compose for Desktop.
-   [ ] Mostrar un texto "Hola KMP" en la ventana.
-   [ ] Asegurarse de que la aplicación de escritorio puede compilarse y ejecutarse en la máquina de desarrollo.

---

## Riesgos

-   **Complejidad de Gradle:** La configuración de KMP puede ser compleja. Mitigación: Seguir la documentación oficial de JetBrains y empezar con una configuración mínima.
-   **Coexistencia de Hilt y Koin:** Puede haber conflictos o dificultades al hacer que ambos sistemas de DI trabajen juntos. Mitigación: Definir claramente los límites de cada uno (Hilt para `androidApp`, Koin para `shared`).
-   **Curva de Aprendizaje:** El equipo puede necesitar tiempo para familiarizarse con las nuevas librerías KMP (SQLDelight, Ktor). Mitigación: Realizar pruebas de concepto (PoCs) para cada librería antes de la integración completa.
