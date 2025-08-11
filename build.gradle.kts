// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt)
}

// Configure Detekt for code quality analysis
detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom("$projectDir/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    allRules = false
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true) 
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

// Custom tasks for code analysis
tasks.register("codeQuality") {
    description = "Run all code quality checks (detekt + tests)"
    group = "verification"
    dependsOn("detektAll")
    // Add test dependencies for modules that have tests
    project.subprojects.forEach { subproject ->
        subproject.tasks.findByName("test")?.let { testTask ->
            dependsOn(testTask)
        }
    }
}

tasks.register("detektAll") {
    description = "Run detekt analysis on all modules"
    group = "verification"
    dependsOn(
        ":app:detekt",
        ":core:common:detekt",
        ":core:data:detekt", 
        ":core:database:detekt",
        ":core:datastore:detekt",
        ":core:designsystem:detekt",
        ":core:ui:detekt",
        ":data:local:detekt",
        ":feature:home:detekt"
    )
}

tasks.register("detektFormat") {
    description = "Run detekt with auto-correct formatting"
    group = "verification"
    dependsOn("detektAll")
    doLast {
        project.subprojects.forEach { subproject ->
            subproject.tasks.findByName("detekt")?.let { task ->
                (task as io.gitlab.arturbosch.detekt.Detekt).autoCorrect = true
            }
        }
    }
}

// Apply Detekt to all subprojects
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    
    detekt {
        toolVersion = rootProject.libs.versions.detekt.get()
        config.setFrom("${rootProject.projectDir}/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
    }
    
    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
    }
}