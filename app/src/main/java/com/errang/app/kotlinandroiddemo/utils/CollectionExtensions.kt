package com.errang.app.kotlinandroiddemo.utils

/**
 * 集合类扩展函数
 * Created by zengpu on 2017/5/25.
 */

fun <K, V : Any> MutableMap<K, V?>.toVarargArray(): Array<out Pair<K, V>>
        = map({ Pair(it.key, it.value!!) }).toTypedArray()

