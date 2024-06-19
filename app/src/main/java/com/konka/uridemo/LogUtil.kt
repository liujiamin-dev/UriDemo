package com.konka.uridemo

import android.util.Log

object LogUtil {
    fun d(tag: String, msg: String) {

            Log.d(tag, msg)

    }

    fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    fun e(tag: String, e: Throwable) {
        Log.e(tag, e.message, e)
    }
}