package com.konka.uridemo

import android.app.Application
import android.content.Context

object GlobalConfig {
    @JvmField
    val DEBUG = true

    lateinit var appContext: Context

    fun setAppContext(context: Application) {
        appContext = context
    }
}