package com.pro.quickplay.presentation.video_screen

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.pro.quickplay.presentation.components.CustomDropDownMenu
import com.pro.quickplay.presentation.video_screen.components.MenuTypes
import com.pro.quickplay.presentation.video_screen.components.SliderWithTime
import com.pro.quickplay.presentation.video_screen.components.VideoControlSection
import com.pro.quickplay.presentation.video_screen.components.VideoScreenTop
import com.pro.quickplay.util.PlaybackSpeed
import com.pro.quickplay.util.ResizeMode
import com.pro.quickplay.util.aspectRatioList
import com.pro.quickplay.util.speedList

@OptIn(UnstableApi::class)
@Composable
fun VideoScreen(
    exoPlayer: ExoPlayer,
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    totalDuration: String,
    currentDuration: String,
    sliderProgression: Float,
    isInLockMode: Boolean,
    onLockBtnClick: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onSeek: (duration: Int) -> Unit,
    onPlaybackSpeedChange: (Float) -> Unit,
    slideSlider: (Float) -> Unit,
    togglePlayMode: () -> Unit,
) {

    var shouldShowOptions by remember { mutableStateOf(true) }
    var isDropDownExpanded by remember { mutableStateOf(false) }
    var currentMenu by remember { mutableStateOf(MenuTypes.ASPECT) }
    var videoResizeMode by remember { mutableStateOf(ResizeMode.FIT) }



    val insets = WindowInsets.waterfall
    val density = LocalDensity.current

//    Scaffold(modifier = modifier) {it->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(insets.asPaddingValues())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (isInLockMode) {
                            return@detectTapGestures
                        }
                        shouldShowOptions = !shouldShowOptions
                    },
                    onDoubleTap = {
                        val width = size.width
                        if (it.x < width / 2) onSeek(-10)
                        else if (it.x > width / 2) onSeek(+10)
                    }
                )

            }

    ) {

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true
                    useController = false
                }
            },
            update = { view ->
                view.resizeMode = when (videoResizeMode) {
                    ResizeMode.FIT -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                    ResizeMode.CROP -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    ResizeMode.STRETCH -> AspectRatioFrameLayout.RESIZE_MODE_FILL
                    ResizeMode.ZOOM -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            }
        )


        //top section
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopEnd),
            visible = shouldShowOptions && !isInLockMode,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it }),
            content = {
                VideoScreenTop(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .padding(5.dp)
                        .wrapContentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .wrapContentSize(),
                    onClick = {
                        when (it) {
                            MenuTypes.ASPECT -> {
                                currentMenu = MenuTypes.ASPECT
                                isDropDownExpanded = true
                            }

                            MenuTypes.SPEED -> {
                                currentMenu = MenuTypes.SPEED
                                isDropDownExpanded = true
                            }

                        }
                    }
                )
            }
        )


        //dropdown menu
        CustomDropDownMenu(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 100.dp, end = 40.dp),
            isExpanded = isDropDownExpanded,
            onMenuItemClick = {
                when (currentMenu) {
                    MenuTypes.ASPECT -> handleAspectMenuItemClick(it, changeResizeMode = { resize ->
                        videoResizeMode = resize
                    })

                    MenuTypes.SPEED -> handleSpeedMenuItemClick(
                        it,
                        onPlaybackSpeedChange = { speed ->
                            onPlaybackSpeedChange(speed)
                        })

                    else -> {}
                }
                isDropDownExpanded = false
            },
            onDismissReq = { isDropDownExpanded = false },
            itemList = when (currentMenu) {
                MenuTypes.ASPECT -> aspectRatioList
                MenuTypes.SPEED -> speedList
                else -> {
                    emptyList()
                }
            }
        )


        //  [play pause, prev. next]
        if (shouldShowOptions && !isInLockMode) {
            VideoControlSection(
                modifier = Modifier.align(Alignment.Center),
                isPlaying = isPlaying,
                onSkipPrevious = onSkipPrevious,
                onSkipNext = onSkipNext,
                togglePlayMode = togglePlayMode,
            )
        }



        //slider with time
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .safeDrawingPadding()
            ,
            visible = shouldShowOptions && !isInLockMode,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            SliderWithTime(
                totalDuration = totalDuration,
                currentDuration = currentDuration,
                value = sliderProgression,
                onValueChange = {
                    slideSlider(it)
                }
            )
        }

        if (isInLockMode) {
            IconButton(
                modifier = Modifier
                    .safeDrawingPadding()
                    .align(Alignment.BottomEnd),
                onClick = { onLockBtnClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.LockOpen,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }
        }

    }
}


//}

fun handleAspectMenuItemClick(resizeMode: Any, changeResizeMode: (ResizeMode) -> Unit) {
    when (resizeMode) {
        ResizeMode.FIT -> changeResizeMode(resizeMode as ResizeMode)

        ResizeMode.CROP -> changeResizeMode(resizeMode as ResizeMode)

        ResizeMode.STRETCH -> changeResizeMode(resizeMode as ResizeMode)

        ResizeMode.ZOOM -> changeResizeMode(resizeMode as ResizeMode)
    }
}

fun handleSpeedMenuItemClick(speed: Any, onPlaybackSpeedChange: (Float) -> Unit) {
    when (speed) {
        PlaybackSpeed.X25 -> onPlaybackSpeedChange(0.25f)
        PlaybackSpeed.X50 -> onPlaybackSpeedChange(0.5f)
        PlaybackSpeed.X75 -> onPlaybackSpeedChange(0.5f)
        PlaybackSpeed.X100 -> onPlaybackSpeedChange(1f)
        PlaybackSpeed.X125 -> onPlaybackSpeedChange(1.25f)
        PlaybackSpeed.X150 -> onPlaybackSpeedChange(1.50f)
        PlaybackSpeed.X175 -> onPlaybackSpeedChange(1.75f)
        PlaybackSpeed.X200 -> onPlaybackSpeedChange(2f)

    }
}


fun adjustBrightness(context: Context, percentageChange: Float) {
    val window = (context as Activity).window
    val layoutParams = window.attributes
    layoutParams.screenBrightness =
        (layoutParams.screenBrightness + percentageChange).coerceIn(0.1f, 1f)
    window.attributes = layoutParams
}

fun adjustVolume(context: Context, percentageChange: Float) {

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val newVolume = (currentVolume + (maxVolume * percentageChange)).toInt().coerceIn(0, maxVolume)

    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
}



