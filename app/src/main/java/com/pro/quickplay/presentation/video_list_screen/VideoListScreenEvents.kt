package com.pro.quickplay.presentation.video_list_screen

sealed class VideoListScreenEvents {

    data class Sort(
        val folderName: String,
        val sortBy: SortBy,
        val sortType: SortType,
        val onComplete: (List<String>) -> Unit,
    ) :
        VideoListScreenEvents()
}