# ADR-0001 — Timeout de inactividad

- Estado: Aprobado

## Contexto
Riesgo de sesiones abiertas sin supervisión.

## Decisión
Configurar bloqueo por inactividad (por defecto 2 min, configurable) y reautenticación parcial.

## Alternativas
- Sin bloqueo: riesgo de seguridad.

## Consecuencias
- Mejor seguridad, posible fricción; requiere comunicación UX.
