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
import com.sortisplus.core.data.Persona
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
fun MenuScreen(onSaludo: () -> Unit, onCliente: () -> Unit) {
    AppScaffold(title = "Menú") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(text = "Saludo", onClick = onSaludo)
            PrimaryButton(text = "Cliente", onClick = onCliente)
        }
    }
}

@Composable
fun SaludoScreen(onBack: () -> Unit) {
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
fun ClienteMenuScreen(
    onLista: () -> Unit,
    onCrear: () -> Unit,
    onEliminar: () -> Unit,
    onBuscar: () -> Unit,
    onBack: () -> Unit
) {
    AppScaffold(title = "Cliente") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(text = "Lista de Personas", onClick = onLista)
            PrimaryButton(text = "Crear / Guardar Persona", onClick = onCrear)
            PrimaryButton(text = "Eliminar Persona", onClick = onEliminar)
            PrimaryButton(text = "Buscar Persona por ID", onClick = onBuscar)
            PrimaryButton(text = "Volver", onClick = onBack)
        }
    }
}

@Composable
fun PersonaListScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personaRepository(context) }
    val personasState = remember { mutableStateOf<List<Persona>>(emptyList()) }
    LaunchedEffect(Unit) {
        repository.observeAll().collect { personasState.value = it }
    }
    AppScaffold(title = "Personas") { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(personasState.value) { p ->
                    Text("#${p.id} - ${p.apellido}, ${p.nombre} | ${p.edad} años | Peso: ${p.peso}kg | Zurdo: ${if (p.esZurdo) "Sí" else "No"}")
                }
            }
            PrimaryButton(text = "Volver", onClick = onBack, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun PersonaCreateScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personaRepository(context) }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var fechaNacimientoStr by remember { mutableStateOf("") }
    var pesoStr by remember { mutableStateOf("") }
    var esZurdo by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Validación en tiempo real
    val nombreError = when {
        nombre.isBlank() -> "El nombre es requerido"
        nombre.length > 50 -> "Máximo 50 caracteres"
        else -> null
    }
    
    val apellidoError = when {
        apellido.isBlank() -> "El apellido es requerido" 
        apellido.length > 50 -> "Máximo 50 caracteres"
        else -> null
    }
    
    val pesoError = when {
        pesoStr.isBlank() -> "El peso es requerido"
        pesoStr.toDoubleOrNull() == null -> "Debe ser un número válido"
        (pesoStr.toDoubleOrNull() ?: 0.0) <= 0 -> "Debe ser mayor a 0"
        (pesoStr.toDoubleOrNull() ?: 0.0) > 1000 -> "Debe ser menor a 1000kg"
        else -> null
    }

    val isFormValid = nombreError == null && apellidoError == null && 
                     pesoError == null && fechaNacimientoStr.isNotBlank()

    AppScaffold(title = "Crear Persona") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo Nombre con validación
            OutlinedTextField(
                value = nombre, 
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                supportingText = nombreError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = nombreError != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Apellido con validación  
            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                supportingText = apellidoError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = apellidoError != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Fecha (mejorado)
            OutlinedTextField(
                value = fechaNacimientoStr,
                onValueChange = { fechaNacimientoStr = it },
                label = { Text("Fecha Nacimiento") },
                supportingText = { Text("Formato: aaaa-mm-dd (ej: 1990-05-15)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo Peso con validación
            OutlinedTextField(
                value = pesoStr,
                onValueChange = { pesoStr = it },
                label = { Text("Peso (kg)") },
                supportingText = pesoError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                isError = pesoError != null,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Checkbox zurdo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(checked = esZurdo, onCheckedChange = { esZurdo = it })
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
                    val fecha = try {
                        // Convertir fecha simple YYYY-MM-DD a timestamp
                        val parts = fechaNacimientoStr.split("-")
                        if (parts.size == 3) {
                            val year = parts[0].toInt()
                            val month = parts[1].toInt() - 1 // Calendar months are 0-based
                            val day = parts[2].toInt()
                            java.util.Calendar.getInstance().apply {
                                set(year, month, day)
                            }.timeInMillis
                        } else {
                            fechaNacimientoStr.toLongOrNull() ?: System.currentTimeMillis()
                        }
                    } catch (e: Exception) {
                        System.currentTimeMillis() - (25 * 365 * 24 * 60 * 60 * 1000L) // Default: 25 años atrás
                    }
                    
                    val peso = pesoStr.toDoubleOrNull() ?: 0.0
                    
                    scope.launch {
                        when (val result = repository.create(nombre, apellido, fecha, peso, esZurdo)) {
                            is DatabaseResult.Success -> {
                                resultMessage = "✅ Persona creada exitosamente con ID ${result.data}"
                                isError = false
                                // Limpiar formulario
                                nombre = ""
                                apellido = ""
                                fechaNacimientoStr = ""
                                pesoStr = ""
                                esZurdo = false
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
fun PersonaDeleteScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personaRepository(context) }
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
fun PersonaFindScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personaRepository(context) }
    var idStr by remember { mutableStateOf("") }
    var persona by remember { mutableStateOf<Persona?>(null) }
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
                        persona = null
                        return@PrimaryButton
                    }
                    
                    isLoading = true
                    errorMessage = null
                    persona = null
                    
                    scope.launch {
                        when (val result = repository.getById(id)) {
                            is DatabaseResult.Success -> {
                                if (result.data != null) {
                                    persona = result.data
                                    errorMessage = null
                                } else {
                                    errorMessage = "❌ No se encontró persona con ID $id"
                                    persona = null
                                }
                            }
                            is DatabaseResult.Error -> {
                                errorMessage = "❌ Error: ${result.exception.message}"
                                persona = null
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
            persona?.let { p ->
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
                    Text("Nombre: ${p.nombre} ${p.apellido}")
                    Text("Edad: ${p.edad} años")
                    Text("Peso: ${p.peso}kg")
                    Text("Zurdo: ${if (p.esZurdo) "Sí" else "No"}")
                }
            }
            
            PrimaryButton(text = "Volver", onClick = onBack)
        }
    }
}
