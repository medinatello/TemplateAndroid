package com.sortisplus.core.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.sortisplus.core.ui.navigation.DemoType
import com.sortisplus.core.ui.theme.Dimensions
import com.sortisplus.core.ui.theme.SortisPreviewTheme

/**
 * Home screen showing navigation options and demo features
 * Central hub for accessing different parts of the application
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDemo: (DemoType) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        NavigationItem(
            title = "Profile",
            description = "View and edit your profile",
            icon = Icons.Default.Person,
            onClick = { onNavigateToProfile("current_user") }
        ),
        NavigationItem(
            title = "Settings",
            description = "App preferences and configuration",
            icon = Icons.Default.Settings,
            onClick = onNavigateToSettings
        )
    )

    val demoItems = listOf(
        DemoItem(
            title = "Component Showcase",
            description = "View all available UI components",
            demoType = DemoType.COMPONENTS
        ),
        DemoItem(
            title = "Form Validation",
            description = "See form validation in action",
            demoType = DemoType.VALIDATION
        ),
        DemoItem(
            title = "Theme Toggle",
            description = "Switch between light and dark themes",
            demoType = DemoType.THEME
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sortis Template",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimensions.screenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
        ) {
            item {
                // Welcome Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimensions.paddingMedium),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.cardPadding)
                    ) {
                        Text(
                            text = "Welcome to Sortis Template",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "A modern Android template with Jetpack Compose, Material 3, and clean architecture.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = Dimensions.paddingSmall)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Navigation",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        top = Dimensions.paddingLarge,
                        bottom = Dimensions.paddingSmall
                    )
                )
            }

            items(navigationItems) { item ->
                NavigationCard(
                    item = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "MVP-02 Demos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        top = Dimensions.paddingLarge,
                        bottom = Dimensions.paddingSmall
                    )
                )
            }

            items(demoItems) { item ->
                DemoCard(
                    item = item,
                    onClick = { onNavigateToDemo(item.demoType) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationCard(
    item: NavigationItem,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = item.onClick,
        modifier = modifier.semantics {
            contentDescription = "${item.title}: ${item.description}"
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimensions.elevationLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DemoCard(
    item: DemoItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.semantics {
            contentDescription = "${item.title}: ${item.description}"
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimensions.elevationLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.cardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
        ) {
            Icon(
                imageVector = Icons.Default.Dashboard,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private data class NavigationItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

private data class DemoItem(
    val title: String,
    val description: String,
    val demoType: DemoType
)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    SortisPreviewTheme {
        HomeScreen(
            onNavigateToProfile = { },
            onNavigateToSettings = { },
            onNavigateToDemo = { }
        )
    }
}
