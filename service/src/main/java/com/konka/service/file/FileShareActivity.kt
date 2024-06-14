package com.konka.service.file

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.konka.service.databinding.ActivityFileShareBinding
import java.io.File

/**
 * @CreateTime : 2024/6/13 17:46
 * @Author : Administrator
 * @Description :
 */
class FileShareActivity: AppCompatActivity() {
    private lateinit var binding: ActivityFileShareBinding

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnShareFile.setOnClickListener {
            Thread(Runnable {
                val fileDir = this.filesDir
                Log.i(TAG, "filePath:${fileDir.absolutePath}")
                for(file in fileDir.listFiles()!!) {
                    Log.i(TAG, "subFile:${file.absolutePath}")
                }
                val file = File(fileDir, "test.txt")
                if(!file.exists()) {
                    file.createNewFile()
                    try {
                        file.writeText("Hello World..............")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                runOnUiThread {
                    val uri = FileProvider.getUriForFile(this, "com.konka.service.fileprovider", file)
                    val i = Intent()
                    i.setClassName("com.konka.uridemo", "com.konka.uridemo.FileDemoActivity")
                    i.setPackage("com.konka.uridemo")
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，该过程没有用户交互
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.data = uri
                    startActivity(i)
                }
            }).start()
        }
    }

    companion object {
        const val TAG = "FileShareActivity"
    }
}