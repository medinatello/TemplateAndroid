# MVP-10 — Notificaciones y deep links (Android)

## Historias de usuario
- Como usuario, quiero recibir notificaciones y abrir pantallas específicas.

## Criterios de aceptación
- Registro FCM, canales y deep links funcionales.

## Entregables
- `FirebaseMessagingService`, manejador de routing.

## Código de ejemplo
```kotlin
class FcmSvc: FirebaseMessagingService(){ override fun onMessageReceived(m:RemoteMessage){ /* route */ } }
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Notificaciones
  Scenario: Apertura por deep link
    Given que recibo un push con ruta a Detalle
    When toco la notificación
    Then se abre la pantalla Detalle con el id indicado
```

## DoR extendido
- Diseño de payloads y rutas de deep link acordados.

## DoD extendido
- Registro/refresh de token en backend validado.
