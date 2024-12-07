package com.example.testapp.presentation.screen.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.testapp.navigation.HomeNavGraph
import com.example.testapp.navigation.Screen
import com.example.testapp.ui.theme.Background_Black
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val activity = context as Activity
    //Remember the scaffold state
    val scaffoldState = rememberScaffoldState()

    val systemUiController = rememberSystemUiController()
    val currentPageState = remember {
        mutableStateOf(Screen.HomeScreen.toString())
    }

    //Listener to the navHostController to get the current route
    navHostController.addOnDestinationChangedListener { _, destination, _ ->
        currentPageState.value = destination.route.toString()
        Log.d("route_", "onDestinationChanged: route: ${currentPageState.value}")
    }

    //Set the system bars color to black
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Background_Black,
        )

        //Hide the status bar and set the orientation to landscape for the player screens
        if (currentPageState.value == Screen.Media3Screen.route) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val controller = activity.window.decorView.windowInsetsController
                controller?.hide(WindowInsets.Type.statusBars())
            } else {
                @Suppress("DEPRECATION")
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val controller = activity.window.decorView.windowInsetsController
                controller?.show(WindowInsets.Type.statusBars())
            } else {
                @Suppress("DEPRECATION")
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    //Scaffold is a pre-defined layout structure in Jetpack Compose
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Background_Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Transparent)
        ) {
            //Called the component for the top bar
            if(currentPageState.value != "movie_player_screen/{movie_title}") {
                TopBar(
                    showTitle = currentPageState.value != "details_screen/{movie_id}",
                    onBackPressed = {
                        navHostController.popBackStack()
                    }
                )
            }

            //Called the navigation graph for the MainScreen
            HomeNavGraph(
                navController = navHostController,
            )
        }
    }
}
