package com.errang.app.kotlinandroiddemo.demo_0_forecast

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.errang.app.kotlinandroiddemo.BaseActivity
import com.errang.app.kotlinandroiddemo.R
import com.errang.app.kotlinandroiddemo.demo_0_forecast.API.RequestForecastCommand
import com.errang.app.kotlinandroiddemo.demo_0_forecast.adapter.ForecastListAdapter
import com.errang.app.kotlinandroiddemo.demo_0_forecast.model.Forecast
import com.errang.app.kotlinandroiddemo.demo_1_db.dao.ForecastDao
import com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.RecyclerViewPagerActivity
import com.errang.app.kotlinandroiddemo.extensions.SpDelegateExt
import kotlinx.android.synthetic.main.activity_demo_0_forecast.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
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

    private lateinit var adapter1: ForecastListAdapter


    // 用lazy委托的方式存储到sp
    private var zipCode: Long by SpDelegateExt.preference(this, "zip_code", 94043L)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_0_forecast)

        initView()
    }

    fun initView() {
        forecast_list.layoutManager = LinearLayoutManager(this)
        adapter = ForecastListAdapter()
        adapter1 = ForecastListAdapter()
        adapter.listener = this
        forecast_list.adapter = adapter

        adapter1.listener = object : ForecastListAdapter.OnItemClickListener {

            override fun onItemClick(v: View, position: Int, forecast: Forecast) {
                RecyclerViewPagerActivity.starter(this@ForecastActivity, TAG)
            }
        }

        doAsync {
            val result = RequestForecastCommand("94043").execute()
            Log.d("Preference", "==================temp " + result.temp)
            uiThread {
                adapter.setNewData(result.list.asReversed())
            }
            //存数据库
            Log.d("Preference", "==================")
            zipCode
            Log.d("Preference", "==================")
            zipCode = 94043L
            val dao = ForecastDao()
            dao.saveForecastToLocalDB(result)
        }
    }

    override fun onItemClick(v: View, position: Int, forecast: Forecast) {
        toast(forecast.toString())
        // kotlin写法
        startActivity<RecyclerViewPagerActivity>()
        // java写法
//        RecyclerViewPagerActivity.starter(this, TAG)
    }

    override fun onClick(v: View?) {
        when (v) {
        }
    }
}
