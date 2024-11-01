package com.ram.techtest

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ram.techtes.theme.Screens
import com.ram.techtest.news.ui.SourceDetailsScreen
import com.ram.techtest.news.ui.SourceScreen

@Composable
fun MyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination =Screens.SourceList.route) {
        composable(route = Screens.SourceList.route) {
            SourceScreen(hiltViewModel(),navController)
        }
        composable("source_details/{sourceId}") { backStackEntry ->
            val sourceId = backStackEntry.arguments?.getString("sourceId")
            SourceDetailsScreen(hiltViewModel(), navController, sourceId)
        }
    }
}