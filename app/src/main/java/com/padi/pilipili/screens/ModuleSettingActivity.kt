package com.padi.pilipili.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.padi.pilipili.screens.components.AutoSkipVideoAdSwitch
import com.padi.pilipili.screens.components.CookieGetButton
import com.padi.pilipili.screens.components.DownloadVideoButton
import com.padi.pilipili.screens.components.ModifyPersonalDataButton
import com.padi.pilipili.screens.components.ShowAvNumberSwitch
import com.padi.pilipili.ui.theme.PILIPILITheme
import com.padi.pilipili.utils.SPHelper
import top.sacz.xphelper.activity.BaseActivity

class ModuleSettingActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SPHelper.init(this)
        enableEdgeToEdge()
        setContent {
            PILIPILITheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text("PILIPILI") })
                }) {
                    Surface(modifier = Modifier.padding(it)) {
                        FlowRow(modifier = Modifier.padding(8.dp)) {
                            CookieGetButton()
                            Spacer(modifier = Modifier.width(8.dp))
                            ModifyPersonalDataButton()
                            ShowAvNumberSwitch()
                            DownloadVideoButton()
                            AutoSkipVideoAdSwitch()
                        }
                    }
                }
            }
        }
    }
}
