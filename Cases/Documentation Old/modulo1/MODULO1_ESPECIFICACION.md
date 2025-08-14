# [ANDROID][MVP-01] UI básica — Especificación y Planificación

> Proyecto: **Android base**  
> Módulo: **[ANDROID][MVP-01] UI básica**  
> Plataforma: **Android 14 (targetSdk 34)** · **minSdk 26** · **Kotlin + Jetpack Compose**

---

## 1. Propósito y Alineación

**Objetivo**: Crear el esqueleto de una app Android modular con **UI mínima**, navegación básica y **sistema de diseño centralizado**, sin persistencia ni red. Este módulo sienta las bases (estándares, estructura y tooling interno nativo) para crecer de forma segura en los siguientes MVPs.

**Drivers de diseño**:
- Nativo y moderno (Compose, Material 3, Navigation Compose).
- Modular desde el día 1 para facilitar **mantenimiento**, **pruebas**, **escalado** y **reemplazo de capas**.
- Dependencias externas: **cero** en este MVP (salvo librerías oficiales AndroidX/Google). Se prioriza lo nativo.
- Documentación y orden: definimos una **plantilla** para historias de usuario, desglose de tareas, criterios de aceptación y flujo de trabajo.

**Fuera de alcance (MVP-01)**:
- Persistencia (DB/Datastore/Preferences).
- Llamadas a red o sincronización.
- Telemetría y DI avanzadas (se dejan los ganchos preparados).
- Testing a profundidad (solo esqueleto mínimo de pruebas).

---

## 2. Historias de Usuario y Criterios de Aceptación

### HU-001: Navegación básica entre pantallas
**Como** usuario inicial de la app  
**quiero** ver una pantalla de inicio con un botón que me lleve a una segunda pantalla  
**para** validar que la app navega correctamente y respeta el diseño base.

**Criterios de aceptación (Gherkin)**:
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

### HU-002: Sistema de diseño centralizado
**Como** desarrollador del proyecto  
**quiero** definir temas, colores y tipografía en un módulo **:core:designsystem**  
**para** reutilizar estilos y asegurar consistencia visual en todas las features.

**Criterios de aceptación (Gherkin)**:
```gherkin
Dado el módulo :core:designsystem
Cuando se aplica el tema de la app
Entonces los colores, tipografía y formas vienen de un solo origen (Material 3)
Y los componentes de UI usan ese tema sin redefinir estilos locales
```

---

## 3. Plan de Implementación y Checklist

### Fases de Desarrollo
1.  **Configuración inicial (1 día)**: Crear proyecto, configurar Gradle y dependencias de Compose.
2.  **Implementación de Tema (1 día)**: Crear esquema de colores, tipografía y estilos en `:core:designsystem`.
3.  **Desarrollo de Componentes Base (2 días)**: Implementar botones, scaffolds y contenedores reutilizables en `:core:ui`.
4.  **Implementación de Pantallas (3 días)**: Desarrollar `HomeScreen` y `DetailsScreen` en `:feature:home`.
5.  **Pruebas y Ajustes (2 días)**: Escribir pruebas de UI básicas y verificar criterios de aceptación.

### Checklist General
- [ ] Definir objetivo claro del MVP-01.
- [ ] Implementar `HomeScreen` y `DetailsScreen`.
- [ ] Configurar navegación entre pantallas con Navigation Compose.
- [ ] Implementar tema Material 3 (Light/Dark mode).
- [ ] Crear componentes UI reutilizables.
- [ ] Escribir pruebas básicas para navegación y UI.
- [ ] Verificar todos los criterios de aceptación Gherkin.
- [ ] Documentar decisiones de diseño y lecciones aprendidas.
