@file:OptIn(ExperimentalCoroutinesApi::class)

package com.ram.techtest.news.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ram.techtest.news.data.model.Sources
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.repository.SourceRepository
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
class SourceViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    private lateinit var sourceViewModel: SourceViewModel

    @Mock
    private lateinit var mockRepository: SourceRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sourceViewModel = SourceViewModel(getSourceListUseCase = SourceListUseCase(mockRepository))
    }
    @Test
    fun `fetchSources should update sourcesResult with success`() = coroutineTestRule.runBlockingTest {
        // Prepare mock data
        val mockSources = listOf(Sources(
            id = "abc-news",
            name = "ABC News",
            description = "Your trusted source for breaking news,..",
            url = "https://abcnews.go.com",
            category = "general",
            language = "en",
            country = "us",
        ))
        whenever(mockRepository.sourceList()).thenReturn(flow {
            emit(NetworkResult.Loading)
            emit(NetworkResult.Success(mockSources))
        })

        sourceViewModel.fetchSources()

        val job = launch {
            sourceViewModel.sourcesResult.collect { result ->
                when(result) {
                    is NetworkResult.Loading -> {
                        assertTrue(result == NetworkResult.Loading)
                    }
                    is NetworkResult.Success -> {
                        assertEquals(mockSources, result.data)
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
    fun `fetchSources should update sourcesResult with failure`() = coroutineTestRule.runBlockingTest {
        whenever(mockRepository.sourceList()).thenReturn(flow {
            emit(NetworkResult.Loading)
            emit(NetworkResult.Failure("Error fetching data"))
        })
        sourceViewModel.fetchSources()

        val job = launch {
            sourceViewModel.sourcesResult.collect { result ->
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