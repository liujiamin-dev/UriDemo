package com.konka.uridemo

import androidx.appcompat.app.AppCompatActivity
import com.konka.uridemo.databinding.ActivityWelcomeBinding

/**
 * @CreateTime : 2024/5/31 17:30
 * @Author : ljm
 * @Description :
 */
class WelcomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}