package com.osh.ui.pager

import android.util.Log
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

typealias PagerUpdateCallback = () -> Unit

@Composable
fun createTrackableGridState(
    isPageIsInUpdate: Boolean,
    totalItemsCount: Int,
    pagerUpdateCallback: PagerUpdateCallback,
): LazyGridState {

    val lazyListState: LazyGridState = rememberLazyGridState()
    val scrollTracker =
        remember(lazyListState, pagerUpdateCallback) { ScrollTracker(pagerUpdateCallback) }
    LaunchedEffect(scrollTracker, isPageIsInUpdate) {
        combine(
            snapshotFlow { lazyListState.firstVisibleItemIndex },
            snapshotFlow { lazyListState.isScrollInProgress },
            snapshotFlow { totalItemsCount },
            snapshotFlow {
                lazyListState.layoutInfo.visibleItemsInfo.maxByOrNull { it.index }?.index ?: 0
            },
            snapshotFlow { isPageIsInUpdate },
            ::ScrollState
        )
            .distinctUntilChanged()
            .collectLatest(scrollTracker::onScrolled)
    }
    return lazyListState
}

private class ScrollTracker(
    private val pagerUpdateCallback: PagerUpdateCallback
) {
    private var lastScrollState = ScrollState()

    fun onScrolled(scrollState: ScrollState) {
        if (scrollState.isPageIsInUpdate) return
        if (checkIsPreloadRequired(lastScrollState, scrollState)) {
            notifyPagerUpdate()
        }
        lastScrollState = scrollState
    }

    private fun notifyPagerUpdate() {
        Log.d("ScrollTracker", "notifyPagerUpdate")
        pagerUpdateCallback.invoke()
    }

    private fun checkIsPreloadRequired(
        oldScrollState: ScrollState,
        newScrollState: ScrollState
    ): Boolean {
        val deltaLast = newScrollState.lastVisibleItemIndex - oldScrollState.lastVisibleItemIndex
        val deltaFirst = newScrollState.firstVisibleItemIndex - oldScrollState.firstVisibleItemIndex
        val delta = if (deltaFirst == 0) deltaLast else deltaFirst

        return when {
            delta >= 0 -> {
                val numberHiddenItemsToEnd =
                    oldScrollState.totalItemsCount - oldScrollState.lastVisibleItemIndex
                numberHiddenItemsToEnd < ITEM_NUMBER_THRESHOLD
            }
            else -> false
        }
    }

    companion object {
        const val ITEM_NUMBER_THRESHOLD = 4
    }
}

private data class ScrollState(
    val firstVisibleItemIndex: Int = -1,
    val isScrollInProgress: Boolean = false,
    val totalItemsCount: Int = 0,
    val lastVisibleItemIndexInList: Int = -1,
    val isPageIsInUpdate: Boolean = false,
) {
    val lastVisibleItemIndex: Int =
        if (lastVisibleItemIndexInList >= totalItemsCount) totalItemsCount - 1 else lastVisibleItemIndexInList
}