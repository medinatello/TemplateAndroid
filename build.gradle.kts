// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// Helper task to diagnose why the Android App might not be runnable in Android Studio
// Usage: ./gradlew diagnoseRunConfiguration
// It checks for common issues: missing app module plugin, missing launcher activity, gradlew permissions.
tasks.register("diagnoseRunConfiguration") {
    group = "verification"
    description = "Diagnose common reasons why Run is not enabled for the Android app."
    doLast {
        val root = project.rootDir
        val appDir = file("app")
        if (!appDir.exists()) {
            println("[ERROR] :app module not found at ${appDir}")
            return@doLast
        }
        val appBuild = file("app/build.gradle.kts")
        if (!appBuild.exists()) {
            println("[ERROR] app/build.gradle.kts not found.")
        } else {
            val text = appBuild.readText()
            val hasAppPlugin = text.contains("android.application")
            val hasNamespace = text.contains("namespace = \"com.sortisplus.templateandroid\"")
            val hasAppId = text.contains("applicationId = \"com.sortisplus.templateandroid\"")
            println("- com.android.application plugin: ${if (hasAppPlugin) "OK" else "MISSING"}")
            println("- namespace configured: ${if (hasNamespace) "OK" else "MISSING"}")
            println("- applicationId configured: ${if (hasAppId) "OK" else "MISSING"}")
        }
        val manifest = file("app/src/main/AndroidManifest.xml")
        if (!manifest.exists()) {
            println("[ERROR] AndroidManifest.xml not found under app/src/main.")
        } else {
            val m = manifest.readText()
            val hasMain = m.contains("android.intent.action.MAIN")
            val hasLauncher = m.contains("android.intent.category.LAUNCHER")
            val hasActivity = m.contains("activity")
            println("- Launcher Activity (MAIN/LAUNCHER): ${if (hasMain && hasLauncher) "OK" else "MISSING"}")
            if (!hasActivity) println("  [HINT] No <activity> tag detected in manifest.")
        }
        val gradlew = file("gradlew")
        println("- gradlew executable: ${if (gradlew.canExecute()) "OK" else "NO"}")
        if (!gradlew.canExecute()) {
            println("  [HINT] Run: chmod +x gradlew")
        }
        println()
        println("If all items are OK but Run is still disabled in Android Studio:")
        println("1) Close Android Studio")
        println("2) Re-open THIS folder (TemplateAndroid) as the project")
        println("3) File > Sync Project with Gradle Files")
        println("4) After sync, select app configuration or create one: Run > Edit Configurations > + > Android App")
    }
}