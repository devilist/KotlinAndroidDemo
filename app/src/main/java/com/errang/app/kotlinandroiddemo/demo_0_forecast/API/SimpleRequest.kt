package com.errang.app.kotlinandroiddemo.demo_0_forecast.API

import com.errang.app.kotlinandroiddemo.demo_0_forecast.model.ForecastResult
import com.google.gson.Gson
import java.net.URL

/**
 * 简单的数据请求
 * Created by zengpu on 2017/5/22.
 */
class SimpleRequest(private val zipCode: String) {

    companion object {
        private val APP_ID = "15646a06818f61f7b8d7823ca833e1ce"
        private val URL = "http://api.openweathermap.org/data/2.5/" +
                "forecast/daily?mode=json&units=metric&cnt=7"
        private val COMPLETE_URL = "$URL&APPID=$APP_ID&q="
    }

    fun getForecast(): ForecastResult {
        val resultJsonStr = URL(COMPLETE_URL + zipCode).readText()
        return Gson().fromJson(resultJsonStr, ForecastResult::class.java)
    }
}