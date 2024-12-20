package com.ram.techtest.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ram.techtest.news.data.model.Sources
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.domain.SourceListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val getSourceListUseCase: SourceListUseCase,
) : ViewModel() {

    private val _sourcesResult = MutableStateFlow<NetworkResult<List<Sources>>>(NetworkResult.Loading)
    val sourcesResult: StateFlow<NetworkResult<List<Sources>>> get() = _sourcesResult

    fun fetchSources() {
        viewModelScope.launch(Dispatchers.IO) {
            getSourceListUseCase.getSourceList().collect { result ->
                _sourcesResult.value = result // Collect and emit each state
            }
        }
    }
}

