package com.padi.pilipili

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.padi.pilipili.hook.AutoSkipVideoAd
import com.padi.pilipili.hook.CookieUtils
import com.padi.pilipili.hook.DownloadVideoButton
import com.padi.pilipili.hook.FreeCopy
import com.padi.pilipili.hook.HideGameMenu
import com.padi.pilipili.hook.HomeAnimation
import com.padi.pilipili.hook.ModifyPersonalData
import com.padi.pilipili.hook.SettingButton
import com.padi.pilipili.hook.ShowAvNumber
import com.padi.pilipili.utils.SPHelper
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage
import top.sacz.xphelper.XpHelper


class Hook : IXposedHookLoadPackage, IXposedHookZygoteInit {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun handleLoadPackage(p0: XC_LoadPackage.LoadPackageParam) {
        if (p0.packageName != "tv.danmaku.bili") return
        if (!p0.isFirstApplication) return
        Application::class.java.hook("attach", Context::class.java, after = {
            val application = it.thisObject as Application
            val loader = application.classLoader
            XpHelper.initContext(application)
            XpHelper.injectResourcesToContext(application)
            SPHelper.init(application)
            listOf(
                SettingButton,
                CookieUtils,
                ModifyPersonalData,
                ShowAvNumber,
                DownloadVideoButton,
                AutoSkipVideoAd,
                FreeCopy,
                HomeAnimation,
                HideGameMenu
            ).forEach { funName ->
                funName.apply {
                    findDex(application)
                    init(application)
                }
            }

        })
    }

    override fun initZygote(p0: IXposedHookZygoteInit.StartupParam) {
        XpHelper.initZygote(p0)
    }

}