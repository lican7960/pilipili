package com.padi.pilipili.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SPHelper private constructor() {
    companion object {
        private const val SP_NAME = "PiliPili"
        private lateinit var sp: SharedPreferences

        @Volatile
        private var instance: SPHelper? = null

        fun init(context: Context) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        }

        fun getInstance(): SPHelper {
            return instance ?: synchronized(this) {
                instance ?: SPHelper().also { instance = it }
            }
        }
    }

    // 存储数据
    fun put(key: String, value: Any) {
        sp.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    // 获取数据
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> sp.getString(key, defaultValue) as T
            is Int -> sp.getInt(key, defaultValue) as T
            is Boolean -> sp.getBoolean(key, defaultValue) as T
            is Float -> sp.getFloat(key, defaultValue) as T
            is Long -> sp.getLong(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    // 删除数据
    fun remove(key: String) {
        sp.edit { remove(key) }
    }

    // 清空所有数据
    fun clear() {
        sp.edit { clear() }
    }

    // 检查是否包含某个key
    fun contains(key: String): Boolean {
        return sp.contains(key)
    }
}