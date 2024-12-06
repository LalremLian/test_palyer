package com.example.testapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testapp.presentation.screen.main.MainScreen
import com.example.testapp.presentation.screen.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RootNavGraph(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.SplashScreen,
    ){
        composable<Screen.SplashScreen> {
            SplashScreen(navController = navHostController)
        }
        composable<Graph.HomeGraph> {
            MainScreen(navController = rememberNavController())
        }
    }

}