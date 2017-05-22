package com.errang.app.kotlinandroiddemo.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.errang.app.kotlinandroiddemo.model.ForecastResult
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.text.DateFormat
import java.util.*

/**
 * Created by zengpu on 2017/5/22.
 */
class ForecastListAdapter(var items: ForecastResult)
    : RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastListAdapter.ViewHolder {
        return ViewHolder(TextView(parent.context))
    }

    override fun onBindViewHolder(holder: ForecastListAdapter.ViewHolder, position: Int) {
        val forecast = items.list[position]
        with(forecast) {
            holder.textView.text =
                    "${DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(forecast.dt * 1000)} " +
                            "- ${forecast.weather[0].description} " +
                            "- ${forecast.temp.max.toInt()}/${forecast.temp.min.toInt()}"
        }
        holder.textView.onClick { }
    }

    override fun getItemCount(): Int = items.list.size

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }
}