package com.padi.pilipili.hook

import android.app.Application
import com.google.gson.Gson
import com.padi.pilipili.HookInit
import com.padi.pilipili.findClass
import com.padi.pilipili.hook
import com.padi.pilipili.invokeStatic
import com.padi.pilipili.log
import top.sacz.xphelper.dexkit.DexFinder
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object CookieUtils : HookInit {

    private var cookie: String = ""
    private var decode: Method? = null

    override fun init(application: Application) {
        val loader = application.classLoader
        "com.bilibili.lib.accounts.AccountEncryptInfo".hook(loader, "getCookie", after = {
            runCatching {
                val cookieInfo = decode?.invoke(null, it.result)
                val gson = Gson()
                val jsonString = gson.toJson(cookieInfo)
                val cookieData = gson.fromJson(jsonString, CookieData::class.java)
                cookie = cookieData.cookies.joinToString("; ") { "${it.name}=${it.value}" }
            }.onFailure { exception ->
                exception.log()
            }
        })
    }

    fun getBiliCookie(): String {
        return cookie
    }

    override fun dexFind(application: Application) {
        decode = DexFinder.findMethod {
            searchPackages = arrayOf("com.bilibili.lib.accounts")
            parameters = arrayOf(String::class.java)
            modifiers = Modifier.STATIC or Modifier.PUBLIC
            returnType =
                "com.bilibili.lib.accounts.model.CookieInfo".findClass(application.classLoader)
        }.firstOrNull()
    }
}

data class CookieData(
    val cookies: List<Cookie>, val domains: List<String>
)

data class Cookie(
    val expires: Long, val httpOnly: Int, val name: String, val secure: Int, val value: String
)