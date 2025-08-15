# MVP-04 — Cliente HTTP y API (Android)

## Historias de usuario
- Como usuario, quiero ver datos remotos (lista de usuarios).

## Criterios de aceptación
- Cliente Retrofit/Ktor con interceptores de logging y auth.
- Manejo de errores unificado (`Result<T>`/sealed).

## Entregables
- `ApiService`, `NetworkModule`, mapeo DTO→domain.

## Comandos
- `./gradlew :core:network:test`

## Código de ejemplo
```kotlin
interface Api { @GET("/users") suspend fun users(): List<UserDto> }
val api = Retrofit.Builder().baseUrl(BASE).build().create(Api::class.java)
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Listado de usuarios
  Scenario: Carga exitosa
    Given que tengo conectividad
    When consulto /users
    Then veo la lista decodificada
  Scenario: Error 401
    Given que mi token expiró
    When consulto /users
    Then recibo un mensaje de reautenticación
```

## DoR extendido
- Esquema de errores HTTP→dominio acordado.
- DTOs y endpoints priorizados.

## DoD extendido
- MockWebServer cubre 200/401/500 y timeouts.
- Retries/backoff parametrizados y testeados.
