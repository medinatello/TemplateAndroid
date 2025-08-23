package com.sortisplus.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.sortisplus.core.ui.theme.Dimensions
import com.sortisplus.core.ui.theme.SortisPreviewTheme

/**
 * Validated text field component with error state support
 * Follows Material 3 design guidelines and accessibility standards
 */
@Composable
fun SortisTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = if (isError && errorMessage != null) {
                {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else null,
            isError = isError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription?.let { this.contentDescription = it }
                }
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = Dimensions.paddingMedium,
                    top = Dimensions.paddingExtraSmall
                )
            )
        }
    }
}

/**
 * Password text field with visibility toggle
 */
@Composable
fun SortisPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    contentDescription: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    SortisTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        keyboardType = KeyboardType.Password,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        modifier = modifier,
        contentDescription = contentDescription
    )
}

/**
 * Email text field with proper keyboard type
 */
@Composable
fun SortisEmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    contentDescription: String? = null
) {
    SortisTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        modifier = modifier,
        contentDescription = contentDescription
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPreview() {
    SortisPreviewTheme {
        Column(
            modifier = Modifier.padding(Dimensions.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.paddingMedium)
        ) {
            SortisTextField(
                value = "",
                onValueChange = { },
                label = "Normal Field"
            )

            SortisTextField(
                value = "Invalid input",
                onValueChange = { },
                label = "Error Field",
                isError = true,
                errorMessage = "This field contains an error"
            )

            SortisEmailTextField(
                value = "",
                onValueChange = { },
                label = "Email",
                placeholder = "example@domain.com"
            )

            SortisPasswordTextField(
                value = "",
                onValueChange = { },
                label = "Password",
                placeholder = "Enter your password"
            )
        }
    }
}
