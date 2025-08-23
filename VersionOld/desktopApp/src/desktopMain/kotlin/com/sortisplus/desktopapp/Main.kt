package com.sortisplus.desktopapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sortisplus.shared.SharedSDK

fun main() = application {
    // Initialize Koin for desktop
    SharedSDK.initializeSDK()
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "Template Android Desktop - KMP Demo"
    ) {
        DesktopApp()
    }
}