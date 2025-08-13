# MVP-09 — Archivos: subida/descarga (Android)

## Historias de usuario
- Como usuario, quiero subir y descargar archivos con progreso.

## Criterios de aceptación
- Upload multipart y descarga con reanudación.

## Entregables
- `FileRepository`, UI de progreso, reintentos.

## Código de ejemplo
```kotlin
val part = MultipartBody.Part.createFormData("file", f.name, f.asRequestBody())
api.upload(part)
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Transferencias de archivos
  Scenario: Subida con progreso
    Given que selecciono un archivo de 20MB
    When inicio la subida
    Then veo el progreso y se completa sin errores
```

## DoR extendido
- Tamaños máximos/MIME y políticas de reintento acordadas.

## DoD extendido
- Reanudación validada tras cierre de app.
