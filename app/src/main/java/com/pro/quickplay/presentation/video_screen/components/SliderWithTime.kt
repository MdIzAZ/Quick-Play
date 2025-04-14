package com.pro.quickplay.presentation.video_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderWithTime(
    currentDuration: String,
    totalDuration: String,
    value: Float,
    onValueChange: (Float) -> Unit,
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 50.dp, vertical = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .wrapContentSize()
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
            .padding(8.dp)
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentDuration,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.background
            )

            Row {
                Text(
                    text = totalDuration,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background
                )



            }
        }


        Slider(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            value = value,
            thumb = {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .size(20.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            },
            track = { sliderPositions ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
                Box(
                    Modifier
                        .fillMaxWidth(sliderPositions.value)
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.inversePrimary)
                )
            },
            onValueChange = { onValueChange(it) }
        )


    }


}