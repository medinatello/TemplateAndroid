# ADR-0002 — Esquema de eventos y métricas

- Estado: Aprobado

## Contexto
Requerimos consistencia en nombres y etiquetas de eventos.

## Decisión
Definir taxonomía: `domain.action.result` y etiquetas `userId`, `traceId`.

## Alternativas
- Libres: inconsistente, difícil de explotar.

## Consecuencias
- Mejor analítica; disciplina en nuevos eventos.
