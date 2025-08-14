# ADR-0002 — Límites y formatos de archivos

- Estado: Aprobado

## Contexto
Controlar tamaño, tipos MIME y almacenamiento seguro.

## Decisión
Limitar a 50MB por archivo, MIME permitidos (pdf,jpg,png), checksum opcional.

## Alternativas
- Sin límites: riesgo de recursos y UX.

## Consecuencias
- Validaciones adicionales y mensajes claros a usuario.
