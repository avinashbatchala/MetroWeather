package com.pranshulgg.weather_master_app.feature.main.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pranshulgg.weather_master_app.core.model.domain.weather.Weather
import com.pranshulgg.weather_master_app.core.model.domain.weather.WeatherUnits
import com.pranshulgg.weather_master_app.core.model.weather.TemperatureUnit
import com.pranshulgg.weather_master_app.core.model.weather.toIcon
import com.pranshulgg.weather_master_app.core.model.weather.toLabel
import com.pranshulgg.weather_master_app.core.ui.components.WeatherIconBox
import com.pranshulgg.weather_master_app.core.utils.formatters.getCurrentTimeFor
import kotlin.math.roundToInt

/**
 * Windows 10 Mobile style current-conditions "live tile": a large square accent panel
 * with the location, oversized light temperature, condition and high/low.
 */
@Composable
fun MetroCurrentWeatherHero(
    weather: Weather,
    units: WeatherUnits,
    context: Context,
    modifier: Modifier = Modifier
) {
    val current = weather.current
    val daily = weather.daily.getOrNull(0)

    val currentTemp = TemperatureUnit.CELSIUS.convert(current.temperature, units.tempUnit)?.roundToInt()
    val feelsLike = TemperatureUnit.CELSIUS.convert(current.feelsLike, units.tempUnit)?.roundToInt()
    val maxTemp = TemperatureUnit.CELSIUS.convert(daily?.temperatureMax, units.tempUnit)?.roundToInt()
    val minTemp = TemperatureUnit.CELSIUS.convert(daily?.temperatureMin, units.tempUnit)?.roundToInt()

    val displayName = weather.location.customName?.takeIf { it.isNotBlank() } ?: weather.location.name
    val icon = current.weatherCondition.toIcon(
        targetTimeMilli = getCurrentTimeFor(weather.location.timezone),
        daily = weather.daily.firstOrNull()
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.primary, RectangleShape)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = displayName,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${currentTemp ?: "-"}°",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(12.dp))
                WeatherIconBox(icon, size = 64.dp)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = current.weatherCondition.toLabel(context),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "High ${maxTemp ?: "-"}°   Low ${minTemp ?: "-"}°   Feels like ${feelsLike ?: "-"}°",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
