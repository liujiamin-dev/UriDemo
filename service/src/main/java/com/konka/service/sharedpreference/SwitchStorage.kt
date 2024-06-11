package com.konka.service.sharedpreference

import android.content.Context
import android.content.SharedPreferences

/**
 * @CreateTime : 2024/5/31 19:36
 * @Author : Administrator
 * @Description :
 */
class SwitchStorage(private val mContext: Context) {
    private val sharedPreferences by lazy {
        mContext.getSharedPreferences("switch_storage", Context.MODE_PRIVATE)
    }

    fun getSwitch(): Boolean {
        return sharedPreferences.getBoolean(KEY_SWITCH, false)
    }

    fun setSwitch(switch: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_SWITCH, switch).apply()
    }

    companion object {
        const val KEY_SWITCH = "switch"
    }
}