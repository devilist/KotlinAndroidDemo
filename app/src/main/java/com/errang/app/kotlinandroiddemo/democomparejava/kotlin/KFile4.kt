package com.errang.app.kotlinandroiddemo.democomparejava.kotlin

import com.errang.app.kotlinandroiddemo.democomparejava.java.JBook


var varString: String = "kotlin"
    private set


fun test() {
    varString = "ddddd"
    KPerson.eyeNum
}


interface IPerson {
    fun speak(language: String)
}

open class KPerson(_name: String, val gender: Int = -1) : IPerson {
    val name: String
    var age: Int

    init {
        name = _name
        this.age = 0
    }

    constructor(name: String, gender: Int, _age: Int) : this(name, gender) {
        this.age = _age
    }

    fun eat() {}
    open fun sleep() {}
    override fun speak(language: String) {
        print(language)
    }

    class NestPerson
    inner class InnerPerson

    companion object {
        val eyeNum = 2
        const val legNum = 2
        @JvmField
        val earNum = 2
        @JvmStatic
        val handNum = 2

        fun height() {}
        @JvmStatic
        fun weight() {
        }
    }

}

class Student(var _name: String, gender: Int = -1) : KPerson(_name, gender) {

    var intro: String = "name:{$name},gender:{$gender}"
        private set
        get() {
            val tmp = field
            return "$tmp,age:{$age}"
        }

    override fun sleep() {
        print(" student sleep")
        val book = JBook("book","",10f)
        book.name
    }
}

object S {
    val student = Student("name", 2)
}


// 对子类作出严格限制
sealed class SealK() {
}

