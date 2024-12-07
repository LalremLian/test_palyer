package com.example.testapp.presentation.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.testapp.navigation.Graph
import com.example.testapp.navigation.Screen
import com.example.testapp.presentation.global_components.CustomText
import com.example.testapp.ui.theme.Background_Black
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController? = null,
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Background_Black,
        )
    }

    LaunchedEffect(Unit) {
        delay(2000).apply {
            navController?.navigate(Graph.HomeGraph.route) {
                popUpTo(Screen.SplashScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_Black),
        contentAlignment = Alignment.Center
    ) {
        CustomText(
            text = "Developed by Lalrem Lian B. Tlung",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(bottom = 80.dp)
                .align(Alignment.Center)
        )
    }
}
