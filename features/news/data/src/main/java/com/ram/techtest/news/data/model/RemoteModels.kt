package com.ram.techtest.news.data.model

data class RemoteSource(
    val status: String,
    val sources: List<Sources>
)

data class Sources(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String,
)

data class RemoteSourceDetails(
    val status: String,
    val totalResults: Int = 0,
    val articles: List<Article>
)

data class Article(
    val source: ArticleSource,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
)

data class ArticleSource(
    val id: String,
    val name: String
)