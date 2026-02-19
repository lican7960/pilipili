package com.padi.pilipili.hook

import android.app.Application
import com.padi.pilipili.HookInit
import com.padi.pilipili.findClass
import com.padi.pilipili.hook
import com.padi.pilipili.invoke
import com.padi.pilipili.log
import com.padi.pilipili.utils.SPHelper
import top.sacz.xphelper.dexkit.DexFinder
import java.lang.reflect.Method

object ScreenAdClose : HookInit {
    private var checkHotSplash: Method? = null
    private var checkHotSplash2: Method? = null
    private var hideSplashFragment: Method? = null
    override fun init(application: Application) {
        hideSplashFragment?.hook(before = {
            val spHelper = SPHelper.getInstance()
            val enabled = spHelper.get("enable_screen_ad_close", false)
            if (!enabled) return@hook
            val mMainActivityV2 = it.args[0]
            mMainActivityV2.invoke("hideSplashFragment", false)
        })
        checkHotSplash?.hook(after = {
            val spHelper = SPHelper.getInstance()
            val enabled = spHelper.get("enable_screen_ad_close", false)
            if (!enabled) return@hook
            it.result = null
        })
        checkHotSplash2?.hook(after = {
            val spHelper = SPHelper.getInstance()
            val enabled = spHelper.get("enable_screen_ad_close", false)
            if (!enabled) return@hook
            it.result = null
        })
    }

    override fun findDex(application: Application) {
        hideSplashFragment = DexFinder.findMethod {
            declaredClass =
                "tv.danmaku.bili.MainActivitySplashComponentExtKt".findClass(application.classLoader)
            paramCount = 2
            usedString = arrayOf(
                "hideSplashWithDelay", "jumped", "MainActivitySplashComponent"
            )
        }.firstOrNull()

        checkHotSplash = DexFinder.findMethod {
            searchPackages = arrayOf("tv.danmaku.bili.splash.ad.core")
            paramCount = 1
            usedString = arrayOf(
                "SplashHotStartComponent", "checkHotSplash", "bilibili://main/hot-splash2"
            )
        }.firstOrNull()

        checkHotSplash2 = DexFinder.findMethod {
            searchPackages = arrayOf("tv.danmaku.bili.ui.splash.ad")
            paramCount = 1
            usedString = arrayOf(
                "[Splash]SplashHelper", "checkHotSplash", "bilibili://main/hot-splash"
            )
        }.firstOrNull()

        checkHotSplash2.log()

    }
}