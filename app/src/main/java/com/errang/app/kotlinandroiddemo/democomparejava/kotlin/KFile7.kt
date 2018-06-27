package com.errang.app.kotlinandroiddemo.democomparejava.kotlin

import android.app.Activity
import android.content.DialogInterface
import android.view.View
import org.jetbrains.anko.AlertBuilder
import org.jetbrains.anko.alert

/**
 * Created by zengpu on 2018/6/27
 */

fun <T : Activity> T.bindUI(block: (T) -> View) = block(this)

fun Activity.showDialog(title: String, message: String,
                        okProcess: () -> Unit): AlertBuilder<DialogInterface> =
        alert {
            this.title = title
            this.message = message
            positiveButton("ok") {
                okProcess()
                it.dismiss()
            }
            negativeButton("cancel") { it.dismiss() }
        }
