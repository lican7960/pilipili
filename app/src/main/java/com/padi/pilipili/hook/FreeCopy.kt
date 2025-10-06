package com.padi.pilipili.hook

import android.app.AlertDialog
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.TextView
import com.padi.pilipili.HookInit
import com.padi.pilipili.hook
import com.padi.pilipili.utils.SPHelper
import de.robv.android.xposed.XposedBridge
import top.sacz.xphelper.util.ActivityTools

object FreeCopy : HookInit {
    override fun init(application: Application) {
        ClipboardManager::class.java.declaredMethods.filter {
            it.name == "setPrimaryClip"
        }.forEach { method ->
            method.hook(
                before = { param ->
                    val spHelper = SPHelper.getInstance()
                    val enabled = spHelper.get("enable_free_copy", false)
                    if (!enabled) return@hook
                    val isMyCopy = Throwable().stackTrace.find { stackTrace ->
                        stackTrace.className == "android.widget.TextView" && stackTrace.methodName == "setPrimaryClip"
                    } != null
                    if (isMyCopy) return@hook
                    val cm = param.thisObject
                    val clip = param.args[0] as ClipData
                    val activity = ActivityTools.getTopActivity()
                    AlertDialog.Builder(activity).setTitle("自由复制")
                        .setMessage(clip.getItemAt(0).text).setPositiveButton("复制原始") { _, _ ->
                            XposedBridge.invokeOriginalMethod(param.method, cm, param.args);
                        }.show().apply {
                            findViewById<TextView>(android.R.id.message).setTextIsSelectable(
                                true
                            )
                        }
                    param.result = null
                })

        }

    }

    override fun findDex(application: Application) {

    }
}