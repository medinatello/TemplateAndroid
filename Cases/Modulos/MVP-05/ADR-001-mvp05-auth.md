# ADR-001 — Estrategia de Autenticación y Autorización

## Contexto
Se requiere implementar un mecanismo de autenticación que funcione tanto en Android como en Desktop y otras plataformas futuras. Debe soportar login, refresh de tokens y logout de manera segura.

## Decisión
Adoptar un flujo **OAuth2/OIDC** utilizando **Ktor Client** para realizar las peticiones a un servidor de identidad. El módulo `shared` definirá un `AuthService` que encapsule la lógica de autenticación. Para el almacenamiento de tokens se usará `KeyValueStore` con implementaciones cifradas por plataforma.

## Consecuencias
- Facilita la integración con proveedores estándar y reutiliza la misma lógica en todas las plataformas.
- Requiere gestión de URIs de redirección específicas para cada plataforma.
- Añade dependencias adicionales de OAuth2/OIDC.

## Alternativas consideradas
- Usar Firebase Auth: no multiplataforma de manera oficial.
- Implementar autenticación propietaria con backend: supone mayor esfuerzo y seguridad delegada a la app.

## Plan de reversa
Si el proveedor seleccionado no satisface los requisitos, encapsular las llamadas en `AuthService` permite sustituirlo por otra implementación sin afectar al dominio.