package com.errang.app.kotlinandroiddemo

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.errang.app.kotlinandroiddemo.extensions.ctx
import kotlinx.android.synthetic.main.item_main.view.*

/**
 * Created by zengpu on 2017/5/23.
 */
class MainListAdapter(var list: List<Array<String>>, val onItemClick: (v: View, position: Int, className: String) -> Unit)
    : RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    class ViewHolder(itemLayout: View) : RecyclerView.ViewHolder(itemLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.ctx).inflate(R.layout.item_main, parent, false))
    }

    override fun onBindViewHolder(holder: MainListAdapter.ViewHolder, position: Int) {
        Log.e("MainListAdapter", "item " + list[position][1])
        holder.itemView.tv_item.text = list[position][0]
        holder.itemView.setOnClickListener {
            onItemClick(holder.itemView, position, list[position][1])
        }
    }

    override fun getItemCount(): Int = list.size

}