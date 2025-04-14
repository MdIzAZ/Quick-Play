package com.pro.quickplay.presentation.video_screen

sealed class VideoEvents {

    data object TogglePlayMode: VideoEvents()

    data class ProgressionChanged(val progress: Float) : VideoEvents()

    data object SkipNext: VideoEvents()

    data object SkipPrevious: VideoEvents()

    data class ChangeVideoSpeed(val speed: Float) : VideoEvents()

    data class SeekFor(val sec: Int): VideoEvents()

}