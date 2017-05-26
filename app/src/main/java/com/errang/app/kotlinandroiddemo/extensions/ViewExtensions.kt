package com.errang.app.kotlinandroiddemo.extensions

import android.content.Context
import android.view.View

/**
 * 工具类
 * Created by zengpu on 2017/5/23.
 */
val View.ctx: Context
    get() = context

