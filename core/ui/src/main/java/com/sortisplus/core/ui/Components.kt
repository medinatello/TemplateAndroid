package com.sortisplus.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sortisplus.core.designsystem.AppTheme
import androidx.compose.foundation.layout.RowScope

/**
 * Modern app scaffold with Material 3 styling
 * 
 * Provides consistent app bar styling with elevation and color scheme integration.
 * Supports custom actions in the top app bar.
 * 
 * @param title Screen title displayed in the top app bar
 * @param actions Custom action composables for the top app bar
 * @param content Screen content with padding values for proper spacing
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { 
            TopAppBar(
                title = { 
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    ) 
                },
                actions = actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) 
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { inner ->
        content(inner)
    }
}

/**
 * Primary button with Material 3 styling
 * 
 * @param text Button text content
 * @param onClick Click handler
 * @param modifier Optional modifier for customization
 * @param enabled Whether the button is enabled
 */
@Composable
fun PrimaryButton(
    text: String, 
    onClick: () -> Unit, 
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick, 
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp)
    ) { 
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        ) 
    }
}

/**
 * Secondary outlined button
 * 
 * @param text Button text content
 * @param onClick Click handler
 * @param modifier Optional modifier for customization
 */
@Composable
fun SecondaryButton(
    text: String, 
    onClick: () -> Unit, 
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick, 
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) { 
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        ) 
    }
}

/**
 * Menu item card with modern Material 3 design
 * 
 * @param title Item title
 * @param subtitle Optional subtitle text
 * @param icon Leading icon
 * @param onClick Click handler
 * @param modifier Optional modifier
 */
@Composable
fun MenuItemCard(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(4.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Section header for grouping menu items
 * 
 * @param title Section title
 * @param modifier Optional modifier
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * Modern search bar with Material 3 design
 * 
 * @param query Current search query
 * @param onQueryChange Callback when search query changes
 * @param placeholder Placeholder text for the search field
 * @param modifier Optional modifier for customization
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.clickable { onQueryChange("") },
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

/**
 * Screen container with proper Material 3 theming
 * 
 * @param modifier Optional modifier for customization
 * @param content Screen content
 */
@Composable
fun ScreenContainer(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) { 
        content() 
    }
}

// ================================
// COMPOSE PREVIEWS
// ================================

@Preview(name = "Primary Button")
@Composable
fun PrimaryButtonPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(text = "Save", onClick = { })
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Loading...", onClick = { }, enabled = false)
        }
    }
}

@Preview(name = "Secondary Button")
@Composable
fun SecondaryButtonPreview() {
    AppTheme {
        SecondaryButton(text = "Cancel", onClick = { })
    }
}

@Preview(name = "Menu Item Card Light")
@Preview(name = "Menu Item Card Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MenuItemCardPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MenuItemCard(
                title = "Manage People",
                subtitle = "View and edit person information",
                icon = Icons.Default.Person,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
            MenuItemCard(
                title = "Settings", 
                icon = Icons.Default.Settings,
                onClick = { }
            )
        }
    }
}

@Preview(name = "Search Bar")
@Composable
fun SearchBarPreview() {
    AppTheme {
        var query by remember { mutableStateOf("Sample search") }
        Column(modifier = Modifier.padding(16.dp)) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = "Search people..."
            )
            Spacer(modifier = Modifier.height(8.dp))
            SearchBar(
                query = "",
                onQueryChange = { },
                placeholder = "Empty search bar"
            )
        }
    }
}

@Preview(name = "Section Header")
@Composable
fun SectionHeaderPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(title = "Main Section")
            SectionHeader(title = "Data Management")
        }
    }
}
