# MVP-06 — Sesión y estado de usuario (Android)

## Historias de usuario
- Como usuario, quiero que la app bloquee tras inactividad.

## Criterios de aceptación
- Temporizador de inactividad configurable (p. ej., 2 min).
- Restauración de sesión al relanzar si no expiró.

## Entregables
- `SessionManager`, integración con `Lifecycle` y `DataStore`.

## Código de ejemplo
```kotlin
class SessionManager { var last = SystemClock.elapsedRealtime()
  fun touch(){ last = SystemClock.elapsedRealtime() }
  fun expired() = SystemClock.elapsedRealtime()-last > TIMEOUT
}
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Bloqueo por inactividad
  Scenario: Auto-bloqueo
    Given que no interactúo por 2 minutos
    When regreso a la app
    Then se solicita reautenticación parcial
```

## DoR extendido
- Tiempo de inactividad y reglas de excepción acordados.

## DoD extendido
- Estados persistentes probados cold/warm start.
- No hay bloqueos falsos positivos bajo multitarea.
