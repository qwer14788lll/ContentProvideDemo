package com.example.contentprovidedemo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import kotlinx.android.synthetic.main.activity_to_database.*

class ToDatabaseActivity : AppCompatActivity() {

    private val uriDatabase = Uri.parse("content://com.example.databasedemo.provider/Book")
    private var bookId = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_database)

        InsertButton.setOnClickListener {
            val value = contentValuesOf(
                "书名" to "深入解析Android 虚拟机",
                "作者" to "钟世礼",
                "页数" to 509,
                "价格" to 99.99,
                "分类_id" to "Android类"
            )
            val newUri = contentResolver.insert(uriDatabase, value)
            Log.i("ToDatabaseActivity", newUri.toString())
            bookId = newUri?.pathSegments?.get(1).toString()
            //get(0)表名，get(1)ID
            Log.i("ToDatabaseActivity", newUri?.pathSegments?.get(0).toString())
            QueryText.text = bookId
        }

        QueryButton.setOnClickListener {
            val stringBuilder = StringBuilder()
            contentResolver.query(uriDatabase, null, null, null, null)?.apply {
                while (moveToNext()) {
                    val id = getString(getColumnIndex("id"))
                    val name = getString(getColumnIndex("书名"))
                    val author = getString(getColumnIndex("作者"))
                    val pages = getString(getColumnIndex("页数"))
                    val price = getString(getColumnIndex("价格"))
                    val cate = getString(getColumnIndex("分类_id"))
                    stringBuilder.append(id).append(" ").append(name).append(" ").append(author)
                        .append(" ").append(pages + "页").append(" ").append(price + "元").append(" ")
                        .append(cate).append('\n')
                }
                close()
            }
            QueryText.text = stringBuilder.toString()
        }

        UpdateButton.setOnClickListener {
            val value = contentValuesOf("价格" to 88.88)
            val uri = Uri.parse("content://com.example.databasedemo.provider/Book/$bookId")
            val count = contentResolver.update(uri, value, null, null)
            QueryText.text = StringBuilder().append("更新了 $count 条数据").toString()
        }

        DeleteButton.setOnClickListener {
            val uri = Uri.parse("content://com.example.databasedemo.provider/Book/$bookId")
            val count = contentResolver.delete(uri, null, null)
            QueryText.text = StringBuilder().append("删除了 $count 条数据").toString()
        }
    }
}