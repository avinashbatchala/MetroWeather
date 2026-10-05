package com.pranshulgg.weather_master_app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.pranshulgg.weather_master_app.R

/** Square Windows checkbox row. */
@Composable
fun CheckboxRow(
    label: String,
    checked: Boolean,
    hasPadding: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    val accent = MaterialTheme.colorScheme.primary
    val subtle = MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier
            .clickable { onCheckedChange(!checked) }
            .fillMaxWidth()
            .padding(
                horizontal = if (hasPadding) 16.dp else 0.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .border(2.dp, if (checked) accent else subtle, RectangleShape)
                .background(if (checked) accent else Color.Transparent, RectangleShape),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Symbol(R.drawable.check_24px, color = Color.White, size = 14.dp)
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            label,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
