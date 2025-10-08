package com.padi.pilipili.hook

import android.app.Application
import android.view.Menu
import androidx.core.view.forEach
import com.padi.pilipili.HookInit
import com.padi.pilipili.hook
import com.padi.pilipili.utils.SPHelper
import top.sacz.xphelper.dexkit.DexFinder
import java.lang.reflect.Method

object HideGameMenu : HookInit {
    private var method: Method? = null

    override fun init(application: Application) {
        method?.hook(
            after = { param ->
                val spHelper = SPHelper.getInstance()
                val enabled = spHelper.get("enable_hide_game_menu", false)
                if (!enabled) return@hook
                val menu = param.args[0] as Menu
                menu.forEach { item ->
                    val itemId = item.itemId
                    val title = item.title?.toString() ?: "无标题"
                    if (title == "游戏中心") {
                        item.isVisible = false
                    }
                }
            })
    }

    override fun findDex(application: Application) {
        method = DexFinder.findMethod {
            searchPackages = arrayOf("com.bilibili.lib.homepage.startdust.menu")
            usedString = arrayOf(
                "the result of forBadgeServer() must be NoNull.",
                "the id of badge server must be NoNull."
            )
            usingNumbers = longArrayOf(0, 2)
        }.firstOrNull()
    }
}