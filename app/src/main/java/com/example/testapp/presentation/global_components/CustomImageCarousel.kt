package com.example.testapp.presentation.global_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.network.model.MoviesResponse
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

// Custom Image Carousel composable to display the images in a carousel view
@Composable
fun CustomImageCarousel(
    sliderList: MoviesResponse,
    onGlobalPositionedSize: ((selectedPosition: Int, carouselHeight: Float) -> Unit)? = null,
    onItemClick: (String) -> Unit = {},
) {
    val pagerState = rememberPagerState(
        initialPage = 3,
        pageCount = { sliderList.Search.size }
    )

    Column(modifier = Modifier.wrapContentSize()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 95.dp),
            pageSpacing = 5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .onGloballyPositioned {
                    onGlobalPositionedSize?.invoke(
                        pagerState.currentPage % sliderList.Search.size,
                        it.size.height.toFloat()
                    )
                }
        ) { index ->
            // key is used to make sure that the item is re-created when the index changes
            key(sliderList.Search[index].imdbID ?: "")
            {
                Card(
                    border = BorderStroke(1.dp, Color(0xFFB3B3B3)),
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .aspectRatio(0.67f / 1f)
                        .clickable { onItemClick(sliderList.Search[index].imdbID ?: "") }
                        .graphicsLayer {
                            val pageOffset =
                                (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
                            lerp(
                                start = 0.9f,
                                stop = 1f,
                                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                            ).also { scale ->
                                scaleX = scale * 1.1f
                                scaleY = scale * 1.2f
                            }
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                            )
                        }
                ) {
                    Box {
                        CustomImageAsync(
                            imageUrl = "${sliderList.Search[index].Poster}",
                            size = 512,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "ImageRequest example",
                        )
                    }
                }
            }
        }

        Text(
            text = sliderList.Search[pagerState.currentPage].Title ?: "",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = sliderList.Search[pagerState.currentPage].Year ?: "",
                color = Color(0xFFB3B3B3),
                fontSize = 14.sp
            )
        }
    }

    // Auto scroll the carousel on every 4 seconds
    LaunchedEffect(key1 = Unit) {
        repeat(
            times = Int.MAX_VALUE,
            action = {
                delay(4000)
                try {
                    val nextPage = (pagerState.currentPage + 1) % sliderList.Search.size
                    pagerState.animateScrollToPage(page = nextPage)
                } catch (exp: Exception) {
                    exp.printStackTrace()
                }
            }
        )
    }
}