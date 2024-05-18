package cn.dreamfruits.yaoguo.module.main.home.labeldetails

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.InterceptTouchLinearLayout
import cn.dreamfruits.yaoguo.module.main.home.MaxRecyclerView
import cn.dreamfruits.yaoguo.module.main.home.child.FeedViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.WaterfallAdapter
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.message.bean.SharePostEntity
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.share.ShareAllPop
import cn.dreamfruits.yaoguo.module.share.ShareToActivity
import cn.dreamfruits.yaoguo.module.share.ShareViewModel
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.module.share.state.ShareShortUrlState
import cn.dreamfruits.yaoguo.module.share.state.ShareUseRecomendListState
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.changeList
import cn.dreamfruits.yaoguo.util.Singleton.mLabelDetailsList
import cn.dreamfruits.yaoguo.util.singleClick
import cn.dreamfruits.yaoguo.view.LoadingPop
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout


class LabelDetailsFragment : Fragment() {
    private var mRootView: View? = null
    private var labelJoinImg: ImageView? = null//头像
    private var mRecyclerView: MaxRecyclerView? = null
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private val feedViewModel by viewModels<FeedViewModel>()
    private var mAdapter: WaterfallAdapter? = null

    private var refreshLayout: SmartRefreshLayout? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载
    private var isNoMoreData = false
    private val requestNum = 10

