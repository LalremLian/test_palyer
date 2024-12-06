package com.example.testapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object HomeScreen: Screen()
    @Serializable
    data object SplashScreen: Screen()
}
