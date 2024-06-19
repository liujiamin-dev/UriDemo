package com.konka.uridemo

import io.github.album.interfaces.AlbumLogger

object Logger : AlbumLogger {
    override fun d(tag: String, message: String) {
        LogUtil.d(tag, message)
    }

    override fun e(tag: String, e: Throwable) {
        LogUtil.e(tag, e)
    }
}