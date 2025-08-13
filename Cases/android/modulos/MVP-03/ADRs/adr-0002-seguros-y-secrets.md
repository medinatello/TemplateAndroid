# ADR-0002 — Secretos en Keystore/EncryptedPrefs

- Estado: Aprobado

## Contexto
Tokens y secretos deben persistirse cifrados.

## Decisión
Usar Android Keystore + EncryptedSharedPreferences para tokens/secretos.

## Alternativas
- Archivos cifrados custom: riesgo de implementación.

## Consecuencias
- Dependencia en hardware/OS; mayor seguridad.
