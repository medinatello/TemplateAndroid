package com.sortisplus.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sortisplus.core.data.DatabaseResult
import com.sortisplus.core.designsystem.AppTheme
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.PrimaryButton
import com.sortisplus.data.local.LocalProviders
import kotlinx.coroutines.launch
import com.sortisplus.core.common.R as CommonR

/**
 * Screen for creating new people with form validation
 * 
 * Provides a comprehensive form for entering person details with real-time validation.
 * Includes fields for name, birth date, weight and handedness preference.
 * 
 * @param onBack Callback function to navigate back to previous screen
 * @param isDarkTheme Current theme state (true for dark, false for light)
 * @param onToggleTheme Callback function to toggle between light and dark theme
 */
@Composable
fun PersonCreateScreen(
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personRepository(context) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDateStr by remember { mutableStateOf("") }
    var weightStr by remember { mutableStateOf("") }
    var isLeftHanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Real-time validation
    val firstNameError = when {
        firstName.isBlank() -> stringResource(CommonR.string.label_first_name) + " " + stringResource(CommonR.string.error_field_required)
        firstName.length > 50 -> stringResource(CommonR.string.error_max_50_chars)
    else -> null
    }
    
    val lastNameError = when {
        lastName.isBlank() -> stringResource(CommonR.string.label_last_name) + " " + stringResource(CommonR.string.error_field_required)
        lastName.length > 50 -> stringResource(CommonR.string.error_max_50_chars)
    else -> null
    }
    
    val weightError = when {
        weightStr.isBlank() -> stringResource(CommonR.string.label_weight) + " " + stringResource(CommonR.string.error_field_required)
        weightStr.toDoubleOrNull() == null -> stringResource(CommonR.string.error_invalid_number)
        (weightStr.toDoubleOrNull() ?: 0.0) <= 0 -> stringResource(CommonR.string.error_greater_than_zero)
        (weightStr.toDoubleOrNull() ?: 0.0) > 1000 -> stringResource(CommonR.string.error_less_than_1000kg)
        else -> null
    }

    val isFormValid = firstNameError == null && lastNameError == null && 
                     weightError == null && birthDateStr.isNotBlank()

    AppScaffold(
        title = stringResource(CommonR.string.person_create_title),
        actions = {
            IconButton(onClick = onToggleTheme) {
                if (isDarkTheme) {
                    Icon(Icons.Filled.LightMode, contentDescription = stringResource(CommonR.string.action_toggle_theme))
                } else {
                    Icon(Icons.Filled.DarkMode, contentDescription = stringResource(CommonR.string.action_toggle_theme))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo Nombre con validación
            OutlinedTextField(
                value = firstName, 
                onValueChange = { firstName = it },
                label = { Text(stringResource(CommonR.string.label_first_name)) },
                supportingText = firstNameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = firstNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Apellido con validación  
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(CommonR.string.label_last_name)) },
                supportingText = lastNameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = lastNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Fecha (mejorado)
            OutlinedTextField(
                value = birthDateStr,
                onValueChange = { birthDateStr = it },
                label = { Text(stringResource(CommonR.string.label_birth_date)) },
                supportingText = { Text(stringResource(CommonR.string.label_birth_date_hint)) },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Peso con validación
            OutlinedTextField(
                value = weightStr,
                onValueChange = { weightStr = it },
                label = { Text(stringResource(CommonR.string.label_weight_kg)) },
                supportingText = weightError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = weightError != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Checkbox zurdo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(checked = isLeftHanded, onCheckedChange = { isLeftHanded = it })
                Text(stringResource(CommonR.string.label_is_left_handed))
            }
            
            // Botón guardar mejorado
            PrimaryButton(
                text = if (isLoading) stringResource(CommonR.string.button_saving) else stringResource(CommonR.string.button_save),
                onClick = {
                    if (!isFormValid) {
                        resultMessage = context.getString(CommonR.string.msg_fix_errors)
                        isError = true
                        return@PrimaryButton
                    }
                    
                    isLoading = true
                    val birthDate = try {
                        // Convertir fecha simple YYYY-MM-DD a timestamp
                        val parts = birthDateStr.split("-")
                        if (parts.size == 3) {
                            val year = parts[0].toInt()
                            val month = parts[1].toInt() - 1 // Calendar months are 0-based
                            val day = parts[2].toInt()
                            java.util.Calendar.getInstance().apply {
                                set(year, month, day)
                            }.timeInMillis
                        } else {
                            birthDateStr.toLongOrNull() ?: System.currentTimeMillis()
                        }
                    } catch (e: Exception) {
                        System.currentTimeMillis() - (25 * 365 * 24 * 60 * 60 * 1000L) // Default: 25 years ago
                    }
                    
                    val weight = weightStr.toDoubleOrNull() ?: 0.0
                    
                    scope.launch {
                        when (val result = repository.create(firstName, lastName, birthDate, weight, isLeftHanded)) {
                            is DatabaseResult.Success -> {
                                resultMessage = context.getString(CommonR.string.msg_person_created, result.data)
                                isError = false
                                // Clear form
                                firstName = ""
                                lastName = ""
                                birthDateStr = ""
                                weightStr = ""
                                isLeftHanded = false
                            }
                            is DatabaseResult.Error -> {
                                resultMessage = context.getString(CommonR.string.msg_error_with_reason, result.exception.message ?: "")
                                isError = true
                            }
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Mensaje de resultado
            resultMessage?.let { 
                Text(
                    text = it,
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
            
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack)
        }
    }
}

// ================================
// COMPOSE PREVIEWS
// ================================

@Preview(name = "Person Create Light")
@Preview(name = "Person Create Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PersonCreateScreenPreview() {
    AppTheme {
        PersonCreateScreen(
            onBack = { },
            isDarkTheme = false,
            onToggleTheme = { }
        )
    }
}