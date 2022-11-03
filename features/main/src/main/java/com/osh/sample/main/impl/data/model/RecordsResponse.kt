package com.osh.sample.main.impl.data.model

import com.google.gson.annotations.SerializedName

internal data class RecordsResponse(
    val data: RecordsResponseData
)

internal data class RecordsResponseData(
    val sessions: List<RecordsResponseSessionItem>
)

internal data class RecordsResponseSessionItem(
    val name: String,
    @SerializedName("listener_count")
    val listenerCount: Int,
    val genres: List<String>,
    @SerializedName("current_track")
    val currentTrack: RecordsResponseSessionItemTrackInfo
)

internal data class RecordsResponseSessionItemTrackInfo(
    val title: String,
    @SerializedName("artwork_url")
    val artworkUrl: String
)