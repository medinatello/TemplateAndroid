package com.sortisplus.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
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
import com.androidbase.core.data.Persona
import com.androidbase.data.local.LocalProviders
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
    var savedMessage by remember { mutableStateOf<String?>(null) }

    AppScaffold(title = "Crear Persona") { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = fechaNacimientoStr, onValueChange = { fechaNacimientoStr = it }, label = { Text("Fecha Nacimiento (epoch ms)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = pesoStr, onValueChange = { pesoStr = it }, label = { Text("Peso (kg)") }, modifier = Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Checkbox(checked = esZurdo, onCheckedChange = { esZurdo = it })
                Text("Es zurdo")
            }
            PrimaryButton(text = "Guardar", onClick = {
                val fecha = fechaNacimientoStr.toLongOrNull() ?: 0L
                val peso = pesoStr.toDoubleOrNull() ?: 0.0
                scope.launch {
                    val id = repository.create(nombre, apellido, fecha, peso, esZurdo)
                    savedMessage = "Persona creada con id $id"
                }
            })
            savedMessage?.let { Text(it) }
            PrimaryButton(text = "Volver", onClick = onBack)
        }
    }
}

@Composable
fun PersonaDeleteScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personaRepository(context) }
    var idStr by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    AppScaffold(title = "Eliminar Persona") { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = idStr, onValueChange = { idStr = it }, label = { Text("ID") })
            PrimaryButton(text = "Eliminar", onClick = {
                val id = idStr.toLongOrNull()
                if (id != null) {
                    scope.launch {
                        val ok = repository.delete(id)
                        result = if (ok) "Eliminado" else "No encontrado"
                    }
                } else {
                    result = "ID inválido"
                }
            })
            result?.let { Text(it) }
            PrimaryButton(text = "Volver", onClick = onBack)
        }
    }
}

@Composable
fun PersonaFindScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personaRepository(context) }
    var idStr by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Persona?>(null) }
    val scope = rememberCoroutineScope()

    AppScaffold(title = "Buscar Persona") { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = idStr, onValueChange = { idStr = it }, label = { Text("ID") })
            PrimaryButton(text = "Buscar", onClick = {
                val id = idStr.toLongOrNull()
                if (id != null) {
                    scope.launch {
                        result = repository.getById(id)
                    }
                }
            })
            result?.let { p ->
                Text("${p.id}: ${p.nombre} ${p.apellido} | ${p.edad} años | Peso ${p.peso}kg | Zurdo: ${if (p.esZurdo) "Sí" else "No"}")
            }
            PrimaryButton(text = "Volver", onClick = onBack)
        }
    }
}
