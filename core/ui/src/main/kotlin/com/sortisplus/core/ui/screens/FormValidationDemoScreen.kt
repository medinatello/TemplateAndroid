package com.sortisplus.core.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.sortisplus.core.ui.components.SortisEmailTextField
import com.sortisplus.core.ui.components.SortisPasswordTextField
import com.sortisplus.core.ui.components.SortisPrimaryButton
import com.sortisplus.core.ui.components.SortisTextField
import com.sortisplus.core.ui.theme.Dimensions
import com.sortisplus.core.ui.theme.SortisPreviewTheme
import com.sortisplus.core.ui.validation.ValidationResult
import com.sortisplus.core.ui.validation.Validators
import com.sortisplus.core.ui.validation.and

/**
 * Form Validation Demo Screen - T-007: Pantallas Demo y Showcase
 * Demonstrates the validation system in action
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormValidationDemoScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    var isSubmitted by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // Validators
    val nameValidator = Validators.required("Name is required") and
                       Validators.minLength(2, "Name must be at least 2 characters")
    val emailValidator = Validators.required("Email is required") and Validators.email()
    val passwordValidator = Validators.required("Password is required") and
                           Validators.minLength(8, "Password must be at least 8 characters") and
                           Validators.password(
                               minLength = 8,
                               requireUppercase = true,
                               requireLowercase = true,
                               requireDigit = true
                           )

    fun validateFirstName(): Boolean {
        val result = nameValidator.validate(firstName)
        firstNameError = if (result is ValidationResult.Invalid) result.errorMessage else null
        return result.isValid
    }

    fun validateLastName(): Boolean {
        val result = nameValidator.validate(lastName)
        lastNameError = if (result is ValidationResult.Invalid) result.errorMessage else null
        return result.isValid
    }

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

    fun validateConfirmPassword(): Boolean {
        val confirmValidator = Validators.required("Please confirm your password") and
                              Validators.match(password, "Passwords do not match")
        val result = confirmValidator.validate(confirmPassword)
        confirmPasswordError = if (result is ValidationResult.Invalid) result.errorMessage else null
        return result.isValid
    }

    fun validateForm(): Boolean {
        val isFirstNameValid = validateFirstName()
        val isLastNameValid = validateLastName()
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        val isConfirmPasswordValid = validateConfirmPassword()

        return isFirstNameValid && isLastNameValid && isEmailValid &&
               isPasswordValid && isConfirmPasswordValid
    }

    fun handleSubmit() {
        if (validateForm()) {
            isSubmitted = true
            focusManager.clearFocus()
            // In real app, this would submit the form
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Form Validation Demo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimensions.screenPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimensions.inputSpacing)
        ) {
            Text(
                text = "Registration Form",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = Dimensions.paddingMedium)
            )

            Text(
                text = "This form demonstrates real-time validation with different rules for each field.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isSubmitted) {
                androidx.compose.material3.Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "✅ Form submitted successfully!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(Dimensions.cardPadding)
                    )
                }
            }

            // First Name
            SortisTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    if (firstNameError != null) validateFirstName()
                },
                label = "First Name",
                placeholder = "Enter your first name",
                isError = firstNameError != null,
                errorMessage = firstNameError,
                imeAction = ImeAction.Next,
                contentDescription = "First name input field"
            )

            // Last Name
            SortisTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    if (lastNameError != null) validateLastName()
                },
                label = "Last Name",
                placeholder = "Enter your last name",
                isError = lastNameError != null,
                errorMessage = lastNameError,
                imeAction = ImeAction.Next,
                contentDescription = "Last name input field"
            )

            // Email
            SortisEmailTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (emailError != null) validateEmail()
                },
                label = "Email",
                placeholder = "example@domain.com",
                isError = emailError != null,
                errorMessage = emailError,
                imeAction = ImeAction.Next,
                contentDescription = "Email input field"
            )

            // Password
            SortisPasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError != null) validatePassword()
                    // Re-validate confirm password if it has a value
                    if (confirmPassword.isNotEmpty() && confirmPasswordError != null) {
                        validateConfirmPassword()
                    }
                },
                label = "Password",
                placeholder = "Enter a strong password",
                isError = passwordError != null,
                errorMessage = passwordError,
                imeAction = ImeAction.Next,
                contentDescription = "Password input field"
            )

            // Confirm Password
            SortisPasswordTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (confirmPasswordError != null) validateConfirmPassword()
                },
                label = "Confirm Password",
                placeholder = "Confirm your password",
                isError = confirmPasswordError != null,
                errorMessage = confirmPasswordError,
                imeAction = ImeAction.Done,
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { handleSubmit() }
                ),
                contentDescription = "Confirm password input field"
            )

            // Submit Button
            SortisPrimaryButton(
                text = if (isSubmitted) "Submitted ✓" else "Submit Form",
                onClick = { handleSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimensions.paddingLarge),
                enabled = !isSubmitted,
                contentDescription = "Submit form button"
            )

            // Reset Button
            if (isSubmitted) {
                com.sortisplus.core.ui.components.SortisOutlinedButton(
                    text = "Reset Form",
                    onClick = {
                        firstName = ""
                        lastName = ""
                        email = ""
                        password = ""
                        confirmPassword = ""
                        firstNameError = null
                        lastNameError = null
                        emailError = null
                        passwordError = null
                        confirmPasswordError = null
                        isSubmitted = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = "Reset form button"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormValidationDemoScreenPreview() {
    SortisPreviewTheme {
        FormValidationDemoScreen(
            onNavigateBack = { }
        )
    }
}
