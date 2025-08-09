# Criterios de Aceptación (Gherkin)

```gherkin
Dado que abro la app por primera vez
Cuando se renderiza la pantalla Home
Entonces veo un título de la app, un texto descriptivo y un botón primario "Continuar"

Dado que estoy en Home
Cuando toco el botón "Continuar"
Entonces navego a la pantalla Details y veo un texto estático y un botón "Volver"

Dado que estoy en Details
Cuando toco "Volver"
Entonces regreso a Home sin errores ni cierres inesperados
```

## Historia de Usuario Relacionada

### HU-002: Sistema de diseño centralizado
**Como** desarrollador del proyecto  
**quiero** definir temas, colores y tipografía en un módulo **:core:designsystem**  
**para** reutilizar estilos y asegurar consistencia visual en todas las features.
