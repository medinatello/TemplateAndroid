package com.sortisplus.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.PrimaryButton
import com.sortisplus.core.common.R

@Composable
fun HomeScreen(onContinue: () -> Unit) {
    AppScaffold(title = stringResource(R.string.home_screen_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.home_screen_welcome_message))
            PrimaryButton(text = stringResource(R.string.button_continue), onClick = onContinue)
        }
    }
}

@Composable
fun DetailsScreen(onBack: () -> Unit) {
    AppScaffold(title = stringResource(R.string.details_screen_title)) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.details_screen_message))
            PrimaryButton(text = stringResource(R.string.button_back), onClick = onBack)
        }
    }
}
