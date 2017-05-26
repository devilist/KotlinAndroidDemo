package com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.errang.app.kotlinandroiddemo.utils.LogUtil


/**
 * 具有viewpager效果的recyclerview
 * Created by zengpu on 2017/5/26.
 */

class RecyclerViewPager : RecyclerView, View.OnTouchListener, GestureDetector.OnGestureListener {

    /**
     * 触发翻页动作的最小滑动距离
     */
    private var mFlingSlop = 0f

    /**
     * 触发翻页动作的最小滑动距离的比例因子
     */
    private var mFlingFactor = 0.5f

    /**
     * 触发翻页动作的最小滑动速度
     */
    private var mVelocitySlop = 0f

    /**
     * 当前recyclerView第一个可见的item的位置
     */
    private var currentPosition = 0

    /**
     * 滑动事件结束后，选中的item的位置
     */
    private var mSelectedPosition = 0

    /**
     * recyclerView的item个数
     */
    private var mItemCount = 0

    /**
     * touch操作按下的位置 x
     */
    private var mTouchDownX = 0f

    /**
     * touch操作抬起的位置 x
     */
    private var mTouchUpX = 0f

    /**
     * 滑动过程中是否触发了onFling事件
     */
    private var is_trigger_onFling = false

    var onPageSelectListener: RecyclerViewPager.OnPageSelectListener? = null
    private var gestureDetector: GestureDetector? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {

        val mScreenWidth = context.resources.displayMetrics.widthPixels
        mFlingFactor = 0.55f
        mFlingSlop = mScreenWidth * mFlingFactor
        mVelocitySlop = 2000f
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        gestureDetector = GestureDetector(context, this)

        this.setOnTouchListener(this)

        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    /*
                     * 页面停止滚动后，偶尔会出现目标页面没有完全滚入屏幕的情况，
                     * 通过获得当前item的偏移量来判断是否完全滚入
                     * 如果偏移量不为0，则需要用 smoothScrollBy()方法完成页面滚动
                     */
                    val mSelectedPageOffsetX = getScollOffsetX(mSelectedPosition)
                    if (mSelectedPageOffsetX != 0) {
                        smoothScrollBy(mSelectedPageOffsetX, 0)
                    }
                }

