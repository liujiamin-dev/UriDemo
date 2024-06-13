package com.konka.service.sqlite

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

/**
 * @CreateTime : 2024/6/11 17:50
 * @Author : Administrator
 * @Description :
 */
class SqliteContentProvider: ContentProvider() {
    private lateinit var sqliteHelper: MyDatabaseHelper

    override fun onCreate(): Boolean {
        sqliteHelper = MyDatabaseHelper(context!!,null, 1)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.i(TAG, "query: uri = $uri")
        val writeDatabase = sqliteHelper.writableDatabase
        return when(uriMatcher.match(uri)) {
            BOOK_DIR -> {
                writeDatabase.query("Book", projection, selection, selectionArgs, null, null, sortOrder)
            }

            BOOK_ITEM -> {
                val id = uri.pathSegments[1]
                writeDatabase.query("Book", projection, "id = ?", arrayOf(id), null, null, sortOrder)
            }

            else -> {
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)) {
            BOOK_DIR -> "vnd.android.cursor.dir/vnd.com.konka.service.sqliteprovider.book"
            BOOK_ITEM -> "vnd.android.cursor.item/vnd.com.konka.service.sqliteprovider.book"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.i(TAG, "insert: uri = $uri")
        val writeDatabase = sqliteHelper.writableDatabase
        return when(uriMatcher.match(uri)) {
            BOOK_DIR, BOOK_ITEM -> {
                val id = writeDatabase.insert("Book", null, values)
                val uriReturn = Uri.parse("content://$AUTHORITY/book/$id")
                context!!.contentResolver.notifyChange(uriReturn, null)
                uriReturn
            }

            else -> {
                null
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.i(TAG, "delete: uri = $uri")
        val writeDatabase = sqliteHelper.writableDatabase
        val res =  when(uriMatcher.match(uri)) {
            BOOK_DIR -> {
                writeDatabase.delete("Book", selection, selectionArgs)
            }

            BOOK_ITEM -> {
                Log.i(TAG, "delete url pathSegments: ${uri.pathSegments}")
                val id = uri.pathSegments[1]
                writeDatabase.delete("Book", "id = ?", arrayOf(id))
            }

            else -> {
                0
            }
        }
        if(res > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return res
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.i(TAG, "update: uri = $uri")
        val writeDatabase = sqliteHelper.writableDatabase
        val res =  when(uriMatcher.match(uri)) {
            BOOK_DIR -> {
                writeDatabase.update("Book", values, selection, selectionArgs)
            }

            BOOK_ITEM -> {
                Log.i(TAG, "update url pathSegments: ${uri.pathSegments}")
                val id = uri.pathSegments[1]
                writeDatabase.update("Book", values, "id = ?", arrayOf(id))
            }

            else -> {
                0
            }
        }
        if(res > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return res
    }

    companion object {
        const val TAG = "sqliteProvider"
        private const val AUTHORITY = "com.konka.service.sqliteprovider"
        private const val BOOK_DIR = 0
        private const val BOOK_ITEM = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "book", BOOK_DIR)
            addURI(AUTHORITY, "book/#", BOOK_ITEM)
        }
    }
}