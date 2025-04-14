package com.pro.quickplay.presentation.homescreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pro.quickplay.domain.entity.FolderItem
import com.pro.quickplay.presentation.homescreen.components.FolderCard
import com.pro.quickplay.presentation.homescreen.components.HomeTopBar

@Composable
fun HomeScreen(
    folderItems: List<FolderItem>,
    onClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            HomeTopBar(
                title = "Quick Play",
                shouldShowNavigationIcon = false,
                onBackClick = onBackClick,
                onSortIconClick = {}
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                folderItems.forEach { folder ->
                    item {
                        FolderCard(
                            folderName = folder.folderName,
                            onClick = { onClick(folder.folderName) },
                            videoCount = folder.videoCount
                        )
                    }
                }

            }
        }
    )


}






















