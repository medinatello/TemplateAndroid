# ADR-0002 — Almacenamiento de tokens

- Estado: Aprobado

## Contexto
Tokens de acceso/refresh deben protegerse y rotarse.

## Decisión
Guardar tokens en Keystore/EncryptedPrefs, con refresh automático y revocación en logout.

## Alternativas
- Guardar en memoria o DataStore sin cifrado: inseguro.

## Consecuencias
- Manejo de expiración/errores; interceptores dependientes de estado.
