package com.example.testapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.presentation.details.MovieDetailsScreen
import com.example.testapp.presentation.screen.home.HomeScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun HomeNavGraph(
    navController: NavHostController
) {
    //This is the navigation graph for the MainScreen
    NavHost(
        navController = navController,
        route = Graph.HomeGraph.route,
        startDestination = Screen.HomeScreen.route,
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Screen.MovieDetailsScreen.route) { backStackEntry ->
            //Get the movie id from the arguments and pass it to the MovieDetailsScreen
            val arg = backStackEntry.arguments?.getString(Screen.MOVIE_ID) ?: ""
            MovieDetailsScreen(navController, id = arg)
        }
    }
}