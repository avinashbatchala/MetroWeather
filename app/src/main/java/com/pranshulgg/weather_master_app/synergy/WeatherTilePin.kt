package com.pranshulgg.weather_master_app.synergy

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * Bridges MetroWeather to the Win10 Start launcher: sends a request that the launcher
 * turns into a pinned Weather live tile on its Start screen.
 *
 * The launcher exposes a permission-guarded, no-UI activity for
 * `com.ab.action.PIN_WEATHER_TILE`. This app declares that action in its `<queries>` so
 * the activity is visible on Android 11+. If the launcher is not installed the request
 * is a no-op and we surface a short hint.
 */
object WeatherTilePin {

    const val ACTION_PIN_WEATHER_TILE = "com.ab.action.PIN_WEATHER_TILE"
    private const val TAG = "WeatherTilePin"
    private const val EXTRA_PLACE_LABEL = "place_label"
    private const val EXTRA_SIZE = "size"
    private const val EXTRA_LATITUDE = "lat"
    private const val EXTRA_LONGITUDE = "lon"
    private const val EXTRA_TIMEZONE = "tz"
    private const val EXTRA_LOCATION_ID = "lid"

    /**
     * Returns true if a launcher handled the request. The city's coordinates are sent so the
     * launcher can show that city's weather instead of falling back to IP geolocation.
     */
    fun pin(
        context: Context,
        placeLabel: String?,
        latitude: Double? = null,
        longitude: Double? = null,
        timezone: String? = null,
        locationId: String? = null,
        size: String = "wide"
    ): Boolean {
        val intent = Intent(ACTION_PIN_WEATHER_TILE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra(EXTRA_PLACE_LABEL, placeLabel)
            putExtra(EXTRA_SIZE, size)
            if (latitude != null && longitude != null) {
                putExtra(EXTRA_LATITUDE, latitude)
                putExtra(EXTRA_LONGITUDE, longitude)
            }
            if (timezone != null) putExtra(EXTRA_TIMEZONE, timezone)
            if (locationId != null) putExtra(EXTRA_LOCATION_ID, locationId)
            // Safe when called from a non-Activity context and harmless otherwise.
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(intent)
            Log.d(TAG, "pin request sent for place=$placeLabel size=$size lat=$latitude lon=$longitude")
            true
        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "no launcher handled $ACTION_PIN_WEATHER_TILE", e)
            Toast.makeText(context, "Install the Win10 Start launcher to pin a tile", Toast.LENGTH_SHORT).show()
            false
        } catch (e: Exception) {
            Log.e(TAG, "pin request failed", e)
            false
        }
    }
}
