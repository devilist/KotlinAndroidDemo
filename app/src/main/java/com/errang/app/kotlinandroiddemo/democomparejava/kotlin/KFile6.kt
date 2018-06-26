package com.errang.app.kotlinandroiddemo.democomparejava.kotlin

import android.content.res.Resources
import android.widget.TextView
import com.errang.app.kotlinandroiddemo.KotlinApplication
import com.errang.app.kotlinandroiddemo.democomparejava.java.JCity
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.textColor

/**
 * Created by zengpu on 2018/6/26
 */

// 函数式编程  扩展函数

// let  apply  with  also  run  takeIf

// 接收者类型
inline fun <T, R> custom_with(receiver: T, block: T.() -> R): R = receiver.block()

inline fun <T, R> T.custom_let(block: (T) -> R): R = block(this)

inline fun <T> T.custom_apply(block: T.() -> Unit): T {
    block()
    return this
}

inline fun <T> T.custom_also(block: (T) -> Unit): T {
    block(this)
    return this
}

inline fun <R> custom_run(block: () -> R): R = block()

inline fun <T> T.custom_takeIf(predicate: (T) -> Boolean): T? = if (predicate(this)) this else null

fun test6(city: JCity) {

    val hasBookStore = with(city) {
        null != bookStore
    }

    city.bookStore?.let { jBookStore ->
        print("bookStore is not null !")
    }

    city.apply {
        setCity("杭州")
    }

    val textView = TextView(KotlinApplication.instance.applicationContext)
    textView.apply {
        text = "i am a textview!"
        textColor = 0x000000
        textSize = 14f
        setOnClickListener {
            print("onClick!")
        }
    }

    city.also { jBookStore ->
        print("bookStore !")
    }

    run {
        print("run !")
    }

    city.takeIf {
        it.bookStore != null
    }
}

class Goods(val price: Float, val configure: Int)

fun calcuPrice(goods: Goods, oriPriceFun: (Goods) -> Float): (Float) -> Float {

    val oriPrice = oriPriceFun(goods)
    return when {
        oriPrice < 1000 -> { ori: Float -> ori * 0.9f + 135 }
        oriPrice < 2000 -> { ori: Float -> ori * 0.85f }
        oriPrice < 5000 -> { ori: Float -> ori * 0.73f }
        else -> { ori: Float -> ori * 0.67f }
    }
}

fun testPrice() {
    val oriPrice = { goods: Goods ->
        when (goods.configure) {
            1 -> goods.price * 0.8f + 1569
            2 -> (goods.price + 139f) * 2.14f
            else -> goods.price
        }
    }
    val goods = Goods(387.4f, 2)
    val sellPrice = calcuPrice(goods, oriPrice)

    print(sellPrice(1999f))
}