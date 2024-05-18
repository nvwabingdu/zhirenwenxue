package cn.dreamfruits.yaoguo.module.main.home.vlistvideo

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.ShareWebContent
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.FeedViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.ViewPagerFragmentAdapter
import cn.dreamfruits.yaoguo.module.main.home.postdetails.PostDetailsViewModel
import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.CommentFragment
import cn.dreamfruits.yaoguo.module.main.home.postdetails.custombanner.scaleimg.BannerImgFragment
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManager
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManagerListener
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.SaveFileUtils
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.home.tool.StatuBar
import cn.dreamfruits.yaoguo.module.main.message.bean.SharePostEntity
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.share.ShareAllPop
import cn.dreamfruits.yaoguo.module.share.SharePostPop
import cn.dreamfruits.yaoguo.module.share.ShareToActivity
import cn.dreamfruits.yaoguo.module.share.ShareViewModel
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.module.share.state.ShareShortUrlState
import cn.dreamfruits.yaoguo.module.share.state.ShareUseRecomendListState
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.StatusBarUtil
import cn.dreamfruits.yaoguo.view.LoadingPop
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.XPopup
import com.vivo.vms.IPCInvoke.Stub
import com.yc.pagerlib.pager.VerticalViewPager
import com.zr.kernel.inter.AbstractVideoPlayer
import com.zr.kernel.utils.VideoLogUtils
import com.zr.video.config.ConstantKeys
import com.zr.video.player.VideoPlayer
import com.zr.video.tool.PlayerUtils
import com.zr.videocache.cache.PreloadManager
import com.zr.videocache.cache.ProxyVideoCacheManager
import java.io.File
import java.util.*

/**
 * @Author qiwangi
 * @Date 2023/5/11
 * @TIME 09:46
 * 修改为kotlin类
 */
@RequiresApi(Build.VERSION_CODES.O)
class ListVideoActivity : BaseActivity() {
    private val feedViewModel by viewModels<FeedViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel

    private var mCurPos = 0//当前播放位置
    private var mListVideoAdapter: ListVideoAdapter? = null
    private var mViewPager: VerticalViewPager? = null//适配器
    private var mPreloadManager: PreloadManager? = null//预加载管理器
    private var mVideoPlayer: VideoPlayer<AbstractVideoPlayer>? = null// TODO: 此处修改为抽象类 考究是否有问题

    //private var mController: BasisVideoController? = null//控制器
    private var mController: ListVideoController? = null//控制器

    private var mActivity: Activity? = null//仅用于评论回复时，获取当前activity
    private var mLifecycleOwner: LifecycleOwner? = null
    private var feedId: Long = 0L//标签id

    var mVideoListFeedList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()//视频动态


    var bean: WaterfallFeedBean.Item? = null
    var position = 0

    private val shareViewModel by viewModels<ShareViewModel>()//

    //互动消息传递过来需要的参数
    private var isComment: Boolean = false
    private var highlightId: Long = 0L
    private var messageIds: String? = null//消息页过来需要的id
    private var messageCommentIds: String? = null//消息页面过来的id
    private var mTag = 1

