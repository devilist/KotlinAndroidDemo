package com.errang.app.kotlinandroiddemo.API

/**
 * Created by zepung on 2017/5/22.
 */
interface RequestCommand<T> {

    fun execute(): T
}