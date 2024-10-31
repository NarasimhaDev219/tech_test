package com.ram.techtest.news.domain

import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.model.Sources
import com.ram.techtest.news.data.repository.SourceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SourceListUseCase @Inject constructor(
    private val sourceRepository: SourceRepository
) {
    suspend fun getSourceList(): Flow<NetworkResult<List<Sources>>> {
        return sourceRepository.sourceList()
    }
}