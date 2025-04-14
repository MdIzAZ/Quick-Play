package com.pro.quickplay.presentation.video_screen

data class VideoScreenState(
    val isPlaying: Boolean = false,
    val totalTime: String = "0: 00",
    val currentTime: String = "0: 00",
    val isLandscape: Boolean = false,
    val progress: Float = 0f,
    val shouldShowControls: Boolean = false,
)