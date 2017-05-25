package com.errang.app.kotlinandroiddemo

import android.app.Application
import com.errang.app.kotlinandroiddemo._custom_delegates.DelegatesExt

/**
 * Created by zengpu on 2017/5/23.
 */
class KotlinApplication : Application() {

    companion object {
        var instance: KotlinApplication by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}