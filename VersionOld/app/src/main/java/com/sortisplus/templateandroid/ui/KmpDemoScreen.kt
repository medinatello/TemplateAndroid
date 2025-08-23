package com.sortisplus.templateandroid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.sortisplus.shared.domain.usecase.AppInfo
import com.sortisplus.shared.domain.usecase.GetAppInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KmpDemoViewModel @Inject constructor(
    private val getAppInfoUseCase: GetAppInfoUseCase
) : ViewModel() {
    
    fun getAppInfo(): AppInfo = getAppInfoUseCase.execute()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KmpDemoScreen(
    viewModel: KmpDemoViewModel = hiltViewModel()
) {
    var appInfo by remember { mutableStateOf<AppInfo?>(null) }
    
    LaunchedEffect(Unit) {
        appInfo = viewModel.getAppInfo()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KMP Demo") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Shared Logic Demo",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    appInfo?.let { info ->
                        Text("App Name: ${info.appName}")
                        Text("Version: ${info.version}")
                        Text("Current Time: ${info.currentTime}")
                        Text("Last Open Time: ${info.lastOpenTime ?: "First time!"}")
                    } ?: run {
                        CircularProgressIndicator()
                    }
                }
            }
            
            Text(
                text = "This data comes from the shared KMP module!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}