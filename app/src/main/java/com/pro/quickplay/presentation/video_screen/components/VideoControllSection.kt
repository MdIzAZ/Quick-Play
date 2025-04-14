package com.pro.quickplay.presentation.video_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VideoControlSection(
    modifier: Modifier,
    isPlaying: Boolean,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    togglePlayMode: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        /*IconButton(
            modifier = Modifier
                .size(50.dp),
            onClick = { onSkipPrevious() },
            content = {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(modifier = Modifier.size(20.dp))

        IconButton(
            modifier = Modifier
                .size(60.dp),
            onClick = { togglePlayMode() },
            content = {
                val imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(modifier = Modifier.size(20.dp))

        IconButton(
            modifier = Modifier
                .size(50.dp),
            onClick = { onSkipNext() },
            content = {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )*/

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(ControlOptions.entries) {
                CustomIcon(
                    imageVector = when(it) {
                        ControlOptions.PRE -> Icons.Default.SkipPrevious
                        ControlOptions.PP -> if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow
                        ControlOptions.NEXT -> Icons.Default.SkipNext
                    },
                    size = 48,
                    paddingInDp = 5,
                    onClick = {
                        when(it) {
                            ControlOptions.PRE -> onSkipPrevious()
                            ControlOptions.PP -> togglePlayMode()
                            ControlOptions.NEXT -> onSkipNext()
                        }
                    },
                    tint = Color.Black,
                    boxTint = Color.Gray.copy(alpha = .5f)
                )
            }
        }


    }
}




enum class ControlOptions{
    PRE, PP, NEXT
}