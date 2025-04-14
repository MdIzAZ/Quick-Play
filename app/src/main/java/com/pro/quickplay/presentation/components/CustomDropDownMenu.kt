package com.pro.quickplay.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pro.quickplay.util.DropDownItem


@Composable
fun CustomDropDownMenu(
    modifier: Modifier,
    isExpanded: Boolean,
    onDismissReq: () -> Unit,
    itemList: List<DropDownItem>,
    onMenuItemClick: (Any) -> Unit,
) {

    Box(
        modifier = modifier
    ) {
        DropdownMenu(
            modifier = Modifier,
            expanded = isExpanded,
            onDismissRequest = onDismissReq,
            content = {
                itemList.forEach {
                    DropdownMenuItem(
                        onClick = { onMenuItemClick(it.text) },
                        text = { Text(it.text.toString())  },
                        leadingIcon = { it.imageVector?.let { it1 -> Icon(it1, null) } }
                    )
                }
            }
        )
    }
}






