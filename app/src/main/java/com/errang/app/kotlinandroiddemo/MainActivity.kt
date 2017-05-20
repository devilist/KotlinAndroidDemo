package com.errang.app.kotlinandroiddemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(),
        View.OnClickListener {

    var TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    fun initView() {
        tv_hello.text = "hello,kotlin!"
        tv_hello.leftPadding = 10
        tv_hello.textSize = 24f
        tv_hello.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_hello -> {
                toast(TAG)
            }

        }

    }
}
