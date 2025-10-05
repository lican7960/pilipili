package com.padi.pilipili.hook

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import com.padi.pilipili.HookInit
import com.padi.pilipili.hook
import com.padi.pilipili.hookCtor
import com.padi.pilipili.invoke
import com.padi.pilipili.log
import com.padi.pilipili.utils.SPHelper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import top.sacz.xphelper.util.ActivityTools
import java.io.IOException

object AutoSkipVideoAd : HookInit{
    override fun init(application: Application) {
        var jumpVideo = ""
        var lastJumpTime = 0L
        val processedSegments = mutableSetOf<String>()
        "com.bilibili.playerbizcommonv2.widget.seek.v3.PlayerSeekWidget3".hookCtor(
            application.classLoader,
            Context::class.java,
            AttributeSet::class.java,
            Int::class.java,
            after = {
                runCatching {
                    val spHelper = SPHelper.getInstance()
                    val enabled = spHelper.get("enable_auto_skip_video_ad", false)
                    if (!enabled) return@hookCtor
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url("https://bsbsb.top/api/skipSegments?videoID=${ShowAvNumber.getBvNumber()}")
                        .get().build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            jumpVideo = response.body?.string() ?: ""
                            processedSegments.clear()
                            lastJumpTime = 0L
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }
                    })
                }.onFailure { e ->
                    e.log()
                }
            })


        "com.bilibili.playerbizcommonv2.widget.seek.v3.PlayerSeekWidget3".hook(
            application.classLoader, "getPlayerCoreService", after = {
                runCatching {
                    val spHelper = SPHelper.getInstance()
                    val enabled = spHelper.get("enable_auto_skip_video_ad", false)
                    if (!enabled) return@hook
                    val playerCoreService = it.result
                    val currentPosition = playerCoreService.invoke("getCurrentPosition") as Int
                    if (jumpVideo.isBlank()) return@hook
                    val jsonArray = JSONArray(jumpVideo)
                    val now = System.currentTimeMillis()
                    if (now - lastJumpTime < 1000) return@hook
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val category = item.optString("category")
                        if (item.optString("actionType") != "skip") continue
                        val segment = item.getJSONArray("segment")
                        val segmentStart = (segment.optDouble(0) * 1000).toInt()
                        val segmentEnd = (segment.optDouble(1) * 1000).toInt()
                        val uuid = item.optString("UUID")
                        if (processedSegments.contains(uuid)) continue
                        if (currentPosition >= segmentStart && currentPosition <= segmentEnd) {
                            playerCoreService?.invoke("seekTo", segmentEnd, true)
                            val context = ActivityTools.getTopActivity()
                            Toast.makeText(
                                context, "已跳过${category}类型广告", Toast.LENGTH_LONG
                            ).show()
                            processedSegments.add(uuid)
                            lastJumpTime = now
                            break
                        }
                    }
                }.onFailure { e -> e.log() }
            })

    }

    override fun dexFind(application: Application) {

    }

}