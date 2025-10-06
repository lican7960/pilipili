package com.padi.pilipili.hook

import android.app.Application
import com.google.gson.Gson
import com.padi.pilipili.HookInit
import com.padi.pilipili.findClass
import com.padi.pilipili.hook
import com.padi.pilipili.screens.components.PersonData
import com.padi.pilipili.utils.SPHelper
import top.sacz.xphelper.dexkit.DexFinder
import top.sacz.xphelper.ext.setFieldValue
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object ModifyPersonalData : HookInit {

    private var method: Method? = null

    override fun init(application: Application) {
        method?.hook(before = {
            val spHelper = SPHelper.getInstance()
            val enabled = spHelper.get("enable_modify_personal_data", false)
            if (!enabled) return@hook

            val json = spHelper.get("modify_personal_data", "")
            if (json.isEmpty()) return@hook

            val personData = runCatching {
                Gson().fromJson(json, PersonData::class.java)
            }.getOrNull() ?: return@hook

            val accountMine = it.args[0]
            personData.name?.let { accountMine.setFieldValue("name", it) }
            personData.dynamic?.let { accountMine.setFieldValue("dynamic", it) }
            personData.following?.let { accountMine.setFieldValue("following", it) }
            personData.follower?.let { accountMine.setFieldValue("follower", it) }
            personData.newFollowers?.let { accountMine.setFieldValue("newFollowers", it) }
            personData.bcoin?.let { accountMine.setFieldValue("bcoin", it) }
            personData.coin?.let { accountMine.setFieldValue("coin", it) }
            personData.rank?.let { accountMine.setFieldValue("rank", it) }
        })
    }

    override fun findDex(application: Application) {
        method = DexFinder.findMethod {
            modifiers = Modifier.PRIVATE
            declaredClass =
                "tv.danmaku.bili.ui.main2.mine.HomeUserCenterFragment".findClass(application.classLoader)
            returnType = Void.TYPE
            usedFields = arrayOf(
                DexFinder.findField {
                    fieldName = "garbEntrance"
                })
            parameters = arrayOf(
                "tv.danmaku.bili.ui.main2.api.AccountMine".findClass(application.classLoader),
                "tv.danmaku.bili.ui.main2.AccountMineRequestResource".findClass(application.classLoader),
                Boolean::class.java
            )
        }.firstOrNull()

    }
}