package com.pranshulgg.weather_master_app.core.ui.metro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Windows pivot: thin light lowercase headers that translate in step with a swipeable pager.
 * The active heading stays at the page inset while following headings remain visible.
 */
@Composable
fun MetroPivot(
    titles: List<String>,
    modifier: Modifier = Modifier,
    initialIndex: Int = 0,
    onPageChanged: (Int) -> Unit = {},
    content: @Composable (Int) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex.coerceIn(0, (titles.size - 1).coerceAtLeast(0)),
        pageCount = { titles.size }
    )
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        PivotHeader(
            titles = titles,
            pagerState = pagerState,
            onSelect = { index -> scope.launch { pagerState.animateScrollToPage(index) } }
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) { page ->
            content(page)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }
}

@Composable
private fun PivotHeader(
    titles: List<String>,
    pagerState: PagerState,
    onSelect: (Int) -> Unit
) {
    val headerWidths = remember { mutableStateListOf(*Array(titles.size) { 0 }) }
    val offsets = IntArray(titles.size)
    var acc = 0
    for (i in titles.indices) {
        offsets[i] = acc
        acc += headerWidths.getOrElse(i) { 0 }
    }
    val endOffset = acc
    val page = pagerState.currentPage
    val fraction = pagerState.currentPageOffsetFraction
    val currentStart = offsets.getOrElse(page) { 0 }.toFloat()
    val nextStart = if (page + 1 < titles.size) offsets[page + 1].toFloat() else endOffset.toFloat()
    val translation = currentStart + fraction * (nextStart - currentStart)

    val fg = MaterialTheme.colorScheme.onSurface
    val subtle = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = PageInset)
            .clipToBounds()
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.Start, unbounded = true)
                .graphicsLayer { translationX = -translation },
            verticalAlignment = Alignment.CenterVertically
        ) {
            titles.forEachIndexed { index, title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = if (index == page) fg else subtle
                    ),
                    modifier = Modifier
                        .onGloballyPositioned { headerWidths[index] = it.size.width }
                        .clickable { onSelect(index) }
                        .padding(end = 24.dp, top = 6.dp, bottom = 10.dp)
                )
            }
        }
    }
}
