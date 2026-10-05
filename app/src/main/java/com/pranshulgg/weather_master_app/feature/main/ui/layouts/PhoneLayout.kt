package com.pranshulgg.weather_master_app.feature.main.ui.layouts

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pranshulgg.weather_master_app.R
import com.pranshulgg.weather_master_app.core.model.domain.airquality.AirQuality
import com.pranshulgg.weather_master_app.core.model.domain.alerts.Alert
import com.pranshulgg.weather_master_app.core.model.domain.weather.Weather
import com.pranshulgg.weather_master_app.core.model.domain.weather.WeatherBlock
import com.pranshulgg.weather_master_app.core.model.domain.weather.WeatherUnits
import com.pranshulgg.weather_master_app.core.model.weather.TemperatureUnit
import com.pranshulgg.weather_master_app.core.model.weather.WeatherCondition
import com.pranshulgg.weather_master_app.core.model.weather.toIcon
import com.pranshulgg.weather_master_app.core.model.weather.toLabel
import com.pranshulgg.weather_master_app.core.prefs.AppPrefsState
import com.pranshulgg.weather_master_app.core.ui.components.WeatherIconBox
import com.metro.ui.components.MetroButton
import com.metro.ui.components.MetroDetailGrid
import com.metro.ui.components.MetroEmpty
import com.metro.ui.components.MetroListRow
import com.metro.ui.components.MetroPivot
import com.metro.ui.components.MetroSectionHeader
import com.metro.ui.components.PageInset
import com.pranshulgg.weather_master_app.core.ui.navigation.NavRoutes
import com.pranshulgg.weather_master_app.core.ui.theme.MetroColors
import com.pranshulgg.weather_master_app.core.utils.formatters.getCurrentTimeFor
import com.pranshulgg.weather_master_app.core.utils.formatters.getLastUpdatedTimeString
import com.pranshulgg.weather_master_app.core.utils.formatters.to12HourTimeString
import com.pranshulgg.weather_master_app.core.utils.formatters.to24HourTimeString
import com.pranshulgg.weather_master_app.core.utils.formatters.toWeekdayString
import com.pranshulgg.weather_master_app.core.utils.weather.forecast.findMatchingHourly
import com.pranshulgg.weather_master_app.core.utils.weather.location.getFullLocationName
import com.pranshulgg.weather_master_app.data.store.WeatherBlocksStoreState
import kotlin.math.roundToInt

/**
 * Windows 10 Mobile weather home: black canvas, condition-coloured hero and a
 * swipeable `now · hourly · daily · places` pivot. Content mirrors the wphone
 * MSN Weather app, restyled to the Metro language shared with the launcher.
 */
@Composable
fun PhoneLayout(
    weather: Weather,
    units: WeatherUnits,
    context: Context,
    isFroggyLayout: Boolean,
    navController: NavController,
    alerts: List<Alert>,
    prefs: AppPrefsState,
    onWeatherSourceInfoClick: () -> Unit,
    isShowSummary: Boolean,
    airQuality: AirQuality?,
    weatherBlocks: WeatherBlocksStoreState,
    onUpdateBlocks: (List<WeatherBlock>) -> Unit,
    onLocationSelect: (com.pranshulgg.weather_master_app.core.model.domain.location.Location) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = PageInset, vertical = 2.dp)) {
            Text(
                text = "METROWEATHER",
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = (weather.location.customName ?: weather.location.name).lowercase(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Light
                )
            )
        }

        MetroPivot(
            titles = listOf("now", "hourly", "daily", "places"),
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> NowPage(weather, units, context, alerts, prefs.is24HrTimeFormat)
                1 -> HourlyPage(weather, units, context, prefs)
                2 -> DailyPage(weather, units, context, prefs)
                else -> PlacesPage(weather, units, navController, onLocationSelect)
            }
        }
    }
}

