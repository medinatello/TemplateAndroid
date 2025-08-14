package com.sortisplus.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sortisplus.core.ui.theme.Dimensions
import com.sortisplus.core.ui.theme.SortisPreviewTheme

/**
 * Primary button component following Material 3 design
 * High emphasis button for primary actions
 */
@Composable
fun SortisPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    contentDescription: String? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(Dimensions.buttonHeight)
            .semantics {
                contentDescription?.let { this.contentDescription = it }
            },
        enabled = enabled && !isLoading,
        contentPadding = PaddingValues(
            horizontal = Dimensions.paddingMedium,
            vertical = Dimensions.paddingSmall
        )
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dimensions.iconSizeSmall),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            }
            leadingIcon != null -> {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.iconSize)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = Dimensions.paddingSmall)
                )
            }
            else -> {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Secondary button component
 * Medium emphasis button for secondary actions
 */
@Composable
fun SortisSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    contentDescription: String? = null
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .height(Dimensions.buttonHeight)
            .semantics {
                contentDescription?.let { this.contentDescription = it }
            },
        enabled = enabled && !isLoading,
        contentPadding = PaddingValues(
            horizontal = Dimensions.paddingMedium,
            vertical = Dimensions.paddingSmall
        )
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dimensions.iconSizeSmall),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    strokeWidth = 2.dp
                )
            }
            leadingIcon != null -> {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.iconSize)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = Dimensions.paddingSmall)
                )
            }
            else -> {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Outlined button component
 * Medium emphasis button with outline
 */
@Composable
fun SortisOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    contentDescription: String? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(Dimensions.buttonHeight)
            .semantics {
                contentDescription?.let { this.contentDescription = it }
            },
        enabled = enabled && !isLoading,
        contentPadding = PaddingValues(
            horizontal = Dimensions.paddingMedium,
            vertical = Dimensions.paddingSmall
        ),
        border = ButtonDefaults.outlinedButtonBorder
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dimensions.iconSizeSmall),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
            }
            leadingIcon != null -> {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.iconSize)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = Dimensions.paddingSmall)
                )
            }
            else -> {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Text button component
 * Low emphasis button for tertiary actions
 */
@Composable
fun SortisTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .height(Dimensions.buttonHeight)
            .semantics {
                contentDescription?.let { this.contentDescription = it }
            },
        enabled = enabled,
        contentPadding = PaddingValues(
            horizontal = Dimensions.paddingMedium,
            vertical = Dimensions.paddingSmall
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    SortisPreviewTheme {
        Column(
            modifier = Modifier.padding(Dimensions.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingSmall)
        ) {
            SortisPrimaryButton(
                text = "Primary Button",
                onClick = { }
            )

            SortisPrimaryButton(
                text = "Loading",
                onClick = { },
                isLoading = true
            )

            SortisSecondaryButton(
                text = "Secondary Button",
                onClick = { }
            )

            SortisOutlinedButton(
                text = "Outlined Button",
                onClick = { }
            )

            SortisTextButton(
                text = "Text Button",
                onClick = { }
            )

            SortisPrimaryButton(
                text = "Disabled",
                onClick = { },
                enabled = false
            )
        }
    }
}
