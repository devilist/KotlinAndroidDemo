package com.errang.app.kotlinandroiddemo.demo_2_recyclerviewpager.widget

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.*
import android.widget.AbsListView
import android.widget.Scroller
import com.errang.app.kotlinandroiddemo.R
import com.errang.app.kotlinandroiddemo.utils.LogUtil
import kotlinx.android.synthetic.main.rvp_pull_to_load_footer.view.*
import kotlinx.android.synthetic.main.rvp_pull_to_refresh_header.view.*

/**
 * 带刷新头的recyclerviewpager
 * Created by zengpu on 16/10/29.
 */

open class RefreshRecyclerViewPager : ViewGroup, AbsListView.OnScrollListener {

    /**
     * 帮助View滚动的辅助类Scroller
     */
    protected var mScroller: Scroller? = null

    protected var mRecyclerView: RecyclerView? = null

    /**
     * 右滑刷新刷新时显示的headerView
     */
    protected var mRefreshHeaderView: View? = null

    /**
     * 左滑加载更多时显示的footerView
     */
    protected var mLoadMoreFooterView: View? = null

    /**
     * 本次触摸滑动x坐标上的偏移量
     */
    protected var mXOffset: Int = 0

    /**
     * 手指滑动时的摩擦系数
     */
    private val mTouchScrollFrictionFactor = 0.35f

    /**
     * 触发刷新加载操作的最小距离
     */
    protected var mTouchSlop: Int = 0

    /**
     * 最初的滚动位置.第一次布局时滚动header的宽度的距离
     */
    protected var mInitScrollX = 0
    /**
     * 最后一次触摸事件的X轴坐标
     */
    protected var mLastX = 0

    /**
     * 当前状态
     */
    protected var mCurrentStatus = STATUS_IDLE

    /**
     * 是否滚到了最右侧。滚到最右侧后执行左滑加载更多
     */
    protected var isScrollToRight = false

    /**
     * 是否滚到了最左侧。滚到最左侧后执行右滑刷新
     */
    protected var isScrollToLeft = false

    /**
     * 屏幕宽度
     */
    private var mScreenWidth: Int = 0
    /**
     * Header宽度
     */
    private var mHeaderWidth: Int = 0
    /**
     * Footer宽度
     */
    private var mFooterWidth: Int = 0

    /**
     * 右滑刷新监听器
     */
    var onRefreshListener: OnRefreshListener? = null
    /**
     * 左滑加载更多监听器
     */
    var onLoadMoreListener: OnLoadMoreListener? = null

    /**
     * 是否需要右滑刷新功能
     */
    private var isCanRefresh = true

    /**
     * 是否需要左滑加载更多功能
     */
    var isCanLoadMore = true

    /**
     * 右滑刷新是否失败，用于处理失败后header的隐藏问题
     */
    private var isRefreshFailure = false

    /**
     * 左滑加载是否失败，用于处理失败后footer的隐藏问题
     */
    private var isLoadFailure = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {

        // 初始化Scroller对象
        mScroller = Scroller(context)
        // 获取屏幕高度
        mScreenWidth = context.resources.displayMetrics.widthPixels
        // header 的宽度为屏幕宽度的 1/3
        mHeaderWidth = mScreenWidth / 3
        mFooterWidth = mScreenWidth / 3
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

        // 初始化整个布局
        //        config = initHeaderAndFooter();
        initLayout(context)

    }

    /**
     * 初始化整个布局
     * @param context
     */
    private fun initLayout(context: Context) {
        /* 往布局里添加 headerView,mHeaderView = getChildAt(0)*/
        mRefreshHeaderView = LayoutInflater.from(context).inflate(R.layout.rvp_pull_to_refresh_header, this, false)
        mRefreshHeaderView!!.layoutParams = LayoutParams(mHeaderWidth, LayoutParams.MATCH_PARENT)
        mRefreshHeaderView!!.setPadding(mHeaderWidth / 2, 0, 0, 0)
        addView(mRefreshHeaderView)
        /* 初始化footerView，添加到布局里,mFooterView = getChildAt(1); */
        mLoadMoreFooterView = LayoutInflater.from(context).inflate(R.layout.rvp_pull_to_load_footer, this, false)
        mLoadMoreFooterView!!.layoutParams = LayoutParams(mFooterWidth, LayoutParams.MATCH_PARENT)
        mLoadMoreFooterView!!.setPadding(0, 0, mFooterWidth / 2, 0)
        addView(mLoadMoreFooterView)
    }

