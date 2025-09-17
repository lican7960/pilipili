package com.padi.pilipili

import android.util.Log

private val TAG = "Demo"

fun Throwable.log(text: String = "Throwable"): Throwable {
    Log.e(TAG, text, this)
    return this
}

fun String.d(text: String = "Debug"): String {
    Log.d(TAG, "$text : $this")
    return this
}

fun String.e(text: String = "Error"): String {
    Log.e(TAG, "$text : $this")
    return this
}

fun Any?.log(text: String = "Debug"): Any? {
    if (this == null)
        "null".d(text)
    else
        this.toString().d(text)
    return this
}

fun Any.printCurrentStackTrace() {
    this.log("printCurrentStackTrace")
    Thread.currentThread().stackTrace.forEachIndexed { index, stackTraceElement ->
        if (index > 2)
            stackTraceElement.log(">")
    }
}
