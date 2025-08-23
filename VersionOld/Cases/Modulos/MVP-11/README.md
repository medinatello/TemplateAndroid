# MVP-11 — Sprint 11

Este módulo corresponde al **MVP 11** del proyecto.  A estas alturas del
desarrollo, se busca consolidar las funcionalidades ya implementadas y
preparar la aplicación para futuras ampliaciones.  Se parte del trabajo
realizado hasta el MVP 10, manteniendo la consistencia arquitectónica y la
calidad del código.

## Objetivo principal

Implementar las historias de usuario asignadas al sprint 11, integrando
correctamente el módulo `shared` cuando corresponda y asegurando que las
funciones añadidas sigan los **estándares de desarrollo** definidos en la
carpeta `Cases/Documentation Main`.  Todas las nuevas funcionalidades deben
ir acompañadas de pruebas automáticas y documentación.

## Criterios de aceptación (Definition of Done)

1. **Código implementado** siguiendo la arquitectura limpia y las normas
   especificadas en `DEVELOPMENT_STANDARDS.md`.
2. **Pruebas unitarias** y, si corresponde, de integración con cobertura
   mínima del **80 %** en la lógica de negocio añadida en este sprint.
3. **Documentación actualizada**, incluyendo este README, las tareas en
   `TAREAS.md` y la creación de `resultado.md` al finalizar.
4. **Build exitoso** para todas las plataformas afectadas (Android y, si
   aplica, Desktop).  El proyecto debe compilar sin errores.
5. **Pipeline CI verde**: todas las tareas de lint, pruebas y compilación
   deben completarse satisfactoriamente en la integración continua.
6. **Revisión de código aprobada**: los cambios han sido revisados y
   aprobados mediante un pull request.

## Pasos de trabajo

1. **Revisión de contexto**: Lee la documentación en
   `Cases/Documentation Main`, repasa el trabajo realizado hasta el MVP 10
   y estudia las historias de usuario asignadas a este sprint.
2. **Planificación de tareas**: Desglosa las historias en subtareas en
   `TAREAS.md`, definiendo una **Definición de Hecho** para cada una.
3. **Implementación**: Desarrolla las funcionalidades siguiendo la
   arquitectura modular.  Si necesitas lógica compartida,
   colócala en el módulo `shared` y genera los bloques `expect/actual` en los
   submódulos de plataforma.
4. **Pruebas**: Escribe pruebas unitarias y de integración.  Verifica que la
   cobertura supere el 80 % en la lógica añadida.  Ajusta la configuración
   del build si es necesario.
5. **Documentación y CI**: Actualiza la documentación y crea un
   `resultado.md` al completar el sprint.  Ejecuta localmente las tareas de
   CI (`ktlintCheck`, `detekt`, pruebas y build) antes de abrir el PR.

Al completar el sprint, la carpeta `MVP-11` deberá contener todos los
archivos mencionados y servir como referencia para futuros desarrolladores.