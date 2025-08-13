# ADR-0002 — Estrategia de navegación

- Estado: Aprobado

## Contexto
Se requiere routing claro entre pantallas con paso de parámetros.

## Decisión
Rutas tipadas en Navigation-Compose; un único `NavHost` con gráficas por feature.

## Alternativas
- Deep links manuales: propenso a errores.

## Consecuencias
- Navegación predecible; fácil de testear.
