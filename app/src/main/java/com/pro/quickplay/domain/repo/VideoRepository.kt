package com.pro.quickplay.domain.repo

import com.pro.quickplay.domain.entity.VideoItem
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getAllVideosAndFolders(): Flow<Map<String, List<VideoItem>>>

}