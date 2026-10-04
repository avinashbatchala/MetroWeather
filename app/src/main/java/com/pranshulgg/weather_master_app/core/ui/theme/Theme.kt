package com.pranshulgg.weather_master_app.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.pranshulgg.weather_master_app.core.prefs.LocalAppPrefs

/**
 * Windows 10 Mobile / Windows Phone Metro theme.
 *
 * Replaces the dynamic Material You / MaterialKolor theming with the flat Windows
 * visual language: pure black or white canvases, a single accent colour, square
 * geometry and thin Segoe-style typography.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeatherMasterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    seedColor: Color = MetroColors.Blue,
    @Suppress("UNUSED_PARAMETER") dynamicTheme: Boolean = false,
    @Suppress("UNUSED_PARAMETER") themeVariantType: ThemeVariantType,
    applySystemUi: Boolean = true,
    content: @Composable () -> Unit
) {
    val accent = seedColor

    val background = if (darkTheme) MetroColors.BackgroundBlack else MetroColors.BackgroundWhite
    val container = if (darkTheme) MetroColors.SurfaceDark else MetroColors.SurfaceLight
    val onBackground = if (darkTheme) MetroColors.TextWhite else MetroColors.TextBlack
    val subtle = if (darkTheme) MetroColors.TextDim else MetroColors.TextSubtle

    // Canvas stays pure black/white; tiles/cards use `surface`, which is a slightly
    // lighter shade so the square Metro tiles read against the background.
    val tileHigh = if (darkTheme) Color(0xFF262626) else Color(0xFFE8E8E8)
    val tileHighest = if (darkTheme) Color(0xFF2E2E2E) else Color(0xFFDEDEDE)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = accent,
            onPrimary = Color.White,
            primaryContainer = accent,
            onPrimaryContainer = Color.White,
            inversePrimary = accent,
            secondary = accent,
            onSecondary = Color.White,
            background = background,
            onBackground = onBackground,
            surface = container,
            onSurface = onBackground,
            surfaceVariant = tileHigh,
            onSurfaceVariant = subtle,
            surfaceContainer = container,
            surfaceContainerHigh = tileHigh,
            surfaceContainerHighest = tileHighest,
            outline = subtle,
            outlineVariant = MetroColors.DividerDark
        )
    } else {
        lightColorScheme(
            primary = accent,
            onPrimary = Color.White,
            primaryContainer = accent,
            onPrimaryContainer = Color.White,
            inversePrimary = accent,
            secondary = accent,
            onSecondary = Color.White,
            background = background,
            onBackground = onBackground,
            surface = container,
            onSurface = onBackground,
            surfaceVariant = tileHigh,
            onSurfaceVariant = subtle,
            surfaceContainer = container,
            surfaceContainerHigh = tileHigh,
            surfaceContainerHighest = tileHighest,
            outline = subtle,
            outlineVariant = MetroColors.DividerLight
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode && applySystemUi) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = getAppTypography(useGoogleSans = false),
        shapes = MetroShapes,
        motionScheme = MotionScheme.expressive(),
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
