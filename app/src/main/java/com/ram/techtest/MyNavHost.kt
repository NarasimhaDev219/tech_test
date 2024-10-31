package com.ram.techtest

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ram.techtest.news.ui.SourceDetailsScreen
import com.ram.techtest.news.ui.SourceScreen

@Composable
fun MyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination ="SourceScreen") {
        composable(route = "SourceScreen") {
            SourceScreen(hiltViewModel(),navController)
        }
        composable(route = "SourceDetailsScreen") {
            SourceDetailsScreen(hiltViewModel() ,navController = navController)
        }
    }
}