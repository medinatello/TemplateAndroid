# ADR-0002 — Enrutamiento por deep link

- Estado: Aprobado

## Contexto
Permitir abrir pantallas específicas desde notificaciones/URLs.

## Decisión
Usar `NavDeepLinkBuilder` con rutas tipadas y validación de parámetros.

## Alternativas
- Parsing manual de URIs: propenso a errores.

## Consecuencias
- Navegación consistente; casos edge a cubrir en tests.
