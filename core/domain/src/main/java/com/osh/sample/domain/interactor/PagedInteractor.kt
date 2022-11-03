package com.osh.sample.domain.interactor

import com.osh.sample.domain.repository.PagedRepository
import com.osh.sample.domain.repository.PagedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext

data class PagedInteractorState<I : Any, Q : Any>(
    val query: Q,
    val items: List<I> = emptyList(),
    val pageIndex: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val isPageLoadingInProgress: Boolean = false,
    val lastError: Throwable? = null,
)

interface PagedInteractor<I : Any, Q : Any> {
    fun observe(): Flow<PagedInteractorState<I, Q>>
    suspend fun request(query: Q)
    suspend fun requestMore()
}

sealed class InteractorAction<I : Any, Q : Any> {
    data class Requesting<I : Any, Q : Any>(val query: Q) : InteractorAction<I, Q>()
    class RequestingNextPage<I : Any, Q : Any> : InteractorAction<I, Q>()
    data class RequestSuccessful<I : Any, Q : Any>(
        val data: List<I>,
        val pageIndex: Int,
        val totalPages: Int,
    ) : InteractorAction<I, Q>()

    data class Fail<I : Any, Q : Any>(val throwable: Throwable) : InteractorAction<I, Q>()
}

open class BasePagedInteractor<I : Any, Q : Any>(
    private val repository: PagedRepository<Q, I>,
    initialState: PagedInteractorState<I, Q>,
) : PagedInteractor<I, Q> {
    private val context = Dispatchers.IO + Job()
    private val state = MutableStateFlow(initialState)

    override fun observe(): Flow<PagedInteractorState<I, Q>> = state

    override suspend fun request(query: Q) {
        context.cancelChildren()
        reduceCurrentState(InteractorAction.Requesting(query))
        enqueueSafe {
            val response = repository.requestPage(query, 0)
            reduceCurrentState(response.toInteractorAction())
        }
    }

    override suspend fun requestMore() = enqueueSafe {
        forState { state ->
            try {
                val nextPage = state.pageIndex + 1
                if (nextPage < state.totalPages) {
                    reduceCurrentState(InteractorAction.RequestingNextPage())
                    val response = repository.requestPage(state.query, nextPage)
                    reduceCurrentState(response.toInteractorAction())
                }
            } catch (e: Exception) {
                reduceCurrentState(InteractorAction.Fail(e))
            }
        }
    }

    private suspend fun enqueueSafe(body: suspend CoroutineScope.() -> Unit) {
        withContext(context) {
            try {
                body()
            } catch (e: Exception) {
                reduceCurrentState(InteractorAction.Fail(e))
            }
        }
    }

    private fun <I : Any> PagedResponse<I>.toInteractorAction(): InteractorAction.RequestSuccessful<I, Q> {
        return InteractorAction.RequestSuccessful<I, Q>(
            page,
            pageIndex,
            totalPages
        )
    }

    private suspend fun reduceCurrentState(action: InteractorAction<I, Q>) {
        forState { state.tryEmit(reduceState(it, action)) }
    }

    private suspend fun forState(body: suspend (PagedInteractorState<I, Q>) -> Unit) {
        state.take(1).collect(body)
    }

    protected open fun reduceState(
        oldState: PagedInteractorState<I, Q>,
        action: InteractorAction<I, Q>
    ): PagedInteractorState<I, Q> {
        return when (action) {
            is InteractorAction.Fail -> oldState.copy(
                isLoading = false,
                isPageLoadingInProgress = false,
                lastError = action.throwable
            )
            is InteractorAction.RequestSuccessful -> oldState.copy(
                isLoading = false,
                lastError = null,
                isPageLoadingInProgress = false,
                items = oldState.items.toMutableList().apply { addAll(action.data) },
                totalPages = action.totalPages,
                pageIndex = action.pageIndex,
            )
            is InteractorAction.Requesting -> oldState.copy(
                query = action.query,
                isLoading = true,
                lastError = null,
                isPageLoadingInProgress = false,
                totalPages = 0,
                pageIndex = 0,
                items = emptyList()
            )
            is InteractorAction.RequestingNextPage -> oldState.copy(
                isLoading = false,
                lastError = null,
                isPageLoadingInProgress = true,
            )
        }
    }
}