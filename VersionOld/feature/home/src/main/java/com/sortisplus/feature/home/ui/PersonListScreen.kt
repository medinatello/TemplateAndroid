package com.sortisplus.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sortisplus.core.data.Person
import com.sortisplus.core.designsystem.AppTheme
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.PrimaryButton
import com.sortisplus.core.ui.SearchBar
import com.sortisplus.data.local.LocalProviders
import com.sortisplus.core.common.R as CommonR

/**
 * Screen displaying list of people with search functionality and theme toggle
 * 
 * Shows all people from the database with a search filter and theme switching capability.
 * Includes real-time search filtering by first name or last name.
 * 
 * @param onBack Callback function to navigate back to previous screen
 * @param isDarkTheme Current theme state (true for dark, false for light)
 * @param onToggleTheme Callback function to toggle between light and dark theme
 */
@Composable
fun PersonListScreen(
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { LocalProviders.personRepository(context) }
    val personsState = remember { mutableStateOf<List<Person>>(emptyList()) }
    LaunchedEffect(Unit) {
        repository.observeAll().collect { personsState.value = it }
    }
    AppScaffold(
        title = stringResource(CommonR.string.person_list_title),
        actions = {
            IconButton(onClick = onToggleTheme) {
                if (isDarkTheme) {
                    Icon(Icons.Filled.LightMode, contentDescription = stringResource(CommonR.string.action_toggle_theme))
                } else {
                    Icon(Icons.Filled.DarkMode, contentDescription = stringResource(CommonR.string.action_toggle_theme))
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Modern search filter
            var query by remember { mutableStateOf("") }
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = stringResource(CommonR.string.search_hint),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val filtered = personsState.value.filter { p ->
                    val q = query.trim().lowercase()
                    if (q.isEmpty()) true else
                        p.firstName.lowercase().contains(q) || p.lastName.lowercase().contains(q)
                }
                items(filtered) { p ->
                    Text("#${p.id} - ${p.lastName}, ${p.firstName} | ${p.age} " +
                        stringResource(CommonR.string.years_unit) +
                        " | " + stringResource(CommonR.string.label_weight) + ": ${p.weightKg}" +
                        stringResource(CommonR.string.kg_unit) +
                        " | " + stringResource(CommonR.string.label_left_handed) + ": " +
                        if (p.isLeftHanded) stringResource(CommonR.string.label_yes) else stringResource(CommonR.string.label_no)
                    )
                }
            }
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

// ================================
// COMPOSE PREVIEWS
// ================================

@Preview(name = "Person List Screen Light")
@Preview(name = "Person List Screen Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PersonListScreenPreview() {
    AppTheme {
        PersonListScreen(
            onBack = { },
            isDarkTheme = false,
            onToggleTheme = { }
        )
    }
}