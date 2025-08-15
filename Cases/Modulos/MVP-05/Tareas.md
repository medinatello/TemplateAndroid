# MVP-05 — Autenticación y autorización (Android)

## Historias de usuario
- Como usuario, quiero iniciar sesión con mi cuenta (OIDC).

## Criterios de aceptación
- Flujo Auth Code + PKCE con AppAuth/Credential Manager.
- Tokens seguros (Keystore), refresh automático, logout seguro.

## Entregables
- `AuthRepository`, `AuthInterceptor`, manejo de scopes.

## Código de ejemplo (esqueleto)
```kotlin
val service = AuthorizationService(context)
// construir AuthorizationRequest y lanzar intent; manejar redirect en Activity
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Inicio de sesión OIDC
  Scenario: Login exitoso
    Given que inicio el flujo con PKCE
    When el IdP retorna el code
    Then intercambio por tokens y navego a Home
```

## DoR extendido
- Datos del IdP (issuer, clientId, redirect, scopes) confirmados.

## DoD extendido
- Refresh token automático probado; logout borra credenciales.
- Tokens cifrados en Keystore; sin fugas en logs.
