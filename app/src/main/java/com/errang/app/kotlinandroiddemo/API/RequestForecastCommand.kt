package com.errang.app.kotlinandroiddemo.API

import com.errang.app.kotlinandroiddemo.model.ForecastResult

/**
 * Created by tao on 2017/5/22.
 */
class RequestForecastCommand(val zipCode: String) : RequestCommand<ForecastResult> {

    override fun execute(): ForecastResult {
        return SimpleRequest(zipCode).getForecast()
    }
}