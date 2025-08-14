package com.sortisplus.core.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sortisplus.core.ui.components.SortisPrimaryButton
import com.sortisplus.core.ui.components.SortisSecondaryButton
import com.sortisplus.core.ui.theme.Dimensions
import com.sortisplus.core.ui.theme.SortisPreviewTheme
import com.sortisplus.core.ui.theme.SortisTheme

/**
 * Theme Toggle Demo Screen - T-007: Pantallas Demo y Showcase
 * Demonstrates theme switching capabilities and design system colors
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeToggleDemoScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var useDynamicColor by remember { mutableStateOf(true) }

    SortisTheme(
        darkTheme = isDarkTheme,
        dynamicColor = useDynamicColor
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Theme Demo") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Navigate back"
                            )
                        }
                    }
                )
            },
            modifier = modifier
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = Dimensions.screenPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Dimensions.paddingLarge)
            ) {
                Text(
                    text = "Theme Customization",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = Dimensions.paddingMedium)
                )

                // Theme Controls
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.cardPadding),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
                    ) {
                        Text(
                            text = "Theme Settings",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Dark Theme Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Dark Theme",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { isDarkTheme = it }
                            )
                        }

                        // Dynamic Color Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Palette,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Dynamic Color (Android 12+)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Switch(
                                checked = useDynamicColor,
                                onCheckedChange = { useDynamicColor = it }
                            )
                        }
                    }
                }

                // Color Palette Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.cardPadding),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
                    ) {
                        Text(
                            text = "Color Palette",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        ColorRow("Primary", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
                        ColorRow("Secondary", MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
                        ColorRow("Tertiary", MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.onTertiary)
                        ColorRow("Error", MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
                        ColorRow("Surface", MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)
                        ColorRow("Background", MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onBackground)
                    }
                }

                // Component Showcase with Current Theme
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.cardPadding),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
                    ) {
                        Text(
                            text = "Components in Current Theme",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        SortisPrimaryButton(
                            text = "Primary Button",
                            onClick = { },
                            modifier = Modifier.fillMaxWidth()
                        )

                        SortisSecondaryButton(
                            text = "Secondary Button",
                            onClick = { },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Body Text - This text adapts to the current theme automatically.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                // Typography Showcase
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.cardPadding),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
                    ) {
                        Text(
                            text = "Typography Scales",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Text(
                            text = "Headline Large",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Text(
                            text = "Title Medium",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Text(
                            text = "Body Large - Main content text that adapts to the theme.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Text(
                            text = "Label Medium - For UI elements and buttons.",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorRow(
    name: String,
    backgroundColor: Color,
    contentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.size(Dimensions.iconSizeLarge),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            // Empty card showing the color
        }

        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "on$name",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeToggleDemoScreenPreview() {
    SortisPreviewTheme {
        ThemeToggleDemoScreen(
            onNavigateBack = { }
        )
    }
}
