package com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.errang.app.kotlinandroiddemo.BaseActivity
import com.errang.app.kotlinandroiddemo.R
import com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.adapter.RVPAdapter
import com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.adapter.RVPAdapter.OnItemClickListener
import com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.model.AppInfo
import com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.widget.RecyclerViewPager
import com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.widget.RefreshRecyclerViewPager
import com.errang.app.kotlinandroiddemo.utils.LogUtil
import kotlinx.android.synthetic.main.rvp_activity.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by zengpu on 2016/10/28.
 */
class RecyclerViewPagerActivity : BaseActivity(),
        RecyclerViewPager.OnPageSelectListener, RefreshRecyclerViewPager.OnRefreshListener,
        RefreshRecyclerViewPager.OnLoadMoreListener, OnItemClickListener {

    private val bgColorList: MutableList<String> = ArrayList()

    companion object {
        // 翻译java的starter写法
        fun starter(context: Context, from: String) {
            val intent = Intent(context, RecyclerViewPagerActivity::class.java)
            intent.putExtra("from", from)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rvp_activity)
        supportActionBar?.hide()
        Toast.makeText(this, "from " + intent.getStringExtra("from"), Toast.LENGTH_SHORT).show()
        initView()
    }

    private fun initView() {

        tl_toolbar.text = "请等待..."
        rrvp_pager.visibility = View.INVISIBLE
        doAsync {
            val appInfolist = initData()
            uiThread {
                tl_toolbar.text = "我的应用"
                rrvp_pager.visibility = View.VISIBLE
                rrvp_pager.onRefreshListener = this@RecyclerViewPagerActivity
                rrvp_pager.onLoadMoreListener = this@RecyclerViewPagerActivity
                rvp_list.adapter = RVPAdapter(appInfolist)
                (rvp_list.adapter as RVPAdapter).onItemClickListener = this@RecyclerViewPagerActivity
                rvp_list.onPageSelectListener = this@RecyclerViewPagerActivity
            }
        }
    }

    override fun invoke(view: View, appName: String, position: Int) {
        Toast.makeText(this, appName, Toast.LENGTH_SHORT).show()
    }

    private var textFlag = 0

    override fun onRefresh() {
        rrvp_pager.postDelayed({
            // 加载失败
            if (textFlag == 0) {
                rrvp_pager.refreshAndLoadFailure()
                textFlag = 1
            } else if (textFlag == 1) {
                // 加载成功
                // 更新数据
                rrvp_pager.refreshComplete()
                textFlag = 2
            } else if (textFlag == 2) {
                // 没有更多数据
                rrvp_pager.refreshAndLoadNoMore()
                textFlag = 0
            }
        }, 1500)

    }

    override fun onLoadMore() {
        rrvp_pager.postDelayed({
            if (textFlag == 0) {
                rrvp_pager.refreshAndLoadFailure()
                textFlag = 1
            } else if (textFlag == 1) {
                // 更新数据
                // 更新完后调用该方法结束刷新
                rrvp_pager.loadMoreCompelte()
                textFlag = 2
            } else if (textFlag == 2) {
                rrvp_pager.refreshAndLoadNoMore()
                textFlag = 0
            }
        }, 1500)

    }

    override fun onPageScrolled(position: Int, positionOffset: Float) {

    }

    override fun onPageSelected(position: Int) {
        LogUtil.e("RecyclerViewPagerActivity", "position : " + position)
        rl_root.setBackgroundColor(Color.parseColor(bgColorList[position % 10]))
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    private fun initData(): MutableList<AppInfo> {
        val appInfolist: MutableList<AppInfo> = ArrayList()
        val packages = packageManager.getInstalledPackages(0)
        for (i in packages.indices) {
            val packageInfo = packages[i]
            val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            val appIcon = packageInfo.applicationInfo.loadIcon(packageManager)
            val versionCode = packageInfo.versionCode
            val versionName = packageInfo.versionName
            val packageName = packageInfo.packageName

            val appInfo = AppInfo(appName, appIcon, versionCode, versionName, packageName)

            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                appInfolist.add(appInfo)
            }
        }
        bgColorList.add("#448aff")
        bgColorList.add("#00bcd4")
        bgColorList.add("#009688")
        bgColorList.add("#4caf50")
        bgColorList.add("#8bc34a")
        bgColorList.add("#cddc39")
        bgColorList.add("#ffeb3b")
        bgColorList.add("#ff9800")
        bgColorList.add("#ff5722")
        bgColorList.add("#9e9e9e")

        return appInfolist
    }

}
