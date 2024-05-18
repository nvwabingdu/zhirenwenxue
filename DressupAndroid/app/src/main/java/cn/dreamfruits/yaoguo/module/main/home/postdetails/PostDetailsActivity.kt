package cn.dreamfruits.yaoguo.module.main.home.postdetails

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.ShareWebContent
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.DotIndicatorView
import cn.dreamfruits.yaoguo.module.main.home.MaxRecyclerView
import cn.dreamfruits.yaoguo.module.main.home.NetworkUtilsJava
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.AttentionDressAdapter
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.ViewPagerFragmentAdapter
import cn.dreamfruits.yaoguo.module.main.home.commentreplay.AtUserAdapter
import cn.dreamfruits.yaoguo.module.main.home.commentreplay.ShowKeyboardAndPop
import cn.dreamfruits.yaoguo.module.main.home.customview.ZrDefaultPageView
import cn.dreamfruits.yaoguo.module.main.home.dialog.ShowNoFollowPop
import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.ChildCommentListAdapter
import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.CommentListAdapter
import cn.dreamfruits.yaoguo.module.main.home.postdetails.commentpart.CommentViewModel
import cn.dreamfruits.yaoguo.module.main.home.postdetails.custombanner.onlyimg.MyPagerAdapter
import cn.dreamfruits.yaoguo.module.main.home.postdetails.custombanner.scaleimg.BannerImgFragment
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManager
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManagerListener
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity
import cn.dreamfruits.yaoguo.module.main.home.zrrichtext.ExpandableTextView
import cn.dreamfruits.yaoguo.module.main.message.bean.SharePostEntity
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.main.mine.MineFragment
import cn.dreamfruits.yaoguo.module.pop.BottomPop
import cn.dreamfruits.yaoguo.module.share.ShareAllPop
import cn.dreamfruits.yaoguo.module.share.SharePostPop
import cn.dreamfruits.yaoguo.module.share.ShareToActivity
import cn.dreamfruits.yaoguo.module.share.ShareViewModel
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.module.share.state.ShareShortUrlState
import cn.dreamfruits.yaoguo.module.share.state.ShareUseRecomendListState
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.singleClick
import cn.dreamfruits.yaoguo.view.LoadingPop
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import java.lang.reflect.Field
import java.util.*
import android.util.DisplayMetrics as DisplayMetrics1


/**
 * @Author qiwangi
 * @Date 2023/4/10
 * @TIME 10:18
 */
