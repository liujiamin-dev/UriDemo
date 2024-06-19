package com.konka.uridemo

import android.app.Application
import androidx.recyclerview.widget.DefaultItemAnimator
import io.github.album.EasyAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

/**
 * @CreateTime : 2024/6/19 16:20
 * @Author : Administrator
 * @Description :
 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initApplication(this)
    }

    private fun initApplication(context: Application) {
        GlobalConfig.setAppContext(context)
        EasyAlbum.config()
            .setLogger(Logger)
            .setExecutor(Dispatchers.IO.asExecutor())
            //.setImageLoader(GlideImageLoader)
            .setImageLoader(DoodleImageLoader)
            .setDefaultFolderComparator { o1, o2 -> o1.name.compareTo(o2.name)}
            .setItemAnimator(DefaultItemAnimator())
    }
}