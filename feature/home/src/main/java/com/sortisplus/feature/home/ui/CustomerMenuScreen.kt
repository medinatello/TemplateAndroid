package com.sortisplus.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sortisplus.core.designsystem.AppTheme
import com.sortisplus.core.ui.AppScaffold
import com.sortisplus.core.ui.MenuItemCard
import com.sortisplus.core.ui.PrimaryButton
import com.sortisplus.core.ui.ScreenContainer
import com.sortisplus.core.ui.SectionHeader
import com.sortisplus.core.common.R as CommonR

/**
 * Customer management menu with modern card-based design
 *
 * Organized menu for person/customer data operations including
 * viewing, creating, updating and deleting person records.
 *
 * @param onList Navigate to person list screen
 * @param onCreate Navigate to person creation screen
 * @param onDelete Navigate to person deletion screen
 * @param onFind Navigate to person search screen
 * @param onBack Navigate back to previous screen
 */
@Composable
fun CustomerMenuScreen(
    onList: () -> Unit,
    onCreate: () -> Unit,
    onDelete: () -> Unit,
    onFind: () -> Unit,
    onBack: () -> Unit
) {
    AppScaffold(title = stringResource(CommonR.string.customer_title)) { padding ->
        ScreenContainer {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Data management actions
                SectionHeader(title = stringResource(CommonR.string.menu_section_data))

                MenuItemCard(
                    title = stringResource(CommonR.string.customer_list_people),
                    subtitle = "Ver todas las personas registradas",
                    icon = Icons.Default.List,
                    onClick = onList
                )

                MenuItemCard(
                    title = stringResource(CommonR.string.customer_create_person),
                    subtitle = "Agregar nueva persona al sistema",
                    icon = Icons.Default.PersonAdd,
                    onClick = onCreate
                )

                MenuItemCard(
                    title = stringResource(CommonR.string.customer_find_person),
                    subtitle = "Buscar persona por ID",
                    icon = Icons.Default.Search,
                    onClick = onFind
                )

                MenuItemCard(
                    title = stringResource(CommonR.string.customer_delete_person),
                    subtitle = "Eliminar persona del sistema",
                    icon = Icons.Default.PersonRemove,
                    onClick = onDelete
                )

                Spacer(modifier = Modifier.weight(1f))

                // Back button at bottom
                PrimaryButton(
                    text = stringResource(CommonR.string.button_back),
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

// ================================
// COMPOSE PREVIEWS
// ================================

@Preview(name = "Customer Menu Light")
@Preview(name = "Customer Menu Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomerMenuScreenPreview() {
    AppTheme {
        CustomerMenuScreen(
            onList = { },
            onCreate = { },
            onDelete = { },
            onFind = { },
            onBack = { }
        )
    }
}
