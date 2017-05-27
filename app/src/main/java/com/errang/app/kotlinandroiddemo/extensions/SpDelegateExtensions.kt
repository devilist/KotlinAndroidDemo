package com.errang.app.kotlinandroiddemo.extensions

import android.content.Context
import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 特征库存取委托扩展
 * Created by zengpu on 2017/5/27.
 */

object SpDelegateExt {

    fun <T : Any> preference(context: Context, name: String, default: T)
            = Preference(context, name, default)
}


class Preference<T>(val context: Context, val name: String, val default: T)
    : ReadWriteProperty<Any?, T> {

    val prefs by lazy {
        context.getSharedPreferences("default_name", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        Log.d("Preference", "getValue")
        return getPrefValue(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        Log.d("Preference", "setValue")
        putPrefValue(name, value)

    }

    private fun <T> getPrefValue(name: String, default: T): T = with(prefs) {
        val value: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }
        value as T
    }

    private fun <U> putPrefValue(name: String, value: U) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences ")
        }.apply()
    }
}