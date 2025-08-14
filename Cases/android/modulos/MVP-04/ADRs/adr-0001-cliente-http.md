# ADR-0001 — Cliente HTTP (Retrofit + OkHttp)

- Estado: Aprobado

## Contexto
Se requieren llamadas REST, interceptores y testabilidad.

## Decisión
Usar Retrofit + OkHttp; Moshi/Kotlinx para JSON.

## Alternativas
- Ktor Client: multiplataforma; menor adopción en equipos Android puros.
- Fuel: más simple, menos ecosistema.

## Consecuencias
- Interceptores poderosos; reflexión/anotaciones.
