package com.padi.pilipili.hook

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Application
import android.view.View
import android.view.animation.OvershootInterpolator
import com.padi.pilipili.HookInit
import com.padi.pilipili.getObjectField
import com.padi.pilipili.hook
import com.padi.pilipili.log
import com.padi.pilipili.utils.SPHelper
import top.sacz.xphelper.dexkit.DexFinder
import java.lang.reflect.Method

object HomeAnimation : HookInit {
    private var method: Method? = null
    override fun init(application: Application) {
        method?.hook(
            after = { param ->
                val spHelper = SPHelper.getInstance()
                val enabled = spHelper.get("enable_home_animation", false)
                if (!enabled) return@hook
                runCatching {
                    val thisObj = param.thisObject
                    val itemView = thisObj.getObjectField("itemView") as View

                    val context = itemView.context
                    val screenWidth = context.resources.displayMetrics.widthPixels

                    val scaleX = ObjectAnimator.ofFloat(itemView, "scaleX", 0.5f, 1f)
                    val scaleY = ObjectAnimator.ofFloat(itemView, "scaleY", 0.5f, 1f)

                    val fadeIn = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 1f)

                    val translateX = ObjectAnimator.ofFloat(
                        itemView, "translationX", screenWidth.toFloat(), 0f
                    )

                    AnimatorSet().apply {
                        playTogether(scaleX, scaleY, fadeIn, translateX)
                        duration = 800
                        interpolator = OvershootInterpolator(1.2f)
                        start()
                    }
                }.onFailure {
                    it.log()
                }
            })
    }

    override fun findDex(application: Application) {
        val param = DexFinder.findClass {
            interfaces = arrayOf(
                "com.bilibili.pegasus.data.base.BasePegasusData",
                "com.bilibili.pegasus.data.component.IDescButtonData"
            )
            usedString = arrayOf("rcmdReason and descText must not be null at same time", "picture")
        }.firstOrNull()

        method = DexFinder.findMethod {
            parameters = arrayOf(
                param
            )
            searchPackages = arrayOf("com.bilibili.pegasus.holders")
            usingNumbers = longArrayOf(0, 2, 8, 1)
            invokeMethods = arrayOf(
                param.getMethod("getTitle"),
                param.getMethod("getSubtitle"),
                param.getMethod("getTalkBack")
            )

        }.firstOrNull()

    }
}