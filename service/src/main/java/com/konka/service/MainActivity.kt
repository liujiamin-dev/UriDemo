package com.konka.service

import androidx.appcompat.app.AppCompatActivity
import com.konka.service.databinding.ActivityMainBinding

/**
 * @CreateTime : 2024/5/31 17:55
 * @Author : Administrator
 * @Description :
 */
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}