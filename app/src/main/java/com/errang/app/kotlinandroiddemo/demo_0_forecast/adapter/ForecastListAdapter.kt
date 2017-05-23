package com.errang.app.kotlinandroiddemo.demo_0_forecast.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.errang.app.kotlinandroiddemo.R
import com.errang.app.kotlinandroiddemo.demo_0_forecast.model.Forecast
import com.errang.app.kotlinandroiddemo.utils.ctx
import kotlinx.android.synthetic.main.item_demo_0_forecast.view.*
import java.text.DateFormat
import java.util.*

/**
 * Created by zengpu on 2017/5/22.
 */
class ForecastListAdapter : RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {

    var listener: OnItemClickListener? = null
    var items: MutableList<Forecast>? = null

    class ViewHolder(itemLayout: View) : RecyclerView.ViewHolder(itemLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastListAdapter.ViewHolder {
        val layout: View = LayoutInflater.from(parent.ctx).inflate(R.layout.item_demo_0_forecast, parent, false)
        return ViewHolder(layout)
    }

    fun setNewData(data: MutableList<Forecast>) {
        items = data
        notifyDataSetChanged()
    }

    fun addData(data: List<Forecast>) {
        items?.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ForecastListAdapter.ViewHolder, position: Int) {
        val forecast = items?.get(position)
        if (null != forecast)
            with(forecast) {
                Glide.with(holder.itemView.ctx)
                        .load("http://openweathermap.org/img/w/${forecast.weather[0].icon}.png")
                        .into(holder.itemView.icon)

                holder.itemView.date.text =
                        DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(forecast.dt * 1000)
                holder.itemView.description.text = forecast.weather[0].description
                holder.itemView.maxTemperature.text = forecast.temp.max.toInt().toString() + "°C"
                holder.itemView.minTemperature.text = forecast.temp.min.toInt().toString() + "°C"

                holder.itemView.setOnClickListener {
                    listener?.onItemClick(holder.itemView, position, forecast)
                }
            }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int, forecast: Forecast)
    }
}