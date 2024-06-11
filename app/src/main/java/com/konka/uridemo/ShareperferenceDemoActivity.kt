package com.konka.uridemo

import android.content.ContentValues
import android.database.ContentObserver
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.konka.uridemo.databinding.ActivityWelcomeBinding

/**
 * @CreateTime : 2024/5/31 17:30
 * @Author : ljm
 * @Description :
 */
class ShareperferenceDemoActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    private val switchUri = Uri.parse("content://com.konka.service.provider/switch")
    private val deviceNameUri = Uri.parse("content://com.konka.service.provider/device_name")

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchHandler()
        deviceNameHandler()
    }

    private fun switchHandler() {
        contentResolver.registerContentObserver(switchUri, true, object: ContentObserver(android.os.Handler()) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                val switch = getSwitch()
                Log.i(TAG, "onChange: $switch")
                binding.switchStr = if (switch) "开" else "关"
            }
        })

        binding.rgGetSwitch.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                binding.rbOpen.id -> {
                    setSwitch(true)
                }
                binding.rbClose.id -> {
                    setSwitch(false)
                }
            }
        }

        val switch = getSwitch()
        if(switch) {
            binding.switchStr = "开"
            binding.rbOpen.isChecked = true
        } else {
            binding.switchStr = "关"
            binding.rbClose.isChecked = true
        }
    }

    private fun getSwitch(): Boolean {
        var switch = ""
        val cursor = contentResolver.query(switchUri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            switch = cursor.getString(0)
            cursor.close()
        }
        return switch == "true"
    }

    private fun setSwitch(switch: Boolean) {
        val values = ContentValues()
        values.put("switch", switch)
        contentResolver.update(switchUri, values, null, null)
    }

    private fun deviceNameHandler() {
        contentResolver.registerContentObserver(deviceNameUri, true, object: ContentObserver(android.os.Handler()) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                val deviceName = getDeviceName()
                Log.i(TAG, "onChange: $deviceName")
                binding.deviceName = deviceName
            }
        })

        binding.btnDeviceName.setOnClickListener {
            val deviceName = binding.etDeviceName.text.toString()
            setDeviceName(deviceName)
        }

        val deviceName = getDeviceName()
        binding.deviceName = deviceName
        binding.etDeviceName.hint = deviceName
    }

    private fun getDeviceName(): String {
        var deviceName = ""
        val cursor = contentResolver.query(deviceNameUri, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            deviceName = cursor.getString(0)
            cursor.close()
        }
        return deviceName
    }

    private fun setDeviceName(deviceName: String) {
        if(TextUtils.isEmpty(deviceName)) {
            Toast.makeText(this, "deviceName can't be null", Toast.LENGTH_SHORT).show()
            return
        }
        val values = ContentValues()
        values.put("device_name", deviceName)
        contentResolver.update(deviceNameUri, values, null, null)
    }

    companion object{
        const val TAG = "ShareperferenceActivity"
    }
}