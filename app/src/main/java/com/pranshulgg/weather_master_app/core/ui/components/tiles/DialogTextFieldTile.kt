package com.pranshulgg.weather_master_app.core.ui.components.tiles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pranshulgg.weather_master_app.core.ui.metro.MetroButton
import com.pranshulgg.weather_master_app.core.ui.metro.MetroDialogBox
import com.pranshulgg.weather_master_app.core.ui.metro.MetroSearchBox

@Composable
fun DialogTextFieldTile(
    headline: String,
    description: String? = null,
    initialText: String = "",
    onTextSubmitted: (String) -> Unit,
    leading: @Composable (() -> Unit)? = null,
    placeholder: String,
    placeholderTextField: String,
    shapes: RoundedCornerShape,
    itemBgColor: Color = Color.Unspecified,
    trailing: (@Composable (() -> Unit))? = null,
    placeholderAsValue: Boolean = false,
    overline: @Composable (() -> Unit)? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    var textFieldValue by remember(initialText) { mutableStateOf(initialText) }

    val descriptionText = when {
        description != null -> description
        textFieldValue.isNotBlank() -> textFieldValue
        else -> placeholder
    }

    FlatRow(
        title = headline,
        description = descriptionText,
        leading = leading,
        trailing = trailing,
        overline = overline,
        onClick = { showDialog = true }
    )

    if (showDialog) {
        MetroDialogBox(onDismiss = { showDialog = false }) {
            Text(
                text = headline,
                style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.height(12.dp))
            MetroSearchBox(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                placeholder = placeholderTextField,
                leadingIcon = null
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
            ) {
                MetroButton(text = "Cancel", onClick = { showDialog = false }, outlined = true)
                MetroButton(
                    text = "Save",
                    onClick = {
                        onTextSubmitted(textFieldValue)
                        showDialog = false
                    },
                    outlined = false
                )
            }
        }
    }
}
