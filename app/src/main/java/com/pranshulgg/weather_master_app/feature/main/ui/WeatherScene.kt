package com.pranshulgg.weather_master_app.feature.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.pranshulgg.weather_master_app.core.model.domain.weather.Weather
import com.pranshulgg.weather_master_app.core.ui.theme.isThemeDark
import com.pranshulgg.weather_master_app.core.utils.weather.cache.isWeatherDomainSafe
import com.pranshulgg.weather_master_app.feature.main.ui.weatherAnimations.WeatherAnimations

/** Foreground colour that sits on top of the active weather scene. */
val LocalWeatherForeground = staticCompositionLocalOf { Color.White }

@Composable
fun weatherForeground(): Color = if (isThemeDark()) Color.White else Color(0xFF1F1F1F)

/**
 * Full-bleed condition scene: a day/night gradient plus the subtle animated
 * overlay (sun, clouds, rain, snow ...) that makes the app read as weather.
 */
@Composable
fun WeatherBackground(
    weather: Weather?,
    showAnimations: Boolean = true,
    parallaxPx: Float = 0f
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { translationY = parallaxPx }
    ) {
        BackgroundGradient(weather, isScrolled = false)
        if (showAnimations && weather != null && isWeatherDomainSafe(weather)) {
            WeatherAnimations(weather, isFroggyLayout = false)
        }
    }
}
