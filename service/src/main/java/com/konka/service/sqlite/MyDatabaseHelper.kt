package com.konka.service.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * @CreateTime : 2024/6/11 17:38
 * @Author : Administrator
 * @Description :
 */
class MyDatabaseHelper(mContext: Context, factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(mContext, DB_NAME, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_BOOK)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        const val DB_NAME = "konka_service"
        const val CREATE_BOOK = "create table Book(" +
                "id integer primary key autoincrement,"+
                "author text,"+
                "price real," +
                "pages integer," +
                "name text" +
                ")"
    }
}