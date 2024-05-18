package cn.dreamfruits.yaoguo.module.main.home.child

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.ShareWebContent
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.NetworkUtilsJava
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.AttentionAdapter
import cn.dreamfruits.yaoguo.module.main.home.child.controller.AttentionItemVideoController
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManager
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManagerListener
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.message.bean.SharePostEntity
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.pop.BottomPop
import cn.dreamfruits.yaoguo.module.share.ShareAllPop
import cn.dreamfruits.yaoguo.module.share.SharePostPop
import cn.dreamfruits.yaoguo.module.share.ShareToActivity
import cn.dreamfruits.yaoguo.module.share.ShareViewModel
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.module.share.state.ShareShortUrlState
import cn.dreamfruits.yaoguo.module.share.state.ShareUseRecomendListState
import cn.dreamfruits.yaoguo.repository.MMKVRepository
import cn.dreamfruits.yaoguo.repository.bean.feed.GetCareFeedListBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.find.HomeRecommendUserListBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.centerToast
import cn.dreamfruits.yaoguo.util.Singleton.changeList
import cn.dreamfruits.yaoguo.util.Singleton.mCareFeedList
import cn.dreamfruits.yaoguo.view.LoadingPop
import com.blankj.utilcode.util.*
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.xiaomi.push.it
import com.zr.kernel.inter.AbstractVideoPlayer
import com.zr.kernel.utils.VideoLogUtils
import com.zr.video.config.ConstantKeys
import com.zr.video.player.SimpleStateListener
import com.zr.video.player.VideoPlayer
import com.zr.video.player.VideoViewManager
import com.zr.video.tool.PlayerUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AttentionFragment : Fragment() {
    private var mRecyclerview: RecyclerView? = null
    private var refreshLayout: RefreshLayout? = null
    private val feedViewModel by viewModels<FeedViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mAttentionAdapter: AttentionAdapter? = null
    private var view: View? = null
    private val requestNum = 10
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Singleton.showLog(this, "onCreateView")
        view = inflater.inflate(R.layout.home_fragment_attention, container, false)

        mRecyclerview = view?.findViewById<RecyclerView>(R.id.recyclerView)
        refreshLayout = view?.findViewById(R.id.refreshLayout) as RefreshLayout

        initData()//初始化
        setSmartRefresh()//初始化刷新逻辑
        return view
    }

    /**
     * 初始化
     */
    private fun initData() {
        /**没有网络的时候，且数据为空的时候，使用上一次的缓存数据*/
        try {
            // TODO: 断网时之前的数据图片无法显示
            if (!NetworkUtilsJava.isNetworkConnected(context) && mCareFeedList!!.size == 0) {//没网且没有数据的时候
                val json = MMKVRepository.getCommonMMKV()
                    .decodeString(MMKVConstants.GET_CARE_FEED_LIST, "")//从缓存获取数据
                val bean = GsonUtils.fromJson(json, GetCareFeedListBean::class.java)
                mCareFeedList = bean.list.toMutableList()
            } else {//否则就加载数据
                refreshLayout!!.autoRefresh()
            }
        } catch (e: Exception) {
            Log.e("zqr", e.toString())
        }
        /**
         * 绑定适配器
         */
        mLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerview!!.layoutManager = mLinearLayoutManager
        mRecyclerview!!.itemAnimator = null
        mAttentionAdapter = AttentionAdapter(requireActivity(), mCareFeedList!!)
        mRecyclerview!!.adapter = mAttentionAdapter

        /**
         * 回调
         */
        mAttentionAdapter!!.setAttentionAdapterCallBack(object : AttentionAdapter.InnerInterface {
            override fun onComment(id: Long) {
                val intent = Intent("care_comment_pop")
                intent.putExtra("feedId", id)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            /**
             * 为你推荐回调
             */
            override fun onForYou(id: Long, type: Int, flag: Int) {
                when (flag) {
                    0 -> {//表示删除
                        GlobalScope.launch { // 在后台启动一个新的协程并继续
                            delay(500L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
                            feedViewModel.ignoreRecommendUser(id, type)
                        }
                    }
                    1 -> {
                        commonViewModel.getUnfollowUser(id)
                    }
                    2 -> {
                        commonViewModel.getFollowUser(id)
                    }
                }
            }

            /**
             * 是否静音
             */
            override fun onMute(flag: Boolean) {
                //设置是否静音
                mVideoView!!.isMute = !flag
                isMuteFlag = !flag
            }


            override fun onSave(position: Int, id: Int) {

            }

            /**
             * 收藏
             */
            override fun onCollect(
                isCollect: Boolean,
                type: Long,
                targetId: Long,
                outfitStr: String,
            ) {
                if (isCollect) {
                    commonViewModel.getCollect(type, targetId, outfitStr)
                } else {
                    commonViewModel.getUnCollect(targetId, type)
                }
            }

            /**
             * 点赞
             */
            override fun onLaud(isLaud: Boolean, id: Long) {
                if (isLaud) {
                    commonViewModel.getLaudFeed(id)
                } else {
                    commonViewModel.getUnLudFeed(id)
                }
            }

            /**
             * 预加载
             */
            override fun onPreLoad() {
                if (!isNoMoreData) {//没有更多了 就不用触发
                    //预加载逻辑
                    isPreload = true
                    Log.e("dadadadas", "瑜伽子啊")
                    //2.请求关注动态
                    getLoadMoreLogic()
                }
            }

            /**
             * 点击分享
             */
            override fun onShare(position: Int, tag: Int, info: WaterfallFeedBean.Item.Info) {
                showSharePop(requireActivity(), position, tag, info)
            }
        })

        /**
         * 为你推荐 感兴趣的人
         */
        feedViewModel.homeRecommendUserListState.observe(this) {
            when (it) {
                is HomeRecommendUserListState.Success -> {
//                    mCareFeedList.clear()
//                    mCareFeedList= mCareFeedList.take(3).toMutableList()//仅取一条数据

                    Log.e("zqr11", "mCareFeedList.size==" + mCareFeedList!!.size)
                    if (feedViewModel.mHomeRecommendUserListBean!!.list == null || feedViewModel.mHomeRecommendUserListBean!!.list.size == 0) {//如果为你推荐没有数据 直接返回
                        Log.e("zqr11", "为你推荐没有数据……")
                    } else {
                        when (mCareFeedList!!.size) {
                            0 -> {//没有帖子的时候  给它强制加一条假数据  然后在适配器中隐藏下面部分的视图，只显示为你推荐
                                Log.e("zqr11", "====0")
                                val temp =
                                    "{\"list\":[{\"id\":0,\"title\":\"string\",\"content\":\"string\",\"type\":0,\"picUrls\":[{\"url\":\"string\",\"width\":0,\"height\":0}],\"videoUrl\":[{\"url\":\"string\",\"width\":0,\"height\":0}],\"outfitId\":0,\"atUser\":[{\"id\":0,\"name\":\"string\",\"index\":0,\"len\":0}],\"config\":[{\"id\":0,\"name\":\"string\",\"index\":0,\"len\":0}],\"provinceAdCode\":\"string\",\"cityAdCode\":\"string\",\"address\":\"string\",\"longitude\":0,\"latitude\":0,\"userInfo\":{\"id\":0,\"nickName\":\"string\",\"avatarUrl\":\"string\"},\"relation\":0,\"laudCount\":0,\"collectCount\":0,\"commentCount\":0,\"wearCount\":0,\"isLaud\":0,\"isCollect\":0,\"state\":0,\"createTime\":0,\"updateTime\":0,\"commentList\":[{\"id\":0,\"targetId\":0,\"type\":0,\"content\":\"string\",\"createTime\":0,\"uid\":0,\"commentUserInfo\":{\"id\":0,\"nickName\":\"string\",\"avatarUrl\":\"string\",\"relation\":0},\"replys\":null,\"replyCount\":0,\"laudCount\":0,\"isLaud\":0,\"replyUid\":null,\"replyUserInfo\":null}]}],\"hasNext\":0,\"lastTime\":0}"
                                mCareFeedList = GsonUtils.fromJson(
                                    temp,
                                    GetCareFeedListBean::class.java
                                ).list.toMutableList()//关注伪数据
                                mCareFeedList = mCareFeedList!!.take(1).toMutableList()//仅取一条数据
                                Singleton.isCarePageNoData = true
                                mCareFeedList!![0].isShowForYouView = true
                                mCareFeedList!![0].homeRecommendUserListBean =
                                    feedViewModel.mHomeRecommendUserListBean!!//固定在第1个item 也就是顶部 但是要隐藏下面部分
                            }
                            1, 2, 3 -> {
                                Log.e("zqr11", "====123")
                                Singleton.isCarePageNoData = false
                                mCareFeedList!![0].isShowForYouView = true
                                mCareFeedList!![0].homeRecommendUserListBean =
                                    feedViewModel.mHomeRecommendUserListBean!!//固定在第1个item 也就是顶部
                            }
                            in 4..Int.MAX_VALUE -> {
                                Log.e("zqr11", "====4")
                                Singleton.isCarePageNoData = false
                                mCareFeedList!![3].isShowForYouView = true
                                mCareFeedList!![3].homeRecommendUserListBean =
                                    feedViewModel.mHomeRecommendUserListBean!!//固定在第3-4个item之间
                            }
                        }
                    }

                    Log.e("zqr11", "mCareFeedList.size==" + mCareFeedList!!.size)
                    Log.e("zqr11", Singleton.isCarePageNoData.toString())
                    mAttentionAdapter!!.setData(mCareFeedList!!, true)
                }
                is HomeRecommendUserListState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }

                    mAttentionAdapter!!.setData(mCareFeedList!!, true)//设置数据 更新适配器
                }
            }
        }
        /**
         * 忽略感兴趣的人 请求结果回调
         */
        feedViewModel.ignoreRecommendUserState.observe(this) {
            when (it) {
                is IgnoreRecommendUserState.Success -> {
                    //忽略成功 会返回一条数据 加在数据的最后面
                    mCareFeedList!!.forEach {
                        if (it.homeRecommendUserListBean != null) {
                            var tempBean: HomeRecommendUserListBean.Item =
                                HomeRecommendUserListBean.Item(
                                    feedViewModel.mIgnoreRecommendUserBean!!.avatarUrl,
                                    feedViewModel.mIgnoreRecommendUserBean!!.nickName,
                                    feedViewModel.mIgnoreRecommendUserBean!!.reason,
                                    feedViewModel.mIgnoreRecommendUserBean!!.relation,
                                    feedViewModel.mIgnoreRecommendUserBean!!.type,
                                    feedViewModel.mIgnoreRecommendUserBean!!.userId
                                )
                            it.homeRecommendUserListBean.list.toMutableList().add(tempBean)
                        }
                    }
                }
                is IgnoreRecommendUserState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }
                }
            }
        }
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

        /**
         * 关注用户 请求回调
         */
        commonViewModel.followUserBeanState.observe(this) {
            when (it) {
                is FollowUserBeanState.Success -> {

                }
                is FollowUserBeanState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }

                }
            }
        }
        /**
         * 取消关注用户 请求回调
         */
        commonViewModel.unfollowUserBeanState.observe(this) {
            when (it) {
                is UnfollowUserBeanState.Success -> {

                }
                is UnfollowUserBeanState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }

                }
            }
        }

        /**
         * 删除动态回调
         */
        commonViewModel.deleteFeedState.observe(this) {
            when (it) {
                is DeleteFeedState.Success -> {
                    centerToast(requireActivity(), "帖子已删除")
                    mAttentionAdapter!!.delItem(mPosition)
                }
                is DeleteFeedState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }
                }
            }
        }
        /**
         * 初始化分享相关
         */
        initShareData()
    }

    private var isMuteFlag = true
    private var isPreload: Boolean = false
    private var isNoMoreData: Boolean = false

    private var isRefresh: Boolean = false
    private var isLoadMore: Boolean = false

    private var isRefreshSucceed: Boolean = false
    private var isLoadMoreSucceed: Boolean = false

    /**
     * 上拉刷新，下拉加载模块
     */
    private fun setSmartRefresh() {
        refreshLayout?.setRefreshHeader(MRefreshHeader2(activity))
        refreshLayout?.setRefreshFooter(ClassicsFooter(activity))
        //3. 关注列表返回状态 设置逻辑
        feedViewModel.homeGetCareFeedListState.observe(this) {
            when (it) {
                is HomeGetCareFeedListState.Success -> {
                    if (isRefresh) {//刷新成功的逻辑
                        /**刷新标记*/
                        isRefresh = false
                        isRefreshSucceed = true

                        mCareFeedList!!.clear()//清空集合
                        mCareFeedList = changeList(feedViewModel.mGetCareFeedListBean)!!//加载请求后的数据

                        //2.1请求为你推荐动态 感兴趣的人
                        feedViewModel.getHomeRecommendUserList(10)

                        //适配器在为你推荐 更新

                    }
                    if (isPreload || isLoadMore) {//预加载成功
                        /**预加载和加载标记*/
                        isPreload = false
                        isLoadMore = false
                        isLoadMoreSucceed = true

//                        mCareFeedList = (mCareFeedList!!+feedViewModel.mGetCareFeedListBean!!.list) as MutableList<WaterfallFeedBean.Item.Info>// 将 list 转换为 MutableList<GetCareFeedListBean.Item>
                        mCareFeedList =
                            (mCareFeedList!! + changeList(feedViewModel.mGetCareFeedListBean)!!) as MutableList<WaterfallFeedBean.Item.Info> // 将 list 转换为 MutableList<GetCareFeedListBean.Item>
                        mAttentionAdapter!!.setData(
                            changeList(feedViewModel.mGetCareFeedListBean)!!,
                            false
                        )//设置数据 更新适配器
                    }
                }
                is HomeGetCareFeedListState.Fail -> {
                    //1.是否有断网提示
                    activity?.let { it1 -> Singleton.isNetConnectedToast(it1) }
                    //2.各自的失败处理逻辑
                    if (isRefresh) {//刷新失败
                        isRefresh = false
                        isRefreshSucceed = false
                    }
                    if (isPreload || isLoadMore) {//预加载或加载失败
                        isPreload = false
                        isLoadMore = false
                        isLoadMoreSucceed = false
                    }
                }
            }
        }

        /**
         * 下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            //1.设置标签为刷新
            isRefresh = true

            //2.请求关注动态
            feedViewModel.getCareFeedList(requestNum, 0)//第一次请求

            //4.根据返回结果设置刷新结果
            refreshlayout.finishRefresh(isRefreshSucceed)

            // TODO: 待优化
            //刷新之后自动播放第一个
//            try {
//                if (NetworkUtilsJava.isNetworkConnected(context)) {//有网的时候
//                    if (mCareFeedList!=null&&mCareFeedList!!.size>0&&mCareFeedList!![0].videoUrls!=null&& mCareFeedList!![0].videoUrls[0].url != ""){
//                        GlobalScope.launch { // 在后台启动一个新的协程并继续
//                            delay(500L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）//和刷新时间冲突，暂时修改为2s后执行
//                            if (isNoScroll) {
//                                mRecyclerview!!.post {
//                                    startPlay(0)
//                                }
//                                isNoScroll = true
//                            }
//                        }
//                    }
//                }
//            }catch (e:Exception){
//                Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
//            }
        }

        /**
         * 上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            //设置标签为加载（不刷新）
            isLoadMore = true

            //2.请求关注动态
            getLoadMoreLogic()

            //4.根据返回结果设置加载结果
            if (isNoMoreData) {//没有更多数据了
                refreshlayout.finishLoadMoreWithNoMoreData()//显示全部加载完成，并不再触发加载更事件
            } else {
                refreshlayout.finishLoadMore(isLoadMoreSucceed)
            }
        }
    }

    /**
     * 加载请求逻辑
     */
    private fun getLoadMoreLogic() {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (feedViewModel.mGetCareFeedListBean == null) {//等于空 说明没有请求过，发出第一次请求
            feedViewModel.getCareFeedList(requestNum, 0)//第一次请求
        } else if (feedViewModel.mGetCareFeedListBean != null && feedViewModel.mGetCareFeedListBean!!.hasNext == 1) {
            feedViewModel.getCareFeedList(
                requestNum,
                feedViewModel.mGetCareFeedListBean!!.lastTime
            )//第二次及之后请求
        } else if (feedViewModel.mGetCareFeedListBean != null && feedViewModel.mGetCareFeedListBean!!.hasNext == 0) {//表示没有更多数据了
            isNoMoreData = true
        }
    }

    /**
     * ------------------------------视频逻辑
     */
    private var mVideoView: VideoPlayer<*>? = null
    private var mController: AttentionItemVideoController? = null

    /**
     * 当前播放的位置
     */
    private var mCurPos = -1

    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    private var mLastPos = mCurPos
    private var context: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }


    var isNoScroll: Boolean = true
    var lastPosition = -1

    private fun initView(view: View) {
        initVideoView()

        mRecyclerview!!.addOnChildAttachStateChangeListener(object :
            OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                VideoLogUtils.i("addOnChildAttachStateChangeListener-----AttachedToWindow---$view")
            }

            /**
             * 当适配器创建的view（即列表项view）被窗口分离（即滑动离开了当前窗口界面）就会被调用
             *
             * @param view
             */
            override fun onChildViewDetachedFromWindow(view: View) {
                VideoLogUtils.i("addOnChildAttachStateChangeListener-----DetachedFromWindow---$view")
                val playerContainer = view.findViewById<FrameLayout>(R.id.player_container)
                val v = playerContainer.getChildAt(0)
                if (v != null && v === mVideoView && !mVideoView!!.isFullScreen) {
                    releaseVideoView()
                }
            }
        })

        /**
         * 列表滑动监听
         */
        mRecyclerview!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isNoScroll = false//滑动了
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //滚动停止
                    autoPlayVideo(recyclerView)
                }
            }

            private fun autoPlayVideo(view: RecyclerView?) {
                if (view == null) return
                //遍历RecyclerView子控件,如果mPlayerContainer完全可见就开始播放
                val count = view.childCount
                for (i in 0 until count) {
                    val itemView = view.getChildAt(i) ?: continue
                    val holder: AttentionAdapter.MyViewHolder =
                        itemView.tag as AttentionAdapter.MyViewHolder

                    /**
                     * 视频可见区域大于3/4的情况 开始播放
                     */
                    val rect = Rect()
                    holder.mPlayerContainer!!.getLocalVisibleRect(rect)
                    val height: Int = holder.mPlayerContainer!!.height
                    if ((rect.bottom == height && rect.top <= height * 1 / 4) || (rect.top == 0 && rect.bottom >= height * 3 / 4)) {
                        if (lastPosition == holder.mPosition && isPaused) {
                            mVideoView!!.resume()
                        } else {
                            startPlay(holder.mPosition)
                        }
                        lastPosition = holder.mPosition
                        break
                    }
                    /**
                     * 视频可见区域小于1/4 停止播放
                     */
                    val rect2 = Rect()
                    mVideoView!!.getLocalVisibleRect(rect2)
                    val height2: Int = mVideoView!!.height
                    if ((rect2.bottom == height2 && rect2.top > height2 * 3 / 4) || (rect2.top == 0 && rect2.bottom < height2 * 1 / 4)) {
                        mVideoView!!.pause()
                        break
                    }
                }
            }
        })
    }

    var isPaused = false//当前播放视频是否暂停
    private fun initVideoView() {
        mVideoView = VideoPlayer<AbstractVideoPlayer?>(context!!)
        mVideoView!!.setOnStateChangeListener(object : SimpleStateListener() {
            override fun onPlayStateChanged(playState: Int) {
                //监听VideoViewManager释放，重置状态
                if (playState == ConstantKeys.CurrentState.STATE_IDLE) {
                    PlayerUtils.removeViewFormParent(mVideoView)
                    mLastPos = mCurPos
                    mCurPos = -1
                }
                if (playState == ConstantKeys.CurrentState.STATE_PAUSED) {
                    isPaused = true//获取状态
                }
            }
        })
        mController = AttentionItemVideoController(context!!)
        mVideoView!!.setController(mController)
    }

    override fun onPause() {
        super.onPause()
        releaseVideoView()
        Singleton.showLog(this, "onPause")
    }


    private var isVisible = false
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Singleton.showLog(this, "setUserVisibleHint---$isVisibleToUser")
        //先使用这个逻辑
        if (isVisible) {
            if (isVisibleToUser) {
                if (mLastPos == -1) return
                startPlay(mLastPos)//恢复上次播放的位置
            } else {
                releaseVideoView()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        isVisible = true
        if (mLastPos == -1) return
        startPlay(mLastPos)//恢复上次播放的位置
    }


    /**
     * 开始播放
     * @param position 列表位置
     */
    private fun startPlay(position: Int) {
        // TODO: 2020/12/3 0003  补丁
        if (position >= mCareFeedList!!.size) return
        if (mCareFeedList!!.size == 0 || mCareFeedList!![position].type != 1) {
            return
        }


        if (!NetworkUtilsJava.isNetworkConnected(context)) {//没网的时候
            return
        }

        if (mCurPos == position) return
        if (mCurPos != -1) {
            releaseVideoView()
        }
        try {
            mVideoView!!.url = mCareFeedList!!.get(position).videoUrls[0].url//视频链接
        } catch (e: Exception) {
            Toast.makeText(context, "视频异常……", Toast.LENGTH_SHORT).show();
        }

        val itemView = mLinearLayoutManager!!.findViewByPosition(position) ?: return
        val viewHolder = itemView.tag as AttentionAdapter.MyViewHolder
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController!!.addControlComponent(viewHolder.mPrepareView, true)
        PlayerUtils.removeViewFormParent(mVideoView)
        viewHolder.mPlayerContainer!!.addView(mVideoView, 0)
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, "list")
        mVideoView!!.setLooping(true)//循环播放
        mVideoView!!.start()
        mVideoView!!.isMute = isMuteFlag
        mCurPos = position
    }

    /**
     * 释放视频
     */
    private fun releaseVideoView() {
        if (mVideoView == null) {
            return
        }
        if (mVideoView!!.isFullScreen) {
            mVideoView!!.stopFullScreen()
        }
        mVideoView!!.release()
//        if (activity!!.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        }
        mCurPos = -1
    }

    /**
     * 关注页面 分享弹窗
     * tag 0分享我的   1其他用户  2我的个人中心 3他人个人中心
     */

    fun showSharePop(
        mActivity: Activity,
        position: Int,
        tag: Int,
        info: WaterfallFeedBean.Item.Info,
    ) {
        /**
         * 获取推荐用户列表
         */
        mFeedDetailsBean = info
        mTag = tag
        mPosition = position
        shareViewModel.getRecommendShareUserList()
    }

    var mTag = 1
    var mPosition = -1
    var mFeedDetailsBean: WaterfallFeedBean.Item.Info? = null

    /**
     * 确认删除帖子
     */
    private fun showConfirm(mActivity: Activity, delFeedId: Long) {
        val strings = arrayListOf("确认删除")
        XPopup.Builder(mActivity)
            .isDestroyOnDismiss(true)
            .asCustom(
                BottomPop(mActivity, title = "确认删除这条帖子吗?", data = strings)
                    .setOnSelectListener { position, text ->
                        commonViewModel.deleteFeed(delFeedId)
                    }
            ).show()
    }


    private val shareViewModel by viewModels<ShareViewModel>()
    private val chartViewModel by viewModels<ChartViewModel>()
    private fun initShareData() {

        shareViewModel.shortUrl.observe(this)
        {
            when (it) {
                is ShareShortUrlState.Success -> {
                    if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_COPY) {

                        var content = "【腰果】${it.shortUrl}${
                            if (TextUtils.isEmpty(mFeedDetailsBean!!.title)) {
                                "@${mFeedDetailsBean!!.userInfo.nickName}"
                            } else {
                                mFeedDetailsBean!!.title
                            }
                        }在腰果上发布了一条超棒的帖子点击链接直接打开"
                        ClipboardUtils.copyText(content)
                        ToastUtils.showShort("复制成功，快去分享给好友吧")

                    } else if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_POST) {
                        XPopup.Builder(requireContext())
                            .isDestroyOnDismiss(true)
                            .asCustom(
                                SharePostPop(
                                    mContext = requireContext(),
                                    mFeedDetailsBean = mFeedDetailsBean!!,
                                    shortUrl = it.shortUrl
                                )
                            )
                            .show()
                    } else if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT
                        || it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT_CIRCLE
                        || it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_QQ
                    ) {
                        share(it.shareType, it.shortUrl)
                    }
                }

                is ShareShortUrlState.Fail -> {
                    ToastUtils.showShort(it.msg)
                }
            }
        }

