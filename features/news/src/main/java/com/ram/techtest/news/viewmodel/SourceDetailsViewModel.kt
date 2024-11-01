package com.ram.techtest.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ram.techtest.news.data.model.Article
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.domain.SourceDetailsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceDetailsViewModel @Inject constructor(
    private val getSourceDetailsListUseCase: SourceDetailsListUseCase,
) : ViewModel() {

    private val _articlesResult = MutableStateFlow<NetworkResult<List<Article>>>(NetworkResult.Loading)
    val articlesResult: StateFlow<NetworkResult<List<Article>>> get() = _articlesResult


    fun fetchSourceDetails(sourceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getSourceDetailsListUseCase.getSourceDetailsList(sourceId).collect { result ->
                _articlesResult.value = result
            }
        }
    }
}