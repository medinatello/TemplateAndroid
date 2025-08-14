# MVP-07 — Background y sincronización (Android)

## Historias de usuario
- Como usuario, quiero que mis datos se sincronicen en segundo plano.

## Criterios de aceptación
- WorkManager con constraints (red/batería), backoff exponencial.
- Idempotencia y reintentos controlados.

## Entregables
- `SyncWorker`, `SyncScheduler`, tabla de sync.

## Código de ejemplo
```kotlin
class SyncWorker(ctx: Context, p: WorkerParameters): CoroutineWorker(ctx,p){
  override suspend fun doWork() = try { syncAll(); Result.success() } catch(e:Exception){ Result.retry() }
}
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Sincronización periódica
  Scenario: Sync con red disponible
    Given que hay conectividad WiFi
    When se dispara el worker de sync
    Then los datos quedan actualizados
```

## DoR extendido
- Tabla de entidades y prioridad de sync acordadas.

## DoD extendido
- Reintentos y backoff validados con red inestable.
- Idempotencia comprobada (sin duplicados/conflictos).
