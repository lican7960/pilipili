package com.padi.pilipili.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.padi.pilipili.utils.SPHelper
import top.yukonga.miuix.kmp.extra.SuperSwitch

@Composable
fun SettingSwitch(
    title: String,
    key: String,
    description: String? = null,
) {
    val spHelper = SPHelper.getInstance()

    var isEnabled by remember {
        mutableStateOf(spHelper.get(key, false))
    }

    SuperSwitch(
        title = title, summary = description, checked = isEnabled, onCheckedChange = {
            isEnabled = it
            spHelper.put(key, isEnabled)
        })
}