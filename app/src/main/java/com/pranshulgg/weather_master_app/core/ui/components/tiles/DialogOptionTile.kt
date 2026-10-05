package com.pranshulgg.weather_master_app.core.ui.components.tiles
import com.metro.ui.components.FlatRow

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pranshulgg.weather_master_app.R
import com.metro.ui.components.MetroButton
import com.metro.ui.components.MetroDialogBox
import com.metro.ui.components.MetroRadioGroup

data class DialogOption<T>(
    val value: T,
    val label: String
)

@Composable
fun <T> DialogOptionTile(
    headline: String,
    description: String? = null,
    options: List<DialogOption<T>>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    leading: @Composable (() -> Unit)? = null,
    shapes: RoundedCornerShape,
    dialogTitle: String? = null,
    itemBgColor: Color = Color.Unspecified
) {
    var showDialog by remember { mutableStateOf(false) }
    val selectedLabel = options.find { it.value == selectedOption }?.label

    FlatRow(
        title = headline,
        description = description ?: selectedLabel,
        leading = leading,
        onClick = { showDialog = true }
    )

    if (showDialog) {
        var tempSelection by remember { mutableStateOf(selectedOption) }
        MetroDialogBox(onDismiss = { showDialog = false }) {
            Text(
                text = dialogTitle ?: headline,
                style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MetroRadioGroup(
                options = options.map { it.label },
                selectedIndex = options.indexOfFirst { it.value == tempSelection },
                onSelect = { index -> tempSelection = options[index].value }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
            ) {
                MetroButton(text = stringResource(R.string.action_cancel), onClick = { showDialog = false }, outlined = true)
                MetroButton(
                    text = stringResource(R.string.action_save),
                    onClick = {
                        tempSelection?.let { onOptionSelected(it) }
                        showDialog = false
                    },
                    outlined = false
                )
            }
        }
    }
}
