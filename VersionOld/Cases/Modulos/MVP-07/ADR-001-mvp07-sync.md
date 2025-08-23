# ADR-001 — Estrategia de Sincronización en Segundo Plano

## Contexto
La aplicación requiere sincronizar datos periódicamente sin afectar la experiencia del usuario. Cada plataforma maneja trabajos en segundo plano de manera distinta.

## Decisión
Definir una interfaz `SyncScheduler` en `shared` y proveer implementaciones `actual`:
- En Android, usar **WorkManager** con constraints y backoff exponencial.
- En Desktop, utilizar un **ScheduledExecutorService**.

La lógica de sincronización será idempotente y residirá en `shared`.

## Consecuencias
- Proporciona un único punto de entrada para programar sync desde la lógica de dominio.
- Facilita las pruebas y la extensión a otras plataformas.
- Requiere manejar APIs específicas de plataforma en las implementaciones `actual`.

## Alternativas consideradas
- `CoroutineWorker` multiplataforma (no disponible por defecto).
- Bibliotecas de terceros para cron multiplataforma.

## Plan de reversa
Si WorkManager o ScheduledExecutorService no satisfacen los requisitos, el diseño basado en `SyncScheduler` permite sustituir la implementación por otra biblioteca en un MVP futuro.