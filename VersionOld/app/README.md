# :app

Propósito: Módulo aplicación; punto de entrada y NavHost raíz.

Incluye:
- MainActivity que aplica AppTheme y define NavHost (Home ↔ Details).

Dependencias:
- :feature:home (pantallas)
- :core:designsystem (tema)
- :core:ui (componentes)
- :core:common (rutas)

Testing:
- UI test smoke en `src/androidTest` para flujo Home→Details.
