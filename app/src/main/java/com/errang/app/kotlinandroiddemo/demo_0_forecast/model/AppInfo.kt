package com.errang.app.kotlinandroiddemo.demo_0_forecast.model

import android.graphics.drawable.Drawable

/**
 * Created by zengpu on 16/10/29.
 */

class AppInfo {

    var appName: String? = null
        get() = ""
        set(value) {
            field = value
        }
    var appIcon: Drawable? = null
    var versionCode: Int = 0
    var versionName: String? = null
    var packageName: String? = null
}