//        val shareAllPop =

        shareViewModel.recomendUserList.observe(this) {
            when (it) {
                is ShareUseRecomendListState.Success -> {
//                    if (it.recommendUserListBean != null)
//                        shareAllPop.userList = it.recommendUserListBean
//                    shareAllPop.mTag = mTag
//                    shareAllPop.setTag()
                    XPopup.Builder(requireContext())
                        .isDestroyOnDismiss(true)
                        .asCustom(
                            ShareAllPop(
                                mContext = requireContext(),
                                shareType = TIMCommonConstants.SHARE_TYPE_POST,
                                userList = it.recommendUserListBean!!,
                                mTag = mTag,
                                shareOnClick = object : ShareAllPop.ShareOnClick {
                                    override fun onClickCreatePost() {
                                        if (mFeedDetailsBean == null) {
                                            return
                                        }
                                        shareViewModel.getShortUrl(
                                            mFeedDetailsBean!!.id.toString(),
                                            TIMCommonConstants.SHARE_TYPE_POST,
                                            TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_POST
                                        )
                                    }

                                    override fun onClickCopyLink() {
                                        shareViewModel.getShortUrl(
                                            mFeedDetailsBean!!.id.toString(),
                                            TIMCommonConstants.SHARE_TYPE_POST,
                                            TIMCommonConstants.SHARE_CLICK_TYPE_COPY
                                        )
                                    }

                                    override fun onClickTipOff() {

                                    }

                                    override fun onClickShare(shareType: Int) {
                                        shareViewModel.getShortUrl(
                                            mFeedDetailsBean!!.id.toString(),
                                            TIMCommonConstants.SHARE_TYPE_POST,
                                            shareType
                                        )
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
                                            TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY,
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
                                        customData[TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY] =
                                            postEntity
                                        ShareToActivity.start(
                                            requireContext(),
                                            gson.toJson(customData)
                                        )
                                    }

                                    override fun onClickDelete() {
                                        super.onClickDelete()
                                        showConfirm(requireActivity(), mFeedDetailsBean!!.id)
                                    }


                                })
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


        var picUrlsOrigin: MutableList<WaterfallFeedBean.Item.Info.PicUrl> = arrayListOf()

        mFeedDetailsBean!!.picUrls.forEach {
            var picUrl = WaterfallFeedBean.Item.Info.PicUrl(
                height = it.height, width = it.width,
                url = if (it.url.contains("?sign")) {
                    it.url.split("?sign")[0]
                } else
                    it.url, urlX = ""
            )
            picUrlsOrigin.add(picUrl)
        }

        var avatarUrlOrigin =
            if (mFeedDetailsBean!!.userInfo.avatarUrl.contains("?sign")) {
                mFeedDetailsBean!!.userInfo.avatarUrl.split("?sign")[0]
            } else
                mFeedDetailsBean!!.userInfo.avatarUrl

        LogUtils.e(">>> avatarUrl = " + mFeedDetailsBean!!.userInfo.avatarUrl)
        LogUtils.e(">>> avatarUrlOrigin = " + avatarUrlOrigin)

        var postEntity = SharePostEntity(
            picUrls = picUrlsOrigin,
            videoUrls = arrayListOf(),
            title = if (TextUtils.isEmpty(mFeedDetailsBean!!.title))
                "" else mFeedDetailsBean!!.title!!,
            avatarUrl = avatarUrlOrigin,
            nickName = mFeedDetailsBean!!.userInfo.nickName,
            userId = mFeedDetailsBean!!.userInfo.id.toString(),
            id = mFeedDetailsBean!!.id.toString(),
            type = 0,
            coverHeight = mFeedDetailsBean!!.picUrls.first().height,
            coverWidth = mFeedDetailsBean!!.picUrls.first().width
        )
        return postEntity
    }

    private fun share(shareType: Int, shortUrl: String) {

        var content = ShareWebContent()
        content.title = if (!TextUtils.isEmpty(mFeedDetailsBean!!.title)) {
            mFeedDetailsBean!!.title
        } else {
            "@" + mFeedDetailsBean!!.userInfo.nickName + "在微酸上发布了一条超棒的帖子"
        }
        content.description =
            if (!TextUtils.isEmpty(mFeedDetailsBean!!.content)) {
                mFeedDetailsBean!!.content
            } else {
                "万千好物一键上身，美好生活即刻分享"
            }

        Thread {
            AndroidDownloadManager(
                requireContext(),
                mFeedDetailsBean!!.picUrls.first().url
            )
                .setListener(object : AndroidDownloadManagerListener {
                    override fun onPrepare() {
                        Log.d("downloadVideo", "onPrepare")
                    }

                    override fun onSuccess(path: String) {
                        val thumbBmp: Bitmap = ImageUtils.getBitmap(path)
                        LogUtils.e(">>> " + path)
                        content.img = thumbBmp
                        content.webPageUrl = shortUrl
//                        if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_QQ) {
                        var file = ImageUtils.save2Album(
                            ImageUtils.getBitmap(path),
                            Bitmap.CompressFormat.JPEG
                        )
                        content.url = file!!.path
//                        }
//                        FileUtils.delete(path)

                        shareCallback(shareType, content)
                    }

                    override fun onFailed(throwable: Throwable?) {
                        Toast.makeText(
                            requireContext(),
                            "图片下载失败，请重新下载！",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("downloadVideo", "onFailed", throwable)
                    }
                }).download()
        }.start()

    }

    private fun shareCallback(
        shareType: Int,
        content: ShareWebContent,
    ) {
        Social.share(requireContext(),
            if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT) PlatformType.WEIXIN
            else if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_WECHAT_CIRCLE) PlatformType.WEIXIN_CIRCLE
            else PlatformType.QQ,
            content = content,
            onSuccess = { type ->
                LogUtils.e(">>>> onSuccess = type = " + type)
                when (type) {
                    PlatformType.WEIXIN -> {
                        ToastUtils.showShort("分享成功")
                    }
                    PlatformType.QQ -> {
                        ToastUtils.showShort("分享成功")
                    }
                    PlatformType.WEIXIN_CIRCLE -> {
                        ToastUtils.showShort("分享成功")
                    }
                    PlatformType.QQ_ZONE -> TODO()
                    PlatformType.SINA_WEIBO -> TODO()
                    PlatformType.ALI -> TODO()
                }
            },
            onCancel = {
                LogUtils.e(">>>> onCancel = 用户取消 ")
                ToastUtils.showShort("用户取消")
            },
            onError = { _, _, msg ->
                LogUtils.e(">>>> onError =  $msg")
                ToastUtils.showShort("$msg")
            }
        )
    }

    fun refreshData() {
        //1.设置标签为刷新
        isRefresh = true
        //2.请求关注动态
        feedViewModel.getCareFeedList(requestNum, 0)//第一次请求

        //4.根据返回结果设置刷新结果
        refreshLayout?.finishRefresh(isRefreshSucceed)
    }


}