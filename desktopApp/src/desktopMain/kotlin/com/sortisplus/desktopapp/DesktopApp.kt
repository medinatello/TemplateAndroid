package com.sortisplus.desktopapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sortisplus.shared.domain.usecase.AppInfo
import com.sortisplus.shared.domain.usecase.GetAppInfoUseCase
import org.koin.java.KoinJavaComponent.inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopApp() {
    val getAppInfoUseCase: GetAppInfoUseCase by inject(GetAppInfoUseCase::class.java)
    var appInfo by remember { mutableStateOf<AppInfo?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(Unit) {
        try {
            appInfo = getAppInfoUseCase.execute()
        } catch (e: Exception) {
            error = e.message
        }
    }
    
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("KMP Desktop Demo") }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "🎉 KMP Desktop Application",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Divider()
                        
                        if (error != null) {
                            Text(
                                text = "Error: $error",
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            appInfo?.let { info ->
                                Text("📱 App Name: ${info.appName}")
                                Text("🏷️ Version: ${info.version}")
                                Text("⏰ Current Time: ${info.currentTime}")
                                Text("🕐 Last Open Time: ${info.lastOpenTime ?: "First time!"}")
                            } ?: run {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                                    Text("Loading...")
                                }
                            }
                        }
                    }
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "✅ Success!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Text(
                            text = "This desktop application is using shared business logic from the KMP module!",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Text(
                            text = "The same code that powers the Android app also powers this desktop application.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "Powered by Kotlin Multiplatform 🚀",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}