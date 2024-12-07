package com.example.testapp.presentation.screen.player

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.annotation.OptIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.TimeBar
import com.example.testapp.R
import com.example.testapp.presentation.global_components.CustomImage
import com.example.testapp.presentation.global_components.CustomText
import com.example.testapp.ui.theme.Background_Black_40
import com.example.testapp.ui.theme.Loading_Orange
import com.example.testapp.util.findActivity
import com.example.testapp.util.toHhMmSs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@Preview
fun UpperControls(
    title: String = "",
    modifier: Modifier = Modifier,
    onBackIconClick: () -> Unit = {},
) {
    val iconModifier = Modifier
        .padding(8.dp)
        .size(24.dp)
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                onBackIconClick.invoke()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = iconModifier,
                tint = Color.White
            )
        }
        CustomText(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(
                    start = 4.dp,
                    end = 8.dp,
                    top = 12.dp,
                )
                .width(400.dp)
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
@Preview
fun MiddleControls(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    onClick: () -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    onSeekForwardClick: () -> Unit = {},
    onSeekBackwardClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,

        ) {
        MiddleControlsItem(
            icon = R.drawable.seek_backward,
            contentDescription = "Seek Backward",
            onIconClick = onSeekBackwardClick,
            onSingleClick = onClick,
            onDoubleClick = onSeekBackwardClick,
            size = 26.dp
        )

        MiddleControlsItem(
            icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_player,
            contentDescription = "Play/Pause",
            onIconClick = onPlayPauseClick,
            onSingleClick = onClick,
            onDoubleClick = onPlayPauseClick,
            modifier = Modifier.padding(horizontal = 44.dp),
            size = 46.dp
        )

        MiddleControlsItem(
            icon = R.drawable.seek_forward,
            contentDescription = "Seek Forward",
            onIconClick = onSeekForwardClick,
            onSingleClick = onClick,
            onDoubleClick = onSeekForwardClick,
            size = 26.dp
        )
    }
}

@kotlin.OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MiddleControlsItem(
    @DrawableRes icon: Int,
    contentDescription: String,
    onIconClick: () -> Unit,
    onSingleClick: () -> Unit,
    onDoubleClick: () -> Unit,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = onSingleClick,
                onDoubleClick = onDoubleClick
            ),
        contentAlignment = Alignment.Center
    ) {
        CustomImage(
            imageId = icon,
            modifier = Modifier.
            clickable { onIconClick() }
                .size(size)
        )
        IconButton(onClick = onIconClick) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = ""
            )
        }
    }
}

