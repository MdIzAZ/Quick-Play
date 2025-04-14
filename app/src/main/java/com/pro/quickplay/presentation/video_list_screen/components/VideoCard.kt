package com.pro.quickplay.presentation.video_list_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pro.quickplay.domain.entity.VideoItem
import com.pro.quickplay.util.prepareThumbnail
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun VideoCard(
    videoItem: VideoItem,
    onVideoClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .padding(10.dp)
            .clickable { onVideoClick() },
        colors = CardDefaults.elevatedCardColors()
            .copy(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .size(height = 120.dp, width = 180.dp)
            ) {
                GlideImage(
                    imageModel = prepareThumbnail(videoItem.id),
                    modifier = Modifier
                )

                Text(
                    text = videoItem.formattedDuration,
                    color = Color.White,
                    modifier = Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.Black)
                        .align(Alignment.BottomEnd)
                        .padding(2.dp)
                )
            }
            Column(
                modifier = Modifier.height(120.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = videoItem.displayName,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp),
                    maxLines = 1
                )

                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.Yellow)
                            .padding(2.dp),
                        text = videoItem.dateAdded
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.Cyan)
                            .padding(2.dp),
                        text = videoItem.formattedSize,
                    )
                }


            }
        }
    }
}

