package com.pro.quickplay.presentation.homescreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.pro.quickplay.domain.entity.VideoItem
import com.pro.quickplay.domain.repo.VideoRepository
import com.pro.quickplay.presentation.video_list_screen.SortBy
import com.pro.quickplay.presentation.video_list_screen.SortType
import com.pro.quickplay.presentation.video_list_screen.VideoListScreenEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
open class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: VideoRepository,
) : ViewModel() {

    private val _videoFolder = MutableStateFlow<Map<String, List<VideoItem>>>(emptyMap())
    val videos: StateFlow<Map<String, List<VideoItem>>> = _videoFolder.asStateFlow()


    init {
        viewModelScope.launch {
            refreshVideoItems()
        }
    }



    suspend fun refreshVideoItems() {
        try {
            repository.getAllVideosAndFolders().collect {
                _videoFolder.emit(it)
            }
        } catch (e: Exception) {
            Log.d("izaz", "Error while fetching videos")
        }

    }

    fun onEvent(event: VideoListScreenEvents) {
        when (event) {
            is VideoListScreenEvents.Sort -> {
                sortBy(event.folderName, event.sortBy, event.sortType, event.onComplete)
            }
        }
    }


    private fun sortBy(
        folderName: String,
        sortBy: SortBy,
        type: SortType,
        onComplete: (List<String>) -> Unit,
    ) {
        val currentVideos = _videoFolder.value[folderName] ?: return

        val sortedVideos = when (type) {
            SortType.ASC -> {
                when (sortBy) {
                    SortBy.DATE -> currentVideos.sortedBy { it.dateAdded }
                    SortBy.NAME -> currentVideos.sortedBy { it.displayName }
                    SortBy.SIZE -> currentVideos.sortedBy { it.size }
                    SortBy.DURATION -> currentVideos.sortedBy { it.duration }
                }
            }

            SortType.DESC -> {
                when (sortBy) {
                    SortBy.DATE -> currentVideos.sortedByDescending { it.dateAdded }
                    SortBy.NAME -> currentVideos.sortedByDescending { it.displayName }
                    SortBy.SIZE -> currentVideos.sortedByDescending { it.size }
                    SortBy.DURATION -> currentVideos.sortedByDescending { it.duration }
                }
            }
        }

        _videoFolder.value = _videoFolder.value.toMutableMap().apply {
            this[folderName] = sortedVideos
            onComplete(sortedVideos.map { it.filePath })
        }
    }


   /* private fun getAllVideos(context: Context): Map<String, List<VideoItem>> {

        val videoList = mutableListOf<VideoItem>()
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        // Projection: columns we want to retrieve from the MediaStore
        val projection = arrayOf(
            MediaStore.Video.Media._ID,  // Unique ID of the video
            MediaStore.Video.Media.DISPLAY_NAME,  // Video file name
            MediaStore.Video.Media.DATA,  // Video file path
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME, // Folder (bucket) name
            MediaStore.Video.Media.DURATION,  // Video duration
            MediaStore.Video.Media.SIZE,  // File size
            MediaStore.Video.Media.WIDTH,  // Video width
            MediaStore.Video.Media.HEIGHT,  // Video height
            MediaStore.Video.Media.DATE_ADDED
        )

        // Query the MediaStore for video files
        val cursor = context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            "${MediaStore.Video.Media.DATE_ADDED} DESC"  // Sort by date added
        )

        try {
            if (cursor != null && cursor.count > 0) {
                cursor.use {
                    val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                    val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                    val dataColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                    val bucketColumn =
                        it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                    val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                    val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                    val widthColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
                    val heightColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
                    val dateAddedColumn =
                        it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

                    while (it.moveToNext()) {
                        val id = it.getLong(idColumn)
                        val name = it.getString(nameColumn)
                        val data = it.getString(dataColumn)
                        val bucketName = it.getString(bucketColumn) ?: "Unknown"
                        val duration = it.getLong(durationColumn)
                        val size = it.getLong(sizeColumn)
                        val width = it.getInt(widthColumn)
                        val height = it.getInt(heightColumn)
                        val dateAdded = it.getLong(dateAddedColumn)

                        videoList.add(
                            VideoItem(
                                id,
                                name,
                                dateAdded,
                                data,
                                bucketName,
                                duration,
                                size,
                                width,
                                height
                            )
                        )
                    }
                }
            } else {
                Log.d("izaz", "No videos found or cursor is null.")
            }
        } catch (e: Exception) {
            Log.e("izaz", "Error querying videos: ${e.message}", e)
        }

        // return map where key is bucket name
        return videoList.groupBy { it.bucketName }
    }*/


}



