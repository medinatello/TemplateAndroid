#!/usr/bin/env bash
set -euo pipefail

# git-squash-merge.sh
# Hace un squash-merge de una rama fuente hacia una rama destino (por defecto: dev)
# creando un solo commit que resume todos los commits de la rama fuente.
#
# Uso:
#   scripts/git-squash-merge.sh [--from <source-branch>] [--to <target-branch>] [--push] [--dry-run]
#
# Ejemplos:
#   # Desde la rama actual hacia dev, creando un solo commit con resumen
#   scripts/git-squash-merge.sh
#
#   # Desde la rama feature/x hacia dev
#   scripts/git-squash-merge.sh --from feature/x
#
#   # Hacia una rama destino distinta
#   scripts/git-squash-merge.sh --from feature/x --to release/1.0
#
#   # Ejecutar y hacer push automáticamente
#   scripts/git-squash-merge.sh --from feature/x --to dev --push
#
#   # Ensayo sin ejecutar acciones que cambien el repo
#   scripts/git-squash-merge.sh --from feature/x --to dev --dry-run

TARGET_BRANCH="dev"
SOURCE_BRANCH=""
DO_PUSH=false
DRY_RUN=false

function usage() {
  sed -n '1,80p' "$0"
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --from)
      SOURCE_BRANCH="$2"; shift 2 ;;
    --to)
      TARGET_BRANCH="$2"; shift 2 ;;
    --push)
      DO_PUSH=true; shift ;;
    --dry-run)
      DRY_RUN=true; shift ;;
    -h|--help)
      usage; exit 0 ;;
    *)
      echo "Argumento desconocido: $1" >&2; usage; exit 1 ;;
  esac
done

# Asegurar que estamos en un repo git
if ! git rev-parse --git-dir >/dev/null 2>&1; then
  echo "Este script debe ejecutarse dentro de un repositorio Git" >&2
  exit 1
fi

# Detectar rama fuente si no se especifica
if [[ -z "$SOURCE_BRANCH" ]]; then
  SOURCE_BRANCH=$(git rev-parse --abbrev-ref HEAD)
fi

# Validar estado limpio
if [[ -n "$(git status --porcelain)" ]]; then
  echo "Tu working tree no está limpio. Commitea o stash tus cambios antes de continuar." >&2
  exit 1
fi

# Fetch para actualizar refs
run() { echo "+ $*"; $DRY_RUN || eval "$*"; }
run git fetch --all --prune

# Verificar existencia de ramas
if ! git show-ref --verify --quiet "refs/heads/$TARGET_BRANCH" && ! git ls-remote --exit-code --heads origin "$TARGET_BRANCH" >/dev/null 2>&1; then
  echo "La rama destino '$TARGET_BRANCH' no existe localmente ni en remoto." >&2
  exit 1
fi
if ! git show-ref --verify --quiet "refs/heads/$SOURCE_BRANCH" && ! git ls-remote --exit-code --heads origin "$SOURCE_BRANCH" >/dev/null 2>&1; then
  echo "La rama origen '$SOURCE_BRANCH' no existe localmente ni en remoto." >&2
  exit 1
fi

# Asegurar que las ramas están disponibles localmente
if ! git show-ref --verify --quiet "refs/heads/$TARGET_BRANCH"; then
  run git checkout -t "origin/$TARGET_BRANCH"
fi
if ! git show-ref --verify --quiet "refs/heads/$SOURCE_BRANCH"; then
  run git fetch origin "$SOURCE_BRANCH:$SOURCE_BRANCH"
fi

# Calcular base y obtener resumen de commits
MERGE_BASE=$(git merge-base "$TARGET_BRANCH" "$SOURCE_BRANCH")
COMMITS_RANGE="$MERGE_BASE..$SOURCE_BRANCH"

# Si no hay cambios, salir
if [[ -z "$(git log --oneline $COMMITS_RANGE)" ]]; then
  echo "No hay commits nuevos para llevar de '$SOURCE_BRANCH' a '$TARGET_BRANCH'."; exit 0
fi

SUMMARY=$(git log --no-merges --pretty=format:'- %s (%h) — %an' $COMMITS_RANGE)
AUTHORS=$(git log --no-merges --format='%an' $COMMITS_RANGE | sort -u | paste -sd ', ' -)
COUNT=$(git rev-list --count $COMMITS_RANGE)

COMMIT_MSG=$(cat <<EOF
Squash merge: $SOURCE_BRANCH -> $TARGET_BRANCH

Resumen de cambios: $COUNT commit(s)
Autores: $AUTHORS

Detalle de commits:
$SUMMARY
EOF
)

# Mostrar resumen previo
echo "\n====== RESUMEN APLICADO EN EL COMMIT ======"
printf "%s\n" "$COMMIT_MSG"
echo "==========================================\n"

# Cambiar a rama destino, squash-merge y commitear
run git checkout "$TARGET_BRANCH"
# Asegura estar actualizado con el remoto para evitar rechazos al push
if git rev-parse --abbrev-ref --symbolic-full-name @{u} >/dev/null 2>&1; then
  run git pull --ff-only
fi

# Ejecutar el squash merge
if $DRY_RUN; then
  echo "+ git merge --squash $SOURCE_BRANCH"
else
  set +e
  git merge --squash "$SOURCE_BRANCH"
  MERGE_EXIT=$?
  set -e
  if [[ $MERGE_EXIT -ne 0 ]]; then
    echo "Se produjeron conflictos durante el squash-merge. Resuélvelos manualmente y luego ejecuta:"
    echo "  git add -A && git commit -m \"$(echo "$COMMIT_MSG" | head -n1)\" -m \"(ver detalle en el mensaje preparado por el script)\""
    exit $MERGE_EXIT
  fi
fi

# Crear el commit
if $DRY_RUN; then
  echo "+ git commit -m \"(mensaje largo)\""
else
  git commit -m "$(echo "$COMMIT_MSG" | head -n1)" -m "$(echo "$COMMIT_MSG" | tail -n +3)"
fi

# Push opcional
if $DO_PUSH; then
  run git push origin "$TARGET_BRANCH"
else
  echo "\nSquash merge completado en '$TARGET_BRANCH'. Revisa y haz push cuando estés listo:"
  echo "  git push origin $TARGET_BRANCH"
fi
