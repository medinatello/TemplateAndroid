# ADR-0002 — Secretos en Keystore/EncryptedPrefs

- Estado: Aprobado

## Contexto
Los tokens y secretos deben persistirse de forma cifrada y segura, evitando fugas y exposición accidental. El almacenamiento seguro es crítico para proteger credenciales, tokens de acceso y otros datos sensibles en dispositivos Android.

## Decisión
Utilizar Android Keystore junto con EncryptedSharedPreferences para el almacenamiento de tokens y secretos. Esta combinación aprovecha el hardware de seguridad del dispositivo y las APIs nativas de Android para garantizar la confidencialidad y la integridad de los datos.

## Alternativas consideradas
- Archivos cifrados personalizados: mayor riesgo de errores de implementación y menor soporte nativo.
- Guardar secretos en DataStore: no recomendado, ya que no está diseñado para datos sensibles.
- Uso de librerías externas: puede aumentar la complejidad y los riesgos de mantenimiento.

## Consecuencias
- Dependencia en hardware/OS, pero se obtiene mayor seguridad y cumplimiento de buenas prácticas.
- Los secretos nunca deben estar en el control de versiones ni en logs.
- Requiere pruebas específicas para asegurar que los datos no se filtran por error.

## Ejemplo de uso
```kotlin
val prefs = EncryptedSharedPreferences.create(
    "secret_prefs",
    masterKeyAlias,
    context,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
prefs.edit().putString("token", "valor_cifrado").apply()
```