    override fun layoutResId(): Int = R.layout.home_listvideo_activity_verticalviewpager
    override fun initView() {
        immersionBar {
            fitsSystemWindows(false)
            statusBarDarkFont(false)
        }
        setStatusBarH()

        Singleton.showLog(this, "进来了………………")

        //状态栏设置
//        setSystemBar()//设置透明

        hideNavigation()//隐藏底部导航
        /*StateAppBar.translucentStatusBar(this, true);*/


        mActivity = this//赋值
        mLifecycleOwner = this//赋值

        //1.获取传递过来的值
        feedId = intent.getLongExtra("feedId", 0)
        position = intent.getIntExtra("position", 0)

        //消息页 跳转过来独有
        isComment = intent.getBooleanExtra("isComment", false)
        highlightId = intent.getLongExtra("highlightId", 0L)//用于高亮
        messageIds = intent.getStringExtra("messageIds")//一级评论id
        if (messageIds == "") {
            messageIds = null
        }
        messageCommentIds = intent.getStringExtra("messageCommentIds")//一级评论id , 二级评论id
        if (messageCommentIds == "") {
            messageCommentIds = null
        }


        //2.判断是推荐页面跳转过来的还是关注页面跳转过来的 并做相关处理
        when (Singleton.videoTag) {
            0 -> {//推荐页面
                addOneBean(Singleton.mRecommendFeedList)
            }
            1 -> {//关注页面
                addOneBean(Singleton.mCareFeedList)
            }
            2 -> {//搜索页面
                addOneBean(Singleton.mSearchPostList)
            }
            3 -> {//话题tab页面
                addOneBean(Singleton.mTempGetLabelFeedList)
            }
            4 -> {//话题详情页
                addOneBean(Singleton.mLabelDetailsList)
            }
            5 -> {//互动消息跳转过来
                addOneBean(Singleton.mMessageVideoList)
            }
            6 -> {//互动消息跳转过来
                addOneBean(Singleton.mFashionWearList)
            }
            7, 8, 9 -> {//个人中心帖子
                addOneBean(Singleton.mMineFeedListSingle)
            }
            10 -> {
                addOneBean(Singleton.mShareVideoList)
            }
//            8->{//个人中心收藏的帖子
//                addOneBean(Singleton.mMineCollectList)
//            }
//            9->{//个人中心赞过的帖子
//                addOneBean(Singleton.mMineLaudList)
//            }
        }

        //3.初始化viewpager
        mViewPager = findViewById(R.id.vvp)
        initViewPager()

        /**
         * 重要逻辑
         */
        mVideoPlayer = VideoPlayer(this)//初始化播放器
        mVideoPlayer!!.setRenderViewFactory(ListVideoRenderViewFactory.create())//包装渲染器
        mPreloadManager = PreloadManager.getInstance(this)//初始化预加载管理器
        mController = ListVideoController(this)//初始化视频控制器
        /*mController = BasisVideoController(this)*/
        if (mVideoListFeedList!!.size > mCurPos && mVideoListFeedList!![mCurPos] != null) {
            mController!!.setTitle(mVideoListFeedList!![mCurPos].title)//wq 设置标题
        }
        mVideoPlayer!!.setController(mController)//绑定控制器
        mVideoPlayer!!.setLooping(true)//设置循环播放

        /**
         * 请求数据列表
         */
        when (Singleton.videoTag) {
            1, 5, 10 -> {
                //不请求  关注页面仅展示一个视频 无需请求
            }
            else -> {
                feedViewModel.getRecommendFeedList(10, 1)//请求视频列表
            }
        }

        //播放第一个
        mViewPager!!.post { startPlay(0) }

        /**
         * 视频请求接口返回的数据
         */
        feedViewModel.homeRecommendFeedListState.observe(this) {
            when (it) {
                is HomeRecommendFeedListState.Success -> {

                    if (mVideoListFeedList!!.size == 0) {
                        mVideoListFeedList!!.clear()//清空集合
                        mVideoListFeedList =
                            Singleton.changeList(feedViewModel.mWaterfallFeedBean)//加载请求后的数据
                        mListVideoAdapter!!.setData(mVideoListFeedList!!, true)//设置数据 更新适配器
                    } else {
                        mListVideoAdapter!!.setData(
                            Singleton.changeList(feedViewModel.mWaterfallFeedBean)!!,
                            false
                        )//设置数据 更新适配器
                    }
                }
                is HomeRecommendFeedListState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        //通过handler在主线程更新UI的方式
        handler = Handler(Looper.getMainLooper())
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                // 发送消息到主线程
                handler.post {
                    if (isComment) {
                        //底部fragment显示
                        setBottomCommentVisibility(true)
                        //评论弹窗 点击后回调在这里  feedId是帖子id 用于请求评论列表 并弹出评论弹窗
                        val fragment = CommentFragment(
                            mActivity!!,
                            mLifecycleOwner!!,
                            feedId,
                            highlightId,
                            isComment,
                            messageIds!!,
                            messageCommentIds!!
                        )
                        supportFragmentManager.beginTransaction()
                            .add(R.id.comment_fragment_container, fragment).commit()
                    }
                }
            }
        }, 1000)

    }

    fun setStatusBarH() {
        //沉浸状态栏重叠问题
        findViewById<View>(cn.dreamfruits.yaoguo.R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BarUtils.getStatusBarHeight()
            )
    }

    private lateinit var handler: Handler //在结束时移除

    /**
     * 添加一个视频数据 从别处跳转过来的
     */
    private fun addOneBean(tempList: MutableList<WaterfallFeedBean.Item.Info>?) {
        if (tempList != null && tempList.size > position && tempList[position].id == feedId) {
            mVideoListFeedList!!.add(0, tempList[position])//添加到集合中
        }
        /*val tempBean = WaterfallFeedBean.Item(Singleton.mLabelDetailsList!![position], 1)*/
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mVideoPlayer != null) {
            mVideoPlayer!!.release()
        }
        mPreloadManager!!.removeAllPreloadTask()
        //清除缓存，实际使用可以不需要清除
        ProxyVideoCacheManager.clearAllCache(this)

        handler.removeCallbacksAndMessages(null)//移除handler
    }

    override fun onResume() {
        super.onResume()
        if (mVideoPlayer != null) {
            mVideoPlayer!!.resume()
        }
        setStatusBar()//设置黑色背景白色文字
    }

    override fun onPause() {
        super.onPause()
        if (mVideoPlayer != null) {
            mVideoPlayer!!.pause()
        }
    }

    override fun onBackPressed() {
        if (mVideoPlayer == null || !mVideoPlayer!!.onBackPressed()) {
            super.onBackPressed()
            finish()
        }
    }


    override fun initData() {

        //初始化分享相关
        initShareData()

    }

    private fun initViewPager() {
        mViewPager = findViewById<VerticalViewPager>(R.id.vvp)

//        GlobalScope.launch { // 在后台启动一个新的协程并继续
//                            delay(500L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）//和刷新时间冲突，暂时修改为2s后执行
//            mViewPager!!.offscreenPageLimit = 3 //viewpager预加载页面？
//        }
        // TODO: 预加载？
        mViewPager!!.offscreenPageLimit = 3 //viewpager预加载页面？
        mListVideoAdapter = ListVideoAdapter(this, mVideoListFeedList!!)
        mViewPager!!.adapter = mListVideoAdapter
        mViewPager!!.overScrollMode = View.OVER_SCROLL_NEVER
        mViewPager!!.setOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            private var mCurItem = 0//当前页面
            private var mIsReverseScroll = false//VerticalViewPager是否反向滑动

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == mCurItem) {
                    return
                }
                mIsReverseScroll = position < mCurItem
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == mCurPos) return
                startPlay(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {//用户正在拖动
                    mCurItem = mViewPager!!.currentItem
                }
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {//滚动停止
                    mPreloadManager!!.resumePreload(mCurPos, mIsReverseScroll)
                } else {
                    mPreloadManager!!.pausePreload(mCurPos, mIsReverseScroll)
                }
            }
        })

        /**
         * 视频列表适配器回调
         */
        mListVideoAdapter!!.setListVideoAdapterCallBack(object :
            ListVideoAdapter.ListVideoAdapterInterface {
            override fun onShowCommentPop(id: Long?, position: Int) {//视频评论弹窗
                //底部fragment显示
                setBottomCommentVisibility(true)
                //评论弹窗 点击后回调在这里  feedId是帖子id 用于请求评论列表 并弹出评论弹窗
                val fragment =
                    CommentFragment(mActivity!!, mLifecycleOwner!!, id!!, 0L, false, "", "")
                supportFragmentManager.beginTransaction()
                    .add(R.id.comment_fragment_container, fragment).commit()
            }

            //点赞
            override fun onLaud(isLaud: Boolean, id: Long?) {
                if (isLaud) {
                    commonViewModel.getLaudFeed(id!!)
                } else {
                    commonViewModel.getUnLudFeed(id!!)
                }
            }

            //收藏
            override fun onCollect(isCollect: Boolean, id: Long?) {
                if (isCollect) {
                    commonViewModel.getCollect(0, id!!, "")
                } else {
                    commonViewModel.getUnCollect(0, id!!)
                }
            }

            //加载
            override fun onLoadMore() {
                when (Singleton.videoTag) {
                    1, 5 -> {
                        //不请求  关注页面仅展示一个视频 无需请求 互动消息过来也不用请求
                    }
                    else -> {
                        feedViewModel.getRecommendFeedList(10, 1)//请求视频列表
                    }
                }
            }

            //关注
            override fun onCareUser(id: Long?) {
                commonViewModel.getFollowUser(id!!)
            }

            //分享
            override fun onShare(item: WaterfallFeedBean.Item.Info) {
                mFeedDetailsBean = item

                mTag = if (item.userInfo.id == Singleton.getLoginInfo().userId) 0 else 1

                shareViewModel.getRecommendShareUserList()
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

        /**
         * 收藏 请求结果
         */
        commonViewModel.collectBeanState.observe(this) {
            when (it) {
                is CollectBeanState.Success -> {

                }
                is CollectBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)

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
                    Singleton.isNetConnectedToast(this)

                }
            }
        }

        /**
         * 关注
         */
        commonViewModel.followUserBeanState.observe(this) {
            when (it) {
                is FollowUserBeanState.Success -> {
                    Singleton.centerToast(this, Singleton.CARE_TEXT)
                }
                is FollowUserBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }


//        RecyclerView recyclerView = new RecyclerView(this);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            /**
//             * 是否反向滑动
//             */
//            private boolean mIsReverseScroll;
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy>0){
//                    //表示下滑
//                    mIsReverseScroll = false;
//                } else {
//                    //表示上滑
//                    mIsReverseScroll = true;
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == VerticalViewPager.SCROLL_STATE_IDLE) {
//                    mPreloadManager.resumePreload(mCurPos, mIsReverseScroll);
//                } else {
//                    mPreloadManager.pausePreload(mCurPos, mIsReverseScroll);
//                }
//            }
//        });
    }

//    fun start(context: Context, index: Int) {
//        val i = Intent(context, ListVideoActivity::class.java)
//        i.putExtra(KEY_INDEX, index)
//        context.startActivity(i)
//    }

    private fun startPlay(position: Int) {
        val count = mViewPager!!.childCount
        for (i in 0 until count) {
            val itemView = mViewPager!!.getChildAt(i)
            val viewHolder = itemView.tag as ListVideoAdapter.ViewHolder
            if (viewHolder.mPosition == position) {
                mVideoPlayer!!.release()
                PlayerUtils.removeViewFormParent(mVideoPlayer)

                val playUrl =
                    mPreloadManager!!.getPlayUrl(mVideoListFeedList!![position].videoUrls[0].url)

                VideoLogUtils.i("startPlay: position: $position  url: $playUrl")

                mVideoPlayer!!.url = playUrl

                mVideoPlayer!!.setScreenScaleType(ConstantKeys.PlayerScreenScaleType.SCREEN_SCALE_16_9)

                mController!!.addControlComponent(viewHolder.mListVideoView, true)

                viewHolder.mPlayerContainer.addView(mVideoPlayer, 0)

                mVideoPlayer!!.start()

                mCurPos = position

                break
            }
        }
    }

    private fun hideNavigation() {//隐藏底部导航 * 同时在所有themes文件 都添加 <item name="android:navigationBarColor">@color/transparent</item>
        val params: WindowManager.LayoutParams = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.attributes = params
    }

    private fun setSystemBar() {//全透状态栏
        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) {
            //19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**设置状态栏 */
  private fun setStatusBar(){
//      BarUtils.setStatusBarColor(this, Color.WHITE)
//      StatusBarUtil.darkMode(this,false)

//        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
//        darkMode(false)


    }




    /**
     * 开关状态栏暗色模式, 并不会透明状态栏, 只是单纯的状态栏文字变暗色调.
     *
     * @param darkMode 状态栏文字是否为暗色
     */
   private fun darkMode(darkMode: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = if (darkMode) {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            window.decorView.systemUiVisibility = systemUiVisibility
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().setStatusBarColor(Color.WHITE);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
    }


    /**
     * 用于评论弹窗
     */
    private var bottomComment: View? = null//显示隐藏底部评论布局
    fun setBottomCommentVisibility(isShow: Boolean) {
        if (bottomComment == null) {
            bottomComment = findViewById(R.id.comment_bottom)
        }
        if (isShow) {//显示
            val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_bottom_in)
            bottomComment!!.startAnimation(anim)
            bottomComment!!.visibility = View.VISIBLE
        } else {//隐藏
            val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_bottom_out)
            bottomComment!!.startAnimation(anim)
            bottomComment!!.visibility = View.GONE
        }
    }

    private val chartViewModel by viewModels<ChartViewModel>()

    var mFeedDetailsBean: WaterfallFeedBean.Item.Info? = null

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
                        XPopup.Builder(this@ListVideoActivity)
                            .isDestroyOnDismiss(true)
                            .asCustom(
                                SharePostPop(
                                    mContext = this@ListVideoActivity,
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


        shareViewModel.recomendUserList.observe(this) {
            when (it) {
                is ShareUseRecomendListState.Success -> {

                    XPopup.Builder(this)
                        .isDestroyOnDismiss(true)
                        .asCustom(
                            ShareAllPop(
                                mContext = this,
                                userList = it.recommendUserListBean!!,
                                mTag = mTag,
                                shareType = TIMCommonConstants.SHARE_TYPE_POST,
                                shareOnClick = object : ShareAllPop.ShareOnClick {
                                    override fun onClickCreatePost() {
                                        if (mFeedDetailsBean == null) {
                                            return
                                        }
                                        shareViewModel.getShortUrl(
                                            feedId.toString(),
                                            TIMCommonConstants.SHARE_TYPE_POST,
                                            TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_POST
                                        )
                                    }

                                    override fun onClickCopyLink() {
                                        shareViewModel.getShortUrl(
                                            feedId.toString(),
                                            TIMCommonConstants.SHARE_TYPE_POST,
                                            TIMCommonConstants.SHARE_CLICK_TYPE_COPY
                                        )
                                    }

                                    override fun onClickTipOff() {

                                    }

                                    override fun onClickShare(shareType: Int) {
                                        shareViewModel.getShortUrl(
                                            feedId.toString(),
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

                                        XPopup.Builder(mContext)
                                            .asCustom(
                                                LoadingPop(this@ListVideoActivity).setTitle("发送中")
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
                                        ShareToActivity.start(mContext, gson.toJson(customData))
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
        var videoUrlsOrigin: MutableList<WaterfallFeedBean.Item.Info.VideoUrl> = arrayListOf()

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
        mFeedDetailsBean!!.videoUrls.forEach {
            var videoUrl = WaterfallFeedBean.Item.Info.VideoUrl(
                height = it.height, width = it.width,
                url = if (it.url.contains("?sign")) {
                    it.url.split("?sign")[0]
                } else
                    it.url, urlX = ""
            )
            videoUrlsOrigin.add(videoUrl)

        }


        var avatarUrlOrigin =
            if (mFeedDetailsBean!!.userInfo.avatarUrl.contains("?sign")) {
                mFeedDetailsBean!!.userInfo.avatarUrl.split("?sign")[0]
            } else
                mFeedDetailsBean!!.userInfo.avatarUrl

        var postEntity = SharePostEntity(
            picUrls = picUrlsOrigin,
            videoUrls = videoUrlsOrigin,
            title = if (TextUtils.isEmpty(mFeedDetailsBean!!.title))
                "" else mFeedDetailsBean!!.title!!,
            avatarUrl = avatarUrlOrigin,
            nickName = mFeedDetailsBean!!.userInfo.nickName,
            userId = mFeedDetailsBean!!.userInfo.id.toString(),
            id = mFeedDetailsBean!!.id.toString(),
            type = 1
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
                mActivity,
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
                        if (shareType == TIMCommonConstants.SHARE_CLICK_TYPE_QQ) {
                            var file = ImageUtils.save2Album(
                                ImageUtils.getBitmap(path),
                                Bitmap.CompressFormat.JPEG
                            )
                            content.url = file!!.path
                        }
//                        FileUtils.delete(path)

                        shareCallback(shareType, content)
                    }

                    override fun onFailed(throwable: Throwable?) {
                        Toast.makeText(
                            mActivity,
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
        Social.share(this@ListVideoActivity,
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
}