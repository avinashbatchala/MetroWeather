package com.pranshulgg.weather_master_app.core.ui.components.tiles
import com.metro.ui.components.FlatRow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.metro.ui.components.MetroButton
import com.metro.ui.components.MetroDialogBox
import com.metro.ui.components.MetroSlider

@Composable
fun DialogSliderTile(
    headline: String,
    description: String? = null,
    initialValue: Float = 0.5f,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueSubmitted: (Float) -> Unit,
    leading: @Composable (() -> Unit)? = null,
    shapes: RoundedCornerShape,
    labelFormatter: (Float) -> String = { it.toString() },
    dialogTitle: String,
    isDescriptionAsValue: Boolean = false,
    itemBgColor: Color = Color.Unspecified
) {
    var showDialog by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableStateOf(initialValue) }

    FlatRow(
        title = headline,
        description = description ?: labelFormatter(sliderValue),
        leading = leading,
        onClick = { showDialog = true }
    )

    if (showDialog) {
        MetroDialogBox(onDismiss = { showDialog = false }) {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = labelFormatter(sliderValue),
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.height(6.dp))
            LabeledSlider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = valueRange,
                steps = steps,
                labelFormatter = labelFormatter
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
            ) {
                MetroButton(text = "Cancel", onClick = { showDialog = false }, outlined = true)
                MetroButton(
                    text = "Save",
                    onClick = {
                        onValueSubmitted(sliderValue)
                        showDialog = false
                    },
                    outlined = false
                )
            }
        }
    }
}

/** Thin Windows slider bound to a value range (no Material thumb/label bubble). */
@Composable
fun LabeledSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    labelFormatter: (Float) -> String = { it.toString() }
) {
    val span = (valueRange.endInclusive - valueRange.start).takeIf { it != 0f } ?: 1f
    val fraction = ((value - valueRange.start) / span).coerceIn(0f, 1f)
    Column(modifier = Modifier.fillMaxWidth()) {
        MetroSlider(
            value = fraction,
            onValueChange = { onValueChange(valueRange.start + it * span) }
        )
    }
}
