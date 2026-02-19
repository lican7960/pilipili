package com.padi.pilipili.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.padi.pilipili.R
import com.padi.pilipili.screens.components.CookieGet
import com.padi.pilipili.screens.components.ModifyPersonalData
import com.padi.pilipili.screens.components.SettingSwitch
import com.padi.pilipili.ui.theme.Theme
import com.padi.pilipili.utils.SPHelper
import top.sacz.xphelper.activity.BaseActivity
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.extra.SuperArrow
import top.yukonga.miuix.kmp.utils.overScrollVertical

class ModuleSettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SPHelper.init(this)
        enableEdgeToEdge()
        setContent {
            val scrollBehavior = MiuixScrollBehavior()
            val uriHandler = LocalUriHandler.current
            Theme {
                Scaffold(topBar = {
                    TopAppBar(
                        scrollBehavior = scrollBehavior, title = "PILIPILI"
                    )
                }) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .padding(it)
                            .overScrollVertical()
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    ) {
                        item {
                            SuperArrow(title = "帕帝天秀", summary = "@paditianxiu", startAction = {
                                Image(
                                    painter = painterResource(R.drawable.img),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clip(
                                            CircleShape
                                        ),
                                )
                            }, onClick = {
                                uriHandler.openUri("https://t.me/paditinaxiu")
                            })
                            Spacer(modifier = Modifier.width(8.dp))

                            SuperArrow(title = "Telegram频道", onClick = {
                                uriHandler.openUri("https://t.me/niubimokuai")
                            })
                            Spacer(modifier = Modifier.width(8.dp))

                            HorizontalDivider()
                            CookieGet()
                            Spacer(modifier = Modifier.width(8.dp))
                            ModifyPersonalData()
                            SettingSwitch(
                                title = "跳过广告",
                                key = "enable_auto_skip_video_ad",
                                description = "自动跳过植入式视频广告"
                            )
                            SettingSwitch(
                                title = "去除开屏广告",
                                key = "enable_screen_ad_close",
                                description = "开屏广告，退退退🤺🤺🤺"
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