@Composable
private fun NowPage(
    weather: Weather,
    units: WeatherUnits,
    context: Context,
    alerts: List<Alert>,
    is24: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PageInset)
    ) {
        WeatherHero(weather, units, context)

        if (alerts.isNotEmpty()) {
            MetroSectionHeader("alerts")
            alerts.take(2).forEach { alert ->
                MetroListRow(title = alert.event ?: "Weather alert", subtitle = alert.description)
            }
        }

        val current = weather.current
        val today = weather.daily.getOrNull(0)

        val windText = buildString {
            val speed = current.windSpeed?.roundToInt()
            if (speed != null) append("$speed ${units.windUnit.name.lowercase()}")
            if (current.windDirection != null) append(" ${current.windDirection.name}")
        }.ifBlank { "--" }

        MetroSectionHeader("details")
        MetroDetailGrid(
            entries = listOf(
                "wind" to windText,
                "humidity" to (current.humidity?.let { "${it.roundToInt()}%" } ?: "--"),
                "pressure" to (current.pressureMsl?.let { "${it.roundToInt()} hPa" } ?: "--"),
                "uv index" to (current.uvIndex?.let { "${it.roundToInt()} ${uvLabel(it)}" } ?: "--"),
                "visibility" to (current.visibility?.let { "${(it / 1000.0).let { km -> (km * 10).roundToInt() / 10.0 }} km" } ?: "--"),
                "precipitation" to (today?.precipitationProbabilityMax?.let { "$it%" } ?: "--"),
                "sunrise" to formatTime(today?.sunrise, weather.location.timezone, is24),
                "sunset" to formatTime(today?.sunset, weather.location.timezone, is24)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = getFullLocationName(weather.location),
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
            text = "Updated ${getLastUpdatedTimeString(context, current.lastUpdatedInMilli)} · Open-Meteo",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

@Composable
private fun WeatherHero(weather: Weather, units: WeatherUnits, context: Context) {
    val current = weather.current
    val today = weather.daily.getOrNull(0)
    val temp = TemperatureUnit.CELSIUS.convert(current.temperature, units.tempUnit)?.roundToInt()
    val feels = TemperatureUnit.CELSIUS.convert(current.feelsLike, units.tempUnit)?.roundToInt()
    val high = TemperatureUnit.CELSIUS.convert(today?.temperatureMax, units.tempUnit)?.roundToInt()
    val low = TemperatureUnit.CELSIUS.convert(today?.temperatureMin, units.tempUnit)?.roundToInt()
    val icon = current.weatherCondition.toIcon(
        daily = today,
        targetTimeMilli = getCurrentTimeFor(weather.location.timezone)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(conditionColor(current.weatherCondition), RectangleShape)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${temp ?: "-"}°",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(12.dp))
                WeatherIconBox(icon, size = 64.dp)
            }
            Text(
                text = current.weatherCondition.toLabel(context),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Feels like ${feels ?: "-"}°",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
            Text(
                text = "High ${high ?: "-"}°   Low ${low ?: "-"}°",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun HourlyPage(weather: Weather, units: WeatherUnits, context: Context, prefs: AppPrefsState) {
    val hours = findMatchingHourly(
        weather.hourly,
        System.currentTimeMillis(),
        weather.location.source,
        weather.location.timezone,
        alwaysReturn24Hrs = true,
        keepPastHour = false
    )
    if (hours.isEmpty()) {
        MetroEmpty("no hourly data", modifier = Modifier.padding(PageInset))
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PageInset)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        hours.take(24).forEachIndexed { index, item ->
            val temp = TemperatureUnit.CELSIUS.convert(item.temperature, units.tempUnit)?.roundToInt()
            val time = if (prefs.is24HrTimeFormat) {
                to24HourTimeString(item.time, weather.location.timezone)
            } else {
                to12HourTimeString(item.time, weather.location.timezone)
            }
            val pop = item.precipitationProbability
            MetroListRow(
                title = if (index == 0) "Now" else time,
                subtitle = buildString {
                    append(item.weatherCondition.toLabel(context))
                    if (pop != null && pop > 0) append(" · $pop%")
                },
                leadingIcon = item.weatherCondition.toIcon(targetTimeMilli = item.time),
                trailing = {
                    Text(
                        text = "${temp ?: "-"}°",
                        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DailyPage(weather: Weather, units: WeatherUnits, context: Context, prefs: AppPrefsState) {
    val daily = weather.daily
    if (daily.isEmpty()) {
        MetroEmpty("no daily data", modifier = Modifier.padding(PageInset))
        return
    }
    val lo = daily.mapNotNull { it.temperatureMin }.minOrNull() ?: 0.0
    val hi = daily.mapNotNull { it.temperatureMax }.maxOrNull() ?: 1.0
    val span = (hi - lo).takeIf { it > 0.0 } ?: 1.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PageInset)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        daily.forEachIndexed { index, day ->
            val max = TemperatureUnit.CELSIUS.convert(day.temperatureMax, units.tempUnit)?.roundToInt()
            val min = TemperatureUnit.CELSIUS.convert(day.temperatureMin, units.tempUnit)?.roundToInt()
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WeatherIconBox(
                        day.weatherCondition.toIcon(targetTimeMilli = day.time),
                        size = 32.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (index == 0) "Today" else toWeekdayString(day.time, weather.location.timezone),
                            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                        Text(
                            text = buildString {
                                append(day.weatherCondition.toLabel(context))
                                val pop = day.precipitationProbabilityMax
                                if (pop != null && pop > 0) append(" · $pop%")
                            },
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                    Text(
                        text = "$max° / $min°",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                }
                RangeBar(
                    startFraction = (((day.temperatureMin ?: lo) - lo) / span).toFloat().coerceIn(0f, 1f),
                    endFraction = (((day.temperatureMax ?: lo) - lo) / span).toFloat().coerceIn(0f, 1f),
                    modifier = Modifier.padding(top = 6.dp, start = 44.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RangeBar(startFraction: Float, endFraction: Float, modifier: Modifier = Modifier) {
    val start = startFraction.coerceIn(0f, 1f)
    val end = endFraction.coerceIn(start, 1f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f), RectangleShape)
    ) {
        if (start > 0.001f) {
            Spacer(modifier = Modifier.weight(start).fillMaxHeight())
        }
        Box(
            modifier = Modifier
                .weight((end - start).coerceAtLeast(0.05f))
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.onSurface, RectangleShape)
        )
        val tail = (1f - end).coerceAtLeast(0f)
        if (tail > 0.001f) {
            Spacer(modifier = Modifier.weight(tail).fillMaxHeight())
        }
    }
}

@Composable
private fun PlacesPage(
    weather: Weather,
    units: WeatherUnits,
    navController: NavController,
    onLocationSelect: (com.pranshulgg.weather_master_app.core.model.domain.location.Location) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    com.pranshulgg.weather_master_app.feature.locations.ui.PlacesPivotContent(
        onLocationSelect = onLocationSelect,
        onAddPlace = { navController.navigate(NavRoutes.SEARCH) },
        onEdit = { navController.navigate(NavRoutes.EDIT_LOCATION) },
        onPin = {
            com.pranshulgg.weather_master_app.synergy.WeatherTilePin.pin(
                context = context,
                placeLabel = weather.location.customName ?: weather.location.name,
                size = "wide"
            )
        },
        modifier = Modifier.fillMaxSize()
    )
}

private fun formatTime(millis: Long?, timezone: String, is24: Boolean): String {
    if (millis == null) return "--"
    return if (is24) to24HourTimeString(millis, timezone) else to12HourTimeString(millis, timezone)
}

private fun uvLabel(v: Double?): String = when {
    v == null -> ""
    v < 3 -> "low"
    v < 6 -> "moderate"
    v < 8 -> "high"
    v < 11 -> "very high"
    else -> "extreme"
}

private fun conditionColor(condition: WeatherCondition): Color = when (condition) {
    WeatherCondition.CLEAR_SKY,
    WeatherCondition.MOSTLY_CLEAR,
    WeatherCondition.VERY_HOT -> MetroColors.Blue

    WeatherCondition.PARTLY_CLOUDY,
    WeatherCondition.CLEAR_WITH_CLOUDY,
    WeatherCondition.CLOUDY_WITH_CLEAR,
    WeatherCondition.CLEAR_THEN_CLOUDY,
    WeatherCondition.CLOUDY_THEN_CLEAR -> Color(0xFF3A8FC9)

    WeatherCondition.OVERCAST -> Color(0xFF5F7383)

    WeatherCondition.FOG_HAZE -> Color(0xFF7D8A93)

    WeatherCondition.LIGHT_RAIN,
    WeatherCondition.RAIN,
    WeatherCondition.HEAVY_RAIN,
    WeatherCondition.CLEAR_WITH_RAIN,
    WeatherCondition.CLOUDY_WITH_RAIN,
    WeatherCondition.RAIN_WITH_CLEAR,
    WeatherCondition.RAIN_WITH_CLOUDY,
    WeatherCondition.RAIN_THEN_CLEAR,
    WeatherCondition.RAIN_THEN_CLOUDY,
    WeatherCondition.CLEAR_THEN_RAIN,
    WeatherCondition.CLOUDY_THEN_RAIN,
    WeatherCondition.MIXED_PRECIPITATION -> Color(0xFF3C5A78)

    WeatherCondition.LIGHT_SNOW,
    WeatherCondition.SNOW,
    WeatherCondition.HEAVY_SNOW,
    WeatherCondition.SLEET,
    WeatherCondition.CLEAR_WITH_SNOW,
    WeatherCondition.CLOUDY_WITH_SNOW,
    WeatherCondition.SNOW_WITH_CLEAR,
    WeatherCondition.SNOW_WITH_CLOUDY,
    WeatherCondition.SNOW_THEN_CLEAR,
    WeatherCondition.SNOW_THEN_CLOUDY,
    WeatherCondition.CLEAR_THEN_SNOW,
    WeatherCondition.CLOUDY_THEN_SNOW,
    WeatherCondition.RAIN_WITH_SNOW,
    WeatherCondition.SNOW_WITH_RAIN,
    WeatherCondition.RAIN_THEN_SNOW,
    WeatherCondition.SNOW_THEN_RAIN,
    WeatherCondition.VERY_COLD -> Color(0xFF7F9DB8)

    WeatherCondition.THUNDERSTORM,
    WeatherCondition.HAIL -> Color(0xFF3B3F5C)

    WeatherCondition.NO_CONDITION_FOUND -> MetroColors.Blue
}
