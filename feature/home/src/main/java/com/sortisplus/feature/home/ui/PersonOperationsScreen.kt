package com.sortisplus.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sortisplus.core.data.DatabaseResult
import com.sortisplus.core.data.Person
import com.sortisplus.core.designsystem.AppTheme
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.PrimaryButton
import com.sortisplus.data.local.LocalProviders
import kotlinx.coroutines.launch
import com.sortisplus.core.common.R as CommonR

/**
 * Screen for deleting people by ID
 * 
 * @param onBack Callback function to navigate back to previous screen
 * @param isDarkTheme Current theme state (true for dark, false for light)
 * @param onToggleTheme Callback function to toggle between light and dark theme
 */
@Composable
fun PersonDeleteScreen(
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personRepository(context) }
    var idStr by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AppScaffold(
        title = stringResource(CommonR.string.person_delete_title),
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
            OutlinedTextField(
                value = idStr, 
                onValueChange = { idStr = it }, 
                label = { Text(stringResource(CommonR.string.label_person_id)) },
                supportingText = { Text(stringResource(CommonR.string.hint_enter_person_id)) },
                modifier = Modifier.fillMaxWidth()
            )
            
            PrimaryButton(
                text = if (isLoading) stringResource(CommonR.string.button_deleting) else stringResource(CommonR.string.button_delete),
                onClick = {
                    val id = idStr.toLongOrNull()
                    if (id == null) {
                        resultMessage = context.getString(CommonR.string.msg_invalid_id)
                        isError = true
                        return@PrimaryButton
                    }
                    
                    isLoading = true
                    scope.launch {
                        when (val result = repository.delete(id)) {
                            is DatabaseResult.Success -> {
                                if (result.data) {
                                    resultMessage = context.getString(CommonR.string.msg_person_deleted)
                                    isError = false
                                    idStr = "" // Clear field
                                } else {
                                    resultMessage = context.getString(CommonR.string.msg_person_not_found, id)
                                    isError = true
                                }
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

/**
 * Screen for finding people by ID
 * 
 * @param onBack Callback function to navigate back to previous screen
 * @param isDarkTheme Current theme state (true for dark, false for light)
 * @param onToggleTheme Callback function to toggle between light and dark theme
 */
@Composable
fun PersonFindScreen(
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personRepository(context) }
    var idStr by remember { mutableStateOf("") }
    var person by remember { mutableStateOf<Person?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AppScaffold(
        title = stringResource(CommonR.string.person_find_title),
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
            OutlinedTextField(
                value = idStr, 
                onValueChange = { idStr = it }, 
                label = { Text(stringResource(CommonR.string.label_person_id)) },
                supportingText = { Text(stringResource(CommonR.string.hint_enter_person_id_find)) },
                modifier = Modifier.fillMaxWidth()
            )
            
            PrimaryButton(
                text = if (isLoading) stringResource(CommonR.string.button_searching) else stringResource(CommonR.string.button_search),
                onClick = {
                    val id = idStr.toLongOrNull()
                    if (id == null) {
                        errorMessage = context.getString(CommonR.string.msg_invalid_id)
                        person = null
                        return@PrimaryButton
                    }
                    
                    isLoading = true
                    errorMessage = null
                    person = null
                    
                    scope.launch {
                        when (val result = repository.getById(id)) {
                            is DatabaseResult.Success -> {
                                if (result.data != null) {
                                    person = result.data
                                    errorMessage = null
                                } else {
                                    errorMessage = context.getString(CommonR.string.msg_person_not_found, id)
                                    person = null
                                }
                            }
                            is DatabaseResult.Error -> {
                                errorMessage = context.getString(CommonR.string.msg_error_with_reason, result.exception.message ?: "")
                                person = null
                            }
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Mostrar error si existe
            errorMessage?.let { 
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            // Mostrar persona encontrada
            person?.let { p ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(CommonR.string.msg_person_found),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(stringResource(CommonR.string.label_id) + ": ${p.id}")
                    Text(stringResource(CommonR.string.label_name) + ": ${p.firstName} ${p.lastName}")
                    Text(stringResource(CommonR.string.label_age) + ": ${p.age} " + stringResource(CommonR.string.years_unit))
                    Text(stringResource(CommonR.string.label_weight) + ": ${p.weightKg}" + stringResource(CommonR.string.kg_unit))
                    Text(stringResource(CommonR.string.label_left_handed) + ": " + (if (p.isLeftHanded) stringResource(CommonR.string.label_yes) else stringResource(CommonR.string.label_no)))
                }
            }
            
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack)
        }
    }
}

// ================================
// COMPOSE PREVIEWS
// ================================

@Preview(name = "Person Delete Screen")
@Composable
fun PersonDeleteScreenPreview() {
    AppTheme {
        PersonDeleteScreen(
            onBack = { },
            isDarkTheme = false,
            onToggleTheme = { }
        )
    }
}

@Preview(name = "Person Find Screen")
@Composable
fun PersonFindScreenPreview() {
    AppTheme {
        PersonFindScreen(
            onBack = { },
            isDarkTheme = false,
            onToggleTheme = { }
        )
    }
}