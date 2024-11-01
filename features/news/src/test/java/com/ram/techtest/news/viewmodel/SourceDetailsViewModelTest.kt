@file:OptIn(ExperimentalCoroutinesApi::class)

package com.ram.techtest.news.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ram.techtest.news.data.model.Article
import com.ram.techtest.news.data.model.ArticleSource
import com.ram.techtest.news.data.model.Sources
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.repository.SourceDetailsRepository
import com.ram.techtest.news.data.repository.SourceRepository
import com.ram.techtest.news.domain.SourceDetailsListUseCase
import com.ram.techtest.news.domain.SourceListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SourceDetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    private lateinit var sourceDetailsViewModel: SourceDetailsViewModel

    @Mock
    private lateinit var mockRepository: SourceDetailsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sourceDetailsViewModel = SourceDetailsViewModel(getSourceDetailsListUseCase = SourceDetailsListUseCase(mockRepository))
    }
    @Test
    fun `fetchSources Details should return sourcesResult with success`() = coroutineTestRule.runBlockingTest {
        // Prepare mock data
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
        whenever(mockRepository.sourceDetailsList(sourceID)).thenReturn(flow {
            emit(NetworkResult.Loading)
            emit(NetworkResult.Success(article))
        })

        sourceDetailsViewModel.fetchSourceDetails(sourceID)

        val job = launch {
            sourceDetailsViewModel.articlesResult.collect { result ->
                when(result) {
                    is NetworkResult.Loading -> {
                        assertTrue(result == NetworkResult.Loading)
                    }
                    is NetworkResult.Success -> {
                        assertEquals(article, result.data)
                        return@collect
                    }
                    else -> {}
                }
            }
        }
        testScheduler.apply { advanceTimeBy(1000); runCurrent() }
        job.cancel()
    }

    @Test
    fun `fetchSources Details should update sourcesResult with failure`() = coroutineTestRule.runBlockingTest {
        val sourceID = "abc-news"
        whenever(mockRepository.sourceDetailsList(sourceID)).thenReturn(flow {
            emit(NetworkResult.Loading)
            emit(NetworkResult.Failure("Error fetching data"))
        })
        sourceDetailsViewModel.fetchSourceDetails(sourceID)

        val job = launch {
            sourceDetailsViewModel.articlesResult.collect { result ->
                when(result) {
                    is NetworkResult.Loading -> {
                        assertTrue(result == NetworkResult.Loading)
                    }
                    is NetworkResult.Failure -> {
                        assertEquals("Error fetching data", result.msg)
                        return@collect
                    }
                    else -> {}
                }
            }
        }
        testScheduler.apply { advanceTimeBy(1000); runCurrent() }
        job.cancel()
    }
}