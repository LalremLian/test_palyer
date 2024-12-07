package com.example.testapp.presentation.screen.player

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class PlayerViewModel @Inject constructor(
    val player: Player,
) : ViewModel(), PlayerControls {

    // State Flows for player controls
    private val _isPlayingStateFlow = MutableStateFlow(true)
    val isPlayingStateFlow = _isPlayingStateFlow.asStateFlow()
    
    private val _isPlaybackStartedStateFlow = MutableStateFlow(false)
    val isPlaybackStartedStateFlow = _isPlaybackStartedStateFlow.asStateFlow()
    
    private val _isPlayerLoadingStateFlow = MutableStateFlow(true)
    val isPlayerLoadingStateFlow = _isPlayerLoadingStateFlow.asStateFlow()
    
    private val _resizeModeStateFlow = MutableStateFlow(AspectRatioFrameLayout.RESIZE_MODE_FIT)
    val resizeModeStateFlow = _resizeModeStateFlow.asStateFlow()
    
    private val _playerOrientationStateFlow = MutableStateFlow(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val playerOrientationStateFlow = _playerOrientationStateFlow.asStateFlow()
    
    init {
        // Set the player listener
        player.apply { prepare() }
    }
    
    override fun onCleared() {
        super.onCleared()
        player.release()
    }
    
    private fun updateCurrentVideoItem(videoItem: VideoItem, lastPosition: Long) {
        setMediaItem(
            videoItem.contentUri,
            lastPosition = lastPosition
        )
    }

    private var frameList = listOf(
        "fit",
        "fill",
        "crop",
        "zoom"
    )
    private var currentIndex = 0

    // Resize the video frame
    override fun resizeVideoFrame() {
        val mode = frameList[currentIndex]
        _resizeModeStateFlow.value = when (mode) {
            "crop" -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
            "fit" -> AspectRatioFrameLayout.RESIZE_MODE_FIT
            "fill" -> AspectRatioFrameLayout.RESIZE_MODE_FILL
            "zoom" -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            else -> throw IllegalArgumentException("Invalid resize mode: $mode")
        }

        // Update the index for the next call
        currentIndex = (currentIndex + 1) % frameList.size
    }

    // Set the media item to the player
    private fun setMediaItem(
        uri: Uri,
        lastPosition: Long = 0,
    ) {
        val mediaItem = MediaItem.Builder()
            .setUri(uri)
//            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        
        player.apply {
            seekTo(if(lastPosition > 0) lastPosition else 0)
            addMediaItem(mediaItem)
            playWhenReady = true
            if (isPlaying) {
                _isPlayingStateFlow.value = true
            }
        }
    }

    fun releasePlayer() {
        player.release()
    }

    // Player play/pause
    override fun playPause() {
        if (player.isPlaying) {
            player.pause().also {
                _isPlayingStateFlow.value = false
            }
        } else {
            player.play().also {
                _isPlayingStateFlow.value = true
            }
        }
    }

    // Player forward/rewind
    override fun forward(durationMs: Long) {
        player.apply {
            this.seekTo(this.currentPosition + durationMs)
        }
    }
    override fun rewind(durationMs: Long) {
        player.apply {
            this.seekTo(this.currentPosition - durationMs)
        }
    }

    // Handle the playback on lifecycle
    override fun handlePlaybackOnLifecycle(value: Boolean) {
        if (player.isPlaying && value) {
            player.pause().also {
                _isPlayingStateFlow.value = false
            }
        } else if (!player.isPlaying && !value) {
            player.play().also {
                _isPlayingStateFlow.value = true
            }
        }
    }

    // Rotate the screen
    override fun rotateScreen() {
        val orientation = if (playerOrientationStateFlow.value == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        _playerOrientationStateFlow.value = orientation
    }

    // Play a new video in case of a new selection
    override fun playNewVideo(item: VideoItem, lastPosition: Long) {
        player.clearMediaItems()
        updateCurrentVideoItem(item, lastPosition = lastPosition)
    }
    
    override fun setLoadingState(value: Boolean) {
        _isPlayerLoadingStateFlow.value = value
    }

    override fun isPlaybackStarted(value: Boolean) {
        _isPlaybackStartedStateFlow.value = value
    }
    
    companion object {
        const val TAG = "PlayerViewModel"
    }
}
