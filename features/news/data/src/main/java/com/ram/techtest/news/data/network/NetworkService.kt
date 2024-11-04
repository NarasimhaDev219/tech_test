package com.ram.techtest.news.data.network

import com.ram.techtest.news.data.model.RemoteSource
import com.ram.techtest.news.data.model.RemoteSourceDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("v2/top-headlines/sources")
    suspend fun getTopHeadlinesSources(): RemoteSource

    @GET("v2/top-headlines")
    suspend fun getSourceDetails(
        @Query("sources") sourcesId: String
    ): RemoteSourceDetails
}