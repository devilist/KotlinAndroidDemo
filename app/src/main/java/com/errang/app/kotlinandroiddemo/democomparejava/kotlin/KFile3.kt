package com.errang.app.kotlinandroiddemo.democomparejava.kotlin

class TypeKotlin {

    // 不可变非空类型
    val val0: Int = 1
    val val1 = 1
    val val2 = KBook("kotlin", "wds", 13f)
//    val val3: String = null   // 不能为空，不然编译不通过
//    val val4: KCity           // 必须初始化，不然编译不通过

    // 可以用lazy委托延迟初始化
    val val5: KCity by lazy { KCity("hangzhou", null) }

    //========================================================================

    // 可变非空类型
    var var0: Int = 2
    var var1 = 3
//    var var2: Int = null  // 不能为空，不然编译不通过
//    var var3: Long         // 必须初始化，不然编译不通过

    // 可以用 lateinit 延迟初始化
    lateinit var var4: KBook

    //===========================================================================
    // 不可变可空类型
    val valNull0: String? = null
//    val valNull1: String?      // 必须初始化，不然编译不通过

    // 可变可空类型
    var varNull0: String? = null
    var varNullBook: KBook? = null
//    var varNull1: Int?        // 必须初始化，不然编译不通过


    fun test() {
        // 不可变类型不能重新赋值
//        val0 = 1
//        valNull0 = "s"

        // 可变类型可以重新赋值
        var0 = 1
        var4 = KBook("kotlin", "wds", 13f)

        // 安全调用运算符
        varNullBook?.price = 19f
        // elvis运算符
        val s0: String = varNull0 ?: "default"
        // 非空断言
        val s1: String = varNull0!!

    }

    // 可变集合 和 不可变集合
    // 数组
    val list = listOf<String>("a", "b", "c")
    val mutList = mutableListOf<String>("a", "b", "c")
    val set = hashSetOf<String>("s", "d", "ds")
    val map = hashMapOf<Int, String>(0 to "a", 1 to "b", 2 to "c")

    fun testList() {
//        list[0] = "c"   //  不可变集合不能修改元素值
        mutList[2] = "aadsdsdsd"
        list.toMutableList()
    }

}
