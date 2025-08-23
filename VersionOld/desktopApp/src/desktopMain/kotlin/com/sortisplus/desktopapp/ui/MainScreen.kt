package com.sortisplus.desktopapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sortisplus.shared.domain.model.AuthState
import com.sortisplus.shared.domain.model.Person
import com.sortisplus.shared.presentation.viewmodel.AuthenticationViewModel
import com.sortisplus.shared.presentation.viewmodel.PersonListViewModel
import com.sortisplus.shared.presentation.viewmodel.SettingsViewModel
import org.koin.java.KoinJavaComponent.inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val authViewModel: AuthenticationViewModel by inject(AuthenticationViewModel::class.java)
    val personListViewModel: PersonListViewModel by inject(PersonListViewModel::class.java)
    val settingsViewModel: SettingsViewModel by inject(SettingsViewModel::class.java)
    
    val authUiState by authViewModel.uiState.collectAsState()
    val personListUiState by personListViewModel.uiState.collectAsState()
    val settingsUiState by settingsViewModel.uiState.collectAsState()
    
    val currentUser = (authUiState.authState as? AuthState.Authenticated)?.userInfo

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text("KMP Desktop App") 
                    },
                    actions = {
                        // Settings Button
                        IconButton(
                            onClick = { settingsViewModel.toggleDarkTheme() }
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                        
                        // User info and logout
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            currentUser?.let { user ->
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = user.name ?: "User",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = user.email,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            
                            IconButton(
                                onClick = { authViewModel.logout() }
                            ) {
                                Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Sidebar
                Card(
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "📊 Dashboard",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Divider()
                        
                        // App Info Section
                        Text(
                            text = "App Status",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        
                        currentUser?.let { user ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Person, 
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "Logged in as:",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Text(
                                        text = user.name ?: "User",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = user.email,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        
                        // Settings Section
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Dark Theme")
                                    Switch(
                                        checked = settingsUiState.isDarkTheme,
                                        onCheckedChange = { settingsViewModel.setDarkTheme(it) }
                                    )
                                }
                                
                                Text(
                                    text = "List Order: ${settingsUiState.listOrder}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                
                // Main Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Welcome Card
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "🎉 Welcome to KMP Desktop!",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Text(
                                text = "This application demonstrates Kotlin Multiplatform in action.",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            
                            Text(
                                text = "✅ Shared business logic between Android and Desktop",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Text(
                                text = "✅ Reactive state management with shared ViewModels",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Text(
                                text = "✅ Cross-platform authentication and storage",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    // Persons List
                    Card(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "👥 Persons List",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            if (personListUiState.isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else if (personListUiState.persons.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No persons found. (This is a demo - person data would be loaded from the shared repository)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(personListUiState.persons) { person ->
                                        PersonCard(person = person)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonCard(person: Person) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Column {
                Text(
                    text = "${person.firstName} ${person.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Weight: ${person.weightKg}kg • ${if (person.isLeftHanded) "Left-handed" else "Right-handed"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}