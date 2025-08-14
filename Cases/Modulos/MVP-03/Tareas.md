# MVP-03 — Configuración y almacenamiento (Android)

## Historias de usuario
- Como usuario, quiero que la app recuerde mi tema preferido.
- Como sistema, quiero guardar secretos de forma segura.

## Criterios de aceptación
- Preferencias con DataStore funcionales (ej. `theme_dark`).
- Secretos cifrados (EncryptedSharedPreferences/Keystore).

## Entregables
- Wrapper `PreferencesRepository` y `SecureStorage`.

## Comandos
- `./gradlew :app:test`

## Código de ejemplo
```kotlin
val THEME = booleanPreferencesKey("theme_dark")
val ds = context.dataStore
suspend fun setDark(v:Boolean)=ds.edit{it[THEME]=v}
val isDark = ds.data.map{it[THEME]?:false}
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Preferencias de tema
  Scenario: Guardado de preferencia
    Given que activo el modo oscuro en ajustes
    When regreso a la app
    Then la app inicia en modo oscuro
```

## DoR extendido
- Claves de preferencias y migraciones definidas.
- Política de reseteo por defecto acordada.

## DoD extendido
- Pruebas de lectura/escritura y migración verde.
- Secretos nunca en VCS; revisión de logs sin fugas.