    /**
     * 丈量视图的宽、高。
     * 高度为用户设置的高度，
     * 宽度则为header, contentView，footer这三个子控件的高度和。
     * @param widthMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val height = MeasureSpec.getSize(heightMeasureSpec)
        val childCount = childCount
        var finalWidth = 0
        for (i in 0..childCount - 1) {
            val child = getChildAt(i)
            // pathMeasure
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            // 该view所需要的总宽度
            finalWidth += child.measuredWidth
        }
        setMeasuredDimension(finalWidth, height)
    }

    /**
     * 布局函数，将header, contentView,两个view从左到右布局。
     * 布局完成后通过Scroller滚动到header的右侧，
     * 即滚动距离为header的宽度 +本视图的paddingLeft，从而达到隐藏header的效果.
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        /* headview = getChildAt(0);
           footview = getChildAt(1);
           mRecyclerView = getChildAt(2);
           布局的时候要把 mRecyclerView 放中间; */
        val top = paddingTop
        var left = paddingLeft
        val child0 = getChildAt(0)
        val child1 = getChildAt(1)
        val child2 = getChildAt(2)
        child0.layout(left, top, left + child0.measuredWidth, child0.measuredHeight + top)
        left += child0.measuredWidth
        child2.layout(left, top, left + child2.measuredWidth, child2.measuredHeight + top)
        left += child2.measuredWidth
        child1.layout(left, top, left + child1.measuredWidth, child1.measuredHeight + top)
        // 为mRecyclerView添加滚动监听
        mRecyclerView = child2 as RecyclerView
        setRecyclerViewScrollListener()
        // 计算初始化滑动的x轴距离
        mInitScrollX = mRefreshHeaderView!!.measuredWidth + paddingLeft
        // 滑动到headerView宽度的位置, 从而达到隐藏headerView的效果
        LogUtil.d("RefreshRecyclerViewPager", "mInitScrollX is :" + mInitScrollX)
        // 要移动view到坐标点（100，100），那么偏移量就是(0，0)-(100，100）=（-100 ，-100）,
        // 就要执行view.scrollTo(-100,-100),达到这个效果。
        scrollTo(mInitScrollX, 0)

