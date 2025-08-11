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
import com.sortisplus.core.data.DatabaseResult
import com.sortisplus.core.data.Person
import com.sortisplus.data.local.LocalProviders
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.PrimaryButton
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(onContinue: () -> Unit) {
    AppScaffold(title = "Home") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Pantalla inicial de ejemplo")
            PrimaryButton(text = "Ir al Menú", onClick = onContinue)
        }
    }
}

@Composable
fun DetailsScreen(onBack: () -> Unit) {
    AppScaffold(title = "Detalles") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Detalle de ejemplo")
            PrimaryButton(text = "Volver", onClick = onBack)
        }
    }
}

@Composable
fun MenuScreen(onGreeting: () -> Unit, onCustomer: () -> Unit) {
    AppScaffold(title = "Menú") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(text = "Saludo", onClick = onGreeting)
            PrimaryButton(text = "Cliente", onClick = onCustomer)
        }
    }
}

@Composable
fun GreetingScreen(onBack: () -> Unit) {
    AppScaffold(title = "Saludo") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("¡Hola! Esta es la pantalla de saludo.")
            PrimaryButton(text = "Volver", onClick = onBack)
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
    AppScaffold(title = "Cliente") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(text = "Lista de Personas", onClick = onList)
            PrimaryButton(text = "Crear / Guardar Persona", onClick = onCreate)
            PrimaryButton(text = "Eliminar Persona", onClick = onDelete)
            PrimaryButton(text = "Buscar Persona por ID", onClick = onFind)
            PrimaryButton(text = "Volver", onClick = onBack)
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
    AppScaffold(title = "Personas") { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(personsState.value) { p ->
                    Text("#${p.id} - ${p.lastName}, ${p.firstName} | ${p.age} años | Peso: ${p.weightKg}kg | Zurdo: ${if (p.isLeftHanded) "Sí" else "No"}")
                }
            }
            PrimaryButton(text = "Volver", onClick = onBack, modifier = Modifier.padding(top = 16.dp))
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
        firstName.isBlank() -> "El nombre es requerido"
        firstName.length > 50 -> "Máximo 50 caracteres"
        else -> null
    }
    
    val lastNameError = when {
        lastName.isBlank() -> "El apellido es requerido" 
        lastName.length > 50 -> "Máximo 50 caracteres"
        else -> null
    }
    
    val weightError = when {
        weightStr.isBlank() -> "El peso es requerido"
        weightStr.toDoubleOrNull() == null -> "Debe ser un número válido"
        (weightStr.toDoubleOrNull() ?: 0.0) <= 0 -> "Debe ser mayor a 0"
        (weightStr.toDoubleOrNull() ?: 0.0) > 1000 -> "Debe ser menor a 1000kg"
        else -> null
    }

    val isFormValid = firstNameError == null && lastNameError == null && 
                     weightError == null && birthDateStr.isNotBlank()

    AppScaffold(title = "Crear Persona") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo Nombre con validación
            OutlinedTextField(
                value = firstName, 
                onValueChange = { firstName = it },
                label = { Text("Nombre") },
                supportingText = firstNameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = firstNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Apellido con validación  
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                supportingText = lastNameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = lastNameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Fecha (mejorado)
            OutlinedTextField(
                value = birthDateStr,
                onValueChange = { birthDateStr = it },
                label = { Text("Fecha Nacimiento") },
                supportingText = { Text("Formato: aaaa-mm-dd (ej: 1990-05-15)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Peso con validación
            OutlinedTextField(
                value = weightStr,
                onValueChange = { weightStr = it },
                label = { Text("Peso (kg)") },
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
                Text("Es zurdo")
            }
            
            // Botón guardar mejorado
            PrimaryButton(
                text = if (isLoading) "Guardando..." else "Guardar",
                onClick = {
                    if (!isFormValid) {
                        resultMessage = "Por favor corrige los errores"
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
                                resultMessage = "✅ Persona creada exitosamente con ID ${result.data}"
                                isError = false
                                // Limpiar formulario
                                firstName = ""
                                lastName = ""
                                birthDateStr = ""
                                weightStr = ""
                                isLeftHanded = false
                            }
                            is DatabaseResult.Error -> {
                                resultMessage = "❌ Error: ${result.exception.message}"
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
            
            PrimaryButton(text = "Volver", onClick = onBack)
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

    AppScaffold(title = "Eliminar Persona") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp), 
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = idStr, 
                onValueChange = { idStr = it }, 
                label = { Text("ID de la Persona") },
                supportingText = { Text("Ingresa el ID numérico de la persona a eliminar") },
                modifier = Modifier.fillMaxWidth()
            )
            
            PrimaryButton(
                text = if (isLoading) "Eliminando..." else "Eliminar",
                onClick = {
                    val id = idStr.toLongOrNull()
                    if (id == null) {
                        resultMessage = "❌ ID inválido. Debe ser un número"
                        isError = true
                        return@PrimaryButton
                    }
                    
                    isLoading = true
                    scope.launch {
                        when (val result = repository.delete(id)) {
                            is DatabaseResult.Success -> {
                                if (result.data) {
                                    resultMessage = "✅ Persona eliminada exitosamente"
                                    isError = false
                                    idStr = "" // Limpiar campo
                                } else {
                                    resultMessage = "❌ No se encontró persona con ID $id"
                                    isError = true
                                }
                            }
                            is DatabaseResult.Error -> {
                                resultMessage = "❌ Error: ${result.exception.message}"
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
            
            PrimaryButton(text = "Volver", onClick = onBack)
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

    AppScaffold(title = "Buscar Persona") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp), 
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = idStr, 
                onValueChange = { idStr = it }, 
                label = { Text("ID de la Persona") },
                supportingText = { Text("Ingresa el ID numérico de la persona a buscar") },
                modifier = Modifier.fillMaxWidth()
            )
            
            PrimaryButton(
                text = if (isLoading) "Buscando..." else "Buscar",
                onClick = {
                    val id = idStr.toLongOrNull()
                    if (id == null) {
                        errorMessage = "❌ ID inválido. Debe ser un número"
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
                                    errorMessage = "❌ No se encontró persona con ID $id"
                                    person = null
                                }
                            }
                            is DatabaseResult.Error -> {
                                errorMessage = "❌ Error: ${result.exception.message}"
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
                        text = "✅ Persona encontrada:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("ID: ${p.id}")
                    Text("Nombre: ${p.firstName} ${p.lastName}")
                    Text("Edad: ${p.age} años")
                    Text("Peso: ${p.weightKg}kg")
                    Text("Zurdo: ${if (p.isLeftHanded) "Sí" else "No"}")
                }
            }
            
            PrimaryButton(text = "Volver", onClick = onBack)
        }
    }
}
