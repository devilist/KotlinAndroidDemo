package com.errang.app.kotlinandroiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.errang.app.kotlinandroiddemo.demo_0_forecast.ForecastActivity
import com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.RecyclerViewPagerActivity
import kotlinx.android.synthetic.main.activity_main_list.*

class MainActivity : BaseActivity() {

    // 静态常量（伴随对象）
    companion object {
        val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        initView()
    }

    fun initView() {
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = MainListAdapter(createData()) {
            _, _, className ->
            try {
                val activityClazz = Class.forName(className)
                val intent = Intent(this, activityClazz)
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    fun createData(): List<Array<String>> {
        val list: MutableList<Array<String>> = ArrayList()
        list.add(arrayOf("demo_0： 天气预报", ForecastActivity::class.java.name))
        list.add(arrayOf("demo_2： RecyclerViewPager", RecyclerViewPagerActivity::class.java.name))
        return list
    }

}
