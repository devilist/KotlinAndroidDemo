package com.errang.app.kotlinandroiddemo.demo_1_db.table

/**
 * 城市表
 * Created by zengpu on 2017/5/24.
 */
object CityTable {

    val NAME = "CityForecast"
    val ID = "_id"  // zipCode
    val CITY = "city"
    val COUNTRY = "country"
}

object DayForecastTable {

    val NAME = "DayForecast"
    val ID = "_id"
    val DATE = "date"
    val DESCRIPTION = "description"
    val HIGH = "high"
    val LOW = "low"
    val ICON_URL = "iconUrl"
    val CITY_ID = "cityId"
}