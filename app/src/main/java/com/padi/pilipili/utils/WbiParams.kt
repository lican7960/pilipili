package com.padi.pilipili.utils
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

private fun JsonElement?.get(): String {
    check(this != null) { "No contents found" }
    return this.jsonPrimitive.content.split('/').last().removeSuffix(".png")
}

private val mixinKeyEncTab = intArrayOf(
    46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
    33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
    61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
    36, 20, 34, 44, 52
)

@Serializable
data class WbiParams(
    val imgKey: String,
    val subKey: String,
) {
    private val mixinKey: String
        get() = (imgKey + subKey).let { s ->
            buildString {
                repeat(32) {
                    append(s[mixinKeyEncTab[it]])
                }
            }
        }

    // 创建对象(GET获取或者读缓存, 比如Redis)之后, 直接调用此函数处理
    fun enc(params: Map<String, Any?>): String {
        val sorted = params.filterValues { it != null }.toSortedMap()
        return buildString {
            append(sorted.toQueryString())
            val wts = System.currentTimeMillis() / 1000
            sorted["wts"] = wts
            append("&wts=")
            append(wts)
            append("&w_rid=")
            append((sorted.toQueryString() + mixinKey).toMD5())
        }
    }
}

fun getWbiKeys(): Pair<String, String> {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://api.bilibili.com/x/web-interface/nav")
        .headers(
            Headers.headersOf(
                "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Referer", "https://www.bilibili.com/"
            )
        )
        .get()
        .build()

    val response = client.newCall(request).execute()
    val responseBody = response.body?.string() ?: throw Exception("Empty response body")

    val jsonResponse = JSONObject(responseBody)
    val data = jsonResponse.getJSONObject("data")
    val wbiImg = data.getJSONObject("wbi_img")

    val imgUrl = wbiImg.getString("img_url")
    val subUrl = wbiImg.getString("sub_url")

    val imgKey = imgUrl.split("/").last().split(".").first()
    val subKey = subUrl.split("/").last().split(".").first()

    return Pair(imgKey, subKey)
}

