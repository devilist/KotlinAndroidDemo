package com.errang.app.kotlinandroiddemo.demo_1_db.dao

import android.util.Log
import com.errang.app.kotlinandroiddemo.demo_0_forecast.model.ForecastResult
import com.errang.app.kotlinandroiddemo.demo_1_db.ForecastDbHelper
import com.errang.app.kotlinandroiddemo.demo_1_db.model.City
import com.errang.app.kotlinandroiddemo.demo_1_db.model.DayForecast
import com.errang.app.kotlinandroiddemo.demo_1_db.table.CityTable
import com.errang.app.kotlinandroiddemo.demo_1_db.table.DayForecastTable
import com.errang.app.kotlinandroiddemo.utils.clear
import com.errang.app.kotlinandroiddemo.utils.parseList
import com.errang.app.kotlinandroiddemo.utils.parseOpt
import com.errang.app.kotlinandroiddemo.utils.toVarargArray
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * dao层
 * Created by zengpu on 2017/5/24.
 */
class ForecastDao(val forecastDbHelper: ForecastDbHelper = ForecastDbHelper.instance) {

    /**
     * 存数据
     */
    fun saveForecastToLocalDB(forecastResult: ForecastResult) = forecastDbHelper.use {

        clear(CityTable.NAME)
        clear(DayForecastTable.NAME)

        // model转换
        // 从ForecastResult到City和DayForecast的转换
        with(forecastResult) {
            // 每日天气
            val dailyList = forecastResult.list.map {
                DayForecast(it.dt, it.weather[0].description, it.temp.max.toInt(), it.temp.min.toInt(),
                        it.weather[0].icon, forecastResult.city.id)
            }
            // 城市
            val city = City(forecastResult.city.id, forecastResult.city.name,
                    forecastResult.city.country, dailyList)

            // 插入城市信息
            Log.d("ForecastDao", "city vararg values " + city.map.toVarargArray())
            insert(CityTable.NAME, *city.map.toVarargArray())
            // 插入城市天气
            city.dailyForecast.forEach {
                Log.d("ForecastDao", "dailyForecast vararg values " + it.map.toVarargArray())
                insert(DayForecastTable.NAME, *it.map.toVarargArray())
            }
        }
    }

    /**
     * 从数据库查询天气信息
     */
    fun queryForecastFromLocalDB(zipCode: Long, date: Long) = forecastDbHelper.use {

        val dailyRequest = "${DayForecastTable.CITY_ID} = {id}" + "AND ${DayForecastTable.DATE} >= {date}"

        // 天气的查询，两种方式
        // 方式1 扩展SelectQueryBuilder的parseList函数，传入参数为lambda表达式
        val dailyForecast = select(DayForecastTable.NAME)
                .whereArgs(dailyRequest, "id" to zipCode, "date" to date)
                .parseList {
                    // 两种写法
                    // 方式1
                    cursor ->
                    DayForecast(HashMap(cursor))
                    // 方式2：
                    // 如果lambda函数字⾯值只有⼀个参数，那么它的声明可以省略（连同 -> ），其名称是 it
                    // DayForecast(HashMap(it))
                }

        // 方式2 自定义CustomMapRowParser 继承 MapRowParser 作为parseList的传入参数
        // 未测试
        val dailyForecast1 = select(DayForecastTable.NAME)
                .whereArgs(dailyRequest, "id" to zipCode, "date" to date)
                .parseList(CustomMapRowParser { DayForecast(HashMap(it)) })

        // 查询城市
        val city = select(CityTable.NAME)
                .whereSimple("${CityTable.ID} = ?", zipCode.toString())
                .parseOpt { City(HashMap(it), dailyForecast) }
    }

    /**
     * 继承 MapRowParser<T>
     */
    class CustomMapRowParser<out T>(val parser: (Map<String, Any?>) -> T) : MapRowParser<T> {
        override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
    }
}










