package com.errang.app.kotlinandroiddemo.democomparejava.kotlin

import com.errang.app.kotlinandroiddemo.democomparejava.java.IJInterface
import com.errang.app.kotlinandroiddemo.democomparejava.java.JBook


fun test5() {
//    varString = "sdsd"

}


class DelegateList<T> : List<T> {
    private val inner = arrayListOf<T>()
    override val size: Int
        get() = inner.size

    override fun contains(element: T): Boolean = inner.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = inner.containsAll(elements)
    override fun get(index: Int): T = inner[index]
    override fun indexOf(element: T): Int = inner.indexOf(element)
    override fun isEmpty(): Boolean = inner.isEmpty()
    override fun iterator(): Iterator<T> = inner.iterator()
    override fun lastIndexOf(element: T): Int = inner.lastIndexOf(element)
    override fun listIterator(): ListIterator<T> = inner.listIterator()
    override fun listIterator(index: Int): ListIterator<T> = inner.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<T> = inner.subList(fromIndex, toIndex)
}

// 类委托
class DelegateList1<T>(inner: List<T> = ArrayList<T>()) : List<T> by inner


val jinterface = object : IJInterface {

    override fun method(para1: String, para2: MutableList<String?>?, book: JBook?) {

    }
}