        // 显示或隐藏footer
        if (isRecyclerViewCompletelyShow())
            mLoadMoreFooterView!!.visibility = View.GONE
        else
            mLoadMoreFooterView!!.visibility = View.VISIBLE
    }


    /**
     * 为RecyclerView添加滚动监听
     */
    protected fun setRecyclerViewScrollListener() {
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

    }

    /**
     * 是否已经到了最左侧
     * 如果到达最左侧，用户继续滑动则拦截事件;
     * @return
     */
    protected fun isLeft(): Boolean {
        val lm = mRecyclerView!!.layoutManager as LinearLayoutManager
        return lm.findFirstCompletelyVisibleItemPosition() == 0 && scrollX <= mRefreshHeaderView!!.measuredWidth
    }

    /**
     * 是否已经到了最右侧
     * 如果到达最左侧，用户继续滑动则拦截事件;
     * @return
     */
    protected fun isRight(): Boolean {
        val lm = mRecyclerView!!.layoutManager as LinearLayoutManager
        return mRecyclerView != null && mRecyclerView!!.adapter != null
                && lm.findLastCompletelyVisibleItemPosition() == mRecyclerView!!.adapter.itemCount - 1
    }

    /**
     * mRecyclerView 是否充满整个屏幕
     * 如果没有充满整个屏幕，禁用上拉加载更多
     * @return
     */
    protected fun isRecyclerViewCompletelyShow(): Boolean {
        val lm = mRecyclerView!!.layoutManager as LinearLayoutManager
        return mRecyclerView != null && mRecyclerView!!.adapter != null
                && lm.findFirstCompletelyVisibleItemPosition() == 0
                && lm.findLastCompletelyVisibleItemPosition() == mRecyclerView!!.adapter.itemCount - 1
    }

    /**
     * 与Scroller合作,实现平滑滚动。在该方法中调用Scroller的computeScrollOffset来判断滚动是否结束。
     * 如果没有结束，那么滚动到相应的位置，并且调用postInvalidate方法重绘界面，
     * 从而再次进入到这个computeScroll流程，直到滚动结束。
     * view重绘时会调用此方法
     */

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            scrollTo(mScroller!!.currX, mScroller!!.currY)
            postInvalidate()
        }
    }

    /**
     * 在适当的时候拦截触摸事件，两种情况：
     *
     *
     * 1.当mContentView滑动到最左侧，并且是右滑时拦截触摸事件，
     * 2.当mContentView滑动到最右侧，并且是左滑时拦截触摸事件，
     * 其它情况不拦截，交给其childview 来处理。

     * @param ev
     * *
     * @return
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        val action = MotionEventCompat.getActionMasked(ev)
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            return false
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.rawX.toInt()
                if ((mCurrentStatus == STATUS_REFRESHING) or (mCurrentStatus == STATUS_LOADING))
                    return true
            }
            MotionEvent.ACTION_MOVE -> {
                mXOffset = ev.rawX.toInt() - mLastX
                LogUtil.d("RefreshRecyclerViewPager", "mXOffset is: " + mXOffset)
                LogUtil.d("RefreshRecyclerViewPager", "isRight() is: " + isRight())

                // 处理加载失败时，header和footer的隐藏问题
                if (isRefreshFailure && mXOffset < -2) {
                    mScroller!!.startScroll(scrollX, scrollY, mInitScrollX - scrollX, 0)
                    invalidate()
                    isRefreshFailure = false
                }

                if (isLoadFailure && mXOffset >= 2) {
                    mScroller!!.startScroll(scrollX, scrollY, mInitScrollX - scrollX, 0)
                    invalidate()
                    isLoadFailure = false
                }

                // 如果拉到了最左侧, 并且是右滑,则拦截触摸事件,从而转到onTouchEvent来处理右滑刷新事件
                if (isLeft() && mXOffset > 0) {
                    isScrollToLeft = true
                    isScrollToRight = false
                    // 如果RecyclerView没有完全占满屏幕，隐藏footer
                    when (isRecyclerViewCompletelyShow()) {
                        true -> mLoadMoreFooterView!!.visibility = View.GONE
                        false -> mLoadMoreFooterView!!.visibility = View.VISIBLE
                    }
                    return true
                }
                // 如果拉到了最右侧, 并且是左滑,则拦截触摸事件,从而转到onTouchEvent来处理左滑加载更多事件
                if (isRight() && mXOffset < 0) {
                    isScrollToLeft = false
                    LogUtil.d("RefreshRecyclerViewPager", "isRecyclerViewCompletelyShow() is: " + isRecyclerViewCompletelyShow())
                    // 如果RecyclerView没有完全占满屏幕，隐藏footer，并禁用上拉加载功能
                    when (isRecyclerViewCompletelyShow()) {
                        true -> {
                            mLoadMoreFooterView!!.visibility = View.GONE
                            isScrollToRight = false
                        }
                        false -> {
                            mLoadMoreFooterView!!.visibility = View.VISIBLE
                            isScrollToRight = true
                        }
                    }

                    // 是否需要左滑加载功能
                    if (isCanLoadMore) {
                        // 如果RecyclerView没有完全占满屏幕，隐藏footer，并禁用上拉加载功能
                        if (isRecyclerViewCompletelyShow()) {
                            mLoadMoreFooterView!!.visibility = View.GONE
                            isScrollToRight = false
                        } else {
                            mLoadMoreFooterView!!.visibility = View.VISIBLE
                            isScrollToRight = true
                        }
                    } else {
                        mLoadMoreFooterView!!.visibility = View.GONE
                        isScrollToRight = false
                    }
                    return true
                }
            }
        }
        return false
    }

    /**
     * 在这里处理触摸事件以达到右滑刷新或者左滑自动加载的问题
     * @param event
     *
     * @return
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val currentX = event.rawX.toInt()
                mXOffset = currentX - mLastX
                //当处于刷新状态时，不能继续右滑或左滑
                if ((mCurrentStatus == STATUS_REFRESHING && mXOffset >= 0)
                        or (mCurrentStatus == STATUS_LOADING && mXOffset <= 0)) {
                    // do nothing
                } else {
                    changeScrollX((mXOffset * mTouchScrollFrictionFactor).toInt())
                    mLastX = currentX
                }
            }
            MotionEvent.ACTION_UP -> {
                LogUtil.d("RefreshRecyclerViewPager", "mCurrentStatus is: " + mCurrentStatus)
                LogUtil.d("RefreshRecyclerViewPager", "isScrollToRight is: " + isScrollToRight)
                if (isScrollToLeft && mCurrentStatus != STATUS_LOADING && mCurrentStatus != STATUS_REFRESHING)
                    doRefresh()
                if (isScrollToRight && mCurrentStatus != STATUS_REFRESHING && mCurrentStatus != STATUS_LOADING)
                    doLoadMore()
            }
            else -> {
            }
        }
        return false
    }

    /**
     * 根据右滑刷新或左滑加载的距离改变header或footer状态
     * @param distance
     *
     * @return
     */
    protected fun changeScrollX(distance: Int) {
        pull_to_refresh_text.visibility = View.INVISIBLE
        pull_to_loading_text.visibility = View.INVISIBLE
        // 最大值为 scrollX(header 隐藏), 最小值为0 ( header 完全显示).
        //curX是当前X的偏移量，在右滑过程中curX从最大值mInitScrollX逐渐变为0.

        // 下拉刷新过程
        if (isScrollToLeft && mCurrentStatus != STATUS_LOADING) {
            var curX = scrollX
            LogUtil.d("RefreshRecyclerViewPager", "右滑刷新 curX is: " + curX)
            LogUtil.d("RefreshRecyclerViewPager", "右滑刷新 distance is: " + distance)
            // 右滑过程边界处理
            if (distance > 0 && curX - distance > paddingLeft) {
                scrollBy(-distance, 0)
            } else if (distance < 0 && curX - distance <= mInitScrollX) {
                // 左滑过程边界处理
                scrollBy(-distance, 0)
            }
            curX = scrollX
            val slop = mInitScrollX / 2
            // curX是当前X的偏移量，在右滑过程中curX从最大值mInitScrollX逐渐变为0.
            if (curX in 1..(slop - 1)) {
                mCurrentStatus = STATUS_RELEASE_TO_REFRESH
            } else if (curX > 0 && curX > slop) {
                mCurrentStatus = STATUS_PULL_TO_REFRESH
            }
        }

        // 左滑加载过程
        if (isScrollToRight && mCurrentStatus != STATUS_REFRESHING) {
            var curX = scrollX - mHeaderWidth
            //            LogUtil.d("RefreshRecyclerViewPager", "左滑加载 curX is: " + curX);
            //            LogUtil.d("RefreshRecyclerViewPager", "左滑加载 distance is: " + distance);
            // 右滑过程边界处理
            if (distance > 0 && curX - distance > 0) {
                scrollBy(-distance, 0)
            } else if (distance < 0 && curX - distance <= mFooterWidth) {
                // 左滑过程边界处理
                scrollBy(-distance, 0)
            }
            curX = scrollX - mHeaderWidth
            val slop = mInitScrollX / 2

            if (curX in 1..(slop - 1)) {
                mCurrentStatus = STATUS_PULL_TO_LOAD
            } else if (curX > 0 && curX > slop) {
                mCurrentStatus = STATUS_RELEASE_TO_LOAD
            }
        }
    }

    /**
     * 执行右滑刷新
     */
    protected fun doRefresh() {
        changeHeaderViewStaus()
        // 执行刷新操作
        if (mCurrentStatus == STATUS_REFRESHING && onRefreshListener != null) {
            onRefreshListener!!.onRefresh()
        }
    }

    /**
     * 执行左滑(自动)加载更多的操作
     */
    protected fun doLoadMore() {
        changeFooterViewStatus()
        if (mCurrentStatus == STATUS_LOADING && onLoadMoreListener != null) {
            onLoadMoreListener!!.onLoadMore()
        }
    }

    /**
     * 右滑刷新
     * 手指抬起时,根据用户右滑的宽度来判断是否是有效的右滑刷新操作。如果右滑的距离超过headerview宽度的
     * 1/2那么则认为是有效的右滑刷新操作，否则恢复原来的视图状态.
     */
    private fun changeHeaderViewStaus() {
        val curScrollX = scrollX
        // 超过1/2则认为是有效的下拉刷新, 否则还原
        if (curScrollX <= mInitScrollX / 2) {
            mScroller!!.startScroll(curScrollX, scrollY, mRefreshHeaderView!!.paddingLeft - curScrollX, 0)
            mCurrentStatus = STATUS_REFRESHING
            pull_to_refresh_progress.visibility = View.VISIBLE
        } else {
            mScroller!!.startScroll(curScrollX, scrollY, mInitScrollX - curScrollX, 0)
            mCurrentStatus = STATUS_IDLE
        }
        invalidate()
    }

    /**
     * 左滑加载
     * 手指抬起时,根据用户左滑的宽度来判断是否是有效的左滑刷新操作。如果左滑的距离超过footerview宽度的
     * 1/2那么则认为是有效的左滑加载操作，否则恢复原来的视图状态.
     */
    private fun changeFooterViewStatus() {
        val curScrollX = scrollX
        // 超过1/2则认为是有效的下拉刷新, 否则还原
        if (curScrollX >= mHeaderWidth + mFooterWidth / 2) {
            mScroller!!.startScroll(curScrollX, scrollY,
                    mHeaderWidth + mLoadMoreFooterView!!.paddingRight - curScrollX, 0)
            mCurrentStatus = STATUS_LOADING
            pull_to_loading_progress.visibility = View.VISIBLE
        } else {
            mScroller!!.startScroll(curScrollX, scrollY, mHeaderWidth - curScrollX, 0)
            mCurrentStatus = STATUS_IDLE
            pull_to_loading_progress.visibility = View.GONE
        }
        invalidate()
    }

    /**
     * 刷新结束，恢复状态
     */
    fun refreshComplete() {
        mScroller!!.startScroll(scrollX, scrollY, mInitScrollX - scrollX, 0)
        mCurrentStatus = STATUS_IDLE
        invalidate()
        isRefreshFailure = false
        pull_to_refresh_progress.visibility = View.GONE
        pull_to_refresh_text.visibility = View.INVISIBLE
        mRefreshHeaderView!!.setOnClickListener(null)
    }

    /**
     * 加载结束，恢复状态
     */
    fun loadMoreCompelte() {
        mScroller!!.startScroll(scrollX, scrollY, mInitScrollX - scrollX, 0)
        mCurrentStatus = STATUS_IDLE
        invalidate()
        isLoadFailure = false
        pull_to_loading_progress.visibility = View.GONE
        pull_to_loading_text.visibility = View.INVISIBLE
        mLoadMoreFooterView!!.setOnClickListener(null)
    }

    /**
     * 右滑刷新或左滑加载没有更多数据
     */
    fun refreshAndLoadNoMore() {

        if (mCurrentStatus == STATUS_REFRESHING) {
            pull_to_refresh_progress.visibility = View.INVISIBLE
            pull_to_refresh_text.visibility = View.VISIBLE
            pull_to_refresh_text.setBackgroundResource(R.drawable.no_more)
            isRefreshFailure = false
            mRefreshHeaderView!!.setOnClickListener(null)

        } else if (mCurrentStatus == STATUS_LOADING) {
            pull_to_loading_progress.visibility = View.INVISIBLE
            pull_to_loading_text.visibility = View.VISIBLE
            pull_to_loading_text.setBackgroundResource(R.drawable.no_more)
            isLoadFailure = false
            mLoadMoreFooterView!!.setOnClickListener(null)
        }

        postDelayed({
            // 隐藏header
            mScroller!!.startScroll(scrollX, scrollY, mInitScrollX - scrollX, 0)
            invalidate()
            mCurrentStatus = STATUS_IDLE
        }, 1000)

    }

    /**
     * 下拉刷新上拉加载失败
     */
    fun refreshAndLoadFailure() {

        if (mCurrentStatus == STATUS_REFRESHING) {

            pull_to_refresh_text.visibility = View.VISIBLE
            pull_to_refresh_text.setBackgroundResource(R.drawable.load_failure)
            pull_to_refresh_progress.visibility = View.INVISIBLE
            isRefreshFailure = true

        } else if (mCurrentStatus == STATUS_LOADING) {

            pull_to_loading_text.visibility = View.VISIBLE
            pull_to_loading_text.setBackgroundResource(R.drawable.load_failure)
            pull_to_loading_progress.visibility = View.INVISIBLE
            isLoadFailure = true
        }

        mCurrentStatus = STATUS_IDLE

        mRefreshHeaderView!!.setOnClickListener(loadFailureLisenter)
        mLoadMoreFooterView!!.setOnClickListener(loadFailureLisenter)
    }

    /**
     * 加载失败时点击重新加载
     */
    private val loadFailureLisenter = View.OnClickListener { v ->
        when (v.id) {
            R.id.rl_header -> refreshing()
            R.id.rl_footer -> loading()
        }
    }

    /**
     * 手动设置刷新
     */
    fun refreshing() {

        scrollTo(mInitScrollX / 2, 0)
        mCurrentStatus = STATUS_REFRESHING
        pull_to_refresh_progress.visibility = View.VISIBLE
        pull_to_refresh_text.visibility = View.INVISIBLE

        doRefresh()
    }

    /**
     * 手动上拉加载
     */
    private fun loading() {

        scrollTo(mInitScrollX + mFooterWidth / 2, 0)
        mCurrentStatus = STATUS_LOADING
        pull_to_loading_progress.visibility = View.VISIBLE
        pull_to_loading_text.visibility = View.INVISIBLE

        doLoadMore()
    }

    /**
     * 当前是否处于加载状态
     * @return
     */
    fun isLoading(): Boolean = mCurrentStatus == STATUS_LOADING

    /**
     * 当前是否处于刷新状态
     * @return
     */
    fun isRefreshing(): Boolean = mCurrentStatus == STATUS_REFRESHING


    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {

    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

    }

    interface OnRefreshListener {
        fun onRefresh()
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    companion object {

        /**
         * 空闲状态
         */
        val STATUS_IDLE = 0

        /**
         * 右滑状态, 还没有到达可刷新的状态
         */
        val STATUS_PULL_TO_REFRESH = 1

        /**
         * 右滑状态，达到可刷新状态
         */
        val STATUS_RELEASE_TO_REFRESH = 2
        /**
         * 刷新中
         */
        val STATUS_REFRESHING = 3

        /**
         * 左滑状态, 还没有到达可加载更多的状态
         */
        val STATUS_PULL_TO_LOAD = 4

        /**
         * 左滑状态，达到可加载更多状态
         */
        val STATUS_RELEASE_TO_LOAD = 5

        /**
         * 加载更多中
         */
        val STATUS_LOADING = 6
    }

}
