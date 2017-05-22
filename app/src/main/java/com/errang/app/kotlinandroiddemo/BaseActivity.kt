package com.errang.app.kotlinandroiddemo

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by zengpu on 2017/5/22.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun toast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }
}