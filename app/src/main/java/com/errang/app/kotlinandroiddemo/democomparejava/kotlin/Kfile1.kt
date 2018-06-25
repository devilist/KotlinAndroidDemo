package com.errang.app.kotlinandroiddemo.democomparejava.kotlin


data class KBookCategory(val id: Int, val bookList: List<KBook?>? = null)

data class KBookStore(val name: String, val categoryList: List<KBookCategory?>? = null)

data class KCity(val name: String, val kBookStore: KBookStore?)

fun KBook?.print() = if (null != this) print(this) else Unit

fun kPrintBooks(city: KCity?): Unit {
    // 安全调用运算符
    city?.kBookStore?.categoryList?.forEach {
        it?.bookList?.forEach {
            print(it ?: "")   // elvis运算符
            it.print()
        }
    }
}