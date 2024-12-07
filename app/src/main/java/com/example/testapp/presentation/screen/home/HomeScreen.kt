package com.example.testapp.presentation.screen.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.data.Resource
import com.example.testapp.navigation.Screen
import com.example.testapp.presentation.composables.CustomAlertDialog
import com.example.testapp.presentation.composables.CustomImageCarousel
import com.example.testapp.ui.theme.Loading_Orange

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val moviesListState by viewModel.detailsFlow.collectAsState()

    val isExitDialogVisible = rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler { isExitDialogVisible.value = true }

    val lazyMovieItems = viewModel.moviesFlow.collectAsLazyPagingItems()

    CustomAlertDialog(
        showDialog = isExitDialogVisible,
        message = "Are you sure you want to close the application?",
        confirmText = "Yes",
        dismissText = "No",
        onConfirmClick = { activity.finish() },
        onDismissClick = { isExitDialogVisible.value = false }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Slider Section
        item {
            moviesListState.let {
                when (it) {
                    is Resource.Loading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LinearProgressIndicator(
                                color = Loading_Orange,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                            )
                        }
                    }

                    is Resource.Success -> {
                        val movie = it.data
                        Box(
                            modifier = Modifier
                                .wrapContentHeight()
                                .background(Color.Transparent)
                        ) {
                            CustomImageCarousel(
                                sliderList = movie,
                                onItemClick = { movieId ->
                                    navController.navigate(
                                        Screen.MovieDetailsScreen.passArguments(
                                            movieId = movieId
                                        )
                                    )
                                }
                            )
                        }
                    }

                    is Resource.Error -> {}

                    else -> {}
                }
            }
        }

        //Pagination
        items(lazyMovieItems.itemCount) { index ->
            key(lazyMovieItems[index]?.imdbID ?: index) {
                MovieTrendingItem(
                    it = lazyMovieItems[index]!!,
                    onItemClick = { id ->
                        navController.navigate(
                            Screen.MovieDetailsScreen.passArguments(
                                movieId = id ?: ""
                            )
                        )
                    }
                )
            }
        }
    }
}
