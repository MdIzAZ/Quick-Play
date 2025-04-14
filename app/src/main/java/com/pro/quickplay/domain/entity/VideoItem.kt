package com.pro.quickplay.domain.entity

data class VideoItem(
    val id: Long = 0,
    val displayName: String = "Unknown",
    val dateAdded: String ,
    val filePath: String = "",
    val bucketName: String = "",
    val formattedDuration: String = "",
    val formattedSize: String,
    val duration: Long = 0,
    val size: Long = 0,
    val width: Int = 0,
    val height: Int = 0
)
