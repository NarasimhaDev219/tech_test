package com.ram.techtest.news.repository

import com.ram.techtest.news.data.model.Article
import com.ram.techtest.news.data.model.ArticleSource
import com.ram.techtest.news.data.model.RemoteSourceDetails
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.network.NetworkService
import com.ram.techtest.news.data.repository.SourceDetailsRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SourceDetailsRepositoryImplTest {

    private val networkService: NetworkService = mock(NetworkService::class.java)
    private val repository = SourceDetailsRepositoryImpl(networkService)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `sourceList should emit Success when successful`() = runBlocking {
        // mock response data
        val sourceID = "abc-news"
        val source = ArticleSource(
            id = "abc-news",
            name = "ABC News"
            )
        val article = listOf(Article(
            source= source,
            author = "abc-news",
            title = "ABC News",
            description = "Your trusted source for breaking news,..",
            url = "https://abcnews.go.com",
            urlToImage = "general",
            publishedAt = "en",
            content = "us",
        ))
        val remoteSourceDetails = RemoteSourceDetails(status = "ok", totalResults = 10 , articles = article)
        whenever(networkService.getSourceDetails(sourceID)).thenReturn(remoteSourceDetails)
        val emissions = mutableListOf<NetworkResult<List<Article>>>()
        repository.sourceDetailsList(sourceID).collect { result ->
            emissions.add(result)
        }
        // Check the first emission is Loading
        assert(emissions[0] is NetworkResult.Loading)
        assert(emissions[1] is NetworkResult.Success)
        assertEquals(article, (emissions[1] as NetworkResult.Success).data)
    }

    @Test
    fun `emit Failure when no data is available`() = runBlocking {
        // mock response data
        val sourceID = "abc-news"
        val remoteSourceDetails = RemoteSourceDetails(status = "ok", totalResults = 10 , articles = emptyList())
        whenever(networkService.getSourceDetails(sourceID)).thenReturn(remoteSourceDetails)
        val emissions = mutableListOf<NetworkResult<List<Article>>>()
        repository.sourceDetailsList(sourceID).collect { result ->
            emissions.add(result)
        }
        // Check the first emission is Loading
        assert(emissions[0] is NetworkResult.Loading)
        assert(emissions[1] is NetworkResult.Failure)
        assertTrue((emissions[1] as NetworkResult.Failure).msg == "No data available")
    }

    @Test
    fun `sourceList should emit Failure on network error`() = runBlocking {
        val sourceID = "abc-news"
        // Throw Runtime exception
        `when`(networkService.getSourceDetails(sourceID)).thenThrow(RuntimeException("Network Error"))
        val emissions = mutableListOf<NetworkResult<List<Article>>>()
        repository.sourceDetailsList(sourceID).collect { result ->
            emissions.add(result)
        }
        assertTrue(emissions[0] is NetworkResult.Loading)
        assertTrue(emissions[1] is NetworkResult.Failure)
        assertTrue((emissions[1] as NetworkResult.Failure).msg?.contains("Network Error")==true)
    }
}
