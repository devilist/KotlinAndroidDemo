package com.errang.app.kotlinandroiddemo.extensions

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.SelectQueryBuilder

/**
 * 数据库操作的一些扩展函数
 * Created by zengpu on 2017/5/25.
 */


/**
 * 扩展SelectQueryBuilder的parseList函数
 *
 * @param parser lambda表达式:输入参数为Map<String, Any?>,返回值为T
 *
 * @return List<T>
 */
fun <T : Any> SelectQueryBuilder.parseList(parser: (Map<String, Any?>) -> T): List<T> =
        parseList(object : MapRowParser<T> {
            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
        })

/**
 * 扩展SelectQueryBuilder的parseOpt函数
 *
 * @param parser lambda表达式:输入参数为Map<String, Any?>,返回值为T
 *
 * @return T
 */
fun <T : Any> SelectQueryBuilder.parseOpt(parser: (Map<String, Any?>) -> T): T? =
        parseOpt(object : MapRowParser<T> {
            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
        })


/**
 * 清空表格
 * @param tableName 表名
 */
fun SQLiteDatabase.clear(tableName: String) {
    execSQL("delete from $tableName")
}