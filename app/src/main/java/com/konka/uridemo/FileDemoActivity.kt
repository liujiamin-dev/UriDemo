package com.konka.uridemo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * @CreateTime : 2024/6/13 18:03
 * @Author : ljm
 * @Description :
 */
class FileDemoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        Log.i(TAG, "data: ${intent.data}")
        val uri = intent.data
        if(uri != null) {
            Thread(Runnable {
                Log.i(TAG, "content: ${readFileFromUri(this, uri)}")
            }).start()

        }
    }

    private fun readFileFromUri(context: Context, fileUri: Uri): String? {
        val contentResolver = context.contentResolver
        val fileContent = StringBuilder()
        try {
            contentResolver.openInputStream(fileUri).use { inputStream ->
                InputStreamReader(inputStream).use { inputStreamReader ->
                    BufferedReader(inputStreamReader).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            fileContent.append(line).append("\n")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return fileContent.toString()
    }

    companion object {
        const val TAG = "FileDemoActivity"
    }
}