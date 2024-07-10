package com.konka.uridemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.konka.uridemo.classuser.UriDemoActivity
import com.konka.uridemo.classuser.UrlDemoActivity
import com.konka.uridemo.databinding.ActivityHomeBinding
import com.konka.uridemo.security.SecurityActivity
import io.github.album.EasyAlbum
import io.github.album.MediaData
import io.github.album.interfaces.MediaFilter

/**
 * @CreateTime : 2024/6/17 17:24
 * @Author : Administrator
 * @Description :
 */
class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnShareperference.setOnClickListener {
                val intent = Intent(this@HomeActivity, ShareperferenceDemoActivity::class.java)
                startActivity(intent)
            }

            btnSqlite.setOnClickListener {
                val intent = Intent(this@HomeActivity, SqliteDemoActivity::class.java)
                startActivity(intent)
            }

            btnAlbum.setOnClickListener {
                var isGranted = true
                val notPerssions = mutableListOf<String>()
                for(permission in storagePermissions) {
                    val i = ActivityCompat.checkSelfPermission(this@HomeActivity, permission)
                    if(i != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false
                        notPerssions.add(permission)
                    }
                }
                if(isGranted) {
                    goAlbumActivity()
                } else {
                    ActivityCompat.requestPermissions(this@HomeActivity, notPerssions.toTypedArray(), 1001)
                }
            }

            btnUrl.setOnClickListener {
                val intent = Intent(this@HomeActivity, UrlDemoActivity::class.java)
                startActivity(intent)
            }

            btnUri.setOnClickListener {
                val intent = Intent(this@HomeActivity, UriDemoActivity::class.java)
                startActivity(intent)
            }

            btnSecurity.setOnClickListener {
                val intent = Intent(this@HomeActivity, SecurityActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isGranted = true
        if(requestCode == 1001) {
            if(grantResults.isNotEmpty()) {
                for(i in grantResults.indices) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false
                        break
                    }
                }
                if(isGranted) {
                    goAlbumActivity()
                }
            }
        }
    }

    private fun goAlbumActivity() {
        EasyAlbum.from(this)
            .setFilter(TypeMediaFilter(Option.ALL))
            .setSelectedLimit(9)
            .setOverLimitCallback {

            }.setAllString("All")
            .enableOriginal()
            .setAlbumListener {

            }.start {

            }
    }

    enum class Option(val text: String) {
        ALL("All"),
        VIDEO("All Videos"),
        IMAGE("All Images")
    }

    private class TypeMediaFilter(private val opt: Option) : MediaFilter {
        override fun accept(media: MediaData): Boolean {
            return when (opt) {
                Option.VIDEO -> media.isVideo
                Option.IMAGE -> !media.isVideo
                else -> true
            }
        }

        override fun tag(): String {
            return "Filter${opt.text}"
        }
    }

    companion object {
        private val storagePermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}