package com.ram.techtest.news.domain

import com.ram.techtest.news.data.model.Article
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.repository.SourceDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SourceDetailsListUseCase @Inject constructor(
    private val sourceRepository: SourceDetailsRepository
) {
    suspend fun getSourceDetailsList(): Flow<NetworkResult<List<Article>>> {
        return sourceRepository.sourceDetailsList()
    }
}