package com.sortisplus.core.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.sortisplus.core.ui.components.SortisEmailTextField
import com.sortisplus.core.ui.components.SortisPasswordTextField
import com.sortisplus.core.ui.components.SortisPrimaryButton
import com.sortisplus.core.ui.theme.Dimensions
import com.sortisplus.core.ui.theme.SortisPreviewTheme
import com.sortisplus.core.ui.validation.ValidationResult
import com.sortisplus.core.ui.validation.Validators
import com.sortisplus.core.ui.validation.and

/**
 * Login screen with form validation
 * Demonstrates T-004: Sistema de Validación de Formularios
 */
@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Validators
    val emailValidator = Validators.required() and Validators.email()
    val passwordValidator = Validators.required() and Validators.minLength(6)

    fun validateEmail(): Boolean {
        val result = emailValidator.validate(email)
        emailError = if (result is ValidationResult.Invalid) result.errorMessage else null
        return result.isValid
    }

    fun validatePassword(): Boolean {
        val result = passwordValidator.validate(password)
        passwordError = if (result is ValidationResult.Invalid) result.errorMessage else null
        return result.isValid
    }

    fun validateForm(): Boolean {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        return isEmailValid && isPasswordValid
    }

    fun handleLogin() {
        if (validateForm()) {
            isLoading = true
            // Simulate login process
            // In real app, this would call a use case
            keyboardController?.hide()
            focusManager.clearFocus()

            // Simulate success after delay
            // For demo purposes, always succeed
            isLoading = false
            onNavigateToHome()
        }
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimensions.screenPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Login Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.cardPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Sign in to continue",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Dimensions.paddingSmall)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.paddingLarge),
                verticalArrangement = Arrangement.spacedBy(Dimensions.inputSpacing)
            ) {
                // Email Field
                SortisEmailTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (emailError != null) validateEmail()
                    },
                    label = "Email",
                    placeholder = "Enter your email",
                    isError = emailError != null,
                    errorMessage = emailError,
                    enabled = !isLoading,
                    imeAction = ImeAction.Next,
                    contentDescription = "Email input field"
                )

                // Password Field
                SortisPasswordTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (passwordError != null) validatePassword()
                    },
                    label = "Password",
                    placeholder = "Enter your password",
                    isError = passwordError != null,
                    errorMessage = passwordError,
                    enabled = !isLoading,
                    imeAction = ImeAction.Done,
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = { handleLogin() }
                    ),
                    contentDescription = "Password input field"
                )

                // Login Button
                SortisPrimaryButton(
                    text = "Sign In",
                    onClick = { handleLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimensions.paddingMedium),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && !isLoading,
                    isLoading = isLoading,
                    contentDescription = "Sign in button"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    SortisPreviewTheme {
        LoginScreen(
            onNavigateToHome = { }
        )
    }
}
