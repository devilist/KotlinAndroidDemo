package com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.errang.app.kotlinandroiddemo.R
import com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.model.AppInfo
import com.errang.app.kotlinandroiddemo.extensions.ctx
import kotlinx.android.synthetic.main.rvp_activity_item.view.*


/**
 * Created by zengpu on 2016/10/28.
 */

class RVPAdapter(val appInfolist: MutableList<AppInfo>, val layoutRes: Int = R.layout.rvp_activity_item)
    : RecyclerView.Adapter<RVPAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVPAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.ctx).inflate(layoutRes, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RVPAdapter.ViewHolder, position: Int) {
        val (appName, appIcon, _, versionName, packageName) = appInfolist[position]
        holder.itemView.tv_app_name.text = appName
        holder.itemView.iv_app_icon.setImageDrawable(appIcon)
        holder.itemView.tv_app_version_name.text = "v$versionName"
        holder.itemView.tv_app_package_name.text = packageName

        holder.itemView.cv_view.setOnClickListener {
            onItemClickListener?.invoke(holder.itemView.cv_view, appName ?: "", position)
        }
    }

    override fun getItemCount(): Int = appInfolist.size

    interface OnItemClickListener {
        operator fun invoke(view: View, appName: String, position: Int)
    }
}

