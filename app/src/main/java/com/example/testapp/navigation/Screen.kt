package com.example.testapp.navigation

//This sealed class is used to define the screens in the app
sealed class Screen(val route: String) {
    //This companion object is used to define the movieId that will be passed to the details screen
    companion object {
        const val MOVIE_ID = "movie_id"
        const val MOVIE_PLAYER_ID = "movie_player_id"
        const val MOVIE_TITLE = "movie_title"
    }

    data object HomeScreen: Screen("home_screen")
    data object SplashScreen: Screen("splash_screen")
    data object MovieDetailsScreen: Screen("details_screen/{$MOVIE_ID}"){
        //This function is used to pass the movieId to the details screen
        fun passArguments(movieId: String): String {
            return "details_screen/$movieId"
        }
    }
    data object Media3Screen: Screen("movie_player_screen/{$MOVIE_TITLE}"){
        fun passArguments(movieTitle: String): String {
            return "movie_player_screen/$movieTitle"
        }
    }
}
