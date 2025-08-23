package com.sortisplus.desktopapp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.sortisplus.desktopapp.ui.LoginScreen
import com.sortisplus.desktopapp.ui.MainScreen
import com.sortisplus.shared.domain.model.AuthState
import com.sortisplus.shared.presentation.viewmodel.AuthenticationViewModel
import org.koin.java.KoinJavaComponent.inject

@Composable
fun DesktopApp() {
    val authViewModel: AuthenticationViewModel by inject(AuthenticationViewModel::class.java)
    val authUiState by authViewModel.uiState.collectAsState()
    
    // Apply dark theme based on settings
    val darkTheme = false // TODO: Integrate with SettingsViewModel if needed
    
    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    ) {
        // Navigate based on authentication state
        when (authUiState.authState) {
            is AuthState.Authenticated -> {
                MainScreen()
            }
            is AuthState.Unauthenticated,
            is AuthState.Loading,
            is AuthState.Error -> {
                LoginScreen()
            }
        }
    }
}