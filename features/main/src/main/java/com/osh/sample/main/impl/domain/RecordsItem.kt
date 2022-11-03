package com.osh.sample.main.impl.domain

import java.util.UUID

data class RecordsItem(
    val id: String = UUID.randomUUID().toString(),
    val artworkUrl: String,
    val name: String = "",
    val genres: List<String> = emptyList(),
    val trackTitle: String = "",
    val listenerCount: Int = 0,
)