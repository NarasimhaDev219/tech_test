package com.ram.techtest.news.data.model

import com.google.gson.annotations.SerializedName

data class RemoteSource(
    @SerializedName("status") val status: String,
    @SerializedName("sources") val sources: List<Sources>
)

data class Sources(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("category") val category: String,
    @SerializedName("language") val language: String,
    @SerializedName("country") val country: String,
)

data class RemoteSourceDetails(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int = 0,
    @SerializedName("articles") val articles: List<Article>
)

data class Article(
    @SerializedName("source") val source: ArticleSource,
    @SerializedName("author") val author: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content") val content: String,
)

data class ArticleSource(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)