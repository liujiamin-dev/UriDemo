package com.konka.service.apkinstall

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.konka.service.databinding.ActivityInstallerBinding
import java.io.File


/**
 * @CreateTime : 2024/7/19 10:28
 * @Author : ljm
 * @Description :
 */
class InstallerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInstallerBinding

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstallerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果没有权限，就请求权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // 如果已经有权限，执行需要权限的操作
            // 例如读取或写入外部存储
        }
        binding.btnInstall.setOnClickListener {
            Thread(Runnable {
                val targetFilePath = "${this.filesDir.absolutePath}/lebo.apk"
                Log.i(TAG, "targetFilePath:${targetFilePath}")
                if (!File(targetFilePath).exists()) {
                    copyAssetFile(this, "lebo.apk", targetFilePath)
                }
                runOnUiThread {
                    installApk(this, targetFilePath)
                }
            }).start()
        }
        registerReceiver(this)
    }

    private fun copyAssetFile(context: Context, fileName: String, targetFilePath: String) {
        Log.i(TAG, "copy---")
        val inputStream = context.assets.open(fileName)
        val outputStream = File(targetFilePath).outputStream()
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
    }

    private fun installApk(context: Context, filePath: String) {
        Log.i(TAG, "installApk: ${filePath}")
        val apkFile = File(filePath)
        Log.i(TAG, "file length:${apkFile.length()}")
        if (apkFile.exists()) {
            val uri = FileProvider.getUriForFile(context, "com.konka.service.fileprovider", apkFile)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        }
    }

    private val appReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //收不到？？？
            Log.i(TAG, "onReceive: ${intent?.action}")
            when (intent?.action) {
                Intent.ACTION_PACKAGE_ADDED -> {
                    Log.i(TAG, "onReceive: install: ${intent.data?.schemeSpecificPart}")
                }
                Intent.ACTION_PACKAGE_REMOVED -> {
                    Log.i(TAG, "onReceive: uninstall : ${intent.data?.schemeSpecificPart}")
                }
            }
        }
    }
    private fun registerReceiver(context: Context) {
        val installFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }
        context.registerReceiver(appReceiver, installFilter)
    }

    private fun unRegisterReceiver(context: Context) {
        context.unregisterReceiver(appReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterReceiver(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isGrant = true
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isGrant = false
                        break
                    }
                }
                //用户同意，执行操作
                if (isGrant) {

                } else {
                    finish()
                }
            } else {
                finish()
            }
        }
    }

    companion object {
        const val TAG = "InstallerActivity"
        const val PERMISSION_REQUEST_CODE = 100
    }
}