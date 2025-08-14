# ADR-0001 — Preferencias con DataStore

- Estado: Aprobado

## Contexto
Reemplazar SharedPreferences por una opción segura y reactiva.

## Decisión
Usar DataStore (Preferences/Proto) para configuración no sensible.

## Alternativas
- SharedPreferences: legacy, condiciones de carrera.
- Room: sobrecarga para simples pares clave/valor.

## Consecuencias
- Flujo reactivo, menos bugs de concurrencia.
