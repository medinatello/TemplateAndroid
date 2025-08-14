# ADR-0001 — Estrategia de WorkManager

- Estado: Aprobado

## Contexto
Sincronizaciones confiables respetando batería y conectividad.

## Decisión
Usar WorkManager con constraints (red, batería) y backoff exponencial.

## Alternativas
- AlarmManager/Jobs sin persistencia: frágiles.

## Consecuencias
- Tiempos no deterministas; mayor resiliencia.
