package com.example.testapp.navigation

//sealed class Graph (val route: String ){
//    data object RootGraph: Graph("root_graph")
//    data object HomeGraph: Graph("home_graph")
//}

import kotlinx.serialization.Serializable

@Serializable
sealed class Graph{
    @Serializable
    data object HomeGraph: Graph()
}
