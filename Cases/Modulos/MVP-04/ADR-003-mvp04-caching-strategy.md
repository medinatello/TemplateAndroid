# ADR-003 — Estrategia de Caché con SQLDelight

## Contexto

Para mejorar la experiencia de usuario y la resiliencia de la aplicación, es necesario implementar una estrategia de caché para las respuestas de la red. Esto permite mostrar datos al usuario de forma casi instantánea en visitas posteriores y disponer de información incluso en condiciones de conectividad limitada o nula. El proyecto de referencia en Swift implementó un sistema de caché avanzado con SwiftData. Se necesita una solución equivalente en Kotlin Multiplatform.

## Decisión

Se implementará una capa de caché persistente utilizando **SQLDelight**. SQLDelight genera APIs de Kotlin a partir de sentencias SQL, proporcionando una solución de base de datos ligera, segura en tipos y totalmente compatible con Kotlin Multiplatform.

**Estrategia de Implementación:**
1.  **Dependencias:** Se añadirá el plugin y las dependencias de SQLDelight a los módulos correspondientes.
2.  **Esquema SQL:** Se crearán archivos `.sq` en `shared/commonMain` para definir las tablas de la base de datos de caché. Cada tabla incluirá, además de los datos, una columna para el **timestamp de inserción** (para calcular el TTL).
3.  **Queries:** Se escribirán las queries necesarias (INSERT, SELECT, DELETE) en los archivos `.sq`.
4.  **Driver Factory:** Se usará el patrón `expect/actual` para proporcionar el `SqlDriver` específico de cada plataforma (Android, JVM).
5.  **CacheService:** Se creará una interfaz `CacheService` en `commonMain` y una implementación `SqlDelightCacheService` que usará las APIs generadas por SQLDelight para interactuar con la base de datos.
6.  **TTL (Time-To-Live):** La lógica para determinar si un dato cacheado ha expirado se implementará en el `CacheService`. Se consultará el timestamp y se comparará con el TTL configurado para ese tipo de dato.
7.  **Integración:** El `UserRepository` se modificará para consultar primero el `CacheService`. Si hay datos válidos, se devuelven. Si no, se consulta la red, se guardan los nuevos datos en el caché y luego se devuelven.

## Consecuencias

- **Positivas:**
    - Proporciona una base de datos SQL compartida y segura en tipos para todas las plataformas.
    - Desacopla la lógica de caché de la capa de red.
    - Mejora significativamente el rendimiento percibido por el usuario.
    - Permite el funcionamiento básico de la app sin conexión.
- **Negativas:**
    - Incrementa la complejidad de la capa de datos.
    - Requiere escribir SQL, aunque SQLDelight valida la sintaxis en tiempo de compilación.
    - Aumenta ligeramente el tamaño de la aplicación.

## Alternativas consideradas

- **Room:** Es el estándar en Android, pero no tiene soporte para KMP. Se podría usar solo en Android, pero el objetivo es un caché compartido.
- **Realm:** Es una base de datos multiplataforma, pero es más pesada y compleja que SQLDelight, introduciendo un modelo de objetos propio (RealmObjects).
- **Ktor Client Caching:** Ktor tiene un plugin de caché HTTP, pero es en memoria y no persistente, por lo que no sobrevive al cierre de la aplicación.
- **Settings/DataStore:** Adecuados para pares clave-valor simples, pero no para almacenar y consultar colecciones de objetos complejos.

## Plan de reversa

La lógica de caché estará encapsulada detrás de la interfaz `CacheService`. Si SQLDelight resulta inadecuado, se puede escribir una nueva implementación de `CacheService` (por ejemplo, una que no haga nada o que use otra tecnología) y reemplazarla a través de Koin sin afectar a los repositorios o casos de uso que dependen de ella.