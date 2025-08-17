# MVP-04 — Cliente HTTP y API (KMP)

Este MVP se enfoca en implementar la capa de red para la aplicación multiplataforma utilizando Ktor. La implementación se realizará en el módulo `shared` (`commonMain`), permitiendo que tanto Android como Escritorio compartan el mismo código para las llamadas a la API.

## Objetivos Principales

1.  **Implementar Cliente Ktor:** Configurar un cliente Ktor en `commonMain` con las siguientes características:
    *   Serialización con `kotlinx.serialization`.
    *   Logging de peticiones y respuestas.
    *   Manejo de autenticación (ej. interceptor para añadir tokens).
    *   Política de reintentos.

2.  **Definir Modelos de Datos (DTOs):** Crear los Data Transfer Objects en `commonMain` para representar las respuestas de la API.

3.  **Crear Repositorio de Red:** Abstraer las llamadas a la API en un `NetworkRepository` dentro de `commonMain`.

4.  **Manejo de Errores:** Implementar un sistema unificado para manejar errores de red (ej. conectividad, errores del servidor) en el código compartido.
