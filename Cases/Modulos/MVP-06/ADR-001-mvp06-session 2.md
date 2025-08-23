# ADR-001 — Gestión de Sesión e Inactividad

## Contexto
Se requiere una capa de sesión que controle la autenticación del usuario y maneje la inactividad de forma uniforme en todas las plataformas.

## Decisión
Se implementará un **SessionManager** en `shared` que emite estados mediante `Flow`. El temporizador de inactividad se gestionará usando corrutinas y se configurará un timeout configurable. El almacenamiento de estado persistente se realizará con `KeyValueStore` o almacenes seguros.

## Consecuencias
- Permite un control centralizado del ciclo de vida de la sesión.
- Las UIs pueden suscribirse a los cambios de estado.
- Requiere pruebas que simulen el paso del tiempo y múltiples plataformas.

## Alternativas consideradas
- Temporizadores específicos por plataforma (WorkManager, Timer). Menor portabilidad.
- Uso exclusivo de third‑party libs. Preferimos solución integrada.

## Plan de reversa
Si `Flow` o corrutinas no satisfacen los requisitos, encapsular el temporizador en una interfaz `expect/actual` para sustituir por implementaciones nativas.