@Preview
@UnstableApi
@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    player: Player? = null,
    totalTime: Long = 0L,
    onRotateScreenClick: () -> Unit = {},
    resizeMode: () -> Unit = {},
    onResizeModeChange: () -> Unit = {}
) {
    var currentTime by remember {
        mutableLongStateOf(player?.currentPosition ?: 0L)
    }

    var isSeekInProgress by remember {
        mutableStateOf(false)
    }

    val timerCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        timerCoroutineScope.launch {
            while (true) {
                delay(500)
                if (!isSeekInProgress) {
                    currentTime = player?.currentPosition ?: 0L
                    Log.d("PlayerScreen", "timer running $currentTime")
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomSeekBar(
                player = player,
                isSeekInProgress = { isInProgress ->
                    isSeekInProgress = isInProgress
                },
                onSeekBarMove = { position ->
                    currentTime = position
                },
                totalDuration = totalTime,
                currentTime = currentTime,
                modifier = Modifier.weight(1F)
            )

            IconButton(
                onClick = resizeMode,
                modifier = Modifier
                    .size(32.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_full_screen),
                    contentDescription = null,
                    tint = Color.White
                )
            }

//            IconButton(
//                onClick = onResizeModeChange,
//                modifier = Modifier
//                    .size(32.dp)
//            ) {
//                Icon(
//                    painterResource(id = R.drawable.ic_pip),
//                    stringResource(id = R.string.picture_in_picture),
//                    tint = Color.White,
//                )
//            }
        }
        CustomText(
            text = "${currentTime.toHhMmSs()} / ${totalTime.toHhMmSs()}",
            modifier = Modifier.padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@UnstableApi
@Composable
fun CustomSeekBar(
    modifier: Modifier = Modifier,
    player: Player? = null,
    isSeekInProgress: (Boolean) -> Unit,
    onSeekBarMove: (Long) -> Unit,
    currentTime: Long,
    totalDuration: Long
) {
    AndroidView(
        factory = { context ->
            val listener = object : TimeBar.OnScrubListener {

                var previousScrubPosition = 0L

                override fun onScrubStart(timeBar: TimeBar, position: Long) {
                    isSeekInProgress(true)
                    previousScrubPosition = position
                }

                override fun onScrubMove(timeBar: TimeBar, position: Long) {
                    onSeekBarMove(position)
                }

                override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                    if (canceled) {
                        player?.seekTo(previousScrubPosition)
                    } else {
                        player?.seekTo(position)
                    }
                    isSeekInProgress(false)
                }
            }
            DefaultTimeBar(context).apply {
                setScrubberColor(Loading_Orange.toArgb())
                setPlayedColor(Loading_Orange.toArgb())
                setUnplayedColor(Background_Black_40.toArgb())
                addListener(listener)
                setDuration(totalDuration)
                setPosition(player?.currentPosition ?: 0)
            }
        },
        update = {
            it.apply {
                setPosition(currentTime)
            }
        },

        modifier = modifier
    )
}

@SuppressLint("CommitPrefEdits")
@OptIn(UnstableApi::class)
@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel,
    isPlaying: Boolean = false,
    isPlaybackStarted: Boolean = false,
    resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_FIT,
    isControlsVisible: Boolean = false,
    onClick: (Boolean) -> Unit = {}
) {
    val mPref = LocalContext.current.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            // Callback when the playback state changes
            when (state) {
                Player.STATE_IDLE, Player.STATE_BUFFERING -> {
                    // Handle buffering state
                    playerViewModel.setLoadingState(true)
                    onClick(false)
                }

                Player.STATE_READY -> {
                    // Handle Ready and End State
                    playerViewModel.setLoadingState(false)

                    if (state == Player.STATE_READY) {
                        playerViewModel.isPlaybackStarted(true)
                    }
                }
                Player.STATE_ENDED -> {
                    playerViewModel.player.seekTo(0)
                    playerViewModel.player.playWhenReady = true
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.d("PlaybackError", "onPlayerError: ${error.message}")
        }
    }

    var lifecycle by rememberSaveable {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner, key2 = context) {
        val activity = context.findActivity()
        val window = activity?.window ?: return@DisposableEffect onDispose {}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val observer = LifecycleEventObserver { _, event ->
                lifecycle = event
                when (event) {
                    // Save the last position of the video when the app goes to the background
                    Lifecycle.Event.ON_STOP -> {
                        mPref.edit().putString("lastPosition", playerViewModel.player.currentPosition.toString()).apply()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        mPref.edit().putString("lastPosition", playerViewModel.player.currentPosition.toString()).apply()
                        playerViewModel.player.release()
                    }
                    else -> {}
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            WindowInsetsControllerCompat(activity.window, activity.window.decorView).let { controller ->
                controller.hide(
                    WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.displayCutout()
                )
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                activity.window.attributes = activity.window.attributes.apply {
                    layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }

            onDispose {
                playerViewModel.player.removeListener(listener)
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        } else {
            // For Android versions below R, use deprecated methods
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            onDispose {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    AndroidView(
        factory = { context ->
            androidx.media3.ui.PlayerView(context).apply {
                player = playerViewModel.player.also {
                    it.addListener(listener)
                }
                this.useController = false
                this.resizeMode = resizeMode
                this.keepScreenOn = isPlaying
            }
        },
        update = {
            it.apply {
                this.useController = false
                this.resizeMode = resizeMode
                this.keepScreenOn = isPlaying
            }
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    mPref.edit().putString("lastPosition", it.player?.currentPosition.toString()).apply()
                }

                else -> Unit
            }
        },
        modifier = modifier.clickable(
            onClick = {
                if (isPlaybackStarted) {
                    onClick(!isControlsVisible)
                }
            },
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        )
    )
}