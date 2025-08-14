package com.sortisplus.core.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sortisplus.core.ui.components.SortisOutlinedButton
import com.sortisplus.core.ui.components.SortisPrimaryButton
import com.sortisplus.core.ui.components.SortisSecondaryButton
import com.sortisplus.core.ui.components.SortisTextButton
import com.sortisplus.core.ui.components.SortisTextField
import com.sortisplus.core.ui.theme.Dimensions
import com.sortisplus.core.ui.theme.SortisPreviewTheme

/**
 * Component Showcase Screen - T-007: Pantallas Demo y Showcase
 * Demonstrates all available UI components from the design system
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentShowcaseScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Component Showcase") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimensions.screenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingLarge)
        ) {
            item {
                Text(
                    text = "Design System Components",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = Dimensions.paddingMedium)
                )
            }

            item {
                ComponentSection(title = "Buttons") {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
                    ) {
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

                        SortisOutlinedButton(
                            text = "Outlined Button",
                            onClick = { },
                            modifier = Modifier.fillMaxWidth()
                        )

                        SortisTextButton(
                            text = "Text Button",
                            onClick = { },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
                        ) {
                            SortisPrimaryButton(
                                text = "With Icon",
                                onClick = { },
                                leadingIcon = Icons.Default.Star,
                                modifier = Modifier.weight(1f)
                            )

                            SortisPrimaryButton(
                                text = "Loading",
                                onClick = { },
                                isLoading = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item {
                ComponentSection(title = "Text Fields") {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
                    ) {
                        SortisTextField(
                            value = "",
                            onValueChange = { },
                            label = "Normal Field",
                            placeholder = "Enter text here"
                        )

                        SortisTextField(
                            value = "Invalid input",
                            onValueChange = { },
                            label = "Error Field",
                            isError = true,
                            errorMessage = "This field contains an error"
                        )

                        SortisTextField(
                            value = "Read-only content",
                            onValueChange = { },
                            label = "Read-only Field",
                            readOnly = true
                        )

                        SortisTextField(
                            value = "",
                            onValueChange = { },
                            label = "Disabled Field",
                            enabled = false
                        )
                    }
                }
            }

            item {
                ComponentSection(title = "Typography") {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
                    ) {
                        Text(
                            text = "Display Large",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = "Headline Large",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(
                            text = "Title Large",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Body Large - This is the main content text style used throughout the application for readability.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Body Medium - Secondary content text with smaller size.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Label Large",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            item {
                ComponentSection(title = "Colors") {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
                    ) {
                        ColorSwatch("Primary", MaterialTheme.colorScheme.primary)
                        ColorSwatch("Secondary", MaterialTheme.colorScheme.secondary)
                        ColorSwatch("Tertiary", MaterialTheme.colorScheme.tertiary)
                        ColorSwatch("Error", MaterialTheme.colorScheme.error)
                        ColorSwatch("Surface", MaterialTheme.colorScheme.surface)
                        ColorSwatch("Background", MaterialTheme.colorScheme.background)
                    }
                }
            }
        }
    }
}

@Composable
private fun ComponentSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = Dimensions.paddingSmall)
        )
        content()
    }
}

@Composable
private fun ColorSwatch(
    name: String,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
    ) {
        Canvas(
            modifier = Modifier.size(Dimensions.iconSizeLarge)
        ) {
            drawRect(color = color)
        }
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ComponentShowcaseScreenPreview() {
    SortisPreviewTheme {
        ComponentShowcaseScreen(
            onNavigateBack = { }
        )
    }
}
