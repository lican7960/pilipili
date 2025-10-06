package com.padi.pilipili.hook

import android.app.Application
import com.padi.pilipili.HookInit
import com.padi.pilipili.findClass
import com.padi.pilipili.hook
import com.padi.pilipili.log
import com.padi.pilipili.utils.SPHelper
import top.sacz.xphelper.dexkit.DexFinder
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object ShowAvNumber : HookInit {
    private var bvNumber: String = ""
    private var method: Method? = null
    override fun init(application: Application) {
        method?.hook(before = {
            bvNumber = it.args[1] as String
            val spHelper = SPHelper.getInstance()
            val enabled = spHelper.get("enable_show_av_number", false)
            if (!enabled) return@hook
            it.result = it.args[0]
        })

    }

    fun getBvNumber(): String {
        return bvNumber
    }

    override fun findDex(application: Application) {
        method = DexFinder.findMethod {
            returnType = String::class.java
            modifiers = Modifier.STATIC or Modifier.PUBLIC
            declaredClass = "com.bilibili.droid.BVCompat".findClass(application.classLoader)
            parameters = arrayOf(String::class.java, String::class.java)
        }.firstOrNull()
    }
}