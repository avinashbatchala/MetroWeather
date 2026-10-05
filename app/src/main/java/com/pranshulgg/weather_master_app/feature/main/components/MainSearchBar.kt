package com.pranshulgg.weather_master_app.feature.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pranshulgg.weather_master_app.R
import com.pranshulgg.weather_master_app.core.model.domain.location.Location
import com.pranshulgg.weather_master_app.core.ui.components.Symbol
import com.pranshulgg.weather_master_app.core.ui.navigation.NavRoutes
import com.pranshulgg.weather_master_app.core.utils.weather.location.getFullLocationName

/** Flat Windows top bar: square refresh / edit / settings actions and a location title. */
@Composable
fun MainSearchBar(
    isFroggyLayout: Boolean = false,
    paddingValues: PaddingValues,
    navController: NavController,
    activeLocation: Location?,
    onEditLocation: () -> Unit,
    layoutDirection: LayoutDirection,
    onRefresh: () -> Unit = {}
) {
    val startPadding = paddingValues.calculateStartPadding(layoutDirection)
    val endPadding = paddingValues.calculateEndPadding(layoutDirection)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = paddingValues.calculateTopPadding() + 4.dp,
                start = startPadding + 16.dp,
                end = endPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = getFullLocationName(activeLocation),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        BarAction(R.drawable.refresh_24px, "Refresh") { onRefresh() }
        BarAction(R.drawable.edit_24px, "Edit location") { onEditLocation() }
        BarAction(R.drawable.settings_24px, "Settings") { navController.navigate(NavRoutes.SETTINGS) }
    }
}

@Composable
private fun BarAction(icon: Int, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Symbol(icon, desc = description, color = MaterialTheme.colorScheme.onSurface, size = 22.dp)
    }
}
