package com.example.testapp.presentation.screen.player

// This is where player controls are defined
interface PlayerControls {
    fun playPause()
    fun forward(durationMs :Long)
    fun rewind(durationMs :Long)
    fun handlePlaybackOnLifecycle(value: Boolean)
    fun rotateScreen()
    fun resizeVideoFrame()
    fun playNewVideo(item: VideoItem, lastPosition: Long)
    fun setLoadingState(value: Boolean)
    fun isPlaybackStarted(value: Boolean)
}

