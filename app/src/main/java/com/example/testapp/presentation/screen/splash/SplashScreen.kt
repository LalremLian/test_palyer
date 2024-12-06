package com.example.testapp.presentation.screen.splash

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.example.testapp.navigation.Graph
import com.example.testapp.navigation.Screen
import com.example.testapp.presentation.composables.CustomText
import com.example.testapp.ui.theme.Background_Black
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun SplashScreen(
    navController: NavController? = null,
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Background_Black,
        )
    }

    LaunchedEffect(Unit) {
        delay(5000).apply {
            navController?.navigate(Graph.HomeGraph) {
                popUpTo(Screen.SplashScreen) {
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
