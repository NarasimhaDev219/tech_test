package com.ram.techtest.news.usecase

import com.ram.techtest.news.data.model.Article
import com.ram.techtest.news.data.model.ArticleSource
import com.ram.techtest.news.data.model.RemoteSourceDetails
import com.ram.techtest.news.data.model.Sources
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.repository.SourceDetailsRepository
import com.ram.techtest.news.data.repository.SourceRepository
import com.ram.techtest.news.domain.SourceDetailsListUseCase
import com.ram.techtest.news.domain.SourceListUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SourceDetailsListUseCaseTest {

    private val sourceRepository: SourceDetailsRepository = mock(SourceDetailsRepository::class.java)
    private val useCase = SourceDetailsListUseCase(sourceRepository)

    @Test
    fun `getSourceList() should return data from repository when successful`() = runBlocking {
        // mock response data
        val sourceID = "abc-news"
        val source = ArticleSource(
            id = "abc-news",
            name = "ABC News"
        )
        val article = listOf(
            Article(
            source= source,
            author = "abc-news",
            title = "ABC News",
            description = "Your trusted source for breaking news,..",
            url = "https://abcnews.go.com",
            urlToImage = "general",
            publishedAt = "en",
            content = "us",
        )
        )
        whenever(sourceRepository.sourceDetailsList(sourceID)).thenReturn(flow { emit(NetworkResult.Success(article)) })
        val result = useCase.getSourceDetailsList(sourceID).first()
        assertTrue(result is NetworkResult.Success && result.data == article)
    }

    @Test
    fun `getSourceList should return error when repository fails`() = runBlocking {
        val sourceID = "abc-news"
        whenever(sourceRepository.sourceDetailsList(sourceID)).thenReturn(flow { emit(NetworkResult.Failure("Error")) })

        val result = useCase.getSourceDetailsList(sourceID).first()

        assertTrue(result is NetworkResult.Failure && result.msg == "Error")
    }
}
