
# MVP-03 — Configuración y almacenamiento (Android)

## Historias de usuario
- Como usuario, quiero que la app recuerde mi tema preferido para mejorar mi experiencia.
- Como sistema, quiero guardar secretos de forma segura para proteger la información sensible.

## Criterios de aceptación
- Las preferencias (ej. `theme_dark`) deben persistir correctamente usando DataStore.
- Los secretos deben almacenarse cifrados y nunca exponerse en el código fuente ni en logs.

## Entregables
- Wrapper `PreferencesRepository` para preferencias.
- Wrapper `SecureStorage` para secretos y tokens.

## Comandos útiles
- Ejecutar pruebas: `./gradlew :app:test`

## Ejemplo de código
```kotlin
val THEME = booleanPreferencesKey("theme_dark")
val ds = context.dataStore
suspend fun setDark(v:Boolean) = ds.edit { it[THEME] = v }
val isDark = ds.data.map { it[THEME] ?: false }
```

## ADRs relevantes
- Revisa las decisiones técnicas en la carpeta `./ADRs/`.

## Ejemplo Gherkin
```gherkin
Feature: Preferencias de tema
  Scenario: Guardado de preferencia
    Given que activo el modo oscuro en ajustes
    When regreso a la app
    Then la app inicia en modo oscuro
```

## Definición de Ready (DoR) extendida
- Claves de preferencias y migraciones documentadas.
- Política de reseteo por defecto acordada y comunicada.

## Definición de Done (DoD) extendida
- Pruebas de lectura/escritura y migración exitosas.
- Verificación de que los secretos nunca se exponen en VCS ni en logs.
