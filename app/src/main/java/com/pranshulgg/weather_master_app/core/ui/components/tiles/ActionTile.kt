package com.pranshulgg.weather_master_app.core.ui.components.tiles
import com.metro.ui.components.FlatRow

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ActionTile(
    headline: String,
    description: String? = null,
    leading: @Composable (() -> Unit)? = null,
    shapes: RoundedCornerShape,
    onClick: () -> Unit,
    colorDesc: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    danger: Boolean = false,
    itemBgColor: Color = Color.Unspecified,
    selected: Boolean = false,
    trailing: @Composable (() -> Unit)? = null,
    overline: @Composable (() -> Unit)? = null,
) {
    FlatRow(
        title = headline,
        description = description,
        leading = leading,
        trailing = trailing,
        overline = overline,
        selected = selected,
        onClick = onClick,
        titleColor = when {
            danger -> MaterialTheme.colorScheme.error
            selected -> MaterialTheme.colorScheme.primary
            else -> null
        }
    )
}
