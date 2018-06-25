package com.errang.app.kotlinandroiddemo.utils

import android.util.Log

/**
 * 日志工具
 * Created by zengpu on 2017/5/26.
 */
object LogUtil {
    private var isDebugMode = true

    private const val VERBOSE = 1

    private const val DEBUG = 2

    private const val INFO = 3

    private const val WARN = 4

    private const val ERROR = 5

    private const val NOTHING = 6

    private var LEVEL = if (isDebugMode) VERBOSE else NOTHING

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
