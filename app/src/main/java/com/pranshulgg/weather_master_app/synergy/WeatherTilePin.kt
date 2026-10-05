package com.pranshulgg.weather_master_app.synergy

import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Bridges MetroWeather to the Win10 Start launcher: sends a request that the launcher
 * turns into a pinned Weather live tile on its Start screen.
 *
 * The launcher exposes a permission-guarded, no-UI activity for
 * `com.ab.action.PIN_WEATHER_TILE`. If the launcher is not installed the request is a
 * no-op and we surface a short hint.
 */
object WeatherTilePin {

    const val ACTION_PIN_WEATHER_TILE = "com.ab.action.PIN_WEATHER_TILE"
    private const val EXTRA_PLACE_LABEL = "place_label"
    private const val EXTRA_SIZE = "size"

    /** Returns true if a launcher handled the request. */
    fun pin(context: Context, placeLabel: String?, size: String = "wide"): Boolean {
        return try {
            val intent = Intent(ACTION_PIN_WEATHER_TILE).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                putExtra(EXTRA_PLACE_LABEL, placeLabel)
                putExtra(EXTRA_SIZE, size)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            Toast.makeText(context, "Install the Win10 Start launcher to pin a tile", Toast.LENGTH_SHORT).show()
            false
        }
    }
}
