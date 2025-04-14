package com.pro.quickplay.presentation.video_list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PhotoSizeSelectLarge
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pro.quickplay.domain.entity.VideoItem
import com.pro.quickplay.presentation.components.CustomDropDownMenu
import com.pro.quickplay.presentation.homescreen.components.HomeTopBar
import com.pro.quickplay.presentation.video_list_screen.components.VideoCard
import com.pro.quickplay.util.DropDownItem

@Composable
fun VideoListScreen(
    folderName: String,
    videos: List<VideoItem>,
    onBackClick: () -> Unit,
    onVideoClick: (Int) -> Unit,
    onSortVideos: (String, SortBy, SortType) -> Unit,
) {

    var showSortByDropDown by remember { mutableStateOf(false) }
    var showSortTypeDropDown by remember { mutableStateOf(false) }

    var sortQuery by remember { mutableStateOf(Pair(SortBy.DATE, SortType.DESC)) }

    Scaffold(
        topBar = {
            HomeTopBar(
                title = folderName,
                shouldShowNavigationIcon = true,
                onBackClick = onBackClick,
                onSortIconClick = { showSortByDropDown = true }
            )
        },
        content = {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                CustomDropDownMenu(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 20.dp),
                    isExpanded = showSortByDropDown,
                    onMenuItemClick = { by ->
                        sortQuery = Pair(by as SortBy, SortType.DESC)
                        showSortTypeDropDown = true
                    },
                    onDismissReq = { showSortByDropDown = false },
                    itemList = sortByList
                )

                CustomDropDownMenu(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 20.dp, end = 110.dp),
                    isExpanded = showSortTypeDropDown,
                    onMenuItemClick = { type ->
                        val sortBy = sortQuery.first
                        val sortType = type as SortType
                        sortQuery = Pair(sortBy, sortType)
                        onSortVideos(folderName, sortQuery.first, sortQuery.second)
                        showSortTypeDropDown = false
                        showSortByDropDown = false
                    },
                    onDismissReq = { showSortTypeDropDown = false },
                    itemList = sortTypeList
                )



                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    itemsIndexed(videos) { idx, videoItem ->
                        VideoCard(videoItem = videoItem, onVideoClick = { onVideoClick(idx) })
                    }
                }
            }

        }
    )

}

val sortByList = listOf(
    DropDownItem(SortBy.DATE, Icons.Default.DateRange),
    DropDownItem(SortBy.NAME, Icons.Default.SortByAlpha),
    DropDownItem(SortBy.SIZE, Icons.Default.PhotoSizeSelectLarge),
    DropDownItem(SortBy.DURATION, Icons.Default.Timelapse),
)

val sortTypeList = listOf(
    DropDownItem(SortType.ASC, Icons.Default.ArrowDropUp),
    DropDownItem(SortType.DESC, Icons.Default.ArrowDropDown)
)

enum class SortBy {
    DATE, NAME, SIZE, DURATION
}

enum class SortType {
    ASC, DESC
}