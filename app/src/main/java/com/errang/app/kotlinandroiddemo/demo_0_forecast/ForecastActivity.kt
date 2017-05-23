package com.errang.app.kotlinandroiddemo.demo_0_forecast

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.errang.app.kotlinandroiddemo.BaseActivity
import com.errang.app.kotlinandroiddemo.R
import com.errang.app.kotlinandroiddemo.demo_0_forecast.API.RequestForecastCommand
import com.errang.app.kotlinandroiddemo.demo_0_forecast.adapter.ForecastListAdapter
import com.errang.app.kotlinandroiddemo.demo_0_forecast.model.Forecast
import kotlinx.android.synthetic.main.activity_demo_0_forecast.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 * 一个天气预报demo
 * Created by zengpu on 2017/5/23.
 */
class ForecastActivity : BaseActivity(),
        View.OnClickListener, ForecastListAdapter.OnItemClickListener {

    // 静态常量（伴随对象）
    companion object {
        val TAG: String = "ForecastActivity"
    }

    /* adapter 初始化有两种方式：
     *
     * 1: ? + null 使用可null类型并且赋值为null，直到我们有了真正想赋的值
     * var adapter: ForecastListAdapter? = null
     * 这种方式需要在每个地方不管是否是null都要去检查，例如 adapter?.listener = this
     *
     * 2: 使用 notNull委托
     * 它会含有一个可null的变量并会在我们设置这个属性的时候分配一个真实的值。
     * 如果这个值在被获取之前没有被分配，它就会抛出一个异常
     *
     */
    private var adapter: ForecastListAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_0_forecast)

        initView()
    }

    fun initView() {
        forecast_list.layoutManager = LinearLayoutManager(this)
        adapter = ForecastListAdapter()
        adapter.listener = this
        forecast_list.adapter = adapter

        doAsync {
            val result = RequestForecastCommand("94043").execute()
            uiThread {
                adapter.setNewData(result.list.asReversed())
            }
        }
    }

    override fun onItemClick(v: View, position: Int, forecast: Forecast) {
        toast(forecast.toString())
    }

    override fun onClick(v: View?) {
        when (v) {
        }
    }
}
