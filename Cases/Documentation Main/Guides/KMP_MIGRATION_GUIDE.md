# Guía de Migración a Kotlin Multiplatform

Esta guía resume los pasos y consideraciones para migrar un proyecto Android existente a **Kotlin Multiplatform (KMP)** manteniendo las mejores prácticas de arquitectura y calidad.

## Objetivos

- Compartir la lógica de negocio, acceso a datos y networking entre Android y Desktop (Windows/Linux/macOS).
- Mantener una arquitectura limpia y modular basada en capas (presentación, dominio, datos).
- Minimizar la duplicación de código y la divergencia de comportamientos entre plataformas.

## Requisitos previos

- Conocimiento básico de Gradle y del plugin **Kotlin Multiplatform**.
- Familiaridad con las librerías KMP: **Ktor**, **SQLDelight**, **Multiplatform‑Settings** y **Koin**.

## Pasos principales

1. **Crear el módulo `shared`** usando el plugin `kotlin-multiplatform`, declarando los targets `androidTarget()` y `jvm("desktop")`.  
2. **Configurar los `sourceSets`** para `commonMain`, `commonTest`, `androidMain`, `androidUnitTest`, `desktopMain` y `desktopTest`.  
3. **Agregar dependencias multiplataforma** en `commonMain`:  
   - `ktor-client-core`, `ktor-client-content-negotiation`, `ktor-client-logging`.  
   - `sqldelight` (runtime y coroutines).  
   - `multiplatform-settings`.  
   - `koin-core`.  
4. **Definir superficies `expect/actual`** para funcionalidades que requieran implementaciones específicas por plataforma, como almacenamiento clave-valor, fecha/hora, drivers de base de datos y motores de HTTP.  
5. **Migrar la lógica compartida** de los módulos existentes (`core`, casos de uso, repositorios) a `commonMain`, adaptando las dependencias a las nuevas librerías multiplataforma.  
6. **Configurar un módulo `desktopApp`** que utilice Compose Multiplatform para validar la configuración multiplataforma con una aplicación “Hola Mundo”.  
7. **Ajustar la app de Android** (`androidApp`) para consumir el módulo `shared`, manteniendo Hilt sólo en la capa de UI y utilizando Koin en la lógica compartida.  
8. **Implementar pruebas unitarias** en `commonTest` y pruebas de integración en cada plataforma para asegurar una cobertura mínima del 80 %.

## Buenas prácticas

- **Mantener la arquitectura limpia** separando presentación, dominio y datos.  
- **Usar interfaces y dependencias inyectables** para aislar la lógica de negocio y facilitar las pruebas.  
- **Documentar las decisiones** mediante registros de arquitectura (ADRs) ubicados dentro de cada MVP.  
- **Automatizar la calidad** con herramientas como ktlint, Detekt y pipelines de CI que ejecuten pruebas y calculen la cobertura.  
- **Iterar de forma incremental**: migra pequeñas partes y valida con pruebas y builds en cada paso.

## Referencias

Para más detalles y ejemplos de implementación, consulta la documentación oficial de cada librería:  
- [Documentación de Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)  
- [Guía de Ktor Client](https://ktor.io/docs/getting-started-ktor-client.html)  
- [SQLDelight](https://cashapp.github.io/sqldelight/)  
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings)  
- [Koin para KMP](https://insert-koin.io/docs/reference/koin-core/injection-in-kmm/)
