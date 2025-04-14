package com.pro.quickplay.presentation.video_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun VideoScreenTop(
    modifier: Modifier,
    onClick: (MenuTypes) -> Unit,
) {

    Row(
        modifier = modifier
    ) {

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(MenuTypes.entries) {
                CustomIcon(
                    imageVector = when(it) {
                        MenuTypes.ASPECT -> Icons.Default.AspectRatio
                        MenuTypes.SPEED -> Icons.Outlined.Speed
                    },
                    size = 48,
                    paddingInDp = 5,
                    onClick = { onClick(it) },
                )
            }
        }


    }

}


@Composable
fun CustomIcon(
    imageVector: ImageVector,
    size: Int,
    paddingInDp: Int,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.onTertiaryContainer,
    boxTint : Color = MaterialTheme.colorScheme.tertiaryContainer
) {


    Box(
        modifier = Modifier.clip(CircleShape).background(boxTint)
    ){
        IconButton(
            modifier = Modifier
                .padding(4.dp)
                .size(size.dp),
            onClick = onClick,
            content = {
                Icon(
                    modifier = Modifier
                        .padding(paddingInDp.dp)
                        .fillMaxSize(),
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = tint
                )
            }
        )
    }



}


enum class MenuTypes {
    ASPECT, SPEED,
//    SETTINGS, QUALITY
}