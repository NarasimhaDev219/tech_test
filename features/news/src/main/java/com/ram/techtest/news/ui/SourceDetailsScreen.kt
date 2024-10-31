package com.ram.techtest.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ram.techtes.theme.ui.CircularProgressBar
import com.ram.techtes.theme.ui.CustomAppBar
import com.ram.techtest.news.data.model.Article
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.viewmodel.SourceDetailsViewModel

@Composable
fun SourceDetailsScreen(viewModel: SourceDetailsViewModel, navController: NavHostController) {

    val articlesResult by viewModel.articlesResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSourceDetails()
    }

    // Main layout
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        topBar = {
            CustomAppBar(title = "Articles", navController = navController, showNavBack = true)
        },
        content = { innerPadding ->
            when (articlesResult) {
                is NetworkResult.Loading -> {
                    CircularProgressBar()
                }
                is NetworkResult.Success<*> -> {
                    val article = (articlesResult as NetworkResult.Success<List<Article>>).data
                    SourcesDetailsList(article, innerPadding)
                }
                is NetworkResult.Failure -> {
                    val message = (articlesResult as NetworkResult.Failure).msg
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = message ?: "An error occurred", color = Color.Red)
                    }
                }
            }
        }
    )
}

@Composable
fun SourcesDetailsList(
    article: List<Article>,
    innerPadding: PaddingValues
) {
    LazyColumn(
        contentPadding = innerPadding
    ) {
        items(article) { article ->
            SourceDetailsItem(article)
        }
    }
}

@Composable
fun SourceDetailsItem(article: Article) {
    ElevatedCard(
        onClick = {},
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        Column(Modifier.padding(vertical = 8.dp, horizontal = 8.dp)){
            Text(text = article.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = article.description, style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        }
    }
}
