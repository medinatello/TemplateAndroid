plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.sortisplus.templateandroid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sortisplus.templateandroid"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug") // Cambiar en producción
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

// Automatically sanitize invalid Android resource names (e.g., files with spaces) before building
// This prevents build failures like: Failed file name validation for .../drawable/ic_launcher_background 2.xml
val sanitizeRes by tasks.register("sanitizeRes") {
    group = "verification"
    description = "Deletes invalid Android resource files that contain spaces in their names under src/**/res/."
    doLast {
        val root = file("src")
        if (root.exists()) {
            val invalidFiles = root.walkTopDown()
                .filter { it.isFile && it.path.contains("/res/") && it.name.contains(" ") }
                .toList()
            if (invalidFiles.isNotEmpty()) {
                logger.lifecycle("sanitizeRes: Deleting ${invalidFiles.size} invalid resource file(s) with spaces in name:")
                invalidFiles.forEach { f ->
                    logger.lifecycle(" - ${f}")
                    f.delete()
                }
            } else {
                logger.lifecycle("sanitizeRes: No invalid resource files found.")
            }
        }
    }
}

// Also clean any previously generated packaged_res files with spaces (from older builds)
val cleanInvalidPackagedRes by tasks.register("cleanInvalidPackagedRes") {
    group = "verification"
    description = "Deletes any files with spaces under build/intermediates/packaged_res to avoid parse failures."
    doLast {
        val packagedRoot = file("build/intermediates/packaged_res")
        if (packagedRoot.exists()) {
            val invalidFiles = packagedRoot.walkTopDown()
                .filter { it.isFile && it.name.contains(" ") }
                .toList()
            if (invalidFiles.isNotEmpty()) {
                logger.lifecycle("cleanInvalidPackagedRes: Deleting ${invalidFiles.size} packaged resource file(s) with spaces in name:")
                invalidFiles.forEach { f ->
                    logger.lifecycle(" - ${f}")
                    f.delete()
                }
            }
        }
    }
}

// Ensure sanitization runs before any build and parsing tasks clean intermediates
// preBuild happens before resource processing, parsing tasks depend on cleaning intermediates
tasks.named("preBuild").configure {
    dependsOn(sanitizeRes)
}

tasks.matching { it.name.startsWith("parse") && it.name.endsWith("Resources") }.configureEach {
    dependsOn(cleanInvalidPackagedRes)
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:home"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}