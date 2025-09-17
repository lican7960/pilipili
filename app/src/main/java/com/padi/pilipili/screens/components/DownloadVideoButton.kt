package com.padi.pilipili.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.padi.pilipili.utils.SPHelper

@Composable
fun DownloadVideoButton() {
    val title = "视频缓存(分享页面)"
    val key = "enable_download_video"
    val spHelper = SPHelper.getInstance()
    
    var isEnabled by remember {
        mutableStateOf(spHelper.get(key, false))
    }
    ListItem(modifier = Modifier.clickable {
        isEnabled = !isEnabled
        spHelper.put(key, isEnabled)
    }, headlineContent = { Text(title) }, trailingContent = {
        Switch(
            checked = isEnabled, onCheckedChange = {
                isEnabled = it
                spHelper.put(key, it)
            })
    })
}