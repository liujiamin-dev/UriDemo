package com.konka.service.sharedpreference

import android.content.Context

/**
 * @CreateTime : 2024/6/11 11:52
 * @Author : Administrator
 * @Description :
 */
class DeviceNameStorage(private val mContext: Context) {
    private val sharedPreferences by lazy {
        mContext.getSharedPreferences("device_name_storage", Context.MODE_PRIVATE)
    }

    fun getDeviceName(): String? {
        return sharedPreferences.getString(KEY_DEVICE_NAME, "unknown")
    }

    fun setDeviceName(deviceName: String) {
        sharedPreferences.edit().putString(KEY_DEVICE_NAME, deviceName).apply()
    }

    companion object {
        const val KEY_DEVICE_NAME = "device_name"
    }
}