package com.osh.sample.main.impl.data.mapper

import com.osh.sample.main.impl.data.model.RecordsResponse
import com.osh.sample.main.impl.domain.RecordsItem
import java.util.UUID
import javax.inject.Inject

internal interface RecordItemsMapper {
    fun map(recordsResponse: RecordsResponse): List<RecordsItem>
}

internal class RecordItemsMapperImpl @Inject constructor() : RecordItemsMapper {
    override fun map(recordsResponse: RecordsResponse): List<RecordsItem> {
        return recordsResponse.data.sessions.map {
            RecordsItem(
                id = UUID.randomUUID().toString(), // generate fake ID
                artworkUrl = it.currentTrack.artworkUrl,
                trackTitle = it.currentTrack.title,
                genres = it.genres,
                name = it.name,
                listenerCount = it.listenerCount,
            )
        }
    }
}