package com.sortisplus.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sortisplus.core.data.DatabaseResult
import com.sortisplus.core.data.Person
import com.sortisplus.data.local.LocalProviders
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.PrimaryButton
import com.sortisplus.core.ui.MenuItemCard
import com.sortisplus.core.ui.SectionHeader
import com.sortisplus.core.ui.ScreenContainer
import com.sortisplus.core.ui.SearchBar
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.sortisplus.core.designsystem.AppTheme
import com.sortisplus.core.common.R as CommonR

/**
 * Home screen displaying the main welcome interface
 * 
 * Shows the application title, description and a button to navigate
 * to the next screen. This is the entry point of the application.
 * 
 * @param onContinue Callback function executed when user taps continue button
 */
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

/**
 * Details screen showing secondary content
 * 
 * Displays static information and provides navigation back to the previous screen.
 * Used as a demonstration of navigation between screens.
 * 
 * @param onBack Callback function executed when user wants to return to previous screen
 */
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

/**
 * Main menu screen with modern Material 3 design
 * 
 * Displays organized sections with card-based navigation items.
 * Features app information and easy access to key functionality.
 * 
 * @param onGreeting Callback function to navigate to greeting screen
 * @param onCustomer Callback function to navigate to customer management section
 */
@Composable
fun MenuScreen(onGreeting: () -> Unit, onCustomer: () -> Unit) {
    AppScaffold(title = stringResource(CommonR.string.menu_title)) { padding ->
        ScreenContainer {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // App header with info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(CommonR.string.app_description),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(CommonR.string.version_info),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Main section
                SectionHeader(title = stringResource(CommonR.string.menu_section_main))
                
                MenuItemCard(
                    title = stringResource(CommonR.string.menu_greeting),
                    subtitle = "Pantalla de ejemplo con saludo",
                    icon = Icons.Default.Face,
                    onClick = onGreeting
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Data management section
                SectionHeader(title = stringResource(CommonR.string.menu_section_data))
                
                MenuItemCard(
                    title = stringResource(CommonR.string.menu_customer),
                    subtitle = "Gestionar información de personas",
                    icon = Icons.Default.Person,
                    onClick = onCustomer
                )
                
                Spacer(modifier = Modifier.weight(1f))
            }
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

/**
 * Customer management menu with modern card-based design
 * 
 * Organized menu for person/customer data operations including
 * viewing, creating, updating and deleting person records.
 * 
 * @param onList Navigate to person list screen
 * @param onCreate Navigate to person creation screen  
 * @param onDelete Navigate to person deletion screen
 * @param onFind Navigate to person search screen
 * @param onBack Navigate back to previous screen
 */
@Composable
fun CustomerMenuScreen(
    onList: () -> Unit,
    onCreate: () -> Unit,
    onDelete: () -> Unit,
    onFind: () -> Unit,
    onBack: () -> Unit
) {
    AppScaffold(title = stringResource(CommonR.string.customer_title)) { padding ->
        ScreenContainer {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Data management actions
                SectionHeader(title = stringResource(CommonR.string.menu_section_data))
                
                MenuItemCard(
                    title = stringResource(CommonR.string.customer_list_people),
                    subtitle = "Ver todas las personas registradas",
                    icon = Icons.Default.List,
                    onClick = onList
                )
                
                MenuItemCard(
                    title = stringResource(CommonR.string.customer_create_person),
                    subtitle = "Agregar nueva persona al sistema",
                    icon = Icons.Default.PersonAdd,
                    onClick = onCreate
                )
                
                MenuItemCard(
                    title = stringResource(CommonR.string.customer_find_person),
                    subtitle = "Buscar persona por ID",
                    icon = Icons.Default.Search,
                    onClick = onFind
                )
                
                MenuItemCard(
                    title = stringResource(CommonR.string.customer_delete_person),
                    subtitle = "Eliminar persona del sistema",
                    icon = Icons.Default.PersonRemove,
                    onClick = onDelete
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Back button at bottom
                PrimaryButton(
                    text = stringResource(CommonR.string.button_back), 
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

/**
 * Screen displaying list of people with search functionality and theme toggle
 * 
 * Shows all people from the database with a search filter and theme switching capability.
 * Includes real-time search filtering by first name or last name.
 * 
 * @param onBack Callback function to navigate back to previous screen
 * @param isDarkTheme Current theme state (true for dark, false for light)
 * @param onToggleTheme Callback function to toggle between light and dark theme
 */
@Composable
fun PersonListScreen(
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personRepository(context) }
    val personsState = remember { mutableStateOf<List<Person>>(emptyList()) }
    LaunchedEffect(Unit) {
        repository.observeAll().collect { personsState.value = it }
    }
    AppScaffold(
        title = stringResource(CommonR.string.person_list_title),
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
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Modern search filter
            var query by remember { mutableStateOf("") }
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = stringResource(CommonR.string.search_hint),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val filtered = personsState.value.filter { p ->
                    val q = query.trim().lowercase()
                    if (q.isEmpty()) true else
                        p.firstName.lowercase().contains(q) || p.lastName.lowercase().contains(q)
                }
                items(filtered) { p ->
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

@Preview(name = "Home Screen Light")
@Preview(name = "Home Screen Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(onContinue = { })
    }
}

@Preview(name = "Menu Screen Light")
@Preview(name = "Menu Screen Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MenuScreenPreview() {
    AppTheme {
        MenuScreen(
            onGreeting = { },
            onCustomer = { }
        )
    }
}

@Preview(name = "Customer Menu Light")
@Preview(name = "Customer Menu Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomerMenuScreenPreview() {
    AppTheme {
        CustomerMenuScreen(
            onList = { },
            onCreate = { },
            onDelete = { },
            onFind = { },
            onBack = { }
        )
    }
}

@Preview(name = "Greeting Screen")
@Composable
fun GreetingScreenPreview() {
    AppTheme {
        GreetingScreen(onBack = { })
    }
}

@Preview(name = "Details Screen")
@Composable
fun DetailsScreenPreview() {
    AppTheme {
        DetailsScreen(onBack = { })
    }
}

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
