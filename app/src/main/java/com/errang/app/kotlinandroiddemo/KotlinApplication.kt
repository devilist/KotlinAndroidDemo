package com.errang.app.kotlinandroiddemo

import android.app.Application
import com.errang.app.kotlinandroiddemo.custom_delegates.DelegatesExt

/**
 * Created by zengpu on 2017/5/23.
 */
class KotlinApplication : Application() {

    companion object {
        // 单例 采用属性委托方式
        private var instance: KotlinApplication by DelegatesExt.notNullSingleValue()

        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}