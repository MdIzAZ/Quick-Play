package com.pro.quickplay.util

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.ui.graphics.vector.ImageVector

fun prepareThumbnail(id: Long): Uri{
    val thumbnailUri = ContentUris.withAppendedId(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id
    )
    return thumbnailUri
}


enum class Swipe {
    VOLUME, BRIGHTNESS, NONE
}

enum class ResizeMode {
    FIT, CROP, STRETCH, ZOOM
}

enum class PlaybackSpeed {
    X25, X50, X75, X100, X125, X150, X175, X200
}


data class DropDownItem(
    val text: Any,
    val imageVector: ImageVector? = null,
)

val aspectRatioList = listOf(
    DropDownItem(ResizeMode.FIT, Icons.Default.FitScreen),
    DropDownItem(ResizeMode.CROP, Icons.Default.Crop),
    DropDownItem(ResizeMode.ZOOM, Icons.Default.ZoomOutMap),
    DropDownItem(ResizeMode.STRETCH, Icons.Default.AspectRatio),
)

val speedList = listOf(
    DropDownItem(PlaybackSpeed.X25, null),
    DropDownItem(PlaybackSpeed.X50, null),
    DropDownItem(PlaybackSpeed.X75, null),
    DropDownItem(PlaybackSpeed.X100, null),
    DropDownItem(PlaybackSpeed.X125, null),
    DropDownItem(PlaybackSpeed.X150, null),
    DropDownItem(PlaybackSpeed.X175, null),
    DropDownItem(PlaybackSpeed.X200, null),
)