package com.osh.sample.main.impl.data

import com.osh.sample.domain.repository.PagedResponse
import com.osh.sample.main.impl.data.mapper.RecordItemsMapper
import com.osh.sample.main.impl.data.service.RecordsService
import com.osh.sample.main.impl.domain.RecordsItem
import com.osh.sample.main.impl.domain.RecordsRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class RecordsRepositoryImpl @Inject constructor(
    private val recordsService: RecordsService,
    private val recordItemsMapper: RecordItemsMapper,
) : RecordsRepository {
    override suspend fun requestPage(query: String, pageIndex: Int): PagedResponse<RecordsItem> {
        delay(1000) // Just add some delay to show all view states.
        return when (query.isEmpty()) {
            true -> recordsService.getRecords().let(recordItemsMapper::map).let {
                PagedResponse(it, pageIndex, totalPages = 5)
            }
            false -> recordsService.searchRecords(query).let(recordItemsMapper::map).let {
                PagedResponse(it.shuffled(), pageIndex, totalPages = 1)
            }
        }
    }
}
