package com.example.testapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testapp.presentation.screen.home.HomeScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen,
        modifier = modifier
    ) {
        composable<Screen.HomeScreen> {
            HomeScreen(navController = navController)
        }
    }
}