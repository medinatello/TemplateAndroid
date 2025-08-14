# ADR-0001 — Subida/Descarga con reanudación

- Estado: Aprobado

## Contexto
Transferencias grandes con cortes de red frecuentes.

## Decisión
OkHttp multipart para upload y DownloadManager/WorkManager para descargas con reanudación.

## Alternativas
- Streams manuales: más código y errores.

## Consecuencias
- Mejor UX; dependencia en servicios del sistema.
