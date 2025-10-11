package com.padi.pilipili.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.padi.pilipili.screens.components.CookieGetButton
import com.padi.pilipili.screens.components.ModifyPersonalDataButton
import com.padi.pilipili.screens.components.SettingSwitch
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
                            SettingSwitch(
                                title = "自动跳过植入式视频广告",
                                key = "enable_auto_skip_video_ad",
                                description = "启用更灵活的复制操作"
                            )

                            SettingSwitch(
                                title = "视频缓存(分享页面)未完善",
                                key = "enable_download_video",
                                description = "视频缓存功能正在开发中"
                            )
                            SettingSwitch(
                                title = "转复制操作为自由复制",
                                key = "enable_free_copy",
                                description = "启用更灵活的复制操作"
                            )

                            SettingSwitch(
                                title = "隐藏主页右上角游戏中心按钮",
                                key = "enable_hide_game_menu",
                                description = "隐藏后可以腾出更多空间"
                            )

                            SettingSwitch(
                                title = "首页列表动画",
                                key = "enable_home_animation",
                                description = "启用首页列表的动画效果"
                            )

                            SettingSwitch(
                                title = "视频显示AV号",
                                key = "enable_show_av_number",
                                description = "在视频页面显示AV/BV号"
                            )
                        }
                    }
                }
            }
        }
    }
}
