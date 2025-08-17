# ADR-001 — Elección de Ktor Client para la capa de red

## Contexto

Se necesita una capa de comunicación HTTP compartida entre Android, escritorio y otros futuros targets. La versión anterior usaba un cliente específico de Android (por ejemplo, Retrofit con OkHttp), lo cual no es portable a multiplataforma.

## Decisión

Utilizar **Ktor Client** en el módulo `shared` para todas las peticiones HTTP. Ktor ofrece:

- Compatibilidad con Kotlin Multiplatform.
- Soporte de serialización con `kotlinx.serialization`.
- Plugins para logging, timeouts y reintentos.
- Motores específicos por plataforma (`OkHttp` en Android, `CIO` en JVM Desktop).

## Consecuencias

- La lógica de red se centraliza y puede probarse en `commonTest`.
- Se reduce la dependencia de frameworks exclusivos de Android.
- Requiere migrar modelos a `kotlinx.serialization` y adaptar las APIs existentes.

## Alternativas consideradas

- **Retrofit/OkHttp**: solo funciona en Android. No viable para multiplataforma.
- **Fuel**: librería HTTP multiplataforma pero menos madura y con menor adopción.

## Plan de reversa

Si Ktor presenta limitaciones insalvables (rendimiento, soporte de protocolos), se encapsulará la creación del cliente en una interfaz y se podrá sustituir por otra implementación en un MVP futuro sin afectar el código de dominio.