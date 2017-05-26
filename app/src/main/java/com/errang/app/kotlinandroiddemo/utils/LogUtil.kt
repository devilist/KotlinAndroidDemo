package com.errang.app.kotlinandroiddemo.utils

import android.util.Log

/**
 * 日志工具
 * Created by zengpu on 2017/5/26.
 */
object LogUtil {
    var isDebugMode = true

    val VERBOSE = 1

    val DEBUG = 2

    val INFO = 3

    val WARN = 4

    val ERROR = 5

    val NOTHING = 6

    var LEVEL = if (isDebugMode) VERBOSE else NOTHING

    fun v(tag: String, msg: String) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg)
        }
    }


    fun w(tag: String, msg: String) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg)
        }
    }
}
