package com.sortisplus.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.sortisplus.core.data.DatabaseResult
import com.sortisplus.core.data.Person
import com.sortisplus.data.local.LocalProviders
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.PrimaryButton
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.sortisplus.core.common.R as CommonR

@Composable
fun HomeScreen(onContinue: () -> Unit) {
    AppScaffold(title = stringResource(CommonR.string.home_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(CommonR.string.home_description))
            PrimaryButton(text = stringResource(CommonR.string.button_continue), onClick = onContinue)
        }
    }
}

@Composable
fun DetailsScreen(onBack: () -> Unit) {
    AppScaffold(title = stringResource(CommonR.string.details_screen_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(CommonR.string.details_screen_message))
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack)
        }
    }
}

@Composable
fun MenuScreen(onGreeting: () -> Unit, onCustomer: () -> Unit) {
    AppScaffold(title = stringResource(CommonR.string.menu_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(text = stringResource(CommonR.string.menu_greeting), onClick = onGreeting)
            PrimaryButton(text = stringResource(CommonR.string.menu_customer), onClick = onCustomer)
        }
    }
}

@Composable
fun GreetingScreen(onBack: () -> Unit) {
    AppScaffold(title = stringResource(CommonR.string.greeting_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(CommonR.string.greeting_message))
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack)
        }
    }
}

@Composable
fun CustomerMenuScreen(
    onList: () -> Unit,
    onCreate: () -> Unit,
    onDelete: () -> Unit,
    onFind: () -> Unit,
    onBack: () -> Unit
) {
    AppScaffold(title = stringResource(CommonR.string.customer_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(text = stringResource(CommonR.string.customer_list_people), onClick = onList)
            PrimaryButton(text = stringResource(CommonR.string.customer_create_person), onClick = onCreate)
            PrimaryButton(text = stringResource(CommonR.string.customer_delete_person), onClick = onDelete)
            PrimaryButton(text = stringResource(CommonR.string.customer_find_person), onClick = onFind)
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack)
        }
    }
}

@Composable
fun PersonListScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personRepository(context) }
    val personsState = remember { mutableStateOf<List<Person>>(emptyList()) }
    LaunchedEffect(Unit) {
        repository.observeAll().collect { personsState.value = it }
    }
    AppScaffold(title = stringResource(CommonR.string.person_list_title)) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(personsState.value) { p ->
                    Text("#${p.id} - ${p.lastName}, ${p.firstName} | ${p.age} " +
                        stringResource(CommonR.string.years_unit) +
                        " | " + stringResource(CommonR.string.label_weight) + ": ${p.weightKg}" +
                        stringResource(CommonR.string.kg_unit) +
                        " | " + stringResource(CommonR.string.label_left_handed) + ": " +
                        if (p.isLeftHanded) stringResource(CommonR.string.label_yes) else stringResource(CommonR.string.label_no)
                    )
                }
            }
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun PersonCreateScreen(onBack: () -> Unit) {
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

    // Validación en tiempo real
    val firstNameError = when {
        firstName.isBlank() -> stringResource(CommonR.string.label_first_name) + " es requerido"
        firstName.length > 50 -> "Máximo 50 caracteres"
    else -> null
    }
    
    val lastNameError = when {
        lastName.isBlank() -> stringResource(CommonR.string.label_last_name) + " es requerido" 
        lastName.length > 50 -> "Máximo 50 caracteres"
    else -> null
    }
    
    val weightError = when {
        weightStr.isBlank() -> stringResource(CommonR.string.label_weight) + " es requerido"
        weightStr.toDoubleOrNull() == null -> "Debe ser un número válido"
        (weightStr.toDoubleOrNull() ?: 0.0) <= 0 -> "Debe ser mayor a 0"
        (weightStr.toDoubleOrNull() ?: 0.0) > 1000 -> "Debe ser menor a 1000kg"
        else -> null
    }

    val isFormValid = firstNameError == null && lastNameError == null && 
                     weightError == null && birthDateStr.isNotBlank()

    AppScaffold(title = stringResource(CommonR.string.person_create_title)) { padding ->
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
                        System.currentTimeMillis() - (25 * 365 * 24 * 60 * 60 * 1000L) // Default: 25 años atrás
                    }
                    
                    val weight = weightStr.toDoubleOrNull() ?: 0.0
                    
                    scope.launch {
                        when (val result = repository.create(firstName, lastName, birthDate, weight, isLeftHanded)) {
                            is DatabaseResult.Success -> {
                                resultMessage = context.getString(CommonR.string.msg_person_created, result.data)
                                isError = false
                                // Limpiar formulario
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

@Composable
fun PersonDeleteScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personRepository(context) }
    var idStr by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AppScaffold(title = stringResource(CommonR.string.person_delete_title)) { padding ->
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
                                    idStr = "" // Limpiar campo
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

@Composable
fun PersonFindScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personRepository(context) }
    var idStr by remember { mutableStateOf("") }
    var person by remember { mutableStateOf<Person?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AppScaffold(title = stringResource(CommonR.string.person_find_title)) { padding ->
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
