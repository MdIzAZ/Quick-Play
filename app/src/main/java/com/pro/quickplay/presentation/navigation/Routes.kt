package com.pro.quickplay.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object HomeScreen : Routes()

    @Serializable
    data class VideoListScreen(val folderName: String) : Routes()

    @Serializable
    data class VideoScreen(val videoPath: String) : Routes()

}