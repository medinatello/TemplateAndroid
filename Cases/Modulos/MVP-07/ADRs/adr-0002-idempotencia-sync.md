# ADR-0002 — Idempotencia en sincronización

- Estado: Aprobado

## Contexto
Evitar duplicados y estados corruptos en reintentos.

## Decisión
Operaciones idempotentes con claves de de-duplicación y marcas de versión.

## Alternativas
- Reintentos ciegos: riesgo de duplicados.

## Consecuencias
- Reglas de resolución de conflicto adicionales.
