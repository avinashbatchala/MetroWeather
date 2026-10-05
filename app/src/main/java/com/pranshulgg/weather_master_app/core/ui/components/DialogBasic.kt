package com.pranshulgg.weather_master_app.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.metro.ui.components.MetroButton
import com.metro.ui.components.MetroDialogBox

/** Square Windows dialog with optional confirm/dismiss actions. */
@Composable
fun DialogBasic(
    show: Boolean,
    title: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit,
    showOnlyDismissAction: Boolean = false,
    showDefaultActions: Boolean = true,
    confirmBtnDisabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (!show) return

    MetroDialogBox(onDismiss = onDismiss) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column { content() }

        if (showDefaultActions) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
            ) {
                MetroButton(text = dismissText, onClick = onDismiss, outlined = true)
                if (!showOnlyDismissAction) {
                    MetroButton(
                        text = confirmText,
                        onClick = {
                            onConfirm()
                            onDismiss()
                        },
                        outlined = false,
                        enabled = !confirmBtnDisabled
                    )
                }
            }
        }
    }
}
