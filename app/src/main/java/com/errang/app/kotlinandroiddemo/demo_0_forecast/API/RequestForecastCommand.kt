package com.errang.app.kotlinandroiddemo.demo_0_forecast.API

import com.errang.app.kotlinandroiddemo.demo_0_forecast.model.ForecastResult

/**
 * Created by tao on 2017/5/22.
 */
class RequestForecastCommand(private val zipCode: String) : RequestCommand<ForecastResult> {

    override fun execute(): ForecastResult {
        return SimpleRequest(zipCode).getForecast()
    }
}