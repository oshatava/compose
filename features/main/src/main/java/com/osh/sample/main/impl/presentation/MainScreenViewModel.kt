package com.osh.sample.main.impl.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osh.sample.main.impl.domain.MainScreenInteractor
import com.osh.sample.main.impl.domain.RecordsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class MainScreenViewModel @Inject constructor(
    private val interactor: MainScreenInteractor
) : ViewModel() {
    private val queryFlow = MutableStateFlow("")
    private val eventChannel = Channel<Event>(Channel.BUFFERED)

    val eventsFlow = eventChannel.receiveAsFlow()
    var state by mutableStateOf(MainScreenViewState())
        private set


    init {
        viewModelScope.launch {
            interactor.observeItems().collectLatest { st ->
                state = MainScreenViewState(
                    isLoading = st.isLoading,
                    isSearching = st.isSearching,
                    isPageLoadingInProgress = st.isPageLoadingInProgress,
                    items = st.items,
                    lastError = st.lastError,
                )
            }
        }
        viewModelScope.launch {
            queryFlow
                .debounce(SEARCH_QUERY_DEBOUNCE)
                .collectLatest(interactor::onSearchChanged)
        }
    }

    private fun sendEvent(event: Event) {
        viewModelScope.launch { eventChannel.send(event) }
    }

    fun onSearchChanged(query: String) {
        queryFlow.tryEmit(query)
    }

    fun onPageUpdateRequired() {
        viewModelScope.launch { interactor.onPageUpdateRequired() }
    }

    fun onItemClicked(item: RecordsItem) {
        sendEvent(Event.NavigateDetails(item.artworkUrl))
    }

    sealed class Event {
        data class NavigateDetails(val id: String) : Event()
    }

    companion object {
        const val SEARCH_QUERY_DEBOUNCE = 300L
    }
}