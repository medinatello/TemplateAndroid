# ADR-0001 — SDK de telemetría (OpenTelemetry + Crashlytics)

- Estado: Aprobado

## Contexto
Necesitamos observabilidad estandarizada y reporte de crashes.

## Decisión
Usar OpenTelemetry para trazas/métricas y Crashlytics para caídas.

## Alternativas
- SDKs propietarios únicamente: lock-in.

## Consecuencias
- Configuración inicial mayor; portabilidad ganada.
