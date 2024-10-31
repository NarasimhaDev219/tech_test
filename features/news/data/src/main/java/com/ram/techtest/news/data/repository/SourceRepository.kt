package com.ram.techtest.news.data.repository

import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.model.Sources
import kotlinx.coroutines.flow.Flow

interface SourceRepository {
    suspend fun sourceList(): Flow<NetworkResult<List<Sources>>>
}