    private val chartViewModel by viewModels<ChartViewModel>()
    private val shareViewModel by viewModels<ShareViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_water_fall_label_layout, container, false)
        mId = arguments!!.getString("id")//获取传递过来的id


        mRecyclerView = mRootView!!.findViewById(R.id.max_recyclerView1)
        labelJoinImg = mRootView!!.findViewById(R.id.label_join_img)
        /**头像*/
        Glide.with(requireActivity())
            .asBitmap()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .error(R.drawable.temp_icon)
            .load(Singleton.UserAvtarUrl)
            .into(labelJoinImg!!)


        initCommon()
        initViewLabel()
        setRefreshLabel()

        refreshLayout!!.autoRefresh()//3.自动刷新

        return mRootView
    }

    private fun initCommon() {//初始化一些公用的逻辑
        val mStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        mStaggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE//1.防止item乱跳错位
        mRecyclerView!!.layoutManager = mStaggeredGridLayoutManager
        mRecyclerView!!.isNestedScrollingEnabled = false
//        2.防止第一页出现空白 3.使用notifyItemRangeChanged
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mStaggeredGridLayoutManager.invalidateSpanAssignments()
            }
        })

        mAdapter = WaterfallAdapter(requireActivity(), 20f, mLabelDetailsList!!)//记得修改
        mRecyclerView!!.adapter = mAdapter

        /**
         * 2.设置回调
         */
        mAdapter!!.setWaterfallAdapterListener(object : WaterfallAdapter.WaterfallAdapterInterface {
            override fun onCallBack(position: Int, id: Long) {
                showPopupWindow(position, id)//内容反馈弹窗
            }

            override fun onPreload(position: Int) {
                if (!isNoMoreData) {
                    refreshState = 2//设置为预加载状态
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
                Singleton.videoTag = 4
            }
        })


        /**
         * 3.设置回调结果逻辑
         */
        /**
         * 点赞 请求结果
         */
        commonViewModel.laudFeedBeanState.observe(this) {
            when (it) {
                is LaudFeedBeanState.Success -> {

                }
                is LaudFeedBeanState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }

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
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }

                }
            }
        }
    }


    /**
     * ==================================================》话题详情页
     */
    //2.话题详情页单独的逻辑
    private val labelDetailsViewModel by viewModels<LabelDetailsViewModel>()//话题详情上面部分
    private var labelBack: ImageView? = null
    private var labelLabelTitle: TextView? = null
    private var labelWatchNum: TextView? = null
    private var labelCollectLayout: View? = null
    private var labelCollectImg: ImageView? = null


    private var mId: String? = "0"
    private fun initViewLabel() {
        labelBack = mRootView!!.findViewById(R.id.label_back)//返回
        labelBack!!.setOnClickListener {
            requireActivity().finish()
        }
        labelLabelTitle = mRootView!!.findViewById(R.id.label_label_title)//帖子标题
        labelWatchNum = mRootView!!.findViewById(R.id.label_watch_num)//帖子浏览规则
        labelCollectLayout = mRootView!!.findViewById(R.id.label_collect_layout)//收藏
        labelCollectImg = mRootView!!.findViewById(R.id.label_collect_img)//收藏图标


        //请求帖子详情结果返回
        labelDetailsViewModel.labelDetailsBeanState.observe(this) {
            when (it) {
                is LabelDetailsBeanState.Success -> {
                    //话题
                    labelLabelTitle!!.text = labelDetailsViewModel.mLabelDetailsBean!!.name

                    //浏览量
                    Singleton.setNumRuler(
                        labelDetailsViewModel.mLabelDetailsBean!!.viewCount,
                        labelWatchNum!!,
                        "0"
                    )
                    if (labelDetailsViewModel.mLabelDetailsBean!!.viewCount == 0) {
                        labelWatchNum!!.visibility = View.GONE
                    } else {
                        labelWatchNum!!.text = "${labelWatchNum!!.text}浏览"
                        labelWatchNum!!.visibility = View.VISIBLE
                    }

                    //收藏逻辑
                    if (labelDetailsViewModel.mLabelDetailsBean!!.isCollect == 1) {
                        labelCollectImg!!.setImageResource(R.drawable.home_collect_ed)
                    } else {
                        labelCollectImg!!.setImageResource(R.drawable.home_collect)
                    }

                    labelCollectLayout!!.setOnClickListener {
                        if (labelDetailsViewModel.mLabelDetailsBean!!.isCollect == 1) {
                            labelDetailsViewModel.mLabelDetailsBean!!.isCollect == 0
                            labelCollectImg!!.setImageResource(R.drawable.home_collect)//取消收藏

                            //取消收藏
                            commonViewModel.getUnCollect(
                                labelDetailsViewModel.mLabelDetailsBean!!.id,
                                3
                            )
                        } else {
                            labelDetailsViewModel.mLabelDetailsBean!!.isCollect == 1
                            labelCollectImg!!.setImageResource(R.drawable.home_collect_ed)//收藏

                            //收藏
                            commonViewModel.getCollect(
                               3,
                                labelDetailsViewModel.mLabelDetailsBean!!.id,
                                null
                            )
                        }
                    }

                    //请求话题列表
                    feedViewModel.getLabelFeedList(mId!!.toLong(), requestNum, 0)//刷新直接请求
                }
                is LabelDetailsBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
                    //请求话题列表
                    feedViewModel.getLabelFeedList(mId!!.toLong(), requestNum, 0)//刷新直接请求
                }
            }
        }


        /**
         * 收藏 请求结果
         */
        commonViewModel.collectBeanState.observe(this) {
            when (it) {
                is CollectBeanState.Success -> {

                }
                is CollectBeanState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }

                }
            }
        }
        /**
         * 取消收藏 请求结果
         */
        commonViewModel.unCollectBeanState.observe(this) {
            when (it) {
                is UnCollectBeanState.Success -> {

                }
                is UnCollectBeanState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }

                }
            }
        }

        mRootView!!.findViewById<ImageView>(R.id.label_share)
            .singleClick {
                shareViewModel.getRecommendShareUserList()
            }

        initShareData()

    }

    private fun setRefreshLabel() {
        refreshLayout = mRootView!!.findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(activity))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(activity))

        //话题返回状态 设置逻辑
        feedViewModel.homeGetLabelFeedListState.observe(this) {
            when (it) {
                is HomeGetLabelFeedListState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            mLabelDetailsList!!.clear()//清空集合
                            mLabelDetailsList = changeList(feedViewModel.mGetLabelListBean)
                            Log.e("zqr1234", "${mLabelDetailsList!!.size}")
                            mAdapter!!.setData(mLabelDetailsList!!, true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }
                        1, 2 -> {//加载 预加载
                            mAdapter!!.setData(
                                changeList(feedViewModel.mGetLabelListBean)!!,
                                false
                            )//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                        }
                    }
                }
                is HomeGetLabelFeedListState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
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
            labelDetailsViewModel.getLabelDetail(mId!!.toLong())//请求话题详情之后会请求话题列表
        }

        /**
         * 上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            if (feedViewModel.mGetLabelListBean != null) {
                refreshState = 1//设置为加载
                getLoadMoreLogicRecommendLabel(feedViewModel.mGetLabelListBean!!)//加载只加载话题列表
            }
        }
    }


    /**
     * 其他话题加载请求逻辑
     * feedViewModel.mWaterfallFeedBeanLabel
     */
    private fun getLoadMoreLogicRecommendLabel(bean: WaterfallFeedBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean == null) {
            feedViewModel.getLabelFeedList(mId!!.toLong(), requestNum, 0)//第一次请求
        } else {
            when (bean.hasNext) {
                0 -> {//没有更多数据了
                    Singleton.setRefreshResult(refreshLayout!!, 5)
                    isNoMoreData = true
                }
                1 -> {//有更多数据
                    feedViewModel.getLabelFeedList(mId!!.toLong(), requestNum, bean.lastTime)
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
    private var touchEvent: MotionEvent? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (view as InterceptTouchLinearLayout).mOnInterceptTouchEventListener = object :
            InterceptTouchLinearLayout.OnInterceptTouchEventListener {
            override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
                //拦截与否只与返回值有关，返回false表示不拦截，否则拦截此次事件。
                when (ev?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        touchEvent = ev//获取点击事件
                        // Log.e("zqr","touchEvent?.rawX?.toInt()"+touchEvent?.rawX?.toInt().toString())
                        // Log.e("zqr","touchEvent?.rawY?.toInt()"+touchEvent?.rawY?.toInt().toString())
                    }
                    MotionEvent.ACTION_MOVE -> {}
                }
                return false
            }
        }
    }

    private var popupWindow: PopupWindow? = null
    var scaleAnimation: ScaleAnimation? = null
    private fun showPopupWindow(position: Int, feedId: Long) {
        //将PopupWindow显示在window的decorView下，位置为按下的event的rawX和rawY的值。
        val inflate = View.inflate(activity, R.layout.home_dialog_content_feedback, null)
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
            popupWindowLogic(inflate, popupWindow)//关键code 弹窗逻辑
            Singleton.bgAlpha(this.requireActivity(), 0.5f) //设置透明度0.5
            popupWindow?.setOnDismissListener {
                Singleton.bgAlpha(this.requireActivity(), 1f) //恢复透明度
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
            activity?.let { it1 -> Singleton.centerToast(it1, "已收到反馈，将为你优化结果") }
        }
    }

    private fun popupWindowLogic(inflate: View, popupWindow: PopupWindow?) {
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
                currentY - Singleton.dp2px(activity!!.applicationContext, 260.0f)
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
                currentY - Singleton.dp2px(activity!!.applicationContext, 260.0f)
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
    private fun popAnim(inflate: View, x: Float, y: Float) {
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


    private fun initShareData() {


        shareViewModel.shortUrl.observe(this)
        {
            when (it) {
                is ShareShortUrlState.Success -> {
                    if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_COPY) {

                        var content = "【腰果】${it.shortUrl} 分享了一个话题 点击链接直接打开"
                        ClipboardUtils.copyText(content)
                        ToastUtils.showShort("复制成功，快去分享给好友吧")

                    }
                }
                is ShareShortUrlState.Fail -> {
                    ToastUtils.showShort(it.msg)
                }
            }
        }

        val shareAllPop = ShareAllPop(
            mContext = requireContext(),
            shareType = TIMCommonConstants.SHARE_TYPE_TOPIC,
            shareOnClick = object : ShareAllPop.ShareOnClick {
                override fun onClickCreatePost() {
                }

                override fun onClickCopyLink() {
                    shareViewModel.getShortUrl(
                        mId.toString(),
                        TIMCommonConstants.SHARE_TYPE_TOPIC,
                        TIMCommonConstants.SHARE_CLICK_TYPE_COPY
                    )
                }

                override fun onClickTipOff() {

                }

                override fun onClickShare(shareType: Int) {

                }

                override fun onSendMsg(
                    userListSel: List<UserInfo>,
                    content: String,
                    shareType: Int,
                ) {
                    val customData: MutableMap<String, SharePostEntity> =
                        mutableMapOf()
                    val gson = Gson()
                    var postEntity = sharePostEntity()

                    customData.put(
                        TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY,
                        postEntity
                    )

                    var userIdList: MutableList<String> = arrayListOf()
                    userListSel.forEach {
                        userIdList.add(it.id)
                    }
                    chartViewModel.sendCustomMsg(
                        gson.toJson(customData).toByteArray(),
                        userIdList,
                        content
                    )

                    XPopup.Builder(requireContext())
                        .asCustom(
                            LoadingPop(mContext = requireContext()).setTitle("发送中")
                                .setStyle(LoadingPop.Style.Spinner)
                        )
                        .show()
                        .delayDismissWith(1500) {
                            ToastUtils.showShort("发送成功")
                        }

                }

                //去私信好友
                override fun onClickToFriend() {
                    val customData: MutableMap<String, SharePostEntity> =
                        mutableMapOf()
                    val gson = Gson()
                    var postEntity = sharePostEntity()
                    customData[TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY] =
                        postEntity
                    ShareToActivity.start(
                        requireContext(),
                        gson.toJson(customData)
                    )
                }
            })

        shareViewModel.recomendUserList.observe(this) {
            when (it) {
                is ShareUseRecomendListState.Success -> {
                    if (it.recommendUserListBean != null)
                        shareAllPop.userList = it.recommendUserListBean

                    XPopup.Builder(requireContext())
                        .isDestroyOnDismiss(true)
                        .asCustom(
                            shareAllPop
                        )
                        .show()
                }
                is ShareUseRecomendListState.Fail -> {
                    ToastUtils.showShort(it.msg)
                }
            }
        }
    }

    private fun sharePostEntity(): SharePostEntity {
        var detailsBean = mLabelDetailsList!!.first()
        return SharePostEntity(
            picUrls = detailsBean.picUrls,
            videoUrls = detailsBean.videoUrls,
            title = if (TextUtils.isEmpty(detailsBean.title))
                "" else detailsBean.title!!,
            avatarUrl = detailsBean.userInfo.avatarUrl,
            nickName = detailsBean.userInfo.nickName,
            userId = detailsBean.userInfo.id.toString(),
            id = mId!!,
            type = detailsBean.type
        )
    }
    open fun  testMethod(){

    }
}

