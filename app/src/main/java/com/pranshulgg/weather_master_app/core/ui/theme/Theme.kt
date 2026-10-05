package com.pranshulgg.weather_master_app.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.metro.ui.theme.MetroTheme
import com.pranshulgg.weather_master_app.core.prefs.LocalAppPrefs

/**
 * App entry to the shared Windows Metro theme (`com.metro.ui.theme.MetroTheme`).
 * Keeps the app-specific system-bar handling and theme preferences.
 */
@Composable
fun WeatherMasterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    seedColor: Color = MetroColors.Blue,
    @Suppress("UNUSED_PARAMETER") dynamicTheme: Boolean = false,
    @Suppress("UNUSED_PARAMETER") themeVariantType: ThemeVariantType,
    applySystemUi: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode && applySystemUi) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MetroTheme(
        accentColor = seedColor,
        darkTheme = darkTheme,
        content = content
    )
}

@Composable
fun isThemeDark(): Boolean {
    val prefs = LocalAppPrefs.current

    return when (prefs.appTheme) {
        "Dark" -> true
        "Light" -> false
        else -> isSystemInDarkTheme()
    }
}
