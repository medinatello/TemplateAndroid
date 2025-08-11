package com.sortisplus.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sortisplus.core.designsystem.AppTheme
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.MenuItemCard
import com.sortisplus.core.ui.ScreenContainer
import com.sortisplus.core.ui.SectionHeader
import com.sortisplus.core.common.R as CommonR

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

// ================================
// COMPOSE PREVIEWS
// ================================

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