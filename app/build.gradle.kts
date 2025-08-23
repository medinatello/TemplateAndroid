plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sortisplus.templateandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sortisplus.templateandroid"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "API_BASE_URL", "\"https://api-dev.sortisplus.com\"")
            buildConfigField("String", "FEATURE_FLAGS_JSON", "\"{\\\"enableAnalytics\\\": false, \\\"enableCrashReporting\\\": true}\"")
        }
        create("stg") {
            dimension = "environment"
            applicationIdSuffix = ".stg"
            versionNameSuffix = "-staging"
            buildConfigField("String", "API_BASE_URL", "\"https://api-staging.sortisplus.com\"")
            buildConfigField("String", "FEATURE_FLAGS_JSON", "\"{\\\"enableAnalytics\\\": true, \\\"enableCrashReporting\\\": true}\"")
        }
        create("prd") {
            dimension = "environment"
            versionNameSuffix = "-prod"
            buildConfigField("String", "API_BASE_URL", "\"https://api.sortisplus.com\"")
            buildConfigField("String", "FEATURE_FLAGS_JSON", "\"{\\\"enableAnalytics\\\": true, \\\"enableCrashReporting\\\": true}\"")
        }
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
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeBom.get()
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

// Clean any merged resources in intermediates that accidentally include spaces (e.g., values-hy 2.xml)
val cleanInvalidMergedRes by tasks.register("cleanInvalidMergedRes") {
    group = "verification"
    description = "Deletes any merged resource files with spaces under build/intermediates/incremental/**/merged.dir."
    doLast {
        val incRoot = file("build/intermediates/incremental")
        if (incRoot.exists()) {
            val invalidFiles = incRoot.walkTopDown()
                .filter { it.isFile && it.path.contains("/merged.dir/") && it.name.contains(" ") }
                .toList()
            if (invalidFiles.isNotEmpty()) {
                logger.lifecycle("cleanInvalidMergedRes: Deleting ${invalidFiles.size} merged resource file(s) with spaces in name:")
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
    dependsOn(cleanInvalidPackagedRes)
    dependsOn(cleanInvalidMergedRes)
}

tasks.matching { it.name.startsWith("parse") && it.name.endsWith("Resources") }.configureEach {
    dependsOn(cleanInvalidPackagedRes)
    dependsOn(cleanInvalidMergedRes)
}

dependencies {
    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose Core
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material.icons)

    // ViewModel & Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.runtime.livedata)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Project modules
    implementation(project(":shared"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    // Remove native Android modules - using shared KMP module instead
    // implementation(project(":core:data"))
    // implementation(project(":core:datastore"))  
    // implementation(project(":data:local"))

    // Dependency Injection - Using Koin from shared module
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // UI Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

tasks.preBuild {
    dependsOn(sanitizeRes)
}
