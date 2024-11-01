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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ram.techtes.theme.Screens
import com.ram.techtes.theme.ui.CircularProgressBar
import com.ram.techtes.theme.ui.CustomAppBar
import com.ram.techtest.news.data.model.Sources
import com.ram.techtest.news.data.network.NetworkResult
import com.ram.techtest.news.viewmodel.SourceViewModel

@Composable
fun SourceScreen(viewModel: SourceViewModel, navController: NavHostController) {

    val sourcesResult by viewModel.sourcesResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSources()
    }

    // Main layout
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        topBar = {
                 CustomAppBar(title = "Top Headlines", navController = navController)
        },
        content = { innerPadding ->
            when (sourcesResult) {
                is NetworkResult.Loading -> {
                    CircularProgressBar()
                }
                is NetworkResult.Success<*> -> {
                    val sources = (sourcesResult as NetworkResult.Success<List<Sources>>).data
                    SourcesList(sources, innerPadding) { source ->
                        navController.navigate("source_details/${source.id}")
                    }
                }
                is NetworkResult.Failure -> {
                    val message = (sourcesResult as NetworkResult.Failure).msg
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
fun SourcesList(
    sources: List<Sources>,
    innerPadding: PaddingValues,
    onItemClick: (Sources) -> Unit
) {
    LazyColumn(contentPadding = innerPadding) {
        items(
            items = sources,
            key = { it.id }
        ) { source ->
            SourceItem(
                source = source,
                onClick = { onItemClick(source) }
            )
        }
    }
}

@Composable
fun SourceItem(source: Sources, onClick: () -> Unit) {
    val context= LocalContext.current.applicationContext
    ElevatedCard(
        onClick = {onClick.invoke()},
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
            Text(text = source.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = source.description, style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        }
    }
}
