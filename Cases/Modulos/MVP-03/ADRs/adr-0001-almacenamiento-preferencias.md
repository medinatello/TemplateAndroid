# ADR-0001 — Preferencias con DataStore

- Estado: Aprobado

## Contexto
Se busca reemplazar SharedPreferences por una opción más segura, moderna y reactiva para el almacenamiento de preferencias no sensibles. El objetivo es mejorar la robustez, la escalabilidad y la integración con la arquitectura moderna de Android.

SharedPreferences presenta limitaciones como condiciones de carrera, falta de soporte para flujos reactivos y menor seguridad. DataStore, en cambio, permite persistencia eficiente, migraciones y mejor integración con Kotlin y corutinas.

## Decisión
Utilizar DataStore (Preferences/Proto) para la configuración y preferencias del usuario que no sean sensibles. Se recomienda documentar las claves utilizadas y definir migraciones desde SharedPreferences si existen datos previos.

## Alternativas consideradas
- SharedPreferences: legado, propenso a condiciones de carrera y menos seguro.
- Room: sobrecarga innecesaria para pares clave/valor simples.
- Archivos planos: poco robustos y sin soporte nativo para migraciones.

## Consecuencias
- Se obtiene un flujo reactivo y menos bugs de concurrencia.
- Mejor integración con arquitectura moderna de Android.
- Facilidad para pruebas unitarias y migraciones.
- Reducción de errores por acceso concurrente.

## Ejemplo de migración
```kotlin
val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
val theme = sharedPrefs.getBoolean("theme_dark", false)
context.dataStore.edit { it[booleanPreferencesKey("theme_dark")] = theme }
```
