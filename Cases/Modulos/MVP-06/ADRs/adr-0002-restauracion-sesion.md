# ADR-0002 — Restauración de sesión

- Estado: Aprobado

## Contexto
Mejorar UX manteniendo el usuario autenticado cuando corresponde.

## Decisión
Persistir estado mínimo (usuario/id) y revalidar tokens al relanzar.

## Alternativas
- Restauración completa: más compleja, riesgo de inconsistencias.

## Consecuencias
- Inicio más rápido; lógica adicional de verificación.
