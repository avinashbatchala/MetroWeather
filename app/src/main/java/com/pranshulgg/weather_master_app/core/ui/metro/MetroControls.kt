package com.pranshulgg.weather_master_app.core.ui.metro

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pranshulgg.weather_master_app.core.ui.components.Symbol
import kotlin.math.roundToInt

val PageInset = 16.dp

private val accent: Color
    @Composable get() = MaterialTheme.colorScheme.primary

private val onSurface: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

private val subtle: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

/** Small uppercase accent section heading (Windows settings/list style). */
@Composable
fun MetroSectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelLarge.copy(color = accent),
        modifier = modifier.padding(top = 18.dp, bottom = 6.dp)
    )
}

/** Label/value information row used in the weather detail grid. */
@Composable
fun MetroDetail(label: String, value: String, modifier: Modifier = Modifier, leading: (@Composable () -> Unit)? = null) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leading != null) {
                leading()
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label.lowercase(),
                style = MaterialTheme.typography.labelMedium.copy(color = subtle)
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(color = onSurface),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** Two-column grid of [MetroDetail] rows. */
@Composable
fun MetroDetailGrid(entries: List<Pair<String, String>>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        entries.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                row.forEach { (label, value) ->
                    MetroDetail(label = label, value = value, modifier = Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

/** Windows list row: optional leading drawable, big light title, subtle subtitle, trailing slot. */
@Composable
fun MetroListRow(
    title: String,
    subtitle: String? = null,
    leadingIcon: Int? = null,
    leadingTint: Color? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Box(
                modifier = Modifier.size(44.dp).background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Symbol(leadingIcon, color = leadingTint ?: onSurface, size = 22.dp)
            }
            Spacer(modifier = Modifier.width(14.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(color = onSurface),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = subtle),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (trailing != null) {
            Spacer(modifier = Modifier.width(10.dp))
            trailing()
        }
    }
}

/** Flat rectangular Windows button. */
@Composable
fun MetroButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    outlined: Boolean = true
) {
    val border = when {
        !outlined -> Color.Transparent
        enabled -> accent
        else -> subtle
    }
    Box(
        modifier = modifier
            .border(if (outlined) 2.dp else 0.dp, border, RectangleShape)
            .background(if (!outlined && enabled) accent else Color.Transparent, RectangleShape)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge.copy(color = onSurface))
    }
}

/** Compact rectangular Windows toggle. */
@Composable
fun MetroToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val border = if (checked) accent else subtle
    Row(
        modifier = modifier
            .toggleable(value = checked, enabled = enabled, onValueChange = onCheckedChange)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 22.dp)
                .border(2.dp, border, RectangleShape)
                .background(if (checked) accent else Color.Transparent, RectangleShape)
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                    .background(onSurface, RectangleShape)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = if (checked) "On" else "Off", style = MaterialTheme.typography.bodyLarge.copy(color = onSurface))
    }
}

/** Windows circular radio group rendered vertically. */
@Composable
fun MetroRadioGroup(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(selected = selected, onClick = { onSelect(index) })
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(2.dp, if (selected) accent else subtle, RectangleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (selected) {
                        Box(modifier = Modifier.size(10.dp).background(accent, RectangleShape))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(color = onSurface))
            }
        }
    }
}

/** Thin Windows slider with a rectangular thumb. */
@Composable
fun MetroSlider(value: Float, onValueChange: (Float) -> Unit, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    var isDragging by remember { mutableStateOf(false) }
    var dragFraction by remember { mutableFloatStateOf(value.coerceIn(0f, 1f)) }
    val currentFraction = if (isDragging) dragFraction else value.coerceIn(0f, 1f)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val f = (offset.x / size.width).coerceIn(0f, 1f)
                    dragFraction = f
                    onValueChange(f)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, _ ->
                        change.consume()
                        val f = (change.position.x / size.width).coerceIn(0f, 1f)
                        dragFraction = f
                        onValueChange(f)
                    }
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val totalWidthPx = constraints.maxWidth.toFloat()
        val thumbPx = with(density) { 10.dp.toPx() }
        val trackColor = MaterialTheme.colorScheme.surfaceVariant
        Box(Modifier.fillMaxWidth().height(4.dp).background(trackColor, RectangleShape))
        Box(Modifier.fillMaxWidth(currentFraction).height(4.dp).background(accent, RectangleShape))
        val travel = (totalWidthPx - thumbPx).coerceAtLeast(0f)
        Box(
            modifier = Modifier
                .offset { IntOffset((currentFraction * travel).roundToInt(), 0) }
                .size(width = 10.dp, height = 24.dp)
                .background(accent, RectangleShape)
        )
    }
}

/** Rectangular Windows search field with a right-aligned hint. */
@Composable
fun MetroSearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(MaterialTheme.colorScheme.surface, RectangleShape)
            .border(2.dp, MaterialTheme.colorScheme.outline, RectangleShape)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Symbol(leadingIcon, color = subtle, size = 20.dp)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(placeholder, style = MaterialTheme.typography.bodyLarge.copy(color = subtle))
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = onSurface),
                cursorBrush = SolidColor(onSurface),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/** Square Windows dialog surface with arbitrary content. */
@Composable
fun MetroDialogBox(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RectangleShape)
                .border(1.dp, subtle, RectangleShape)
                .padding(20.dp),
            content = content
        )
    }
}

/** Square confirmation dialog. */
@Composable
fun MetroDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    MetroDialogBox(onDismiss = onDismiss) {
        Text(title, style = MaterialTheme.typography.headlineSmall.copy(color = onSurface))
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium.copy(color = subtle))
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)) {
            MetroButton(text = dismissText, onClick = onDismiss, outlined = true)
            MetroButton(text = confirmText, onClick = onConfirm, outlined = false)
        }
    }
}

/** Windows indeterminate progress: five accent dots drifting right. */
@Composable
fun MetroLoadingDots(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "loading")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val t = (((phase - index * 0.12f) % 1f) + 1f) % 1f
            val alpha = 0.2f + 0.8f * (1f - kotlin.math.abs(t - 0.5f) * 2f).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(accent.copy(alpha = alpha), RectangleShape)
            )
        }
    }
}

/** Empty/loading placeholder text. */
@Composable
fun MetroEmpty(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(color = subtle),
        modifier = modifier.padding(vertical = 16.dp)
    )
}
