package cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.singlefeed

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.InterceptTouchLinearLayout
import cn.dreamfruits.yaoguo.module.main.home.child.FeedViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.WaterfallAdapter
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.GetFeedListByCvIdBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.LaudFeedBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.UnLudFeedBeanState
import cn.dreamfruits.yaoguo.module.main.stroll.StrollViewModel
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @Author qiwangi
 * @Date 2023/6/13
 * @TIME 13:16
 */
class SingleFeedActivity : BaseActivity() {
    override fun initData() {}
    override fun layoutResId(): Int = R.layout.activity_single_feed

    private var touchEvent: MotionEvent? = null
    override fun initView() {

        //沉浸状态栏重叠问题
       findViewById<View>(cn.dreamfruits.yaoguo.R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BarUtils.getStatusBarHeight()
            )
        //用做根布局触摸事件
        val myLinearLayout = findViewById<InterceptTouchLinearLayout>(R.id.ll_root)
        myLinearLayout.mOnInterceptTouchEventListener = object :
            InterceptTouchLinearLayout.OnInterceptTouchEventListener {
            override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
                // 拦截与否只与返回值有关，返回false表示不拦截，否则拦截此次事件。
                when (ev?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 获取点击事件
                        touchEvent = ev
                    }
                    MotionEvent.ACTION_MOVE -> {}
                }
                return false
            }
        }


        top()
        setInit()
    }

    private fun top() {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }

    //瀑布流
    private var mRecyclerview: RecyclerView? = null
    private val feedViewModel by viewModels<FeedViewModel>()
    private val strollViewModel by viewModels<StrollViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private var mStaggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var mAdapter: WaterfallAdapter? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载
    private var isNoMoreData = false

    private var targetId:Long= 0L

    //初始化控件
    private fun setInit() {
        targetId= intent.getLongExtra("targetId",0L)//targetID

        mRecyclerview =findViewById(R.id.recyclerView)
        refreshLayout = findViewById(R.id.refreshLayout)
        initData2()//初始化
        setSmartRefresh()//初始化刷新逻辑
        refreshLayout!!.autoRefresh()
    }

    private fun initData2() {
        //适配器
        mStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mStaggeredGridLayoutManager!!.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//1.防止item乱跳错位
        mRecyclerview!!.layoutManager = mStaggeredGridLayoutManager
        //mRecyclerview!!.itemAnimator = null
        //2.防止第一页出现空白 3.使用notifyItemRangeChanged
        mRecyclerview!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mStaggeredGridLayoutManager!!.invalidateSpanAssignments()
            }
        })
        mAdapter = WaterfallAdapter(this, 20.0f, Singleton.mFashionWearList!!)
        mRecyclerview!!.adapter = mAdapter

        //回调
        mAdapter!!.setWaterfallAdapterListener(object : WaterfallAdapter.WaterfallAdapterInterface {
            override fun onCallBack(position: Int, id: Long) {
                showPopupWindow(position, id)//内容反馈弹窗
            }

            override fun onPreload(position: Int) {
                if (!isNoMoreData) {//没有更多了 就不用触发
                    refreshState = 2//设置为预加载
                    // TODO: 请求有问题
                    strollViewModel.getFeedListByCvId(
                        targetId,//targetID
                        10,
                        null)
                }
            }

            override fun onLaud(isLaud: Boolean, id: Long) {
                if (isLaud) {
                    commonViewModel.getLaudFeed(id)
                } else {
                    commonViewModel.getUnLudFeed(id)
                }
            }

            override fun onVideo() {
                Singleton.videoTag=6
            }
        })
        /**
         * 点赞 请求结果
         */
        commonViewModel.laudFeedBeanState.observe(this) {
            when (it) {
                is LaudFeedBeanState.Success -> {

                }
                is LaudFeedBeanState.Fail -> {
                    //1.是否有断网提示
                   Singleton.isNetConnectedToast(this)

                }
            }
        }
        /**
         * 取消点赞 请求结果
         */
        commonViewModel.unLudFeedBeanState.observe(this) {
            when (it) {
                is UnLudFeedBeanState.Success -> {

                }
                is UnLudFeedBeanState.Fail -> {
                    //1.是否有断网提示
                   Singleton.isNetConnectedToast(this)
                }
            }
        }
    }
    /**
     * 上拉刷新，下拉加载模块
     */
    private fun setSmartRefresh() {
        refreshLayout?.setRefreshHeader(MRefreshHeader2(this))//自定义头部动画
        refreshLayout?.setRefreshFooter(ClassicsFooter(this))

        //请求结果
        strollViewModel.getFeedListByCvIdBeanState.observe(this) {
            when (it) {
                is GetFeedListByCvIdBeanState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            Singleton.mFashionWearList!!.clear()//清空集合
                            Singleton.mFashionWearList =
                                Singleton.changeList(strollViewModel.mGetFeedListByCvIdBean!!)//加载请求后的数据
                            mAdapter!!.setData(Singleton.mFashionWearList!!, true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }
                        1, 2 -> {//加载 预加载
                            mAdapter!!.setData(Singleton.changeList(strollViewModel.mGetFeedListByCvIdBean)!!, false)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                        }
                    }
                }
                is GetFeedListByCvIdBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.各自的失败处理逻辑
                    when (refreshState) {
                        0 -> {//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 3)
                        }
                        1, 2 -> {//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 4)
                        }
                    }
                }
            }
        }

        /**
         * 下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            refreshState = 0//设置为刷新
            // TODO: 请求有问题
            strollViewModel.getFeedListByCvId(
                targetId,//targetID
                10,
                null)
        }
        /**
         * 上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            if (Singleton.mFashionWearList!!.size != 0){
                refreshState = 1//设置为加载
                getLoadMoreLogic(strollViewModel.mGetFeedListByCvIdBean!!)
            }
        }
    }

    private fun getLoadMoreLogic(bean: WaterfallFeedBean?) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean == null) {
            // TODO: 请求有问题
            strollViewModel.getFeedListByCvId(
                targetId,//targetID
                10,
                null)
        } else {
            when (bean.hasNext) {
                0 -> {//没有更多数据了
                    Singleton.setRefreshResult(refreshLayout!!, 5)
                    isNoMoreData = true
                }
                1 -> {//有更多数据
                    strollViewModel.getFeedListByCvId(
                        targetId,//targetID
                        10,
                        bean.lastTime)
                }
            }
        }
    }


    /**
     * ==================================================》内容反馈模块
     * 1.用自定义根布局 并实现拦截
     * 2.item内部长按监听回调
     * 3.pop弹窗位置确认
     * 4.pop上的点击事件 todo
     */


    private var popupWindow: PopupWindow? = null
    var scaleAnimation: ScaleAnimation? = null
    private fun showPopupWindow(position: Int, feedId: Long) {
        //将PopupWindow显示在window的decorView下，位置为按下的event的rawX和rawY的值。
        val inflate = View.inflate(this, R.layout.home_dialog_content_feedback, null)
        val but1: Button = inflate.findViewById(R.id.home_content_feedback_but1)
        val but2: Button = inflate.findViewById(R.id.home_content_feedback_but2)
        val but3: Button = inflate.findViewById(R.id.home_content_feedback_but3)
        val but4: Button = inflate.findViewById(R.id.home_content_feedback_but4)
        val but5: Button = inflate.findViewById(R.id.home_content_feedback_but5)
        val but6: Button = inflate.findViewById(R.id.home_content_feedback_but6)
        popupWindow = PopupWindow(
            inflate,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //使用系统动画
//        popupWindow!!.animationStyle= org.koin.android.R.style.Animation_AppCompat_DropDownUp
        //不感兴趣
        setOnclick(but1, 1, position, feedId)//不喜欢该内容
        setOnclick(but2, 2, position, feedId)//不喜欢该作者
        //内容反馈
        setOnclick(but3, 3, position, feedId)//广告
        setOnclick(but4, 4, position, feedId)//内容重复
        setOnclick(but5, 5, position, feedId)//内容不适
        setOnclick(but6, 6, position, feedId)//色情低俗

        if (popupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            popupWindow!!.dismiss()
        } else {
            popupWindow!!.isOutsideTouchable = true
            popupWindow!!.isTouchable = true
            popupWindow!!.isFocusable = true
            popupwindowLogic(inflate, popupWindow)//关键code 弹窗逻辑
            Singleton.bgAlpha(this, 0.5f) //设置透明度0.5
            popupWindow?.setOnDismissListener {
                Singleton.bgAlpha(this, 1f) //恢复透明度
            }
        }
    }

    /**
     * 内容反馈 子按钮监听
     */
    private fun setOnclick(but: Button, tag: Int, position: Int, feedId: Long) {
        but.setOnClickListener {
            //1.删除该item
            mAdapter!!.delItem(position)
            //Log.e("zqr=++", "position$position")
            //2.调用接口
            //todo 调用接口
            when (tag) {
                1 -> {
                    feedViewModel.getUninterested(feedId, 1)
                }
                2 -> {
                    feedViewModel.getUninterested(feedId, 2)
                }
                3 -> {
                    feedViewModel.getFeedbackFeed(feedId, 1)
                }
                4 -> {
                    feedViewModel.getFeedbackFeed(feedId, 2)
                }
                5 -> {
                    feedViewModel.getFeedbackFeed(feedId, 3)
                }
                6 -> {
                    feedViewModel.getFeedbackFeed(feedId, 4)
                }
            }
            //3.关闭弹窗
            if (popupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                popupWindow!!.dismiss()
                scaleAnimation?.cancel()
            }
            //4.弹出吐司
            Singleton.centerToast(this, "已收到反馈，将为你优化结果")
        }
    }

    private fun popupwindowLogic(inflate: View, popupWindow: PopupWindow?) {
        var centerX = resources.displayMetrics.widthPixels / 2
        var centerY = resources.displayMetrics.heightPixels / 2

        var currentX = touchEvent?.rawX?.toInt() ?: 0
        var currentY = touchEvent?.rawY?.toInt() ?: 0

        var layoutLeftDown: View = inflate.findViewById(R.id.content_callback_left_down)
        layoutLeftDown.visibility = View.GONE
        var layoutLeftUp: View = inflate.findViewById(R.id.content_callback_left_up)
        layoutLeftUp.visibility = View.GONE
        var layoutRightDown: View = inflate.findViewById(R.id.content_callback_right_down)
        layoutRightDown.visibility = View.GONE
        var layoutRightUp: View = inflate.findViewById(R.id.content_callback_right_up)
        layoutRightUp.visibility = View.GONE

        if (currentX < centerX && currentY > centerY) {
            //左下弹出
            layoutLeftDown.visibility = View.VISIBLE
            popupWindow!!.showAtLocation(
                inflate,
                Gravity.NO_GRAVITY,
                currentX,
                currentY - Singleton.dp2px(this, 260.0f)
            )
            popAnim(inflate, 0.2f, 0.8f)
        } else if (currentX < centerX && currentY < centerY) {
            //左上弹出
            layoutLeftUp.visibility = View.VISIBLE
            popupWindow!!.showAtLocation(inflate, Gravity.NO_GRAVITY, currentX, currentY)
            popAnim(inflate, 0.2f, 0.2f)
        } else if (currentX > centerX && currentY < centerY) {
            //右上弹出
            layoutRightUp.visibility = View.VISIBLE
            popupWindow!!.showAtLocation(inflate, Gravity.NO_GRAVITY, currentX, currentY)
            popAnim(inflate, 0.8f, 0.2f)
        } else if (currentX > centerX && currentY > centerY) {
            //右下弹出
            layoutRightDown.visibility = View.VISIBLE
            popupWindow!!.showAtLocation(
                inflate,
                Gravity.NO_GRAVITY,
                currentX,
                currentY - Singleton.dp2px(this, 260.0f)
            )
            popAnim(inflate, 0.8f, 0.8f)
        } else {
            popupWindow!!.showAtLocation(inflate, Gravity.NO_GRAVITY, currentX, currentY)
            popAnim(inflate, 0.2f, 0.2f)
        }
    }

    /**
     * 分别从左上缩放 0.0f ,0.0f
     * 分别从右上缩放 1.0f ,0.0f
     * 分别从左下缩放 0.0f ,1.0f
     * 分别从右下缩放 1.0f ,1.0f
     */
    fun popAnim(inflate: View, x: Float, y: Float) {
        scaleAnimation = ScaleAnimation(
            0.6f,
            1f,
            0.6f,
            1f,
            Animation.RELATIVE_TO_SELF,//相对于自身
            x,
            Animation.RELATIVE_TO_SELF,
            y
        )
        scaleAnimation!!.duration = 200
        inflate.startAnimation(scaleAnimation)
    }

    override fun onPause() {
        super.onPause()
        if (popupWindow != null && popupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            popupWindow!!.dismiss()
        }

    }


}