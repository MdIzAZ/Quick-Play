package com.pro.quickplay.presentation.navigation

import androidx.annotation.OptIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.TransformOrigin
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pro.quickplay.domain.entity.FolderItem
import com.pro.quickplay.presentation.homescreen.HomeScreen
import com.pro.quickplay.presentation.homescreen.HomeViewModel
import com.pro.quickplay.presentation.video_list_screen.VideoListScreen
import com.pro.quickplay.presentation.video_list_screen.VideoListScreenEvents
import com.pro.quickplay.presentation.video_screen.VideoEvents
import com.pro.quickplay.presentation.video_screen.VideoScreen
import com.pro.quickplay.presentation.video_screen.VideoViewModel


@OptIn(UnstableApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    videoViewModel: VideoViewModel,
    isInLockMode: Boolean,
    onLockBtnClick: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {

        composable<Routes.HomeScreen> {
            val videos by homeViewModel.videos.collectAsStateWithLifecycle()
            val folderItems = videos.map {
                FolderItem(it.key, it.value.size)
            }
            HomeScreen(
                folderItems = folderItems,
                onClick = { folder ->
                    navController.navigate(Routes.VideoListScreen(folder))
                },
                onBackClick = {}
            )
        }

        composable<Routes.VideoListScreen>(
            enterTransition = {
                scaleIn(transformOrigin = TransformOrigin.Center)
            },
            exitTransition = {
                scaleOut(
                    transformOrigin = TransformOrigin.Center,
                )
            }
        ) { it ->
            val args = it.toRoute<Routes.VideoListScreen>()
            val videoMap by homeViewModel.videos.collectAsStateWithLifecycle()
            val videos = videoMap.getValue(args.folderName)
            var filePaths = videos.map { it.filePath }
            val onEvent = homeViewModel::onEvent

            VideoListScreen(
                folderName = args.folderName.replaceFirstChar { firstChar ->
                    firstChar.uppercaseChar()
                },
                videos = videos,
                onBackClick = { navController.navigateUp() },
                onVideoClick = {
                    videoViewModel.preparePlayList(videos)
                    navController.navigate(Routes.VideoScreen("nothing"))
                    videoViewModel.seekToMediaItem(it)
                },
                onSortVideos = { folderName, sortBy, sortType ->
                    onEvent(VideoListScreenEvents.Sort(folderName, sortBy, sortType) { paths ->
                        filePaths = paths
                    })
                }
            )

        }



        composable<Routes.VideoScreen>(
            enterTransition = {
                scaleIn(transformOrigin = TransformOrigin.Center)
            },
            exitTransition = {
                scaleOut(
                    transformOrigin = TransformOrigin.Center,
                )
            }
        ) {
            val args = it.toRoute<Routes.VideoScreen>()
            val videoState by videoViewModel.videoState.collectAsStateWithLifecycle()
            val onEvent = videoViewModel::onEvent


            VideoScreen(
                exoPlayer = videoViewModel.exoPlayer,
                togglePlayMode = { onEvent(VideoEvents.TogglePlayMode) },
                isPlaying = videoState.isPlaying,
                totalDuration = videoState.totalTime,
                currentDuration = videoState.currentTime,
                isInLockMode = isInLockMode,
                onLockBtnClick = onLockBtnClick,
                slideSlider = { f -> onEvent(VideoEvents.ProgressionChanged(f)) },
                sliderProgression = videoState.progress,
                onSkipNext = { onEvent(VideoEvents.SkipNext) },
                onSkipPrevious = { onEvent(VideoEvents.SkipPrevious) },
                onPlaybackSpeedChange = { s -> onEvent(VideoEvents.ChangeVideoSpeed(s)) },
                onSeek = {sec -> onEvent(VideoEvents.SeekFor(sec))}
            )
        }


    }
}