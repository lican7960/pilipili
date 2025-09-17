package com.padi.pilipili.hook

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.padi.pilipili.HookInit
import com.padi.pilipili.R
import com.padi.pilipili.adapter.DownloadViewAdapter
import com.padi.pilipili.adapter.VideoItem
import com.padi.pilipili.findClass
import com.padi.pilipili.hook
import com.padi.pilipili.invoke
import com.padi.pilipili.log
import com.padi.pilipili.new
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import top.sacz.xphelper.dexkit.DexFinder
import top.sacz.xphelper.util.ActivityTools
import java.io.IOException
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object DownloadVideoButton : HookInit {
    private var addButton: Method? = null
    private var onClick: Method? = null
    override fun init(application: Application) {
        val loader = application.classLoader
        addButton?.hook(before = {
            runCatching {
                val list = it.args[0] as MutableList<Any?>
                if (list.size >= 7) return@hook
                val button = list[0]?.javaClass?.new(ActivityTools.getTopActivity())
                button?.invoke("setVisible", true)
                button?.invoke("setTitle", "视频缓存")
                button?.invoke("setIcon", R.drawable.bot)
                list.add(button)
                it.args[0] = list
            }.onFailure {
                it.log()
            }
        })

        onClick?.hook(
            before = {
                runCatching {
                    val view = it.args[0] as View
                    val context = ActivityTools.getTopActivity()
                    val tag = view.tag
                    val title = tag.invoke("getTitle") as String
                    if (title == "视频缓存") {
                        val client = OkHttpClient()
                        val request = Request.Builder()
                            .url("https://api.bilibili.com/x/player/pagelist?bvid=${ShowAvNumber.getBvNumber()}")
                            .headers(
                                Headers.headersOf(
                                    "User-Agent",
                                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                                    "Referer",
                                    "https://www.bilibili.com/"
                                )
                            ).get().build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onResponse(call: Call, response: Response) {
                                val responseBody = response.body?.string() ?: ""
                                val json = JSONObject(responseBody)
                                val data = json.getJSONArray("data")
                                val items = mutableListOf<VideoItem>()
                                for (i in 0 until data.length()) {
                                    val item = data.getJSONObject(i)
                                    val part = item.optString("part")
                                    val cid = item.optLong("cid")
                                    val duration = item.optInt("duration")
                                    val first_frame = item.optString("first_frame", "")
                                    items.add(VideoItem(cid, part, duration, first_frame))
                                }
                                context.runOnUiThread {
                                    val recycler = RecyclerView(context).apply {
                                        layoutManager = LinearLayoutManager(context)
                                        setHasFixedSize(true)
                                        addItemDecoration(
                                            DividerItemDecoration(
                                                context, DividerItemDecoration.VERTICAL
                                            )
                                        )
                                        setPadding(8, 8, 8, 8)
                                        clipToPadding = false
                                    }

                                    val adapter = DownloadViewAdapter(
                                        items, onItemClick = {

                                        })
                                    recycler.adapter = adapter

                                    val dialog = AlertDialog.Builder(context).setTitle("下载视频")
                                        .setView(recycler).setNegativeButton("取消", null).create()
                                    dialog.show()
                                }
                            }

                            override fun onFailure(call: Call, e: IOException) {
                                context.runOnUiThread {
                                    e.printStackTrace()
                                }
                            }
                        })

                    }

                }.onFailure {
                    it.log()
                }
            })
    }

    override fun dexFind(application: Application) {
        addButton = DexFinder.findMethod {
            searchPackages = arrayOf("com.bilibili.app.comm.supermenu.core")
            parameters = arrayOf(List::class.java)
            returnType = Void.TYPE
            callMethods = arrayOf(
                DexFinder.findMethod {
                    searchPackages = arrayOf("com.bilibili.app.comm.supermenu.core")
                    parameters = arrayOf(
                        "com.bilibili.app.comm.supermenu.core.IMenuItem".findClass(application.classLoader)
                    )
                    usingNumbers = longArrayOf(0, 8)
                    returnType = Void.TYPE
                    modifiers = Modifier.PRIVATE
                }.firstOrNull()
            )
        }.firstOrNull()


        onClick = DexFinder.findMethod {
            searchPackages = arrayOf("com.bilibili.app.comm.supermenu.core")
            methodName = "onClick"
            modifiers = Modifier.PUBLIC
            parameters = arrayOf(View::class.java)
            returnType = Void.TYPE
            invokeMethods = arrayOf(
                View::class.java.getMethod("getTag")
            )
            usedFields = arrayOf(
                DexFinder.findField {
                    searchPackages = arrayOf("com.bilibili.app.comm.supermenu.core.listeners")
                    modifiers = Modifier.PRIVATE
                })

        }.firstOrNull()


    }
}