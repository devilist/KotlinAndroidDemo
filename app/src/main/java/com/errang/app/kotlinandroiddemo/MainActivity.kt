package com.errang.app.kotlinandroiddemo

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.errang.app.kotlinandroiddemo.API.RequestForecastCommand
import com.errang.app.kotlinandroiddemo.adapter.ForecastListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : BaseActivity(),
        View.OnClickListener {

    // 静态常量（伴随对象）
    companion object {
        val TAG: String = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    fun initView() {
        forecast_list.layoutManager = LinearLayoutManager(this)

        doAsync {
            val result = RequestForecastCommand("94043").execute()
            uiThread {
                forecast_list.adapter = ForecastListAdapter(result)
            }
        }

    }

    override fun onClick(v: View?) {
        when (v) {

        }
    }

}
