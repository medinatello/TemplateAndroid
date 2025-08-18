# ADR-002 — Elección de Koin para Inyección de Dependencias

## Contexto

A medida que la aplicación crece, la creación manual de dependencias (como `ApiClient`, `UserRepository`, `GetUsersUseCase`) se vuelve compleja y propensa a errores. Se necesita un mecanismo para gestionar el ciclo de vida y la provisión de estas clases de forma centralizada y escalable en un entorno Kotlin Multiplatform. El proyecto de referencia en Swift utiliza una composición de objetos basada en protocolos, que es una forma de inyección de dependencias manual. Para estandarizar y simplificar este proceso en KMP, se requiere una librería dedicada.

## Decisión

Se utilizará **Koin** como framework de inyección de dependencias para todo el proyecto. Koin es una librería ligera, pragmática y escrita puramente en Kotlin, con soporte completo para Kotlin Multiplatform.

**Implementación:**
1.  Se añadirá la dependencia de Koin al `build.gradle.kts` de los módulos correspondientes (`shared`, `app`, etc.).
2.  Se creará un módulo de Koin en `shared/commonMain` donde se definirán las dependencias compartidas (`ApiClient`, repositorios, casos de uso).
3.  En los puntos de entrada de cada plataforma (la clase `Application` en Android, la función `main` en Desktop), se inicializará Koin con los módulos definidos.
4.  Las dependencias se inyectarán en las clases que las necesiten (por ejemplo, en los `ViewModels`) utilizando los delegados de Koin (`by inject()`).

## Consecuencias

- **Positivas:**
    - Simplifica la creación y provisión de objetos.
    - Centraliza la configuración de las dependencias del proyecto.
    - Mejora la capacidad de realizar pruebas al facilitar el reemplazo de dependencias con mocks.
    - Reduce el código boilerplate relacionado con la instanciación manual.
- **Negativas:**
    - Añade una nueva dependencia al proyecto.
    - Requiere que el equipo se familiarice con la API y los conceptos de Koin.
    - Al ser un localizador de servicios, algunos errores de configuración solo se detectan en tiempo de ejecución (aunque Koin ofrece mecanismos para verificar los módulos).

## Alternativas consideradas

- **Inyección de dependencias manual:** Es lo que se venía haciendo. Aunque no introduce librerías, es menos escalable y más verboso.
- **Hilt/Dagger:** Son el estándar en Android nativo, pero no tienen soporte para Kotlin Multiplatform. Se podrían usar solo en el módulo `:app` de Android, pero esto fragmentaría la estrategia de DI.
- **Kodein-DI:** Otra librería de DI para KMP. Es potente pero su API es considerada generalmente más compleja que la de Koin.

## Plan de reversa

Si Koin demuestra ser problemático, se puede eliminar gradualmente. Dado que las clases no tendrán dependencias directas con Koin (solo el módulo de configuración), se podría volver a un esquema de inyección manual o reemplazarlo por otra librería migrando únicamente los módulos de Koin, sin necesidad de refactorizar masivamente el código de negocio.