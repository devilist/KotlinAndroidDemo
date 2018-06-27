package com.errang.app.kotlinandroiddemo.democomparejava.kotlin

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import com.errang.app.kotlinandroiddemo.R
import org.jetbrains.anko.*

/**
 * Created by zengpu on 2018/6/27
 */

class KActivity : KBaseActivity() {
    fun KActivity.bindUI() {
        linearLayout {
            lparams(matchParent, matchParent)
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            backgroundColor = 0xFF666666.toInt()

            imageView(R.mipmap.ic_launcher) {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.lparams {
                width = dip(150)
                height = dip(150)
            }

            textView {
                backgroundColor = Color.WHITE
                padding = dip(10)
                text = "dsl text"
                textSize = sp(16).toFloat()
                textColor = Color.BLACK
                setOnClickListener {
                    showDialog("title", " dsl message!") {
                        toast("i am a dsl dialog")
                    }.show()
                }
            }.lparams {
                width = wrapContent
                height = wrapContent
                topMargin = dip(50)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.bindUI()
//        setUI()
    }

    fun setUI() {
        this.bindUI {
            verticalLayout {
                lparams(matchParent, matchParent)
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                backgroundColor = 0xFF666666.toInt()

                imageView(R.mipmap.ic_launcher) {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }.lparams {
                    width = dip(150)
                    height = dip(150)
                }
            }
        }
    }
}