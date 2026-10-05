package com.pranshulgg.weather_master_app.core.ui.components

import androidx.compose.runtime.Composable
import com.pranshulgg.weather_master_app.core.ui.metro.MetroDialog

@Composable
fun TextAlertDialog(
    show: Boolean,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!show) return
    MetroDialog(
        title = title,
        message = message,
        confirmText = confirmText,
        dismissText = dismissText,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}
