package com.errang.app.kotlinandroiddemo.demo_1_db.model

/**
 * SQLite表与对象之间的互相映射
 * Created by zengpu on 2017/5/24.
 */
class City(val map: MutableMap<String, Any?>, val dailyForecast: List<DayForecast>) {

    var _id: Long by map
    var city: String by map
    var country: String by map

    constructor(id: Long, city: String, country: String,
                dailyForecast: List<DayForecast>) : this(HashMap(), dailyForecast) {
        this._id = id
        this.city = city
        this.country = country
    }
}

class DayForecast(val map: MutableMap<String, Any?>) {

    var _id: Long by map
    var date: Long by map
    var description: String by map
    var high: Int by map
    var low: Int by map
    var icon: String by map
    var cityId: Long by map

    constructor(date: Long, description: String, high: Int, low: Int, iconUrl: String, cityId: Long)
            : this(HashMap()) {
        this.date = date
        this.description = description
        this.high = high
        this.low = low
        this.icon = iconUrl
        this.cityId = cityId
    }

}
