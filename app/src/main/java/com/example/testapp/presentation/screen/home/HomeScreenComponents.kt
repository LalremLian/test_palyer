package com.example.testapp.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.network.model.Search
import com.example.testapp.presentation.composables.CustomImageAsync
import com.example.testapp.presentation.composables.CustomText
import com.example.testapp.ui.theme.Background_Black_70

@Preview(showBackground = false)
@Composable
fun MovieTrendingItem(
    it: Search? = null,
    onItemClick: (String?) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(Color.Transparent)
            .padding(8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(it?.imdbID) }
        ) {
            CustomImageAsync(
                imageUrl = "${it?.Poster}",
                size = 512,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .height(240.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = "ImageRequest example",
            )

            Box(
                modifier = Modifier
                    .width(35.dp)
                    .height(25.dp)
                    .padding(start = 5.dp, top = 5.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .align(Alignment.TopStart)
                    .background(Background_Black_70)
            )
            {
                CustomText(
                    text = "HD",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

        CustomText(
            text = it?.Title ?: "Error",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overFlow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, top = 5.dp, end = 5.dp)
                .align(Alignment.Start)
        )
        CustomText(
            text = "${it?.Year}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overFlow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, top = 5.dp, end = 5.dp)
                .align(Alignment.Start)
        )
    }
}