package com.ram.techtest.news.usecase

import com.ram.techtest.news.data.model.Sources
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.repository.SourceRepository
import com.ram.techtest.news.domain.SourceListUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SourceListUseCaseTest {

    private val sourceRepository: SourceRepository = mock(SourceRepository::class.java)
    private val useCase = SourceListUseCase(sourceRepository)

    @Test
    fun `getSourceList() should return data from repository when successful`() = runBlocking {
        // mock response data
        val sources = listOf(Sources(
            id = "abc-news",
            name = "ABC News",
            description = "Your trusted source for breaking news,..",
            url = "https://abcnews.go.com",
            category = "general",
            language = "en",
            country = "us",
        ))
        whenever(sourceRepository.sourceList()).thenReturn(flow { emit(NetworkResult.Success(sources)) })
        val result = useCase.getSourceList().first()
        assertTrue(result is NetworkResult.Success && result.data == sources)
    }

    @Test
    fun `getSourceList should return error when repository fails`() = runBlocking {
        whenever(sourceRepository.sourceList()).thenReturn(flow { emit(NetworkResult.Failure("Error")) })

        val result = useCase.getSourceList().first()

        assertTrue(result is NetworkResult.Failure && result.msg == "Error")
    }
}
