# ADR-0002 — Serialización JSON (Moshi vs Kotlinx)

- Estado: Aprobado

## Contexto
Necesitamos mapear JSON↔modelos con seguridad y rendimiento.

## Decisión
Permitir Moshi o Kotlinx Serialization según preferencia del equipo.

## Alternativas
- Gson: legado, más lento y sin null-safety moderna.

## Consecuencias
- Anotaciones y adaptadores; pruebas de compatibilidad necesarias.
