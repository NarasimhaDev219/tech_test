package com.ram.techtest.news.data.repository

import com.ram.techtest.news.data.model.Article
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.data.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SourceDetailsRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
) : SourceDetailsRepository {

    override suspend fun sourceDetailsList(): Flow<NetworkResult<List<Article>>> = flow {
        emit(NetworkResult.Loading) // Emit loading state
        try {
            val response = networkService.getSourceDetails()
            val articles = response.articles

            if (!articles.isNullOrEmpty()) {
                emit(NetworkResult.Success(articles)) // Emit success if sources is non-null
            } else {
                emit(NetworkResult.Failure("No data available"))
            }
        } catch (e: IOException) {
            emit(NetworkResult.Failure("Network Error: ${e.message}")) // Handle network errors
        } catch (e: HttpException) {
            emit(NetworkResult.Failure("Server Error: ${e.message()}")) // Handle HTTP errors
        } catch (e: Exception) {
            emit(NetworkResult.Failure("Unexpected Error: ${e.message}")) // Handle other exceptions
        }
    }
}
