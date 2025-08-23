# TemplateAndroid
Plantillas bases para android

## Squash merge a dev en un solo commit

Si quieres pasar todos los cambios de tu rama actual a `dev` en un único commit (resumen de todos los commits), puedes usar el script:

```
scripts/git-squash-merge.sh
```

Opciones disponibles:
- `--from <rama>`: Rama origen. Si no se especifica, usa la rama actual.
- `--to <rama>`: Rama destino. Por defecto `dev`.
- `--push`: Hace push a `origin` automáticamente.
- `--dry-run`: Muestra los comandos que ejecutaría, sin modificar el repo.

Ejemplos:
- Desde la rama actual hacia `dev` en un único commit:
  ```
  scripts/git-squash-merge.sh
  ```
- Desde `feature/x` hacia `dev` y hacer push:
  ```
  scripts/git-squash-merge.sh --from feature/x --to dev --push
  ```

Notas:
- El working tree debe estar limpio (sin cambios sin commitear o sin stashear).
- Si ocurren conflictos, resuélvelos, haz `git add -A` y luego
  `git commit -m "<título>" -m "<detalle>"` como indica el script.
