package com.padi.pilipili.hook

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.padi.pilipili.HookInit
import com.padi.pilipili.findClass
import com.padi.pilipili.getObjectField
import com.padi.pilipili.hook
import com.padi.pilipili.log
import com.padi.pilipili.screens.ModuleSettingActivity
import top.sacz.xphelper.dexkit.DexFinder
import top.sacz.xphelper.ext.setFieldValue
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object SettingButton : HookInit {
    private var addButton: Method? = null
    private var onClick: Method? = null
    override fun init(application: Application) {
        val loader = application.classLoader
        addButton?.hook(
            before = {
                val list = it.args[0] as MutableList<Any?>
                val originalItem = list[0]
                val itemClass = originalItem?.javaClass
                val newItem = itemClass?.newInstance()
                itemClass?.declaredFields?.forEach { field ->
                    field.isAccessible = true
                    val value = field.get(originalItem)
                    field.set(newItem, value)
                }
                newItem?.setFieldValue("title", "PiliPili")
                newItem?.setFieldValue(
                    "icon",
                    "https://i0.hdslb.com/bfs/openplatform/85583f8a269ba6166acf44340b3a4466707c80bd.png"
                )
                list.forEach { item ->
                    val title = item?.getObjectField("title") as String
                    if (title == "稍后再看") {
                        list.add(newItem)
                    }
                    return@forEach
                }
                it.args[0] = list


            })


        onClick?.hook(before = {
            val view = it.args[0] as View
            val thisObject = it.thisObject
            thisObject.javaClass.declaredFields.forEach { field ->
                field.isAccessible = true
                val value = field.get(thisObject)
                if (value is TextView) {
                    val tv = value
                    val context = tv.context
                    val title = tv.text
                    if (title == "PiliPili") {
                        it.result = null
                        val intent = Intent(context, ModuleSettingActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        })

    }


    override fun findDex(application: Application) {
        val loader = application.classLoader
        addButton = DexFinder.findMethod {
            modifiers = Modifier.PUBLIC
            parameters = arrayOf(
                List::class.java,
                "com.bilibili.lib.homepage.mine.MenuGroup".findClass(application.classLoader)
            )
            invokeMethods = arrayOf(
                List::class.java.getMethod("clear")
            )
            usedFields = arrayOf(
                DexFinder.findField {
                    fieldType =
                        "com.bilibili.lib.homepage.mine.MenuGroup".findClass(application.classLoader)
                })
        }.firstOrNull()


        onClick = DexFinder.findMethod {
            methodName = "onClick"
            modifiers = Modifier.PUBLIC
            parameters = arrayOf(View::class.java)
            invokeMethods = arrayOf(
                "com.bilibili.base.util.ContextUtilKt".findClass(loader)
                    .getMethod("findFragmentActivityOrNull"), DexFinder.findMethod {
                    modifiers = Modifier.STATIC or Modifier.PUBLIC
                    searchPackages = arrayOf("tv.danmaku.bili.ui.main2.mine")
                    returnType = Boolean::class.java
                    parameters = arrayOf(
                        "com.bilibili.lib.homepage.mine.MenuGroup.Item".findClass(application.classLoader)
                    )
                }.firstOrNull()
            )
        }.firstOrNull()


    }
}