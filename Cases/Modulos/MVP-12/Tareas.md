# MVP-12 — Pruebas y CI/CD (Android)

## Historias de usuario
- Como equipo, quiero calidad automatizada con cobertura ≥80%.

## Criterios de aceptación
- Jobs de `lint`, `test`, `connectedCheck` y publicación de reportes.

## Entregables
- Config de cobertura y gates en CI.

## Comandos
- `./gradlew lint test connectedCheck`

## Código de ejemplo
```kotlin
// build.gradle.kts (snippet jacoco/reporting)
tasks.test { finalizedBy(tasks.jacocoTestReport) }
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Calidad automatizada
  Scenario: Bloqueo de merge por falla en tests
    Given que existe una prueba que falla
    When ejecuto el pipeline
    Then el PR queda bloqueado hasta corregir la prueba
```

## DoR extendido
- Umbrales de cobertura acordados; reglas de linters definidas.

## DoD extendido
- Reportes publicados como artefactos; badges de estado opcionales.
