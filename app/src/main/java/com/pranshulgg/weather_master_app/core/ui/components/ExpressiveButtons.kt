package com.pranshulgg.weather_master_app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Flat square Windows button (replaces the old Material expressive buttons). */
@Composable
private fun W10Button(
    text: String,
    icon: Int?,
    enabled: Boolean,
    filled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = MaterialTheme.colorScheme.primary
    val fg = MaterialTheme.colorScheme.onSurface
    val border = if (enabled) accent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    Row(
        modifier = modifier
            .border(if (filled) 0.dp else 2.dp, border, RectangleShape)
            .background(if (filled && enabled) accent else Color.Transparent, RectangleShape)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Symbol(icon, color = fg, size = 18.dp)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                color = if (enabled) fg else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun M3eButton(
    modifier: Modifier = Modifier,
    text: String,
    size: Dp = 40.dp,
    onClick: () -> Unit,
    icon: Int? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabled: Boolean = false
) {
    W10Button(text = text, icon = icon, enabled = !disabled, filled = true, onClick = onClick, modifier = modifier)
}

@Composable
fun M3eOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    size: Dp = 40.dp,
    onClick: () -> Unit,
    icon: Int? = null,
    disabled: Boolean = false
) {
    W10Button(text = text, icon = icon, enabled = !disabled, filled = false, onClick = onClick, modifier = modifier)
}

@Composable
fun M3eFilledTonalButton(
    modifier: Modifier = Modifier,
    text: String,
    size: Dp = 40.dp,
    onClick: () -> Unit,
    icon: Int? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabled: Boolean = false
) {
    W10Button(text = text, icon = icon, enabled = !disabled, filled = false, onClick = onClick, modifier = modifier)
}
