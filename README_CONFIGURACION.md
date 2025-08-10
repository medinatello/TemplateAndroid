# 🛠️ Configuración del Proyecto TemplateAndroid

## ❗ Solución: No aparece opción para depurar app

Si solo aparecen opciones de test en el menú de ejecución, sigue estos pasos:

### 1. 📁 Abre el proyecto correctamente
```bash
# Asegúrate de abrir la carpeta raíz del proyecto (TemplateAndroid)
# NO abrir subcarpetas como app/
```

### 2. 🔄 Sincroniza el proyecto
- En Android Studio: **File → Sync Project with Gradle Files**
- Espera a que termine la sincronización completamente

### 3. 🏗️ Verifica la configuración de Run
- Ve a: **Run → Edit Configurations...**
- Haz clic en **+** → **Android App**
- Configura:
  - **Name**: app
  - **Module**: TemplateAndroid.app.main
  - **Launch Activity**: com.sortisplus.templateandroid.MainActivity

### 4. 🔧 Si persiste el problema
Ejecuta en terminal (desde la raíz del proyecto):
```bash
./gradlew clean
./gradlew build
```

### 5. 📱 Configuración del dispositivo
- Conecta un dispositivo Android o inicia un emulador
- Habilita **Opciones de desarrollador** y **Depuración USB**

## 🗂️ Estructura esperada del proyecto:
```
TemplateAndroid/          ← Abre ESTA carpeta en Android Studio
├── app/
│   ├── build.gradle.kts
│   └── src/main/java/com/sortisplus/templateandroid/MainActivity.kt
├── core/
├── feature/
├── build.gradle.kts      ← Archivo raíz de Gradle
├── settings.gradle.kts   ← Configuración de módulos
└── gradlew              ← Wrapper de Gradle
```

## ✅ Verificaciones
- [ ] El proyecto se abrió desde la carpeta raíz
- [ ] La sincronización de Gradle completó sin errores
- [ ] Existe una configuración de "app" en Run Configurations
- [ ] El dispositivo/emulador está disponible

## 🆘 Problemas comunes

### Error: "Module not specified"
**Solución**: Crear nueva Run Configuration manualmente

### Error: "No target device found"  
**Solución**: Iniciar emulador o conectar dispositivo físico

### Error: "Gradle sync failed"
**Solución**: 
1. `./gradlew clean`
2. Reiniciar Android Studio
3. Re-sincronizar

---

**Si sigues estos pasos, deberías ver la opción "app" en el menú de configuraciones de ejecución y poder depurar normalmente.**