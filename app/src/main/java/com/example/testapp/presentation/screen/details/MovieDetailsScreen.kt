package com.example.testapp.presentation.screen.details

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.data.Resource
import com.example.network.model.MovieDetailsResponse
import com.example.testapp.R
import com.example.testapp.navigation.Screen
import com.example.testapp.presentation.global_components.CustomImage
import com.example.testapp.presentation.global_components.CustomImageAsync
import com.example.testapp.presentation.global_components.CustomText
import com.example.testapp.ui.theme.Background_Black
import com.example.testapp.ui.theme.Loading_Orange

@Composable
fun MovieDetailsScreen(
    navController: NavController,
    viewModel: MovieDetailsScreenViewModel = hiltViewModel(),
    id: String,
) {
    val movieDetailsState by viewModel.detailsFlow.collectAsState()

    LaunchedEffect(key1 = id) {
        viewModel.getMoviesDetails(id)
    }

    BackHandler {
        navController.popBackStack()
    }

    movieDetailsState.let {
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

                DetailsScreenContent(
                    movie = movie,
                    navController = navController,
                    viewModel = viewModel,
                    onWatchClick = {
                        navController.navigate(
                            Screen.Media3Screen.passArguments(
                                movieTitle = movie.Title ?: "No title available"
                            )
                        )
                    },
                )
            }

            is Resource.Error -> {
                Toast.makeText(LocalContext.current, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenContent(
    movie: MovieDetailsResponse,
    navController: NavController,
    viewModel: MovieDetailsScreenViewModel? = null,
    onWatchClick: () -> Unit = {},
) {
    val context: Context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background_Black)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            CustomImageAsync(
                imageUrl = "${movie.Poster}",
                size = 512,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentDescription = "Poster",
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFF1A1A1A),
                            ),
                            startY = 20f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .clip(RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically

            ) {
                CustomImageAsync(
                    imageUrl = "${movie.Poster}",
                    size = 512,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(130.dp)
                        .padding(start = 16.dp, end = 8.dp),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "ImageRequest example",
                )

                Column {
                    CustomText(
                        text = movie.Title!!,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    )
                    CustomText(
                        text = movie.Language ?: "No language available",
                        color = Color(0xFFB1B0B0),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    CustomText(
                        text = movie.Director ?: "No director available",
                        color = Color(0xFFB1B0B0),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    CustomText(
                        text = movie.Year.toString(),
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(145.dp)
                    .height(43.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Loading_Orange)
                    .clickable { onWatchClick.invoke() }
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    CustomText(
                        text = "Watch",
                        color = Background_Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .padding(top = 1.dp)
                    )
                    Image(
                        painterResource(id = R.drawable.ic_play),
                        contentDescription = "Watch",
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(20.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(145.dp)
                    .height(43.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                "Coming soon! Please try again later.",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                    .border(1.dp, Loading_Orange, shape = RoundedCornerShape(5.dp))
            ) {
                Row(modifier = Modifier.align(Alignment.Center)) {
                    CustomText(
                        text = "Download",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(top = 1.dp, end = 2.dp)
                    )
                    CustomImage(
                        imageId = R.drawable.ic_download,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(18.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        CustomText(
            text = "Writer",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        CustomText(
            text = movie.Writer!!,
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        CustomText(
            text = "Box Office",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        CustomText(
            text = movie.BoxOffice ?: "No box office available",
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        CustomText(
            text = "Cast",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        CustomText(
            text = movie.Actors ?: "No actors available",
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        CustomText(
            text = "Overview",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp)
        )
        CustomText(
            text = movie.Plot ?: "No plot available",
            color = Color(0xFFBDBBBB),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
