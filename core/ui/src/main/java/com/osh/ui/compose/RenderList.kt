package com.osh.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.osh.ui.pager.createTrackableGridState

interface ListGridItem {
    val id: String
    val spanCount: Int
}

@Composable
fun <T : ListGridItem> RenderList(
    modifier: Modifier = Modifier,
    items: List<T>,
    isPageLoadingInProgress: Boolean,
    totalItemsCount: Int,
    listItem: @Composable LazyGridItemScope.(T) -> Unit,
    onPageUpdateRequired: () -> Unit = {},
    spanCount: Int = 2,
) {

    val lazyListState = createTrackableGridState(
        isPageIsInUpdate = isPageLoadingInProgress,
        totalItemsCount = totalItemsCount,
        pagerUpdateCallback = onPageUpdateRequired
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(spanCount),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState,
        modifier = modifier,
    ) {
        items(
            items = items,
            key = { it.id },
            span = { GridItemSpan(it.spanCount) },
            itemContent = { listItem(it) }
        )
    }
}