# Gemini Project: TemplateAndroid

## Project Overview

This is a multi-module Android application template. It serves as a foundation for new Android projects, providing a clear and scalable architecture.

The project is written in Kotlin and uses modern Android development practices, including:

*   **Jetpack Compose:** For building the user interface.
*   **Kotlin DSL:** For Gradle build scripts.
*   **Multi-module architecture:** To separate concerns and improve maintainability.
*   **Product Flavors:** To manage different build variants (dev, stg, prd).
*   **Dependency Management:** Using a centralized `libs.versions.toml` file.

### Modu

*   **`:app`**: The main application module.
*   **`:core`**: Contains core functionalities shared across the application.
    *   **`:core:common`**: Common utilities and extensions.
    *   **`:core:data`**: Data layer components.
    *   **`:core:database`**: Room database implementation.
    *   **`:core:datastore`**: Jetpack DataStore implementation.
    *   **`:core:designsystem`**: UI components and themes.
    *   **`:core:network`**: Networking layer (e.g., Retrofit).
    *   **`:core:ui`**: Base UI components and utilities.
*   **`:data`**: Data modules.
    *   **`:data:local`**: Local data source implementation.
*   **`:feature`**: Feature modules.
    *   **`:feature:home`**: Home screen feature.

## Building and Running

### Building the project

To build the project, you can use the following Gradle command:

```bash
./gradlew build
```

### Running the application

To install and run the application on a connected device or emulator, use:

```bash
./gradlew installDebug runApp
```

### Running tests

To run unit tests, use:
```bash
./gradlew test
```

To run instrumented tests, use:
```bash
./gradlew connectedAndroidTest
```

## Development Conventions

### Git

This project uses a squash merge strategy for merging feature branches into `dev`. A helper script is provided at `scripts/git-squash-merge.sh`.

### Diagnostics

The project includes two diagnostic tasks to help troubleshoot common issues:

*   `./gradlew diagnoseRunConfiguration`: To diagnose why the application may not be runnable in Android Studio.
*   `./gradlew diagnoseModuleVisibility`: To diagnose why a module may not be visible in Android Studio.
