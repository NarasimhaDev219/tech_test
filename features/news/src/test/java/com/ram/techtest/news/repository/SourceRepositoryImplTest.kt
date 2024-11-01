package com.ram.techtest.news.repository

import com.google.gson.annotations.SerializedName
import com.ram.techtest.news.data.model.RemoteSource
import com.ram.techtest.news.data.network.NetworkService
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.model.Sources
import com.ram.techtest.news.data.repository.SourceRepository
import com.ram.techtest.news.data.repository.SourceRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import java.io.IOException
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SourceRepositoryImplTest {

    private val networkService: NetworkService = mock(NetworkService::class.java)
    private val repository = SourceRepositoryImpl(networkService)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }
    @Test
    fun `sourceList should emit Success when successful`() = runBlocking {
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
        val remoteSource = RemoteSource(status = "ok", sources = sources)
        whenever(networkService.getTopHeadlinesSources()).thenReturn(remoteSource)
        val emissions = mutableListOf<NetworkResult<List<Sources>>>()
        repository.sourceList().collect { result ->
            emissions.add(result)
        }
        // Check the first emission is Loading
        assert(emissions[0] is NetworkResult.Loading)
        assert(emissions[1] is NetworkResult.Success)
        assertEquals(sources, (emissions[1] as NetworkResult.Success).data)
    }

    @Test
    fun `emit Failure when no data is available`() = runBlocking {
        // mock response data
        val remoteSource = RemoteSource(status = "ok", sources = emptyList())
        whenever(networkService.getTopHeadlinesSources()).thenReturn(remoteSource)
        val emissions = mutableListOf<NetworkResult<List<Sources>>>()
        repository.sourceList().collect { result ->
            emissions.add(result)
        }
        // Check the first emission is Loading
        assert(emissions[0] is NetworkResult.Loading)
        assert(emissions[1] is NetworkResult.Failure)
        assertTrue((emissions[1] as NetworkResult.Failure).msg == "No data available")
    }

    @Test
    fun `sourceList should emit Failure on network error`() = runBlocking {
        // Throw Runtime exception
        `when`(networkService.getTopHeadlinesSources()).thenThrow(RuntimeException("Network Error"))
        val emissions = mutableListOf<NetworkResult<List<Sources>>>()
        repository.sourceList().collect { result ->
            emissions.add(result)
        }
        assertTrue(emissions[0] is NetworkResult.Loading)
        assertTrue(emissions[1] is NetworkResult.Failure)
        assertTrue((emissions[1] as NetworkResult.Failure).msg?.contains("Network Error")==true)
    }
}
