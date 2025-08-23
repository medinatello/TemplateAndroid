# ADR-0001 — Elegir framework de DI (Hilt vs Koin)

- Estado: Aprobado

## Contexto
Necesitamos inyección de dependencias para desacoplar módulos y facilitar pruebas.

## Decisión
Usar Hilt (sobre Dagger) por integración nativa con Android, scopes claros y tooling.

## Alternativas
- Koin: más simple (DSL), pero sin generación de código ni validación en compile-time.
- Dagger puro: potente pero verboso; Hilt reduce boilerplate.

## Consecuencias
- Curva de aprendizaje moderada; beneficios en testabilidad y mantenimiento.

## Referencias
- https://dagger.dev/hilt/
