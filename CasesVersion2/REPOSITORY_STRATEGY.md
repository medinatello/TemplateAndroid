# Estrategia de Repositorio - Nueva Rama Version-2

## 🎯 Objetivo: Aprovechar repositorio actual con rama limpia

**Estrategia**: Crear rama `version-2` limpia manteniendo historia y documentación valiosa

---

## 📋 PLAN DE IMPLEMENTACIÓN

### **Fase 1: Preparación (15 minutos)**

#### 1.1 **Crear nueva rama desde main**
```bash
# Stash cambios actuales para no perderlos
git stash push -m "WIP: Limpieza V1 - Archivos duplicados eliminados"

# Crear nueva rama desde main (punto limpio)
git checkout main
git pull origin main  # Asegurar última versión
git checkout -b version-2

# La rama version-2 ahora tiene todo el código V1 sin nuestros cambios
```

#### 1.2 **Verificar punto de partida**
```bash
git status  # Debería estar limpio
git log --oneline -5  # Ver últimos commits de main
ls -la     # Ver estructura completa V1
```

### **Fase 2: Limpieza Selectiva (30 minutos)**

#### 2.1 **Mantener documentación valiosa**
```bash
# ✅ MANTENER estos directorios:
Cases/                    # Historia de sprints y ADRs
CasesVersion2/           # Documentación V2 que creamos
README*.md               # Documentación base
CLAUDE.md                # Instrucciones de desarrollo
GEMINI.md                # Instrucciones alternativas
```

#### 2.2 **Eliminar código problemático**
```bash
# ❌ ELIMINAR completamente:
rm -rf app/              # App Android problemática
rm -rf shared/           # Módulo shared con duplicados
rm -rf core/             # Módulos core híbridos
rm -rf feature/          # Features problemáticas
rm -rf data/             # Data layer problemática
rm -rf desktopApp/       # Desktop app problemática

# ❌ ELIMINAR archivos de configuración problemáticos:
rm -rf build.gradle.kts
rm -rf settings.gradle.kts
rm -rf gradle/libs.versions.toml
rm -rf gradle.properties
```

#### 2.3 **Limpiar archivos residuales**
```bash
# Limpiar build artifacts
rm -rf build/
rm -rf .gradle/
rm -rf */build/
rm -rf */.gradle/

# Limpiar archivos temporales
find . -name "*.tmp" -delete
find . -name "*~" -delete
find . -name ".DS_Store" -delete
```

### **Fase 3: Crear estructura V2 limpia (45 minutos)**

#### 3.1 **Estructura base V2**
```bash
mkdir -p {shared,androidApp,desktopApp}
mkdir -p shared/src/{commonMain,commonTest,androidMain,desktopMain}
mkdir -p shared/src/commonMain/kotlin/com/sortisplus/shared/{domain,data,presentation,di,platform}
mkdir -p androidApp/src/main/kotlin/com/sortisplus/templateandroid
mkdir -p desktopApp/src/desktopMain/kotlin/com/sortisplus/desktopapp
```

#### 3.2 **Configuración V2**
```bash
# Copiar configs desde CasesVersion2/templates/ (que crearemos)
cp CasesVersion2/templates/build.gradle.kts ./
cp CasesVersion2/templates/settings.gradle.kts ./
cp CasesVersion2/templates/gradle.properties ./
cp -r CasesVersion2/templates/gradle ./
```

### **Fase 4: Commit inicial V2 (15 minutos)**
```bash
# Commit de limpieza
git add -A
git commit -m "🧹 Clean V1 codebase - Remove problematic code

- Remove all V1 Android/KMP hybrid code
- Keep valuable documentation in Cases/
- Prepare for V2 KMP-pure implementation

BREAKING CHANGE: Complete codebase reset for V2"

# Commit de estructura base
git add -A  
git commit -m "🏗️ Initial V2 KMP structure

- Add shared/ KMP module structure
- Add androidApp/ and desktopApp/
- Add clean V2 gradle configuration
- Ready for domain layer implementation"
```

---

## ✅ VENTAJAS de esta estrategia

### **1. Historia preservada**
```bash
# Siempre podemos ver la historia V1:
git log main              # Historia completa V1
git checkout main         # Volver a V1 si es necesario
git diff main..version-2  # Ver todas las diferencias
```

### **2. Documentación intacta**
- ✅ `Cases/Modulos/MVP-XX/` - Todos los sprints documentados
- ✅ ADRs y decisiones arquitectónicas
- ✅ `DEVELOPMENT_STANDARDS.md` y guías
- ✅ Historia de `planificacion.md` y reviews

### **3. Transición suave**
```bash
# El equipo puede trabajar en paralelo:
git checkout main         # Para referencia V1
git checkout version-2    # Para desarrollo V2
git checkout feature/vuelta-2  # Para recuperar cambios actuales si es necesario
```

### **4. Rollback seguro**
```bash
# Si V2 tiene problemas, siempre podemos volver:
git checkout main
git checkout -b hotfix/v1-urgent-fix
# Seguir trabajando en V1 mientras se resuelve V2
```

---

## 🔄 WORKFLOW DE DESARROLLO

### **Durante desarrollo V2:**
```bash
# Desarrollo normal en version-2
git checkout version-2
git checkout -b feature/v2-domain-layer
# ... desarrollo ...
git commit -m "feat: implement Person domain model"
git checkout version-2
git merge feature/v2-domain-layer
```

### **Cuando V2 esté listo:**
```bash
# Opción 1: Hacer version-2 la nueva rama principal
git checkout version-2
git tag v2.0.0
git push origin version-2

# Opción 2: Merge a main (reemplazar V1)
git checkout main
git merge version-2  # Fast-forward merge
git tag v2.0.0
git push origin main
```

### **Archivar V1:**
```bash
# Crear tag de archivo
git tag archive/v1-final main^1  # Tag el último commit de V1
git push origin archive/v1-final

# V1 quedará disponible para referencia histórica
```

---

## 📊 COMPARACIÓN FINAL

| Aspecto | Nuevo Repositorio | Nueva Rama (Recomendado) |
|---------|-------------------|---------------------------|
| **Setup time** | 2-3 horas | 30 minutos |
| **Historia preservada** | ❌ Se pierde | ✅ Completa |
| **Documentación** | ❌ Hay que migrar | ✅ Intacta |
| **Team disruption** | Alto | Mínimo |
| **Referencias V1** | ❌ Difícil acceso | ✅ `git checkout main` |
| **CI/CD config** | ❌ Reconfigurar todo | ✅ Mantiene configuración |
| **Issues/PRs** | ❌ Se pierden | ✅ Se mantienen |

---

## 🎯 DECISIÓN RECOMENDADA

**Proceder con nueva rama `version-2`** en este repositorio.

**Razones clave**:
1. ⚡ **Eficiencia**: Setup en 30min vs 3h
2. 📚 **Preservación**: Historia y documentación intactas  
3. 👥 **Equipo**: Transición suave sin disruption
4. 🔄 **Flexibilidad**: Fácil comparison y rollback
5. 🎯 **Pragmatismo**: Aprovecha lo bueno, elimina lo malo

¿Procedemos con esta estrategia?