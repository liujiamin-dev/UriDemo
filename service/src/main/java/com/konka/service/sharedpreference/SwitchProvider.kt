package com.konka.service.sharedpreference

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

/**
 * @CreateTime : 2024/5/31 18:08
 * @Author : Administrator
 * @Description :
 */
class SwitchProvider: ContentProvider() {
    private lateinit var switchStorage: SwitchStorage
    private lateinit var deviceNameStorage: DeviceNameStorage

    override fun onCreate(): Boolean {
        switchStorage = SwitchStorage(context!!)
        deviceNameStorage = DeviceNameStorage(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        Log.i(TAG, "query: $uri")
        val cursor = MatrixCursor(arrayOfNulls(1))
        when (uriMatcher.match(uri)) {
            SWITCH_DIR -> {
                val switchs = arrayOfNulls<String>(1)
                switchs[0] = if(switchStorage.getSwitch()) "true" else "false"
                cursor.addRow(switchs)
            }
            DEVICE_NAME_DIR -> {
                val devices = arrayOfNulls<String>(1)
                devices[0] = deviceNameStorage.getDeviceName()
                cursor.addRow(devices)
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            SWITCH_DIR -> return "vnd.android.cursor.dir/vnd.com.konka.service.provider.switch"
            DEVICE_NAME_DIR -> return "vnd.android.cursor.dir/vnd.com.konka.service.provider.device_name"
        }
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var uriReturn: Uri? = null
        uriReturn = when (uriMatcher.match(uri)) {
            SWITCH_DIR -> {
                switchStorage.setSwitch(values!!.getAsBoolean("switch"))
                Uri.parse("content://$AUTHORITY/switch")
            }

            DEVICE_NAME_DIR -> {
                deviceNameStorage.setDeviceName(values!!.getAsString("device_name"))
                Uri.parse("content://$AUTHORITY/device_name")
            }

            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        if (uriReturn != null) {
            context!!.contentResolver.notifyChange(uriReturn, null)
        }
        return uriReturn
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.i(TAG, "update: $uri")
        var res = 0
        res = when (uriMatcher.match(uri)) {
            SWITCH_DIR -> {
                switchStorage.setSwitch(values!!.getAsBoolean("switch"))
                1
            }

            DEVICE_NAME_DIR -> {
                deviceNameStorage.setDeviceName(values!!.getAsString("device_name"))
                1
            }

            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        context!!.contentResolver.notifyChange(uri, null)
        return res
    }

    companion object {
        const val TAG = "SwitchProvider"
        private const val AUTHORITY = "com.konka.service.provider"
        private const val SWITCH_DIR = 0
        private const val DEVICE_NAME_DIR = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "switch", SWITCH_DIR)
            addURI(AUTHORITY, "device_name", DEVICE_NAME_DIR)
        }
    }
}