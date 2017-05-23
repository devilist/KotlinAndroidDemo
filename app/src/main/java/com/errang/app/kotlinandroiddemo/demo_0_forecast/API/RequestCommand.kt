package com.errang.app.kotlinandroiddemo.demo_0_forecast.API

/**
 * Created by zepung on 2017/5/22.
 */
interface RequestCommand<T> {

    fun execute(): T
}