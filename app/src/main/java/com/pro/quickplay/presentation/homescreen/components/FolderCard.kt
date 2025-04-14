package com.pro.quickplay.presentation.homescreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pro.quickplay.R

@Composable
fun FolderCard(
    folderName: String,
    onClick: (String) -> Unit,
    videoCount: Int
) {
    ElevatedCard(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onClick(folderName)
            }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.folder_icon),
                modifier = Modifier.size(80.dp),
                contentDescription = null
            )

            Column (
                modifier = Modifier.height(80.dp)
            ){
                Text(text = folderName, modifier = Modifier.padding(10.dp))
                Text(text = "$videoCount  videos", modifier = Modifier.padding(horizontal = 10.dp))
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun FolderCardPreview() {
    FolderCard(
        folderName = "Folder Name",
        onClick = {},
        videoCount = 5
    )
}