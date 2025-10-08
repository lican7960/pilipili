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
fun SettingSwitch(
    modifier: Modifier = Modifier,
    title: String,
    key: String,
    description: String? = null,
) {
    val spHelper = SPHelper.getInstance()

    var isEnabled by remember {
        mutableStateOf(spHelper.get(key, false))
    }

    ListItem(modifier = modifier.clickable {
        isEnabled = !isEnabled
        spHelper.put(key, isEnabled)
    }, headlineContent = {
        Text(title)
    }, supportingContent = description?.let {
        { Text(it) }
    }, trailingContent = {
        Switch(
            checked = isEnabled, onCheckedChange = { newState ->
                isEnabled = newState
                spHelper.put(key, newState)
            })
    })
}