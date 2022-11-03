package com.osh.sample.main.impl.domain

import kotlinx.coroutines.flow.Flow

data class MainScreenState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = true,
    val isPageLoadingInProgress: Boolean = false,
    val items: List<RecordsItem> = emptyList(),
    val lastError: Throwable? = null,
)

interface MainScreenInteractor {
    fun observeItems(): Flow<MainScreenState>
    suspend fun onSearchChanged(query: String)
    suspend fun onPageUpdateRequired()
}