package com.pro.quickplay.presentation.video_screen

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.pro.quickplay.PlaybackService
import com.pro.quickplay.domain.entity.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class VideoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val exoPlayer: ExoPlayer,
) : ViewModel() {

    private lateinit var mediaController: MediaController

    private val _videoState = MutableStateFlow(VideoScreenState())
    val videoState = _videoState.asStateFlow()

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture =
            MediaController.Builder(context, sessionToken).buildAsync()
        listenControllerFuture(controllerFuture)

    }



    private fun listenControllerFuture(controllerFuture: ListenableFuture<MediaController>) {
        controllerFuture.addListener(
            {
                mediaController = controllerFuture.get()
                listenMediaCallbacks(mediaController)
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun listenMediaCallbacks(mediaController: MediaController) {
        mediaController.addListener(
            object : Player.Listener {

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    handlePlayState(isPlaying)
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    Log.d("izaz", "metadata changed: ${mediaMetadata.title}")
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    _videoState.value = _videoState.value.copy(
                        currentTime = "0:00"
                    )
                }


                override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                    _videoState.update {
                        it.copy(
                            totalTime = formatTime(mediaController.duration)
                        )
                    }
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                        _videoState.update {
                            it.copy(
                                currentTime = formatTime(newPosition.contentPositionMs),
                                progress = newPosition.contentPositionMs.toFloat()/mediaController.duration.toFloat()
                            )
                        }
                    }
                }
            }
        )
    }

    fun onEvent(event: VideoEvents) {
        when (event) {
            is VideoEvents.ProgressionChanged -> {
                _videoState.value = _videoState.value.copy(
                    progress = event.progress
                )
                val currentPosition = event.progress * mediaController.duration
                seekToPosition(currentPosition.toLong())
            }

            VideoEvents.TogglePlayMode -> {
                if (_videoState.value.isPlaying) {
                    pause()
                } else {
                    playVideo()
                }
            }

            VideoEvents.SkipNext -> {
                seekToNext()
            }

            VideoEvents.SkipPrevious -> {
                seekToPrevious()
            }

            is VideoEvents.ChangeVideoSpeed -> {
                changeVideoPlaybackSpeed(event.speed)
            }

            is VideoEvents.SeekFor -> {
                seekFor(event.sec)
            }
        }

    }


    private fun changeVideoPlaybackSpeed(speed: Float) {
        mediaController.setPlaybackSpeed(speed)
    }

    fun prepareVideo(videoPath: String) {
        val mediaItem = MediaItem.fromUri(videoPath)
        mediaController.addMediaItem(mediaItem)
        mediaController.prepare()
    }


    fun preparePlayList(videos: List<VideoItem>) {


        mediaController.clearMediaItems()
        val mediaItems = videos.map { videoItem ->
            Log.d("izaz", "videoItem: ${videoItem.filePath}")

            val videoUri = Uri.parse(videoItem.filePath)

            MediaItem.Builder()
                .setUri(videoUri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(videoItem.displayName)
                        .build()
                )
                .build()
        }
        mediaController.addMediaItems(mediaItems)
        mediaController.prepare()
        mediaController.playWhenReady = false
    }

    fun playVideo() {
        mediaController.playWhenReady = true
        mediaController.play()
        _videoState.value = _videoState.value.copy(totalTime = formatTime(mediaController.duration))
    }

    fun seekFor(sec: Int) {
        val currentPos = mediaController.currentPosition
        val newPos = currentPos + (sec * 1000)
        mediaController.seekTo(newPos)
    }

    fun seekToNext() {
        if (mediaController.hasNextMediaItem()) {
            mediaController.seekToNext()
        }
    }

    fun seekToPrevious() {
        if (mediaController.hasPreviousMediaItem()) {
            mediaController.seekToPrevious()
        }
    }

    private fun seekToPosition(positionMs: Long) {
        mediaController.seekTo(positionMs)
    }


    fun seekToMediaItem(index: Int) {
        mediaController.seekTo(index, 0L)
    }

    fun pause() {
        mediaController.pause()
    }

    private fun handlePlayState(isPlaying: Boolean) {
        if (isPlaying) {
            _videoState.value = _videoState.value.copy(isPlaying = true)
            startProgressUpdates()
        } else {
            _videoState.value = _videoState.value.copy(isPlaying = false)
//            stopProgressUpdates()
        }
    }


    private var progressJob: Job? = null

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (mediaController.isPlaying) {
                val currentPosition = mediaController.currentPosition
                val progression = (currentPosition / mediaController.duration.toFloat())

                _videoState.value = _videoState.value.copy(
                    progress = progression,
                    currentTime = formatTime(currentPosition)
                )
                delay(1000)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
    }


    private fun formatTime(duration: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
        val seconds = (TimeUnit.MILLISECONDS.toSeconds(duration) % 60).takeIf { it > 0 } ?: 1

        return if (hours > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }

    }


}