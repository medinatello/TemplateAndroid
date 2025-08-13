# ADR-0002 — Puertas de CI

- Estado: Aprobado

## Contexto
Evitar merges con fallos de calidad.

## Decisión
Bloquear PR si fallan linters, tests o cobertura bajo umbral; publicar artefactos.

## Alternativas
- Sin gates: velocidad a costa de calidad.

## Consecuencias
- Menor riesgo en main; feedback temprano.
