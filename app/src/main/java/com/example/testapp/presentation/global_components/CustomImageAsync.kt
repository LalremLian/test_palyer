package com.example.testapp.presentation.global_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.testapp.R

// Custom Image composable to load images from the internet
@Composable
fun CustomImageAsync(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    size: Int = 512,
    contentDescription: String? = null
) {
    val context = LocalContext.current

    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .size(size)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    Box {
        Image(
            painterResource(id = R.drawable.ic_logo),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )

        AsyncImage(
            model = imageRequest,
            modifier = modifier,
            contentScale = contentScale,
            contentDescription = contentDescription,
        )
    }
}