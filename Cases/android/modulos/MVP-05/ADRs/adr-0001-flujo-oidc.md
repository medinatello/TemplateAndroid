# ADR-0001 — Flujo OIDC (Auth Code + PKCE)

- Estado: Aprobado

## Contexto
Requerimos autenticación estándar segura para apps móviles.

## Decisión
Usar AppAuth con flujo Authorization Code + PKCE y Credential Manager.

## Alternativas
- Password grant (obsoleto), login embebido (riesgo).

## Consecuencias
- Configuración del IdP y callbacks; mayor seguridad y compatibilidad.
