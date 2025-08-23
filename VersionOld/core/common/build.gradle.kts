plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.sortisplus.core.common"
    compileSdk = 36

    defaultConfig { minSdk = 29 }

    buildFeatures {
        resValues = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
}

// Clean any packaged_res files with spaces (e.g., "values 2.xml") that can cause duplicate resources
val cleanInvalidPackagedRes by tasks.register("cleanInvalidPackagedRes") {
    group = "verification"
    description = "Deletes files with spaces under build/intermediates/packaged_res to avoid resource merge failures."
    doLast {
        val packagedRoot = file("build/intermediates/packaged_res")
        if (packagedRoot.exists()) {
            val invalidFiles = packagedRoot.walkTopDown()
                .filter { it.isFile && it.name.contains(" ") }
                .toList()
            if (invalidFiles.isNotEmpty()) {
                logger.lifecycle("cleanInvalidPackagedRes(common): Deleting ${invalidFiles.size} packaged resource file(s) with spaces in name:")
                invalidFiles.forEach { f ->
                    logger.lifecycle(" - ${f}")
                    f.delete()
                }
            }
        }
    }
}

// Ensure cleaning runs before parsing/merging resources in this module
tasks.matching { it.name.startsWith("parse") && it.name.endsWith("Resources") || it.name.contains("packageLibResources") || it.name.contains("mergeDebugResources") || it.name.contains("mergeReleaseResources") }.configureEach {
    dependsOn(cleanInvalidPackagedRes)
}

dependencies {
    // vacío por ahora
}
