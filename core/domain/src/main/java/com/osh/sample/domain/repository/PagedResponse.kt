package com.osh.sample.domain.repository

data class PagedResponse<I : Any>(
    val page: List<I>,
    val pageIndex: Int,
    val totalPages: Int,
)

interface PagedRepository<Q : Any, I : Any> {
    suspend fun requestPage(query: Q, pageIndex: Int): PagedResponse<I>
}
