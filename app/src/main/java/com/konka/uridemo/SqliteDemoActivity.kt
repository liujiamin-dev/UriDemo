package com.konka.uridemo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.konka.uridemo.databinding.ActivitySqliteBinding
import java.lang.StringBuilder

/**
 * @CreateTime : 2024/6/13 16:28
 * @Author : Administrator
 * @Description :
 */
class SqliteDemoActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySqliteBinding

    private val uri = Uri.parse("content://${AUTHORITY}/book")
    private var count = 0
    private var pages = 10
    private var price = 100.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySqliteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentResolver.registerContentObserver(uri, true, object: ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                binding.tvResult.text = query()
            }
        })

        binding.btnInsert.setOnClickListener {
            count++
            pages++
            price++
            val values = ContentValues().apply {
                put("name", "java编程思想${count}")
                put("author", "刘亦菲${count}")
                put("pages", pages)
                put("price", price)
            }
            contentResolver.insert(uri, values)
        }

        binding.btnDeleteId.setOnClickListener {
            val id = binding.etDelete.text.toString()
            if(TextUtils.isEmpty(id)) {
                Toast.makeText(this, "id can't be null", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            contentResolver.delete(Uri.parse("content://${AUTHORITY}/book/${id}"), null, null)
        }

        binding.btnUpdateId.setOnClickListener {
            count++
            pages++
            price++
            val id = binding.etUpdate.text.toString()
            if(TextUtils.isEmpty(id)) {
                Toast.makeText(this, "id can't be null", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val values = ContentValues().apply {
                put("name", "java编程思想${count}")
                put("author", "刘亦菲${count}")
                put("pages", pages)
            }
            contentResolver.update(Uri.parse("content://${AUTHORITY}/book/${id}"), values, null, null)
        }

        binding.btnQuery.setOnClickListener {
            binding.tvResult.text = query()
        }
    }

    @SuppressLint("Range")
    private fun query(): String {
        val strBuilder = StringBuilder()
        val cursor = contentResolver.query(uri, null, null, null, null)
        if(cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val pages = cursor.getInt(cursor.getColumnIndex("pages"))
                val price = cursor.getDouble(cursor.getColumnIndex("price"))
                strBuilder.append("id: $id, name: $name, author:$author, pages: $pages, price: $price\n")
            }
        }
        return strBuilder.toString()
    }

    companion object {
        private const val AUTHORITY = "com.konka.service.sqliteprovider"
    }
}