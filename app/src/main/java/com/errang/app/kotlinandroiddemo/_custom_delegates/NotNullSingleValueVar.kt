package com.errang.app.kotlinandroiddemo._custom_delegates

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 自定义属性委托
 * 创建一个 notNull 的委托，它只能被赋值一次，如果第二次赋值，它就会抛异常
 * Created by zengpu on 2017/5/23.
 */
class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        // 如果已经被初始化，则会返回一个值，否则会抛异常
        return value ?: throw IllegalStateException("${property.name} " + "not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        // 如果仍然是null，则赋值，否则会抛异常
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} " + "already initialized")
    }
}