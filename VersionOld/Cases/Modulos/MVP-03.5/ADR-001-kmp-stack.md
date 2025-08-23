# ADR-001 — Stack Multiplataforma en MVP‑03.5

## Contexto

El proyecto original está acoplado a dependencias exclusivas de Android (Room para base de datos, DataStore para preferencias y Hilt para inyección de dependencias). Para soportar escritorio (Windows, Linux, macOS) y, en el futuro, otras plataformas, es necesario migrar a tecnologías multiplataforma.

## Decisión

- **Networking**: utilizar **Ktor Client** en la capa compartida (`commonMain`).
- **Base de datos**: utilizar **SQLDelight**, definiendo el esquema en `commonMain` y proveyendo drivers específicos para cada plataforma.
- **Preferencias**: utilizar **Multiplatform‑Settings** en lugar de DataStore/SharedPreferences.
- **Inyección de dependencias**: utilizar **Koin** en el módulo `shared`. Hilt se mantiene en la capa de UI de Android y se conecta con Koin mediante un adaptador.
- **UI Desktop**: utilizar **Compose Multiplatform** para crear la aplicación de escritorio mínima.

## Consecuencias

- Permite compartir la lógica de negocio, acceso a datos y networking entre Android y Desktop.
- Reduce la dependencia de APIs exclusivas de Android y disminuye el vendor lock‑in.
- Aumenta la cobertura de pruebas al centralizar la lógica en el módulo compartido.
- Requiere un adaptador para que Hilt (Android) y Koin (shared) coexistan sin conflictos.
- Introduce una curva de aprendizaje para el equipo en cuanto a nuevas librerías y herramientas.

## Alternativas consideradas

- **Mantener Room/DataStore/Hilt**: no es viable para un entorno multiplataforma, ya que estas librerías no cuentan con soporte para escritorio.
- **Utilizar únicamente Hilt**: actualmente no existe un soporte oficial de Hilt para `commonMain`, por lo que esta opción no es posible.

## Plan de reversa

En caso de que la migración a las librerías multiplataforma cause problemas graves o bloquee el desarrollo:

1. Mantener wrappers `expect/actual` alrededor de Ktor, SQLDelight y Koin para encapsular su uso.
2. Implementar temporalmente versiones específicas de Android dentro de `androidMain` mientras se resuelven los problemas.
3. Posponer la migración al siguiente MVP, asegurando que la aplicación Android siga funcionando con las dependencias originales.