# MVP-08 — Telemetría y logging (Android)

## Historias de usuario
- Como equipo, quiero observar errores, latencia y flujos críticos.

## Criterios de aceptación
- Logs estructurados (Timber), trazas y métricas básicas (OTel/Crashlytics).

## Entregables
- `Telemetry` façade con exportadores configurables.

## Código de ejemplo
```kotlin
Timber.i("login_success")
// tracer.span("fetch_users").use { /* annotate */ }
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Telemetría
  Scenario: Evento de login registrado
    Given que el usuario inicia sesión
    When se completa el flujo
    Then envío un log info y una traza con traceId
```

## DoR extendido
- Lista de eventos/métricas clave definida.

## DoD extendido
- Dashboards básicos creados; alertas de error configuradas.
