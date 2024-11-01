package com.ram.techtest.news.data.repository

import com.ram.techtest.news.data.model.Article
import com.ram.techtest.news.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SourceDetailsRepository {
    suspend fun sourceDetailsList(sourceId: String): Flow<NetworkResult<List<Article>>>

}