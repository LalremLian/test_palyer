package com.example.testapp.presentation.screen.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.testapp.navigation.HomeNavGraph
import com.example.testapp.navigation.Screen
import com.example.testapp.presentation.composables.CustomText
import com.example.testapp.ui.theme.Background_Black

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
) {
    val scaffoldState = rememberScaffoldState()

    val systemUiController = rememberSystemUiController()
    val currentPageState = remember {
        mutableStateOf(Screen.HomeScreen.toString())
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentPageState.value = destination.route.toString()
        Log.e("CurrentPage", currentPageState.value)
    }

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Background_Black,
        )
    }
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CustomText(
                            text = "TEST APP",
                            color = Color.White,
                        )
                    }
                }
            }

            HomeNavGraph(
                navController = navController,
            )
        }
    }
}
