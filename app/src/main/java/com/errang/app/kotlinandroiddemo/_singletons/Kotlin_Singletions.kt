package com.errang.app.kotlinandroiddemo._singletons

/**
 * singletion的几种kotlin写法
 *
 * Created by zengpu on 2017/5/24.
 */

/**
 * 懒人写法
 * 使用 object-declaration 方式
 */
object Singletion {

    // 在定义的时候就初始化了，因此不需要构造器

    // property声明
    var allMembers = arrayListOf<String>()

    // method声明
    fun doSth() {
    }
}


/**
 * 基本懒加载写法
 * 用到的时候才初始化
 * 线程不安全
 */
class LazyNotThreadSafe private constructor() {

    // 方式1 采用lazy委托方式
    companion object {
        val instance by lazy(LazyThreadSafetyMode.NONE) {
            LazyNotThreadSafe()
        }
    }

/* **************************************************** */

    // 方式2 直译java代码方式
    private var instance_java: LazyNotThreadSafe? = null

    fun getInstance(): LazyNotThreadSafe {
        if (null == instance_java)
            instance_java = LazyNotThreadSafe()
        return instance_java!!
    }
}

/**
 * 同步锁方式
 */
class LazyThreadSafeSynchronized private constructor() {

    companion object {
        private var instance: LazyThreadSafeSynchronized? = null

        @Synchronized
        fun get(): LazyThreadSafeSynchronized {
            if (null == instance)
                instance = LazyThreadSafeSynchronized()
            return instance!!
        }
    }
}

/**
 * double-check方式
 */
class LazyThreadSafeDoubleCheck private constructor() {

    // 方式1 kotlin方式 lazy委托
    companion object doubleCheck {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LazyThreadSafeDoubleCheck()
        }
    }

/* ***************************************************************************** */

    // 方法2 直译java代码 Volatile 不能去掉
    private @Volatile var instance_java1: LazyThreadSafeDoubleCheck? = null

    fun getInstance(): LazyThreadSafeDoubleCheck {
        if (null == instance_java1) {
            synchronized(this) {
                if (null == instance_java1)
                    instance_java1 = LazyThreadSafeDoubleCheck()
            }
        }
        return instance_java1!!
    }
}

/**
 * 静态内部类方法
 */
class LazyThreadSafeInnerObject private constructor() {

    companion object {
        fun instance(): LazyThreadSafeInnerObject = SingletonHolder.instance
    }

    private object SingletonHolder {
        val instance = LazyThreadSafeInnerObject()
    }
}
