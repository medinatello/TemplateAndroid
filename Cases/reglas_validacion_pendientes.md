## Tabla de Resumen de Validación (Pendientes)

| Sprint | Tarea | Método | Estado | Evidencia |
|--------|-------|--------|--------|-----------|
| 5 | Autenticación básica | Test de login, revisión de logs | Faltante | Faltante |
| 6 | Gestión de sesión | Test de expiración, revisión de tokens | Faltante | Faltante |
| 7 | Sincronización de datos | Test de sync, revisión de logs | Faltante | Faltante |
| 8 | Gestión de notificaciones | Test de envío/recepción, revisión de UI | Faltante | Faltante |
| 9 | Subida/descarga de archivos | Test de upload/download, revisión de límites | Faltante | Faltante |
| 10 | Categorías y canales, deeplink | Test de navegación, revisión de rutas | Faltante | Faltante |
| 11 | Soporte de idiomas y accesibilidad | Test de localización, revisión de accesibilidad | Faltante | Faltante |
| 12 | Umbrales de cobertura y CI gates | Revisión de reportes, ejecución de pipeline | Faltante | Faltante |
| 13 | Integración final y QA | Test de integración, checklist QA | Faltante | Faltante |

---

## Sprint 5
### Tarea: Autenticación básica
Manera de validar: Ejecutar test de login con credenciales válidas/erróneas, revisar logs de autenticación.
Criterios de éxito: El usuario accede solo con credenciales válidas, errores se muestran correctamente.
Evidencia: log de autenticación, screenshot de error/success.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Implementar test de login automatizado.
- Habilitar logs de autenticación para pruebas.
Ubicación de código relacionada: `/feature/auth`, `/app/src`, `ADR-001-mvp05-auth.md`

---
## Sprint 6
### Tarea: Gestión de sesión
Manera de validar: Simular expiración de sesión, revisar persistencia y eliminación de tokens.
Criterios de éxito: La sesión expira correctamente, los tokens se eliminan y el usuario es redirigido.
Evidencia: log de expiración, screenshot de redirección.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Implementar test de expiración de sesión.
- Habilitar logs de manejo de tokens.
Ubicación de código relacionada: `/feature/session`, `/app/src`, `ADR-001-mvp06-session.md`

---
## Sprint 7
### Tarea: Sincronización de datos
Manera de validar: Ejecutar test de sincronización, revisar logs y estado de datos antes/después.
Criterios de éxito: Los datos se sincronizan correctamente y los conflictos se resuelven.
Evidencia: log de sync, screenshot de datos sincronizados.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Implementar test de sincronización automatizado.
- Habilitar logs detallados de sync.
Ubicación de código relacionada: `/feature/sync`, `/app/src`, `ADR-001-mvp07-sync.md`

---
## Sprint 8
### Tarea: Gestión de notificaciones
Manera de validar: Enviar y recibir notificaciones, revisar UI y logs.
Criterios de éxito: Las notificaciones se muestran correctamente y se pueden gestionar.
Evidencia: screenshot de notificación, log de recepción.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Implementar test de envío/recepción de notificaciones.
- Habilitar logs de notificaciones.
Ubicación de código relacionada: `/feature/notifications`, `/app/src`

---
## Sprint 9
### Tarea: Subida/descarga de archivos
Manera de validar: Ejecutar test de upload/download, revisar límites y formatos permitidos.
Criterios de éxito: Los archivos se suben/descargan correctamente, se respetan límites y formatos.
Evidencia: log de transferencia, screenshot de archivo.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Implementar test de transferencia de archivos.
- Documentar límites y formatos en README.
Ubicación de código relacionada: `/feature/files`, `/app/src`, `ADRs/adr-0001-subida-descarga.md`, `ADRs/adr-0002-limites-formatos.md`

---
## Sprint 10
### Tarea: Categorías y canales, deeplink
Manera de validar: Navegar usando deeplinks, revisar rutas y categorías en la UI.
Criterios de éxito: Las rutas funcionan y las categorías se muestran correctamente.
Evidencia: screenshot de navegación, log de deeplink.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Implementar test de deeplink y navegación.
- Documentar estructura de categorías y canales.
Ubicación de código relacionada: `/feature/channels`, `/app/src`, `ADRs/adr-0001-categorias-canales.md`, `ADRs/adr-0002-deeplink-routing.md`

---
## Sprint 11
### Tarea: Soporte de idiomas y accesibilidad
Manera de validar: Cambiar idioma en la app, ejecutar Accessibility Scanner y revisar contentDescription.
Criterios de éxito: La app soporta los idiomas definidos y cumple requisitos de accesibilidad.
Evidencia: screenshot de UI en varios idiomas, log de accesibilidad.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Implementar test de localización y accesibilidad.
- Documentar idiomas soportados y requisitos en README.
Ubicación de código relacionada: `/feature/i18n`, `/app/src`, `ADRs/adr-0001-idiomas-soportados.md`, `ADRs/adr-0002-accesibilidad-requisitos.md`

---
## Sprint 12
### Tarea: Umbrales de cobertura y CI gates
Manera de validar: Ejecutar pipeline de CI, revisar reportes de cobertura y gates.
Criterios de éxito: Se cumplen los umbrales definidos y los gates bloquean builds incorrectas.
Evidencia: reporte de cobertura, log de CI.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Configurar umbrales y gates en CI.
- Documentar política de cobertura en README.
Ubicación de código relacionada: `.github/workflows`, `/app`, `/core`, `ADRs/adr-0001-umbrales-cobertura.md`, `ADRs/adr-0002-ci-gates.md`

---
## Sprint 13
### Tarea: Integración final y QA
Manera de validar: Ejecutar test de integración, checklist QA y revisión de entregables.
Criterios de éxito: Todos los módulos funcionan integrados y pasan QA.
Evidencia: log de integración, checklist QA.
Estado de la validación: Faltante
Brechas / acciones necesarias:
- Implementar test de integración final.
- Crear checklist QA por módulo.
Ubicación de código relacionada: `/feature/*`, `/app`, `/core`, `/Cases/Modulos/MVP-13/TAREAS.md`

---

## Checklist QA por Sprint (Pendientes)

### Sprint 5
- [ ] Test de login automatizado
- [ ] Logs de autenticación habilitados

### Sprint 6
- [ ] Test de expiración de sesión
- [ ] Logs de manejo de tokens

### Sprint 7
- [ ] Test de sincronización automatizado
- [ ] Logs detallados de sync

### Sprint 8
- [ ] Test de envío/recepción de notificaciones
- [ ] Logs de notificaciones

### Sprint 9
- [ ] Test de transferencia de archivos
- [ ] Documentar límites y formatos

### Sprint 10
- [ ] Test de deeplink y navegación
- [ ] Documentar categorías y canales

### Sprint 11
- [ ] Test de localización y accesibilidad
- [ ] Documentar idiomas y requisitos

### Sprint 12
- [ ] Configurar umbrales y gates en CI
- [ ] Documentar política de cobertura

### Sprint 13
- [ ] Test de integración final
- [ ] Checklist QA por módulo

---

## Changelog de Archivos Sugeridos para Validación (Pendientes)

- Agregar tests automatizados y logs en `/feature/*`, `/app/src` según cada sprint.
- Documentar límites, formatos, idiomas y requisitos en los README de cada módulo.
- Configurar umbrales y gates en `.github/workflows` y documentar en ADRs.
- Crear checklist QA por módulo en `/Cases/Modulos/MVP-13/TAREAS.md`.
