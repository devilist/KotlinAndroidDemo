package com.errang.app.kotlinandroiddemo.custom_delegates

import kotlin.properties.ReadWriteProperty

/**
 * 委托扩展
 * Created by zengpu on 2017/5/23.
 */
object DelegatesExt {
    fun <T> notNullSingleValue(): ReadWriteProperty<Any, T> = NotNullSingleValueVar()
}