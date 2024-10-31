package com.ram.techtest.news.data.network

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Failure(val msg: String?) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}