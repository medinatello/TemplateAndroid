#!/bin/bash

echo "🔍 Verificando configuración del proyecto TemplateAndroid..."
echo "=================================================="

# Verificar archivos principales
echo "📁 Verificando archivos principales:"
echo -n "build.gradle.kts (raíz): "
if [ -f "build.gradle.kts" ]; then echo "✅ OK"; else echo "❌ FALTA"; fi

echo -n "settings.gradle.kts: "
if [ -f "settings.gradle.kts" ]; then echo "✅ OK"; else echo "❌ FALTA"; fi

echo -n "app/build.gradle.kts: "
if [ -f "app/build.gradle.kts" ]; then echo "✅ OK"; else echo "❌ FALTA"; fi

echo -n "MainActivity.kt: "
if [ -f "app/src/main/java/com/sortisplus/templateandroid/MainActivity.kt" ]; then 
    echo "✅ OK"; 
else 
    echo "❌ FALTA"; 
fi

echo -n "AndroidManifest.xml: "
if [ -f "app/src/main/AndroidManifest.xml" ]; then echo "✅ OK"; else echo "❌ FALTA"; fi

# Verificar configuración
echo ""
echo "⚙️ Verificando configuraciones:"
echo -n "Plugin android.application: "
if grep -q "android.application" app/build.gradle.kts; then echo "✅ OK"; else echo "❌ FALTA"; fi

echo -n "ApplicationId correcto: "
if grep -q "com.sortisplus.templateandroid" app/build.gradle.kts; then echo "✅ OK"; else echo "❌ INCORRECTO"; fi

echo -n "MainActivity en manifest: "
if grep -q "android.intent.action.MAIN" app/src/main/AndroidManifest.xml; then echo "✅ OK"; else echo "❌ FALTA"; fi

# Verificar permisos gradlew
echo ""
echo "🔧 Verificando Gradle:"
echo -n "Permisos gradlew: "
if [ -x "gradlew" ]; then echo "✅ OK"; else echo "❌ SIN PERMISOS"; chmod +x gradlew; echo "✅ CORREGIDO"; fi

echo ""
echo "📋 DIAGNÓSTICO:"
echo "Si todos los elementos están en ✅ OK, entonces el problema es de sincronización en Android Studio."
echo ""
echo "🔄 PASOS PARA SOLUCIONAR:"
echo "1. Cierra Android Studio completamente"
echo "2. Abre ESTA carpeta (TemplateAndroid) en Android Studio"
echo "3. File → Sync Project with Gradle Files"
echo "4. Espera a que termine completamente la sincronización"
echo "5. Run → Edit Configurations → + → Android App"
echo ""
echo "Si el problema persiste, ejecuta:"
echo "./gradlew clean && ./gradlew build"