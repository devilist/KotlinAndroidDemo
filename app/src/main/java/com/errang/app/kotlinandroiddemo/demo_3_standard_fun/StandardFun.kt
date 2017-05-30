package com.errang.app.kotlinandroiddemo.demo_3_standard_fun

import android.util.Log

/**
 * Created by zengpu on 2017/5/29.
 */
class StandardFun {


    val map1 = mapOf("key1" to 1, "key2" to 2)
    val map_with_dafult = map1.withDefault { it.length }


    fun main() {
        Log.d("StandardFun", "map1.key1 value is ${map1.getValue("key1")}")
        Log.d("StandardFun", "map_with_dafult.key3 value is ${map_with_dafult.getValue("key3")}")
    }
}