open class
PostDetailsActivity : BaseActivity() {
    private lateinit var back: ImageView
    private lateinit var shareImg: ImageView
    private lateinit var name: TextView
    private lateinit var address: TextView
    private lateinit var content: ExpandableTextView
    private lateinit var postTitle: TextView
    private lateinit var time: TextView
    private lateinit var postCount: TextView
    private lateinit var replaytext: TextView
    private lateinit var addressLayout: View
    private lateinit var dressLayout: View//衣装 图标 数量
    private lateinit var postIcon: ImageView//当前帖子头像
    private lateinit var postOwnIcon: ImageView//自己的头像

    //图片banner
    private lateinit var viewPager: ViewPager
    private lateinit var dl_user: DrawerLayout
    private lateinit var bannerLayout: View
    private lateinit var imgNumber: TextView//图片翻页数字
    private lateinit var dotIndicatorView: DotIndicatorView//banner小圆点
    private lateinit var pagerAdapter: MyPagerAdapter//banner适配器
    private lateinit var mViewPagerFragmentAdapter: ViewPagerFragmentAdapter//banner适配器
    private var count = 0 //图片数量
    private var images: ArrayList<String>? = ArrayList()

    //banner方式2
    var fragments: MutableList<Fragment> = arrayListOf()
    var mTitleList: MutableList<String> = arrayListOf()//标题集合

    //ViewModel
    private val postDetailsViewModel by viewModels<PostDetailsViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private val shareViewModel by viewModels<ShareViewModel>()//公共的viewmodel

    //衣装
    private var mDressMaxRecyclerView: MaxRecyclerView? = null

    //底部逻辑
    private lateinit var commentBottomLayout: View
    private lateinit var laudPic: ImageView
    private lateinit var laudTv: TextView
    private lateinit var collectPic: ImageView
    private lateinit var collectTv: TextView
    private lateinit var commentPic: ImageView
    private lateinit var commentTv: TextView
    private lateinit var dressPic: ImageView
    private lateinit var dressTv: TextView
    private lateinit var dressTry: View//一键试穿


    private lateinit var followLayout: View//关注布局
    private lateinit var followLl: View//关注
    private lateinit var followTvEd: View//已关注

    //部分变量
    private var feedId: Long = -1

    private var mLaudCount: Int = 0
    private var mCollectCount: Int = 0
    private var mCommentCount: Int = 0


    //互动消息传递过来需要的参数
    private var isComment: Boolean = false
    private var highlightId: Long = 0L
    private var messageIds: String? = null//消息页过来需要的id
    private var messageCommentIds: String? = null//消息页面过来的id

    private var commentEditLayout: LinearLayout? = null

    var mTag = 1

    private var defaultPageView: ZrDefaultPageView? = null

    private lateinit var userCenter: MineFragment


    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun layoutResId(): Int = R.layout.home_activity_post_details
    override fun initData() {}
    override fun initView() {


        mActivity = this//赋值

        /**没有衣装的时候显示这个*/
        commentEditLayout = findViewById(R.id.comment_edit_layout)//评论输入框 这里跳评论

        commentEditLayout!!.setOnClickListener {
            if (feedId.toInt() != -1) {
                isChildComment = false
                //帖子发布评论
                mShowKeyboardAndPop.showKeyboard(this, this, feedId, 0, "", 0, false)
            }
        }

        //消息页独有
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

        commentBottomLayout = findViewById(R.id.comment_bottom_layout)
        laudPic = findViewById(R.id.laud_pic)// 点赞逻辑
        laudTv = findViewById(R.id.laud_tv)
        collectTv = findViewById(R.id.collect_tv)//收藏逻辑
        collectPic = findViewById(R.id.collect_pic)
        dressPic = findViewById(R.id.dress_pic)//衣装逻辑
        dressTv = findViewById(R.id.dress_tv)
        dressPic.setOnClickListener {
            Toast.makeText(this, "衣装", Toast.LENGTH_SHORT).show()
        }

        dressTry = findViewById(R.id.dress_try)//一键试穿

        back = findViewById(R.id.home_post_back)//back返回
        back.setOnClickListener {
            this.finish()
        }
        name = findViewById(R.id.home_post_name)
        address = findViewById(R.id.home_post_address)
        addressLayout = findViewById(R.id.home_post_address_layout)
        dressLayout = findViewById(R.id.dress_layout)
        dl_user = findViewById(R.id.dl_user)


        followLayout = findViewById(R.id.follow_layout)//关注layout  点击关注本帖子的用户
        followLl = findViewById(R.id.follow_ll)//关注
        followTvEd = findViewById(R.id.followed_tv)//已关注

        postTitle = findViewById(R.id.home_post_title)
        content = findViewById(R.id.expanded_text)
        time = findViewById(R.id.home_post_time)
        postCount = findViewById(R.id.home_post_post_count)
        replaytext = findViewById(R.id.home_post_replay_edittext)//回复帖子输入框
        postIcon = findViewById(R.id.home_post_icon)
        //头像跳转
        postIcon.setOnClickListener {
            if (postDetailsViewModel.mFeedDetailsBean == null) {
                return@setOnClickListener
            }

            if (postDetailsViewModel.mFeedDetailsBean!!.userInfo == null) {
                return@setOnClickListener
            }

            /**
             * 跳转到个人中心
             */
            Singleton.startOtherUserCenterActivity(
                this,
                postDetailsViewModel.mFeedDetailsBean!!.userInfo.id ?: 0L
            )
        }
        postOwnIcon = findViewById(R.id.home_post_own_icon)

        postOwnIcon.setOnClickListener {
            if (feedId.toInt() != -1) {
                isChildComment = false
                //帖子发布评论
                mShowKeyboardAndPop.showKeyboard(this, this, feedId, 0, "", 0, false)
            }
        }

        shareImg = findViewById(R.id.home_post_share_img)//分享 或 省略号

        initShareData()

        //获取feedID
        try {
            feedId = intent.getLongExtra("feedId", 0)//需要使用getLongExtra
            postDetailsViewModel.getFeedDetail(feedId)//请求帖子详情
        } catch (e: Exception) {
            Toast.makeText(this, "id exception", Toast.LENGTH_SHORT).show()
        }

        //图片banner
        if (intent.getBooleanExtra("isMessagePage", false)) {
            Log.e("isMessagePage", "isMessagePage")
        } else {
            setBanner()
        }

        //评论
        initViewComment()

        /**重要 请求帖子详情 请求之后再请求评论列表*/

        postDetails()

        //1.底部评论逻辑
        commentPic = findViewById(R.id.comment_pic)
        commentTv = findViewById(R.id.comment_tv)
        commentPic.setOnClickListener {
            if (feedId.toInt() != -1) {
                isChildComment = false
                //帖子发布评论
                mShowKeyboardAndPop.showKeyboard(this, this, feedId, 0, "", 0, false)
            }
        }

        //2.回复评论 弹出键盘
        replaytext.setOnClickListener {
            if (feedId.toInt() != -1) {
                isChildComment = false
                //帖子发布评论
                mShowKeyboardAndPop.showKeyboard(this, this, feedId, 0, "", 0, false)
            }
        }

        /**缺省*/
        defaultPageView = findViewById(R.id.default_page_view)
        defaultPageView!!.setData(
            getString(R.string.default_no_comment),
            R.drawable.comment_pop_default_img
        )
    }


    private fun setBanner() {
        viewPager = findViewById(R.id.viewPager2)
        viewPager.visibility = View.VISIBLE

        //图片比例
        val displayMetrics = resources.displayMetrics
        val w2 = displayMetrics.widthPixels.toFloat() //需要占的宽度 关注页面是屏幕宽度
        val hh = displayMetrics.heightPixels.toFloat() / 3 * 2 //屏幕高度 如果高于屏幕高度 就设置为这个高度
        Log.e("w2312312", displayMetrics.heightPixels.toString())
        Log.e("w23123123333", hh.toString())
        val w = intent.getIntExtra("coverWidth", 0)
        val h = intent.getIntExtra("coverHeight", 0)
        var h2: Float = 0.0f
        try {
            when (Singleton.getImageType(w, h)) {
                1 -> {//比例小于3：4   就为3：4
                    h2 = w2 / (3.0f / 4.0f)
                }

                2 -> {//比例大于3：4  小于4：3  就为1：1
                    h2 = w2 / (w / h.toFloat())
                }

                3 -> {//比例大于4：3  就为4：3
                    h2 = w2 / (4.0f / 3.0f)
                }

                4 -> {//图片宽高比异常
                    h2 = w2
                }
            }
            val params: ViewGroup.LayoutParams = viewPager!!.layoutParams
            //params.height = h2.toInt()
            //这里修改为直接用宽高比设置高度
            val mHeight = (w2 / (w / h.toFloat())).toInt()
            if (mHeight > hh) {//算出来的高度大于屏幕的2/3  就不要超过屏幕的2/3
                params.height = hh.toInt()
            } else {
                params.height = mHeight
            }
            viewPager.layoutParams = params
        } catch (e: Exception) {
            val params: ViewGroup.LayoutParams = viewPager!!.layoutParams
            params.height = w2.toInt()
            viewPager.layoutParams = params
        }

        bannerLayout = findViewById(R.id.banner_layout)//banner父布局
        imgNumber = findViewById(R.id.img_number)//翻页码
        dotIndicatorView = findViewById(R.id.dot_indicator_view)//园点指示器
        bannerLayout.visibility = View.VISIBLE//布局为gone 此处设置为可见

        /**
         * viewpager翻页监听
         */
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                /**绑定指示器*/
                dotIndicatorView.setSelectedIndex(position)
                imgNumber.text = "${position + 1}/${count}"

                // 测试只有ViewPager在第0页时才开启滑动返回
                mSwipeBackHelper!!.setSwipeBackEnable(position == 0)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun setBanner2(height: Int, width: Int) {
        viewPager = findViewById(R.id.viewPager2)
        viewPager.visibility = View.VISIBLE

        //图片比例
        val displayMetrics = resources.displayMetrics
        val w2 = displayMetrics.widthPixels.toFloat() //需要占的宽度 关注页面是屏幕宽度
        val hh = displayMetrics.heightPixels.toFloat() / 3 * 2 //屏幕高度 如果高于屏幕高度 就设置为这个高度
        Log.e("w2312312", displayMetrics.heightPixels.toString())
        Log.e("w23123123333", hh.toString())
        var w = width
        var h = height
        var h2: Float = 0.0f
        try {
            when (Singleton.getImageType(w, h)) {
                1 -> {//比例小于3：4   就为3：4
                    h2 = w2 / (3.0f / 4.0f)
                }

                2 -> {//比例大于3：4  小于4：3  就为1：1
                    h2 = w2 / (w / h.toFloat())
                }

                3 -> {//比例大于4：3  就为4：3
                    h2 = w2 / (4.0f / 3.0f)
                }

                4 -> {//图片宽高比异常
                    h2 = w2
                }
            }
            val params: ViewGroup.LayoutParams = viewPager!!.layoutParams
            //params.height = h2.toInt()
            //这里修改为直接用宽高比设置高度
            val mHeight = (w2 / (w / h.toFloat())).toInt()
            if (mHeight > hh) {//算出来的高度大于屏幕的2/3  就不要超过屏幕的2/3
                params.height = hh.toInt()
            } else {
                params.height = mHeight
            }
            viewPager.layoutParams = params
        } catch (e: Exception) {
            val params: ViewGroup.LayoutParams = viewPager!!.layoutParams
            params.height = w2.toInt()
            viewPager.layoutParams = params
        }

        bannerLayout = findViewById(R.id.banner_layout)//banner父布局
        imgNumber = findViewById(R.id.img_number)//翻页码
        dotIndicatorView = findViewById(R.id.dot_indicator_view)//园点指示器
        bannerLayout.visibility = View.VISIBLE//布局为gone 此处设置为可见


        /**
         * viewpager翻页监听
         */
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                /**绑定指示器*/
                dotIndicatorView.setSelectedIndex(position)
                imgNumber.text = "${position + 1}/${count}"
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun postDetails() {//帖子详情页
        Log.e("postDetails", "postDetails()")
        postDetailsViewModel.feedDetailsBeanState.observe(this) { it ->
            when (it) {
                is FeedDetailsBeanState.Success -> {

                    //没有宽高就从获取的数据中获取
                    setBanner2(
                        postDetailsViewModel.mFeedDetailsBean!!.picUrls[0].height,
                        postDetailsViewModel.mFeedDetailsBean!!.picUrls[0].width
                    )

                    //评论
                    mCommentCount = postDetailsViewModel.mFeedDetailsBean!!.commentCount
                    Singleton.setNumRuler(
                        mCommentCount,
                        commentTv,
                        Singleton.DEFAULT_COMMENT
                    )

                    //点赞
                    mLaudCount = postDetailsViewModel.mFeedDetailsBean!!.laudCount
                    Singleton.setNumRuler(
                        mLaudCount,
                        laudTv,
                        Singleton.DEFAULT_LAUD
                    )
                    if (postDetailsViewModel.mFeedDetailsBean!!.isLaud == 1) {
                        laudPic.setImageResource(R.drawable.home_thumb_ed)
                    } else {
                        laudPic.setImageResource(R.drawable.home_thumb)
                    }
                    laudPic.setOnClickListener {
                        if (postDetailsViewModel.mFeedDetailsBean!!.isLaud == 1) {
                            Log.e("daddada", "取消点赞了")
                            laudPic.setImageResource(R.drawable.home_thumb)
                            postDetailsViewModel.mFeedDetailsBean!!.isLaud = 0

                            Singleton.setNumRuler2(
                                laudTv,
                                Singleton.DEFAULT_LAUD,
                                false
                            )

                            //请求
                            commonViewModel.getUnLudFeed(postDetailsViewModel.mFeedDetailsBean!!.id)
                        } else {
                            Log.e("daddada", "点赞了")
                            laudPic.setImageResource(R.drawable.home_thumb_ed)
                            postDetailsViewModel.mFeedDetailsBean!!.isLaud = 1
                            Singleton.setNumRuler2(
                                laudTv,
                                Singleton.DEFAULT_LAUD,
                                true
                            )
                            //请求
                            commonViewModel.getLaudFeed(postDetailsViewModel.mFeedDetailsBean!!.id)
                        }
                    }
                    //收藏
                    mCollectCount = postDetailsViewModel.mFeedDetailsBean!!.collectCount
                    Singleton.setNumRuler(
                        mCollectCount,
                        collectTv,
                        Singleton.DEFAULT_COLLECT
                    )
                    if (postDetailsViewModel.mFeedDetailsBean!!.isCollect == 1) {
                        collectPic.setImageResource(R.drawable.home_collect_ed)
                    } else {
                        collectPic.setImageResource(R.drawable.home_collect)
                    }
                    collectPic.setOnClickListener {
                        if (postDetailsViewModel.mFeedDetailsBean!!.isCollect == 1) {
                            Log.e("daddada", "取消收藏了")
                            collectPic.setImageResource(R.drawable.home_collect)
                            postDetailsViewModel.mFeedDetailsBean!!.isCollect = 0
                            Singleton.setNumRuler2(
                                collectTv,
                                Singleton.DEFAULT_COLLECT,
                                false
                            )
                            //取消收藏
                            commonViewModel.getUnCollect(
                                postDetailsViewModel.mFeedDetailsBean!!.id,
                                0
                            )
                        } else {
                            Log.e("daddada", "收藏了")
                            collectPic.setImageResource(R.drawable.home_collect_ed)
                            postDetailsViewModel.mFeedDetailsBean!!.isCollect = 1
                            Singleton.setNumRuler2(
                                collectTv,
                                Singleton.DEFAULT_COLLECT,
                                true
                            )
                            //收藏
                            commonViewModel.getCollect(
                                postDetailsViewModel.mFeedDetailsBean!!.id,
                                0,
                                ""
                            )
                        }
                    }


                    /**若果是自己帖子 隐藏关注*/
                    if (postDetailsViewModel.mFeedDetailsBean!!.userInfo.id == Singleton.getUserInfo().userId) {
                        followLayout.visibility = View.GONE
                    } else {
                        //关注   0:未关注 1:我关注他 2:他关注我 3:互相关注
                        if (postDetailsViewModel.mFeedDetailsBean!!.relation != null) {
                            when (postDetailsViewModel.mFeedDetailsBean!!.relation) {
                                0 -> {//没有关注 显示黑色关注按钮
                                    followLl.visibility = View.VISIBLE
                                }

                                1 -> {//关注了 显示灰色已关注按钮
                                    followTvEd.visibility = View.VISIBLE
                                }

                                2 -> {
                                    followLl.visibility = View.VISIBLE
                                }

                                3 -> {
                                    followTvEd.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                    //=============

                    /**点击关注布局*/
                    followLayout.setOnClickListener {
                        //关注   0:未关注 1:我关注他 2:他关注我 3:互相关注
                        if (postDetailsViewModel.mFeedDetailsBean!!.relation != null) {
                            when (postDetailsViewModel.mFeedDetailsBean!!.relation) {
                                0 -> {

                                    postDetailsViewModel.mFeedDetailsBean!!.relation = 1
                                    followTvEd.visibility = View.VISIBLE
                                    followLl.visibility = View.GONE
                                    //关注请求
                                    commonViewModel.getFollowUser(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)



                                }

                                1 -> {
                                    val pop: ShowNoFollowPop = ShowNoFollowPop()
                                    pop.showPop(this)
                                    pop.setShowNoFollowPopCallBack(object : ShowNoFollowPop.InnerInterface{
                                        override fun onclick(state: Int) {
                                            when(state){
                                                0->{}
                                                1->{//操作在这里
                                                    postDetailsViewModel.mFeedDetailsBean!!.relation = 0
                                                    followLl.visibility = View.VISIBLE
                                                    followTvEd.visibility = View.GONE
                                                    //取消关注请求
                                                    commonViewModel.getUnfollowUser(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)
                                                }
                                            }
                                        }
                                    })
                                }

                                2 -> {
                                    postDetailsViewModel.mFeedDetailsBean!!.relation = 3
                                    followTvEd.visibility = View.VISIBLE
                                    followLl.visibility = View.GONE
                                    //关注请求
                                    commonViewModel.getFollowUser(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)
                                }

                                3 -> {
                                    val pop: ShowNoFollowPop = ShowNoFollowPop()
                                    pop.showPop(this)
                                    pop.setShowNoFollowPopCallBack(object : ShowNoFollowPop.InnerInterface{
                                        override fun onclick(state: Int) {
                                            when(state){
                                                0->{}
                                                1->{//操作在这里
                                                    postDetailsViewModel.mFeedDetailsBean!!.relation = 2
                                                    followLl.visibility = View.VISIBLE
                                                    followTvEd.visibility = View.GONE
                                                    //取消关注请求
                                                    commonViewModel.getUnfollowUser(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)
                                                }
                                            }
                                        }
                                    })


                                }
                            }
                        }
                    }


                    //==============








                    //衣装
                    Singleton.setNumRuler(
                        postDetailsViewModel.mFeedDetailsBean!!.wearCount,
                        dressTv,
                        Singleton.DEFAULT_WEAR
                    )

                    if (postDetailsViewModel.mFeedDetailsBean!!.singleList != null && postDetailsViewModel.mFeedDetailsBean!!.singleList.size > 0) {
                        //衣装列表
                        setDress(postDetailsViewModel.mFeedDetailsBean!!.singleList)
                    } else {
                        //没有衣装的时候
                        commentEditLayout!!.visibility = View.VISIBLE
                        dressTry.visibility = View.GONE
                        dressLayout.visibility = View.INVISIBLE
                        dressTry.isEnabled = false
                    }

                    //套装试穿
                    dressTry.setOnClickListener {
                        if (postDetailsViewModel.mFeedDetailsBean!!.singleList != null && postDetailsViewModel.mFeedDetailsBean!!.singleList.size > 0) {

                            val str: StringBuffer = StringBuffer()
                            postDetailsViewModel.mFeedDetailsBean!!.singleList.forEach {
                                str.append(it.id).append(",")
                            }

                            var str2: String =
                                "[" + str.toString().substring(0, str.toString().length - 1) + "]"

                            Log.e("str2", str2)

                            Singleton.isNewScene = true
                            val intent = Intent(this, AndroidBridgeActivity::class.java)
                            intent.putExtra("entryType", "EnterTryMatchPlan")
                            intent.putExtra("tryClothes", str2)
                            startActivity(intent)
                        } else {
                            ToastUtils.showShort("暂无衣装")
                        }
                    }


                    //名字
                    name.text = postDetailsViewModel.mFeedDetailsBean!!.userInfo.nickName
                    name.setOnClickListener {
                        /**
                         * 跳转到个人中心
                         */
                        Singleton.startOtherUserCenterActivity(
                            this,
                            postDetailsViewModel.mFeedDetailsBean!!.userInfo.id ?: 0L
                        )
                    }

                    addUserCenter(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)

                    //地址
                    if (postDetailsViewModel.mFeedDetailsBean!!.address == null || postDetailsViewModel.mFeedDetailsBean!!.address.toString() == "") {
                        addressLayout.visibility = View.GONE
                    } else {
                        addressLayout.visibility = View.VISIBLE
                        address.text = postDetailsViewModel.mFeedDetailsBean!!.address.toString()
                    }


                    if (postDetailsViewModel.mFeedDetailsBean!!.title.equals("")){
                        postTitle.visibility = View.GONE
                    }else{
                        postTitle.visibility = View.VISIBLE
                        //标题
                        postTitle.text = postDetailsViewModel.mFeedDetailsBean!!.title
                    }


                    if (postDetailsViewModel.mFeedDetailsBean!!.content.equals("")){
                        content.visibility = View.GONE

                    }else{
                        try {
                            //富文本逻辑
                            if (postDetailsViewModel.mFeedDetailsBean!!.content != null && postDetailsViewModel.mFeedDetailsBean!!.content != "") {
                                Singleton.setRichStr(
                                    content,
                                    28f,
                                    postDetailsViewModel.mFeedDetailsBean!!.content,
                                    this,//上下文
                                    postDetailsViewModel.mFeedDetailsBean!!.atUser.toMutableList(),
                                    postDetailsViewModel.mFeedDetailsBean!!.config.toMutableList(),
                                    "",
                                    "",
                                    1000,
                                    false
                                )
                            }
                        } catch (e: Exception) {
                            content.text = ""
                        }
                    }







                    //时间
                    time.text = Singleton.timeShowRule(
                        postDetailsViewModel.mFeedDetailsBean!!.createTime,
                        true
                    )

                    //共多少条评论
                    postCount.text =
                        "共${postDetailsViewModel.mFeedDetailsBean!!.commentCount}条评论"

                    //评论头像
                    Glide.with(this)
                        .asBitmap()
                        .dontAnimate()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .error(R.drawable.temp_icon)
                        .load(postDetailsViewModel.mFeedDetailsBean!!.userInfo.avatarUrl)
                        .into(postIcon)
                    /**
                     * 个人头像
                     */
                    Glide.with(this)
                        .asBitmap()
                        .dontAnimate()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .error(R.drawable.temp_icon)
                        .load(Singleton.UserAvtarUrl)//随机数据
                        .into(postOwnIcon)

                    /**显示分享  还是三个点*/
                    if (postDetailsViewModel.mFeedDetailsBean!!.userInfo.id == Singleton.getUserInfo().userId) {//如果当前id==自己的id
                        shareImg.setImageResource(R.drawable.home_three_points)
                        shareImg.setOnClickListener {
//                            Toast.makeText(this, "自己的", Toast.LENGTH_SHORT).show()
                            mTag = 0
                            shareViewModel.getRecommendShareUserList()
                        }
                    } else {
                        shareImg.setImageResource(R.drawable.home_share)
                        shareImg.singleClick {
                            mTag = 1
                            shareViewModel.getRecommendShareUserList()
                        }
                    }


                    //图片banner 数据
                    count = postDetailsViewModel.mFeedDetailsBean!!.picUrls.size//图片数量
                    dotIndicatorView.setCount1(count)//图片指示器  需要设置小点数量
                    if (count <= 1) {
                        imgNumber.visibility = View.GONE
                        dotIndicatorView.visibility = View.GONE
                    } else {
                        imgNumber.text = "${1}/${count}"
                        imgNumber.visibility = View.VISIBLE
                        dotIndicatorView.visibility = View.VISIBLE
                    }

                    //banner
                    postDetailsViewModel.mFeedDetailsBean!!.picUrls.forEach {
                        //不添加标题
                        val mBannerImgFragment = BannerImgFragment()
                        val bundle2 = Bundle()
                        bundle2.putString("url", it.url)//添加图片地址
                        bundle2.putInt("position", 0)
                        mBannerImgFragment.arguments = bundle2
                        fragments.add(mBannerImgFragment)
                    }
                    //banner设置适配器
                    mViewPagerFragmentAdapter =
                        ViewPagerFragmentAdapter(supportFragmentManager, fragments)
                    viewPager.adapter = mViewPagerFragmentAdapter

                    /**
                     * 请求评论列表 通过回调
                     */
                    if (isComment) {
                        getCommentList(feedId, messageIds, messageCommentIds)
                    } else {
                        getCommentList(feedId, null, null)
                    }
                }

                is FeedDetailsBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    /**
                     * 请求评论列表 通过回调
                     */
                    if (isComment) {
                        getCommentList(feedId, messageIds, messageCommentIds)
                    } else {
                        getCommentList(feedId, null, null)
                    }
                }
            }
        }
    }

    private fun setDress(dataList: MutableList<WaterfallFeedBean.Item.Info.Single>) {
        mDressMaxRecyclerView = findViewById(R.id.attention_dress_MaxRecyclerView)
        mDressMaxRecyclerView!!.visibility = View.VISIBLE//设置显示

        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mDressMaxRecyclerView!!.layoutManager = mLayoutManager

        var attentionDressAdapter = AttentionDressAdapter(this, dataList)
        mDressMaxRecyclerView!!.adapter = attentionDressAdapter
    }

    /**
     * =================================================================评论
     * 初始化
     */
    private val commentViewModel by viewModels<CommentViewModel>()
    private var mRecyclerview: MaxRecyclerView? = null
    private var commentListAdapter: CommentListAdapter? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var commentEnd: View? = null

    //评论列表
    private var mFirstCommentList: MutableList<CommentBean.Item>? = ArrayList()

    //获取其实例  重要对象
    private val mShowKeyboardAndPop = ShowKeyboardAndPop()

    //at用户适配器
    private var tempAtUserAdapter: AtUserAdapter? = null
    private var isAtUserFirst: Boolean = false
    private var isSearchUserFirst: Boolean = false
    private var mScrollView: NestedScrollView? = null
    private var mActivity: Activity? = null

    private var isLoadMore: Boolean = false
    private var isNoMoreData: Boolean = false
    private var isLoadMoreSucceed: Boolean = false
    private fun initViewComment() {
        //控件相关
        commentEnd = findViewById(R.id.comment_end)//末尾标记
        mRecyclerview = findViewById(R.id.comment_recyclerview) //评论集合
        // TODO: 作者的userid
        commentListAdapter = CommentListAdapter(-1,this, mFirstCommentList!!)//评论适配器
        mRecyclerview!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerview!!.layoutManager = mLinearLayoutManager
        mRecyclerview!!.adapter = commentListAdapter//设置适配器
        mScrollView = findViewById(R.id.scroll_view)

        /**
         * 刷新控件
         */
        val refreshLayout: RefreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout.setRefreshFooter(ClassicsFooter(this))
        refreshLayout.setEnableRefresh(false)//不用下拉刷新
        //只用加载方法
        refreshLayout.setOnLoadMoreListener { refreshLayout ->
            //设置标签为加载（不刷新）
            isLoadMore = true

            //2.请求
            if (isComment) {
                getCommentList(feedId, messageIds, messageCommentIds)
            } else {
                getCommentList(feedId, null, null)
            }

            //4.根据返回结果设置加载结果
            if (isNoMoreData) {//没有更多数据了
                refreshLayout.finishLoadMoreWithNoMoreData()//显示全部加载完成，并不再触发加载更事件
            } else {
                refreshLayout.finishLoadMore(isLoadMoreSucceed)
            }
        }

        /**
         * 评论适配器回调
         */
        commentListAdapter!!.setCommentCallBack(object : CommentListAdapter.CommentInterface {
            //1.点赞回调
            override fun onLaudClick(isLaud: Boolean, id: Long) {
                if (isLaud) {
                    commentViewModel.getLaudComment(id)//点赞
                } else {
                    commentViewModel.getUnLaudComment(id)//取消点赞
                }
            }

            //2.点击item 长按item 回调
            override fun onItemClick(
                isLongClick: Boolean,
                Id: Long,
                isChild: Boolean,
                replyUser: String,
                replyId: Long,
                replyContent: String,
                mChildCommentListAdapter: ChildCommentListAdapter?,
                position: Int,
            ) {
                mFirstPosition = position

                if (isLongClick) {//长按
                    commonPopup(Id, replyUser, replyId, replyContent)//弹出举报 回复等弹窗
                } else {//短按
                    //评论的评论的评论  3.点击子级item的弹窗
                    mShowKeyboardAndPop.showKeyboard(
                        mActivity,
                        mActivity,
                        Id,
                        1,
                        "回复 @${replyUser}:",
                        replyId,
                        true
                    )
                }
            }

            //3.父评论暂无操作    子评论展开 收起回调
            override fun onLoadMoreClick(ids: String, position: Int) {
                mFirstPosition = position//角标赋值
                getChildCommentList(ids)//子级接口请求  展开更多   这里回来的就是子级的点击   展开逻辑   收起逻辑
            }
        })

        /**
         * 点赞请求结果
         */
        commentViewModel.laudCommentState.observe(this) {
            when (it) {
                is LaudCommentState.Success -> {

                }

                is LaudCommentState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        /**
         * 取消点赞请求结果
         */
        commentViewModel.unLaudCommentState.observe(this) {
            when (it) {
                is UnLaudCommentState.Success -> {

                }

                is UnLaudCommentState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        /**
         * 关注  关系 0:未关注 1:我关注他 2:他关注我 3:互相关注
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

        //评论列表请求返回注册
        commentList()

        /**
         * 艾特用户逻辑
         */
        commentAtUser()

        /**
         * 发布评论逻辑
         */
        commentPublish()
    }

    var isChildComment = false//默认是父级  就是帖子评论
    var mFirstPosition = 0//评论角标

    /**
     * @用户
     */
    private fun commentAtUser() {
        //请求艾特用户接口  回调
        mShowKeyboardAndPop.setSearchUserClickListener(object :
            ShowKeyboardAndPop.SearchUserInterface {
            override fun onclick(
                atUserAdapter: AtUserAdapter,
                isGetAtUserList: Boolean?,
                key: String,
                isKeyChange: Boolean,//所搜索的内容是否变化
                atUser: String?,
                config: String?,
            ) {

                tempAtUserAdapter = atUserAdapter//取到适配器实例

                if (isGetAtUserList == true) {//请求at用户列表
                    if (isKeyChange) {
                        isAtUserFirst = true
                        commentViewModel.getAtUserList(10, 0, null)
                    } else {
                        isAtUserFirst = false
                        commentViewModel.getAtUserList(
                            10,
                            commentViewModel.mAtUserListBean!!.lastTime,
                            atUser!!
                        )//加载更多
                    }
                    //分页
                } else {//请求搜索用户接口
                    if (isKeyChange) {//请求词变化了  页码不变  集合清空  设置适配器
                        isSearchUserFirst = true
                        commentViewModel.getSearchUser(
                            key,//搜索词
                            1,//页码，第一次穿1，后面传入上一次接口返回得page
                            10,//数量
                            null,//用于分页，传入上一次接口返回lastTime
                            null//过滤的用户的ids，例如：id1,id2,id3
                        )
                    } else {//请求词没有变  说明需要继续请求  页码使用上次的页码
                        isSearchUserFirst = false
                        commentViewModel.getSearchUser(
                            key,//搜索词
                            commentViewModel.mSearchUserBean!!.page,//页码，第一次穿1，后面传入上一次接口返回得page
                            10,//数量
                            commentViewModel.mSearchUserBean!!.lastTime,//用于分页，传入上一次接口返回lastTime
                            atUser//过滤的用户的ids，例如：id1,id2,id3
                        )
                    }
                }
            }
        })


        //请求at用户列表 接口返回
        commentViewModel.atUserListBeanState.observe(this) { it ->
            when (it) {
                is AtUserListBeanState.Success -> {
                    tempAtUserAdapter!!.setData(
                        commentViewModel.mAtUserListBean!!.list.toMutableList(),
                        isAtUserFirst
                    )
                }

                is AtUserListBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        //请求搜索用户接口  接口返回
        commentViewModel.searchUserBeanState.observe(this) { it ->
            when (it) {
                is SearchUserBeanState.Success -> {
                    tempAtUserAdapter!!.setData(
                        commentViewModel.mSearchUserBean!!.list.toMutableList(),
                        isSearchUserFirst
                    )
                }

                is SearchUserBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }
    }

    /**
     * =================================================================请求评论列表：帖子评论通过加载    子级评论通过点击加载更多
     *
    id,//动态id，一级评论id
    0,//0-动态 1-评论
    20,//每页条数
    null,//第一次请求传个0
    null//排除的id，例如：11111111,22222222 //随便传
     */
    private fun getCommentList(id: Long, ids: String?, commentIds: String?) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (commentViewModel.mCommentListBean == null) {//等于空 说明没有请求过，发出第一次请求
            commentViewModel.getCommentList(id, 0, 20, null, ids, commentIds)
        } else if (commentViewModel.mCommentListBean != null && commentViewModel.mCommentListBean!!.hasNext == 1) {
            commentViewModel.getCommentList(
                id,
                0,
                20,
                commentViewModel.mCommentListBean!!.lastTime,
                ids,
                commentIds
            )
        } else if (commentViewModel.mCommentListBean != null && commentViewModel.mCommentListBean!!.hasNext == 0) {//表示没有更多数据了
            isNoMoreData = true
            if (defaultPageView!!.visibility == View.VISIBLE) {
                commentEnd!!.visibility = View.GONE
            } else {
                commentEnd!!.visibility = View.VISIBLE
            }
        }
    }

    //子评论 通过展开按钮
    private fun getChildCommentList(ids: String) {
        if (mFirstCommentList!![mFirstPosition].lastTimeChild == 0L) {//等于0 说明没有请求过，发出第一次请求   第一次必然为零  当时间戳为零的时候
            commentViewModel.getCommentListChild(
                mFirstCommentList!![mFirstPosition].id,
                1,
                5,
                null,
                ids
            )
        } else if (mFirstCommentList!![mFirstPosition].hasNextChild == 1) {
            commentViewModel.getCommentListChild(
                mFirstCommentList!![mFirstPosition].id,
                1,
                5,
                mFirstCommentList!![mFirstPosition].lastTimeChild,
                ids
            )
        } else if (mFirstCommentList!![mFirstPosition].hasNextChild == 0) {//表示没有更多数据了
            //没有数据了就设置
            mFirstCommentList!![mFirstPosition].isChildNoMoreData = true
        }
    }

    //评论列表加载
    private fun commentList() {
        //帖子评论列表
        commentViewModel.commentListBeanState.observe(this) {
            when (it) {
                is CommentListBeanState.Success -> {
                    if (mFirstCommentList!!.size == 0) {//说明之前没有加载数据
                        mFirstCommentList = commentViewModel.mCommentListBean!!.list

                        Log.e("aaa11110000", highlightId.toString())
                        //找到需要高亮的id 然后设置为true
                        if (mFirstCommentList!!.size >= 1) {//至少有一条数据
                            if (mFirstCommentList!![0].id == highlightId) {
                                mFirstCommentList!![0].isChangeColor = true
                                Log.e("aaa11111111", mFirstCommentList!![0].id.toString())
                            }
                            if (mFirstCommentList!![0].replys != null) {
                                mFirstCommentList!![0].replys!!.forEach {
                                    if (it.id == highlightId) {
                                        Log.e("aaa1111222", it.id.toString())
                                        it.isChangeColor = true
                                    }
                                }
                            }
                        }
                        commentListAdapter!!.setData(mFirstCommentList!!, true)
                    } else {//有数据
                        commentListAdapter!!.setData(
                            commentViewModel.mCommentListBean!!.list,
                            false
                        )//把装好的临时集合设置进去
                    }

                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            if (isComment) {
                                setViewToCenter(replaytext)
                                isComment = false
                            }
                        }
                    }, 500) // 延迟500毫秒


                    //2.是否显示缺省
                    isShowDefault()
                    //3.状态初始化
                    isLoadMore = false
                    isLoadMoreSucceed = true
                }

                is CommentListBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.是否显示缺省
                    isShowDefault()
                    //3.状态初始化
                    isLoadMore = false
                    isLoadMoreSucceed = false
                }
            }
        }

        //子级评论列表
        commentViewModel.childCommentBeanState.observe(this) {
            when (it) {
                is ChildCommentBeanState.Success -> {
                    mFirstCommentList!![mFirstPosition].hasNextChild =
                        commentViewModel.mChildCommentBean!!.hasNext
                    mFirstCommentList!![mFirstPosition].lastTimeChild =
                        commentViewModel.mChildCommentBean!!.lastTime
                    mFirstCommentList!![mFirstPosition].totalCountChild =
                        commentViewModel.mChildCommentBean!!.totalCount
                    mFirstCommentList!![mFirstPosition].isChildNoMoreData =
                        (commentViewModel.mChildCommentBean!!.hasNext == 0)//重要逻辑
                    mFirstCommentList!![mFirstPosition].mChildCommentListAdapter!!.setData(
                        commentViewModel.mChildCommentBean!!.list,
                        false
                    )//把装好的临时集合设置进去
                    commentListAdapter!!.updateItem(mFirstPosition)//更新此条数据
                }

                is ChildCommentBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }
    }

    /**
     * ScrollView 中的控件移动到中央
     */
    private fun setViewToCenter(tempView: View) {
        val locationInScrollView = IntArray(2)
        tempView.getLocationInWindow(locationInScrollView)//测量需要移动的View相对于屏幕的距离
        val intArray = IntArray(2)
        mScrollView!!.getLocationOnScreen(intArray)
        val mScrollViewTop = intArray[1]//获取ScrollView距离屏幕顶部的距离
        val scrollDistance =
            locationInScrollView[1] - (mScrollViewTop + windowManager.defaultDisplay.height / 2)
        mScrollView!!.fling(scrollDistance)//加上滑动才有效？
        mScrollView!!.smoothScrollBy(0, scrollDistance)
    }


    /**
     * ===========获取recyclerview的高度
     */
    fun getViewHeight(recyclerView: MaxRecyclerView): Int {
        var height = 0
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 获取RecyclerView的高度
                height = recyclerView.height

                // 取消监听器
                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        return height
    }


    /**
     * =================================================================发评论
    targetId,//动态id，一级评论id
    type,//0-动态 1-评论
    content!!,//评论内容
    null,//回复的评论id
    atUser!!,//at的用户字符串,[{"index":0,"len":10,"id":11111,"name":"张三"},{"index":20,"len":5,"id":"222","name":"李四"}]
    config!!//配置字符串
     */
    private fun commentPublish() {
        //发评论的回调
        mShowKeyboardAndPop.setSendCommentClickListener { targetId, type, content, replyId, atUser, config, isChild ->
            if (isChild) {
                if (replyId == 0L) {
                    commentViewModel.getPublishCommentChild(
                        targetId,
                        type,
                        content,
                        null,
                        atUser,
                        config
                    )
                } else {
                    commentViewModel.getPublishCommentChild(
                        targetId,
                        type,
                        content,
                        replyId,
                        atUser,
                        config
                    )
                }
            } else {
                if (replyId == 0L) {
                    commentViewModel.getPublishComment(
                        targetId,
                        type,
                        content,
                        null,
                        atUser,
                        config
                    )
                } else {
                    commentViewModel.getPublishComment(
                        targetId,
                        type,
                        content,
                        replyId,
                        atUser,
                        config
                    )
                }
            }

        }

        //帖子评论发布接口返回 单条数据
        commentViewModel.commentPublishBeanState.observe(this) { it ->
            when (it) {
                is CommentPublishBeanState.Success -> {
                    //评论成功数量加1
                    mCommentCount += 1
                    Singleton.setNumRuler(mCommentCount, commentTv, Singleton.DEFAULT_COMMENT)

                    //弹窗
                    Singleton.centerToast(this, "评论已发送")

                    //添加一条数据
                    commentListAdapter!!.addItem(commentViewModel.mCommentPublishBean!!)

                    //缺省逻辑
                    isShowDefault()
                }

                is CommentPublishBeanState.Fail -> {
                    Log.e("23131231231", "评论遇到了一点小问题……")
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }

        //子发布评论请求返回 单挑数据
        commentViewModel.commentChildPublishBeanState.observe(this) { it ->
            when (it) {
                is CommentChildPublishBeanState.Success -> {
                    //评论成功数量加1
                    mCommentCount += 1
                    Singleton.setNumRuler(mCommentCount, commentTv, Singleton.DEFAULT_COMMENT)

                    //弹窗
                    Singleton.centerToast(this, "评论已发送")

                    //添加子评论数据
                    mFirstCommentList!![mFirstPosition].mChildCommentListAdapter!!.addItems(
                        commentViewModel.mChildCommentPublishBean!!
                    )//调用添加
                }

                is CommentChildPublishBeanState.Fail -> {
                    if (!NetworkUtilsJava.isNetworkConnected(this)) {
                        Singleton.isNetConnectedToast(this)
                    } else {
                        Singleton.centerToast(this, "评论遇到了一点小问题……")
                    }
                }
            }
        }
    }


    /**
     * ===================================无关紧要==============================缺省页面
     */
    private fun isShowDefault() {
        if (mFirstCommentList!!.size == 0) {
            mRecyclerview!!.visibility = View.GONE
            defaultPageView!!.visibility = View.VISIBLE //评论缺省
            //点击之后弹出弹窗
            defaultPageView!!.setOnTextClickListener {
                isChildComment = false
                //动态评论
                mShowKeyboardAndPop.showKeyboard(this, this, feedId, 0, "", 0, false)
            }

        } else {
            mRecyclerview!!.visibility = View.VISIBLE
            defaultPageView!!.visibility = View.GONE //评论缺省
        }
    }

    /**
     * =================================================================弹窗
     */
    private var commonPopupWindow: PopupWindow? = null//普通弹窗
    private fun commonPopup(
        commentId: Long,
        replyUser: String,
        replyId: Long,
        replyContent: String,
    ) {
        val inflateView: View = layoutInflater.inflate(R.layout.dialog_comment_all, null)

        val replyTips = inflateView.findViewById<TextView>(R.id.dialog_comment_reply_tips)
        replyTips.text = "@${replyUser}: ${replyContent}"
        val reply = inflateView.findViewById<TextView>(R.id.dialog_comment_reply)
        reply.setOnClickListener {
            isChildComment = true
            //评论的评论的
            mShowKeyboardAndPop.showKeyboard(
                this,
                this,
                commentId,
                1,
                "回复 @${replyUser}:",
                replyId,
                true
            )
            if (commonPopupWindow != null && commonPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                commonPopupWindow!!.dismiss()
            }
        }
        val copy = inflateView.findViewById<TextView>(R.id.dialog_comment_copy)
        copy.setOnClickListener {
            val clipboard: ClipboardManager =
                this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", replyContent)
            clipboard.setPrimaryClip(clip)
            Singleton.centerToast(this, "评论已复制")
        }
        val report = inflateView.findViewById<TextView>(R.id.dialog_comment_report)
        report.setOnClickListener {
            if (commonPopupWindow != null && commonPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                commonPopupWindow!!.dismiss()
            }
            replayPopup()//举报子弹窗
        }
        // TODO: 需要设置 判断是否是自己的评论
        val deleteLayout = inflateView.findViewById<View>(R.id.dialog_comment_delete_layout)

        if (replyUser.equals(Singleton.getUserInfo().nickName)) {//如果是自己的帖子 显示隐藏
            deleteLayout.visibility = View.VISIBLE
        } else {
            deleteLayout.visibility = View.GONE
        }

        deleteLayout.setOnClickListener {
            Log.e("23131231231", "删除评论")
            /**调取接口删除评论*/
            if (commentId == 0L){
                commentViewModel.getDeleteComment(replyId.toString())
                /**删除子级评论*/

            }else{
                commentViewModel.getDeleteComment(commentId.toString())
                /**删除父级评论*/
                commentListAdapter!!.delItem(mFirstPosition,false)
            }

            if (commonPopupWindow != null && commonPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                commonPopupWindow!!.dismiss()
            }
        }



        val cancel = inflateView.findViewById<TextView>(R.id.dialog_comment_cancel)
        cancel.setOnClickListener {
            if (commonPopupWindow != null && commonPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                commonPopupWindow!!.dismiss()
            }
        }

        /**
         * pop实例
         */
        commonPopupWindow = PopupWindow(
            inflateView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        //动画
        commonPopupWindow!!.animationStyle = R.style.BottomDialogAnimation

        if (commonPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            commonPopupWindow!!.dismiss()
        } else {
            commonPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//?????这
            commonPopupWindow!!.isOutsideTouchable = true
            commonPopupWindow!!.isTouchable = true
            commonPopupWindow!!.isFocusable = true
            commonPopupWindow!!.showAtLocation(inflateView, Gravity.BOTTOM, 0, 0)
            Singleton.bgAlpha(this, 0.5f) //设置透明度0.5
            commonPopupWindow?.setOnDismissListener {
                Singleton.bgAlpha(this, 1f) //恢复透明度
            }
        }
    }

    /**
     * 2.举报子级弹窗
     */
    private var replayPopupWindow: PopupWindow? = null//举报子级评论
    private fun replayPopup() {
        val inflateView: View = layoutInflater.inflate(R.layout.dialog_comment_all, null)

        val dialogCommentCommon = inflateView.findViewById<View>(R.id.dialog_comment_common)
        dialogCommentCommon.visibility = View.GONE
        val dialogCommentReportLayout =
            inflateView.findViewById<View>(R.id.dialog_comment_report_layout)
        dialogCommentReportLayout.visibility = View.VISIBLE


        val breakTheLaw = inflateView.findViewById<TextView>(R.id.dialog_comment_break_the_law)//违法
        breakTheLaw.setOnClickListener {
            Toast.makeText(this, "违法信息", Toast.LENGTH_SHORT).show()
        }
        val withPornographicContent =
            inflateView.findViewById<TextView>(R.id.dialog_comment_with_pornographic_content)//涉黄
        withPornographicContent.setOnClickListener {
            Toast.makeText(this, "涉黄信息", Toast.LENGTH_SHORT).show()
        }
        val personalAttack =
            inflateView.findViewById<TextView>(R.id.dialog_comment_personal_attack)//人身攻击
        personalAttack.setOnClickListener {
            Toast.makeText(this, "人身攻击", Toast.LENGTH_SHORT).show()
        }
        val rumourFraud =
            inflateView.findViewById<TextView>(R.id.dialog_comment_rumour_fraud)//谣言 欺诈
        rumourFraud.setOnClickListener {
            Toast.makeText(this, "谣言 欺诈", Toast.LENGTH_SHORT).show()
        }

        val cancel = inflateView.findViewById<TextView>(R.id.dialog_comment_cancel)//取消
        cancel.setOnClickListener {
            if (replayPopupWindow != null && replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
                replayPopupWindow!!.dismiss()
            }
        }

        /**
         * pop实例
         */
        replayPopupWindow = PopupWindow(
            inflateView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        //动画
        replayPopupWindow!!.animationStyle = R.style.BottomDialogAnimation

        if (replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            replayPopupWindow!!.dismiss()
        } else {
            replayPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//?????这
            replayPopupWindow!!.isOutsideTouchable = true
            replayPopupWindow!!.isTouchable = true
            replayPopupWindow!!.isFocusable = true
            replayPopupWindow!!.showAtLocation(inflateView, Gravity.BOTTOM, 0, 0)
            Singleton.bgAlpha(this, 0.5f) //设置透明度0.5
            replayPopupWindow?.setOnDismissListener {
                Singleton.bgAlpha(this, 1f) //恢复透明度
            }
        }
    }

    /**
     * 3.消灭弹窗
     */
    override fun onPause() {
        super.onPause()
        if (commonPopupWindow != null && commonPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            commonPopupWindow!!.dismiss()
        }
        if (replayPopupWindow != null && replayPopupWindow!!.isShowing) {//如果正在显示，关闭弹窗。
            replayPopupWindow!!.dismiss()
        }
    }

    private val chartViewModel by viewModels<ChartViewModel>()
    private fun initShareData() {


        /**
         * 删除动态回调
         */
        commonViewModel.deleteFeedState.observe(this) {
            when (it) {
                is DeleteFeedState.Success -> {
                    // TODO: 更新关注列表数据
                    Singleton.centerToast(mContext, "帖子已删除")
                    finish()
//                    mAttentionAdapter
                }
                is DeleteFeedState.Fail -> {
                    //1.是否有断网提示
                    mContext?.let { it1 -> Singleton.isNetConnectedToast(it1) }
                }
            }
        }


        shareViewModel.shortUrl.observe(this)
        {
            when (it) {
                is ShareShortUrlState.Success -> {
                    if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_COPY) {

                        var content = "【腰果】${it.shortUrl}${
                            if (TextUtils.isEmpty(postDetailsViewModel.mFeedDetailsBean!!.title)) {
                                "@${postDetailsViewModel.mFeedDetailsBean!!.userInfo.nickName}"
                            } else {
                                postDetailsViewModel.mFeedDetailsBean!!.title
                            }
                        }在腰果上发布了一条超棒的帖子点击链接直接打开"
                        ClipboardUtils.copyText(content)
                        ToastUtils.showShort("复制成功，快去分享给好友吧")

                    } else if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_POST) {
                        XPopup.Builder(this@PostDetailsActivity)
                            .isDestroyOnDismiss(true)
                            .asCustom(
                                SharePostPop(
                                    mContext = this@PostDetailsActivity,
                                    mFeedDetailsBean = postDetailsViewModel.mFeedDetailsBean!!,
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
                                shareType = TIMCommonConstants.SHARE_TYPE_POST,
                                userList = it.recommendUserListBean!!,
                                mTag = mTag,
                                shareOnClick = object : ShareAllPop.ShareOnClick {
                                    override fun onClickCreatePost() {
                                        if (postDetailsViewModel.mFeedDetailsBean == null) {
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
                                                LoadingPop(mContext = this@PostDetailsActivity).setTitle(
                                                    "发送中"
                                                )
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

                                    override fun onClickDelete() {
                                        super.onClickDelete()
                                        showConfirm(this@PostDetailsActivity, feedId)
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

        postDetailsViewModel.mFeedDetailsBean!!.picUrls.forEach {
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
            if (postDetailsViewModel.mFeedDetailsBean!!.userInfo.avatarUrl.contains("?sign")) {
                postDetailsViewModel.mFeedDetailsBean!!.userInfo.avatarUrl.split("?sign")[0]
            } else
                postDetailsViewModel.mFeedDetailsBean!!.userInfo.avatarUrl

        LogUtils.e(">>> avatarUrl = " + postDetailsViewModel.mFeedDetailsBean!!.userInfo.avatarUrl)
        LogUtils.e(">>> avatarUrlOrigin = " + avatarUrlOrigin)

        var postEntity = SharePostEntity(
            picUrls = picUrlsOrigin,
            videoUrls = arrayListOf(),
            title = if (TextUtils.isEmpty(postDetailsViewModel.mFeedDetailsBean!!.title))
                "" else postDetailsViewModel.mFeedDetailsBean!!.title!!,
            avatarUrl = avatarUrlOrigin,
            nickName = postDetailsViewModel.mFeedDetailsBean!!.userInfo.nickName,
            userId = postDetailsViewModel.mFeedDetailsBean!!.userInfo.id.toString(),
            id = postDetailsViewModel.mFeedDetailsBean!!.id.toString(),
            type = 0,
            coverHeight = postDetailsViewModel.mFeedDetailsBean!!.picUrls.first().height,
            coverWidth = postDetailsViewModel.mFeedDetailsBean!!.picUrls.first().width
        )
        return postEntity
    }

    private fun share(shareType: Int, shortUrl: String) {

        var content = ShareWebContent()
        content.title = if (!TextUtils.isEmpty(postDetailsViewModel.mFeedDetailsBean!!.title)) {
            postDetailsViewModel.mFeedDetailsBean!!.title
        } else {
            "@" + postDetailsViewModel.mFeedDetailsBean!!.userInfo.nickName + "在微酸上发布了一条超棒的帖子"
        }
        content.description =
            if (!TextUtils.isEmpty(postDetailsViewModel.mFeedDetailsBean!!.content)) {
                postDetailsViewModel.mFeedDetailsBean!!.content
            } else {
                "万千好物一键上身，美好生活即刻分享"
            }

        Thread {
            AndroidDownloadManager(
                mActivity,
                postDetailsViewModel.mFeedDetailsBean!!.picUrls.first().url
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
        Social.share(this@PostDetailsActivity,
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

    /**
     * 确认删除帖子
     */
    private fun showConfirm(mActivity: Activity, delFeedId: Long) {
        val strings = arrayListOf("确认删除")
        XPopup.Builder(mContext)
            .isDestroyOnDismiss(true)
            .asCustom(
                BottomPop(mContext, title = "确认删除这条帖子吗?", data = strings)
                    .setOnSelectListener { position, text ->
                        commonViewModel.deleteFeed(delFeedId)
                    }
            ).show()

    }


    private fun addUserCenter(userId: Long) {

        dl_user.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (true) {
            return
        }

        val mineFragment = MineFragment()
        val bundle = Bundle()
        bundle.putLong("userId", userId)
        mineFragment.arguments = bundle
        FragmentUtils.add(supportFragmentManager, mineFragment, R.id.fl_user_center)

        var content = findViewById<FrameLayout>(R.id.fl_user_center)
        val params: ViewGroup.LayoutParams = content.layoutParams
        params.height = ScreenUtils.getScreenHeight()
        params.width = ScreenUtils.getScreenWidth()
        content.layoutParams = params

        dl_user.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {
                mSwipeBackHelper!!.setSwipeBackEnable(false)
            }

            override fun onDrawerClosed(drawerView: View) {
                mSwipeBackHelper!!.setSwipeBackEnable(true)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        setDrawerRightEdgeSize(this, dl_user, 1.0f)
    }

    /**
     * 向右边滑
     * 处理DrawerLayout全屏滑动（默认不会全屏滑动）、然后屏蔽掉长按会弹抽屉的方式
     * @param displayWidthPercentage 1.0表示全屏可滑动 0.5只有右半部分可滑屏 0表示没有滑动事件
     */
    fun setDrawerRightEdgeSize(
        activity: Activity,
        drawerLayout: DrawerLayout,
        displayWidthPercentage: Float,
    ) {
        if (activity == null || drawerLayout == null) return
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            val leftDraggerField: Field =
                drawerLayout.javaClass.getDeclaredField("mRightDragger") //Right
            // true表示可全屏滑动
            leftDraggerField.isAccessible = true
            val leftDragger: ViewDragHelper = leftDraggerField[drawerLayout] as ViewDragHelper

            // 找到 edgeSizeField 并设置 Accessible 为true
            val edgeSizeField: Field = leftDragger.javaClass.getDeclaredField("mEdgeSize")
            // true表示可全屏滑动
            edgeSizeField.isAccessible = true
            val edgeSize = edgeSizeField.getInt(leftDragger)

            // 设置新的边缘大小
            val displaySize = Point()
            activity.windowManager.defaultDisplay.getSize(displaySize)
            edgeSizeField.setInt(
                leftDragger, Math.max(
                    edgeSize, (displaySize.x *
                            displayWidthPercentage).toInt()
                )
            )

            //获取 Layout 的 ViewDragCallBack 实例“mLeftCallback”
            //更改其属性 mPeekRunnable
            val leftCallbackField: Field =
                drawerLayout.javaClass.getDeclaredField("mRightCallback")
            // true 表示不可长按弹窗
            leftCallbackField.isAccessible = true

            //因为无法直接访问私有内部类，所以该私有内部类实现的接口非常重要，通过多态的方式获取实例
            val leftCallback: ViewDragHelper.Callback =
                leftCallbackField[drawerLayout] as ViewDragHelper.Callback
            val peekRunnableField: Field = leftCallback.javaClass.getDeclaredField("mPeekRunnable")
            // true 表示不可长按弹窗
            peekRunnableField.isAccessible = true
            peekRunnableField[leftCallback] = Runnable {}

        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
    }

    override fun onBackPressed() {
        if (dl_user.isDrawerOpen(GravityCompat.END)) {
            dl_user.closeDrawer(GravityCompat.END)
            return
        }
        super.onBackPressed()
    }


}