package com.osh.sample.main.impl.presentation

import com.osh.sample.main.impl.domain.RecordsItem

internal data class MainScreenViewState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = true,
    val isPageLoadingInProgress: Boolean = false,
    val items: List<RecordsItem> = emptyList(),
    val lastError: Throwable? = null,
)

