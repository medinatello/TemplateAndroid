# MVP-11 — i18n, accesibilidad y theming (Android)

## Historias de usuario
- Como usuario, quiero que la app respete el idioma y tamaño de texto.

## Criterios de aceptación
- Claves en `strings.xml`, soporte Dynamic Type y contrastes.

## Entregables
- Recursos de idiomas, chequeos de a11y, temas.

## Código de ejemplo
```xml
<!-- res/values/strings.xml -->
<string name="login">Login</string>
```

## ADRs
- Ver decisiones del sprint en `./ADRs/`.
## Gherkin (ejemplos)
```gherkin
Feature: Accesibilidad
  Scenario: Texto grande
    Given que el usuario configura tamaño de texto máximo
    When abre la app
    Then los controles son legibles y navegables
```

## DoR extendido
- Idiomas objetivo y llaves definidas.

## DoD extendido
- Chequeos de contraste y talkback aprobados.
