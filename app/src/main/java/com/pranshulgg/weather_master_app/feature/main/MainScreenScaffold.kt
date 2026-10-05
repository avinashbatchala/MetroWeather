package com.pranshulgg.weather_master_app.feature.main

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pranshulgg.weather_master_app.core.model.domain.location.Location
import com.pranshulgg.weather_master_app.core.model.domain.weather.WeatherBlock
import com.pranshulgg.weather_master_app.core.prefs.AppPrefsState
import com.pranshulgg.weather_master_app.data.store.WeatherBlocksStoreState
import com.pranshulgg.weather_master_app.data.store.WeatherStoreState
import com.pranshulgg.weather_master_app.data.store.WeatherUnitsStoreState
import com.pranshulgg.weather_master_app.feature.main.components.MainSearchBar
import com.pranshulgg.weather_master_app.feature.main.ui.layouts.PhoneLayout
import com.pranshulgg.weather_master_app.feature.main.ui.layouts.TabletLayout

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainScreenScaffold(
    navController: NavController,
    drawerState: DrawerState,
    weatherStore: WeatherStoreState,
    onRefresh: () -> Unit,
    onEditLocation: () -> Unit,
    context: Context,
    onWeatherSourceInfoClick: () -> Unit,
    isTabletLike: Boolean = false,
    prefs: AppPrefsState,
    units: WeatherUnitsStoreState,
    isLoading: Boolean,
    activeLocation: Location?,
    weatherBlocks: WeatherBlocksStoreState,
    onUpdateBlocks: (List<WeatherBlock>) -> Unit,
) {
    val weather = remember(weatherStore.weather) { weatherStore.weather }
    val airQuality = remember(weatherStore.airQuality) { weatherStore.airQuality }
    val alerts = remember(weatherStore.alerts) { weatherStore.alerts }

    val layoutDirection = LocalLayoutDirection.current
    val units = units.units
    val isFroggyLayout = false
    val isShowSummary = prefs.isShowSummary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = weather,
                contentKey = { it?.location?.id },
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { weather ->
                Column(modifier = Modifier.fillMaxSize()) {
                    MainSearchBar(
                        isFroggyLayout = isFroggyLayout,
                        paddingValues = PaddingValues(0.dp),
                        navController = navController,
                        drawerState = drawerState,
                        activeLocation = activeLocation,
                        onEditLocation = onEditLocation,
                        layoutDirection = layoutDirection,
                        onRefresh = onRefresh
                    )
                    if (weather != null) {
                        if (!isTabletLike) {
                            PhoneLayout(
                                weather,
                                units,
                                context,
                                isFroggyLayout,
                                navController,
                                alerts,
                                prefs,
                                onWeatherSourceInfoClick,
                                isShowSummary,
                                airQuality,
                                weatherBlocks,
                                onUpdateBlocks,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Box(modifier = Modifier.weight(1f)) {
                                TabletLayout(
                                    weather,
                                    units,
                                    context,
                                    isFroggyLayout,
                                    navController,
                                    alerts,
                                    prefs,
                                    onWeatherSourceInfoClick,
                                    isShowSummary,
                                    airQuality,
                                    PaddingValues(0.dp),
                                    layoutDirection,
                                    weatherBlocks,
                                    onUpdateBlocks
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
