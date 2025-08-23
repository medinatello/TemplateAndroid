# ADR-0002 — Estructura de módulos

- Estado: Aprobado

## Contexto
Se requiere modularidad para escalar features y aislar capas.

## Decisión
Crear `:app`, `:core:ui`, `:core:network`, `:core:datastore` y `:feature:*`. Dependencias unidireccionales (feature → core).

## Alternativas
- Monolítico: rápido al inicio, difícil de mantener.
- Módulos por pantalla: overhead excesivo.

## Consecuencias
- Builds incrementales más rápidas; configuración Gradle inicial mayor.
