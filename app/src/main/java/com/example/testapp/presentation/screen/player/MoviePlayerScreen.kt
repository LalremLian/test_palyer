package com.example.testapp.presentation.screen.player

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.testapp.ui.theme.Loading_Orange
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class) @Composable
fun MoviePlayerScreen(
    title: String,
    navController: NavController,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val mPref = LocalContext.current.getSharedPreferences("MyPref", Context.MODE_PRIVATE)

    // Video URL is set my default because the video is not available in the demo API
    val url = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"

    val localConfig = LocalConfiguration.current

    //Collecting player states
    val isPlaying by playerViewModel.isPlayingStateFlow.collectAsStateWithLifecycle()
    val isPlaybackStarted by playerViewModel.isPlaybackStartedStateFlow.collectAsStateWithLifecycle()
    val isPlayerLoading by playerViewModel.isPlayerLoadingStateFlow.collectAsStateWithLifecycle()
    val resizeMode by playerViewModel.resizeModeStateFlow.collectAsStateWithLifecycle()
    val playerOrientation by playerViewModel.playerOrientationStateFlow.collectAsStateWithLifecycle()

    val playerHeight = if (playerOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        (localConfig.screenWidthDp * 9 / 16).dp
    } else {
        localConfig.screenHeightDp.dp
    }

    LaunchedEffect(Unit) {
        //Play the video when the screen is launched
        playerViewModel.playNewVideo(
            VideoItem(
                name = "Second Item",
                mediaItem = MediaItem.fromUri(url),
                contentUri = url.toUri()
            ),
            //This is the last position of the video stored in the shared preference
            lastPosition = mPref.getString("lastPosition", "0")?.toLong() ?: 0
        )
    }

    var showControls by rememberSaveable {
        mutableStateOf(false)
    }

    //Handling the visibility of the controls
    LaunchedEffect(key1 = showControls) {
        if (showControls) {
            delay(4_000)
            showControls = false
        }
    }

    //This is the back button handler
    BackHandler { navController.popBackStack() }

    val playerViewModifier = if (playerOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        Modifier
            .fillMaxWidth()
            .height(playerHeight)
    } else {
        Modifier.fillMaxSize()
    }.background(Color.Black)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = playerViewModifier
        ) {
            // PlayerView
            PlayerView(
                modifier = playerViewModifier,
                playerViewModel = playerViewModel,
                isPlaying = isPlaying,
                isPlaybackStarted = isPlaybackStarted,
                resizeMode = resizeMode,
                isControlsVisible = showControls,
                onClick = { showControls = it }
            )

            if (isPlayerLoading) {
                CircularProgressIndicator(
                    color = Loading_Orange, // Set the desired color for the ProgressBar
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Controls overlaying on top of the PlayerView
            Column(
                modifier = Modifier
                    .matchParentSize() // Ensure the Column takes the full height of the Box
                    .background(
                        if (showControls) {
                            Color.Black.copy(0.5f)
                        } else {
                            Color.Transparent
                        }
                    ),
            ) {
                AnimatedVisibility(
                    visible = showControls,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)// Add this modifier
                ) {
                    // Content of the first row (e.g., UpperControls)
                    UpperControls(
                        title = title,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onBackIconClick = { navController.popBackStack() }
                    )
                }

                // Third row
                AnimatedVisibility(
                    showControls,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F) // Add this modifier
                ) {
                    // Content of the third row (e.g., MiddleControls)
                    MiddleControls(
                        isPlaying = isPlaying,
                        onClick = {
                            showControls = !showControls
                        },
                        onPlayPauseClick = {
                            playerViewModel.playPause()
                        },
                        onSeekForwardClick = {
                            playerViewModel.forward(10000)
                        },
                        onSeekBackwardClick = {
                            playerViewModel.rewind(10000)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                AnimatedVisibility(
                    showControls,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                ) {
                    BottomControls(
                        player = playerViewModel.player,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                        totalTime = playerViewModel.player.contentDuration,
                        onRotateScreenClick = {

                        },
                        resizeMode = {
                            playerViewModel.resizeVideoFrame()
                        },
                        onResizeModeChange = {  }
                    )
                }
            }
        }
    }
}
