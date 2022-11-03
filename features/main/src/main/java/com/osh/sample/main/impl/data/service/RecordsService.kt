package com.osh.sample.main.impl.data.service

import com.osh.sample.main.impl.data.model.RecordsResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RecordsService {

    @GET("5df79a3a320000f0612e0115")
    suspend fun getRecords(): RecordsResponse

    @GET("5df79b1f320000f4612e011e")
    suspend fun searchRecords(
        @Query("query") query: String // fake request
    ): RecordsResponse

}