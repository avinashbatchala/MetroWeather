package com.pranshulgg.weather_master_app.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pranshulgg.weather_master_app.R

/** Square Windows back chevron used by every page header. */
@Composable
fun NavigateUpBtn(navController: NavController) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clickable { navController.popBackStack() },
        contentAlignment = Alignment.Center
    ) {
        Symbol(
            R.drawable.arrow_back_24px,
            desc = "Back",
            color = MaterialTheme.colorScheme.onSurface,
            size = 24.dp
        )
    }
}
