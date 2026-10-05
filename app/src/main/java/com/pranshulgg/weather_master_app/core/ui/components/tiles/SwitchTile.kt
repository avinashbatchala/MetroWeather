package com.pranshulgg.weather_master_app.core.ui.components.tiles
import com.metro.ui.components.FlatRow

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.metro.ui.components.MetroToggle

@Composable
fun SwitchTile(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    headline: String,
    description: String? = null,
    leading: @Composable (() -> Unit)? = null,
    shapes: RoundedCornerShape,
    switchEnabled: Boolean = true,
    itemBgColor: Color = Color.Unspecified
) {
    FlatRow(
        title = headline,
        description = description,
        leading = leading,
        onClick = if (switchEnabled) ({ onCheckedChange(!checked) }) else null,
        trailing = {
            MetroToggle(
                checked = checked,
                onCheckedChange = { if (checked != it) onCheckedChange(it) },
                enabled = switchEnabled
            )
        }
    )
}

@Composable
fun SingleSwitchTile(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    headline: String,
    description: String? = null,
    leading: @Composable (() -> Unit)? = null,
    switchEnabled: Boolean = true,
    itemBgColor: Color = Color.Unspecified
) {
    FlatRow(
        title = headline,
        description = description,
        leading = leading,
        onClick = if (switchEnabled) ({ onCheckedChange(!checked) }) else null,
        trailing = {
            MetroToggle(
                checked = checked,
                onCheckedChange = { if (checked != it) onCheckedChange(it) },
                enabled = switchEnabled
            )
        }
    )
}
