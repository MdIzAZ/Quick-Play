package com.pro.quickplay.data.repo

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.pro.quickplay.domain.entity.VideoItem
import com.pro.quickplay.domain.repo.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.log10
import kotlin.math.pow

class VideoRepositoryImp(
    private val context: Context,
) : VideoRepository {

    override fun getAllVideosAndFolders(): Flow<Map<String, List<VideoItem>>> = flow {

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
                                id = id,
                                displayName = name,
                                dateAdded = formatDateAdded(dateAdded),
                                filePath = data,
                                bucketName = bucketName,
                                duration = duration,
                                size = size,
                                width = width,
                                height = height,
                                formattedDuration = formatTime(duration),
                                formattedSize = formatFileSize(size)
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
        emit(videoList.groupBy { it.bucketName })
    }

    private fun formatDateAdded(dateAdded: Long): String {
        val date = Date(dateAdded * 1000)
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return format.format(date)
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

    private fun formatFileSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(sizeInBytes.toDouble()) / log10(1024.0)).toInt()
        return String.format(
            Locale.getDefault(),
            "%.2f %s",
            sizeInBytes / 1024.0.pow(digitGroups.toDouble()),
            units[digitGroups]
        )
    }


}