                onPageSelectListener?.onPageScrollStateChanged(newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onPageSelectListener?.onPageSelected(mSelectedPosition)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                LogUtil.e("RecyclerViewPager4", "dx : " + dx)
                onPageSelectListener?.onPageScrolled(mSelectedPosition, getScollOffsetX(mSelectedPosition).toFloat())
            }
        })
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {

        // 如果recyclerview的item里添加了onClick事件，则触摸事件会被onClick事件消费掉，
        // OnTouchLisenter监听就获取不到ACTION_DOWN时的触摸位置，因此在这里记录mTouchDownX
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchDownX = e.x
                is_trigger_onFling = false
                LogUtil.e("RecyclerViewPager2", "mTouchDownX : " + mTouchDownX)
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {

        currentPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        mSelectedPosition = currentPosition
        mItemCount = adapter.itemCount

        // 将触摸事件传递给GestureDetector
        gestureDetector?.onTouchEvent(event)

        /*
         * 手指滑动时，页面有两种翻页效果：
         * 1.如果不需要页面跟着一起滑动，只在手指抬起后进行翻页，则只需要处理onFling事件即可；在onTouch方法里直接返回true
         * 2.如果滑动时需要页面跟着一起滑动(像ViewPager一样)，则需要同时处理onFling和onScroll事件；
         *   onFling事件在OnGestureListener里处理；
         *   onScroll事件最终需要在onTouch里 ACTION_UP 触发后进行后续判断页面的滚动
         *
         * OnGestureListener监听里事件执行顺序有两种：
         *  onFling事件流： onDown —— onScroll —— onScroll... —— onFling
         *  onScroll事件流：onDown —— onScroll —— onScroll —— onScroll...
         * 当滑动速度比较快时，会进入第一种情况，最后执行onFling；
         * 当滑动速度比较快时，会进入第二种情况，这种情况不会进入到onFling里，最终会进入onTouch的ACTION_UP里
         */
        when (event.action) {
        // ACTION_DOWN 事件在onInterceptTouchEvent方法里记录
            MotionEvent.ACTION_UP ->
                if (!is_trigger_onFling) {
                    LogUtil.e("RecyclerViewPager2", "onTouch: ACTION_UP ")
                    LogUtil.e("RecyclerViewPager2", "is_trigger_onFling : " + is_trigger_onFling)
                    mTouchUpX = event.x - mTouchDownX
                    LogUtil.e("RecyclerViewPager2", "mTouchUpX : " + mTouchUpX)
                    if (mTouchUpX >= mFlingSlop) {
                        // 往右滑，position减小
                        mSelectedPosition = if (currentPosition == 0) 0 else currentPosition
                    } else if (mTouchUpX <= -mFlingSlop) {
                        // 往左滑动，position增大
                        mSelectedPosition = if (currentPosition == mItemCount - 1) mItemCount - 1 else currentPosition + 1
                    } else if (mTouchUpX < mFlingSlop && mTouchUpX > 0) {
                        // 往右滑动，但未达到阈值
                        if (currentPosition == 0 && getScollOffsetX(0) >= 0)
                        // 边界控制，如果当前已经停留在第一页
                            mSelectedPosition = 0
                        else
                            mSelectedPosition = currentPosition + 1
                    } else {
                        mSelectedPosition = currentPosition
                    }
                    smoothScrollToPosition(mSelectedPosition)
                }
        }
        return super.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean {
        LogUtil.e("RecyclerViewPager0", "onDown")
        is_trigger_onFling = false
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        LogUtil.e("RecyclerViewPager0", "onFling")
        is_trigger_onFling = true
        if ((null != e1) and (null != e2)) {
            val x_fling = e2!!.x - e1!!.x
            LogUtil.e("RecyclerViewPager0", "velocityX : " + velocityX)
            LogUtil.e("RecyclerViewPager0", "x_fling : " + x_fling)
            LogUtil.e("RecyclerViewPager0", "mFlingSlop : " + mFlingSlop)
            if ((x_fling >= mFlingSlop) or (velocityX >= mVelocitySlop)) {
                // 往右滑动，position减少
                mSelectedPosition = if (currentPosition == 0) 0 else currentPosition

            } else if ((x_fling <= -mFlingSlop) or (velocityX <= -mVelocitySlop)) {
                // 往左滑动，position增大
                mSelectedPosition = if (currentPosition == mItemCount - 1) mItemCount - 1 else currentPosition + 1

            } else {
                if (x_fling < mFlingSlop && x_fling > 0) {
                    // 往右滑动，未达到阈值
                    if (currentPosition == 0 && getScollOffsetX(0) >= 0)
                    // 边界控制，如果当前已经停留在第一页
                        mSelectedPosition = 0
                    else
                        mSelectedPosition = currentPosition + 1
                } else
                    mSelectedPosition = currentPosition
            }

        } else {
            if (velocityX >= mVelocitySlop) {
                // 往右滑动，position减少
                mSelectedPosition = if (currentPosition == 0) 0 else currentPosition
            } else if (velocityX <= -mVelocitySlop) {
                // 往左滑动，position增大
                mSelectedPosition = if (currentPosition == mItemCount - 1) mItemCount - 1 else currentPosition + 1
            } else if (velocityX < mVelocitySlop && velocityX >= 0) {
                // 往右滑动，未达到速度阈值
                if (currentPosition == 0 && getScollOffsetX(0) >= 0)
                // 边界控制，如果当前已经停留在第一页
                    mSelectedPosition = 0
                else
                    mSelectedPosition = currentPosition + 1
            } else
                mSelectedPosition = currentPosition
        }
        smoothScrollToPosition(mSelectedPosition)
        return true
    }

    override fun onShowPress(e: MotionEvent) {
        LogUtil.e("RecyclerViewPager00", "onShowPress")
        is_trigger_onFling = false
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        LogUtil.e("RecyclerViewPager00", "onSingleTapUp")
        is_trigger_onFling = false
        return true
    }


    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        LogUtil.e("RecyclerViewPager0", "onScroll")
        is_trigger_onFling = false
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        LogUtil.e("RecyclerViewPager0", "onLongPress")
    }

    /**
     * 获得当前页面相对于屏幕左侧边缘的偏移量

     * @param position 当前页面位置
     * *
     * @return 偏移量
     */
    private fun getScollOffsetX(position: Int): Int {

        val childView = layoutManager.findViewByPosition(position) ?: return 0
        return childView.left
    }


    /**
     * recyclerPager页面滚动监听
     */
    interface OnPageSelectListener {

        /**
         * 滚动的过程中被调用

         * @param position
         * *
         * @param positionOffset 当前第一个可见的item的左侧距离屏幕左边缘的距离
         */
        fun onPageScrolled(position: Int, positionOffset: Float)

        /**
         * 滚动事件结束后被调用

         * @param position
         */
        fun onPageSelected(position: Int)

        /**
         * 滚动状态变化时被调用

         * @param state
         */
        fun onPageScrollStateChanged(state: Int)

    }

    companion object {

        /**
         * The RecyclerViewPager is not currently scrolling.
         */
        val SCROLL_STATE_IDLE = 0

        /**
         * The RecyclerViewPager is currently being dragged by outside input such as user touch input.
         */
        val SCROLL_STATE_DRAGGING = 1

        /**
         * The RecyclerViewPager is currently animating to a final position while not under
         * outside control.
         */
        val SCROLL_STATE_SETTLING = 2
    }
}
