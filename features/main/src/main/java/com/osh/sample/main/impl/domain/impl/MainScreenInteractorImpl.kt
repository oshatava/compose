package com.osh.sample.main.impl.domain.impl

import android.util.Log
import com.osh.sample.domain.interactor.BasePagedInteractor
import com.osh.sample.domain.interactor.PagedInteractorState
import com.osh.sample.main.impl.domain.MainScreenInteractor
import com.osh.sample.main.impl.domain.MainScreenState
import com.osh.sample.main.impl.domain.RecordsItem
import com.osh.sample.main.impl.domain.RecordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MainScreenInteractorImpl @Inject constructor(
    recordsRepository: RecordsRepository
) : MainScreenInteractor, BasePagedInteractor<RecordsItem, String>(
    repository = recordsRepository,
    initialState = PagedInteractorState("")
) {
    override fun observeItems(): Flow<MainScreenState> {
        Log.d("MainScreenInteractor", "observeItems")
        return observe().map {
            MainScreenState(
                items = it.items,
                isLoading = it.isLoading,
                isPageLoadingInProgress = it.isPageLoadingInProgress,
                isSearching = it.query.isNotEmpty() && (it.isLoading || it.isPageLoadingInProgress),
                lastError = it.lastError
            )
        }
    }

    override suspend fun onSearchChanged(query: String) {
        Log.d("MainScreenInteractor", "onSearchChanged query $query")
        request(query)
    }

    override suspend fun onPageUpdateRequired() {
        Log.d("MainScreenInteractor", "onPageUpdateRequired")
        requestMore()
    }
}