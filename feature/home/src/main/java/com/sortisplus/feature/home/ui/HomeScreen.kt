package com.sortisplus.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sortisplus.core.designsystem.AppTheme
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.PrimaryButton
import com.sortisplus.core.common.R as CommonR

/**
 * Home screen displaying the main welcome interface
 *
 * Shows the application title, description and a button to navigate
 * to the next screen. This is the entry point of the application.
 *
 * @param onContinue Callback function executed when user taps continue button
 */
@Composable
fun HomeScreen(onContinue: () -> Unit) {
    AppScaffold(title = stringResource(CommonR.string.home_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(CommonR.string.home_description))
            PrimaryButton(text = stringResource(CommonR.string.button_continue), onClick = onContinue)
        }
    }
}

/**
 * Details screen showing secondary content
 *
 * Displays static information and provides navigation back to the previous screen.
 * Used as a demonstration of navigation between screens.
 *
 * @param onBack Callback function executed when user wants to return to previous screen
 */
@Composable
fun DetailsScreen(onBack: () -> Unit) {
    AppScaffold(title = stringResource(CommonR.string.details_screen_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(CommonR.string.details_screen_message))
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack)
        }
    }
}

@Composable
fun GreetingScreen(onBack: () -> Unit) {
    AppScaffold(title = stringResource(CommonR.string.greeting_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(CommonR.string.greeting_message))
            PrimaryButton(text = stringResource(CommonR.string.button_back), onClick = onBack)
        }
    }
}

// ================================
// COMPOSE PREVIEWS
// ================================

@Preview(name = "Home Screen Light")
@Preview(name = "Home Screen Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(onContinue = { })
    }
}

@Preview(name = "Greeting Screen")
@Composable
fun GreetingScreenPreview() {
    AppTheme {
        GreetingScreen(onBack = { })
    }
}

@Preview(name = "Details Screen")
@Composable
fun DetailsScreenPreview() {
    AppTheme {
        DetailsScreen(onBack = { })
    }
}
