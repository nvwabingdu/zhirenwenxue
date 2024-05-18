package cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.ShareWebContent
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.ViewPagerFragmentAdapter
import cn.dreamfruits.yaoguo.module.main.home.dialog.ShowNoFollowPop
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManager
import cn.dreamfruits.yaoguo.module.main.home.saveimageandvideo.AndroidDownloadManagerListener
import cn.dreamfruits.yaoguo.module.main.home.state.ChangeClothesItemNameState
import cn.dreamfruits.yaoguo.module.main.home.state.ChangeClothesItemSeeLimitState
import cn.dreamfruits.yaoguo.module.main.home.state.DeleteClothesItemState
import cn.dreamfruits.yaoguo.module.main.home.state.GetClothesItemInfoBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetFeedListByCvIdBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetSimilarRecommendBeanState
import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity
import cn.dreamfruits.yaoguo.module.main.message.bean.ShareSingleEntity
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_SINGLE
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.main.stroll.StrollViewModel
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.similarbanner.SimilarBannerFragment
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.singlefeed.SingleFeedActivity
import cn.dreamfruits.yaoguo.module.share.*
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.module.share.state.ShareShortUrlState
import cn.dreamfruits.yaoguo.module.share.state.ShareUseRecomendListState
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Tool
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.util.singleClick
import cn.dreamfruits.yaoguo.view.LoadingPop
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.RoundLinesIndicator
import com.youth.banner.listener.OnPageChangeListener

/**
 * @Author qiwangi
 * @Date 2023/6/9
 * @TIME 15:26
 */
class SingleDetailsActivity : BaseActivity() {
    private val strollViewModel by viewModels<StrollViewModel>()
    private var mId: Long = 0//服饰id

    var singleShareCover = ""
    override fun layoutResId(): Int = R.layout.activity_single_details
    override fun initData() {}
    override fun initView() {

        // 设置状态栏背景颜色为白色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.white))
        }

        mId = intent.getLongExtra("id", 0L)
        setViewpager()
        setDetails()

        /**注册弹窗部分接口回调*/
        setNetCallback()

    }

    /**
     * 1设置viewpager
     */
    private fun setViewpager() {
        setViewpagerInitView()
        setViewpagerNet()
    }

    private var imageUrls: MutableList<String>? = ArrayList()
    private var viewPager: Banner<*, *>? = null

    //临时使用
    private var item1: View? = null
    private var item2: View? = null
    private var item3: View? = null
    private var item4: View? = null

    fun setItemInit(tag: Int) {
        item1!!.setBackgroundColor(Color.parseColor("#D8D8D8"))
        item2!!.setBackgroundColor(Color.parseColor("#D8D8D8"))
        item3!!.setBackgroundColor(Color.parseColor("#D8D8D8"))
        item4!!.setBackgroundColor(Color.parseColor("#D8D8D8"))

        when (tag) {
            1 -> {
                item1!!.setBackgroundColor(Color.parseColor("#222222"))
            }

            2 -> {
                item2!!.setBackgroundColor(Color.parseColor("#222222"))
            }

            3 -> {
                item3!!.setBackgroundColor(Color.parseColor("#222222"))
            }

            4 -> {
                item4!!.setBackgroundColor(Color.parseColor("#222222"))
            }
        }
    }

    private fun setViewpagerInitView() {
        initViewPagerData()

        viewPager = findViewById(R.id.d_viewpager)

        // TODO: 临时使用
        item1 = findViewById(R.id.d_item1)
        item2 = findViewById(R.id.d_item2)
        item3 = findViewById(R.id.d_item3)
        item4 = findViewById(R.id.d_item4)


        //使用默认的图片适配器
        var banner = (viewPager as Banner<String, BannerImageAdapter<String>>)
        banner.apply {
            addBannerLifecycleObserver(this@SingleDetailsActivity)
//            setIndicator(CircleIndicator(this@SingleDetailsActivity))
            indicator = RoundLinesIndicator(this@SingleDetailsActivity)
            setAdapter(object : BannerImageAdapter<String>(imageUrls) {
                override fun onBindView(
                    holder: BannerImageHolder,
                    data: String,
                    position: Int,
                    size: Int,
                ) {
                    Glide.with(this@SingleDetailsActivity)
                        .load(data.decodePicUrls())
                        .into(holder.imageView)
//                    holder.imageView.setOnClickListener {
//                        XPopup.Builder(this@SingleDetailsActivity)
//                            .asImageViewer(
//                                holder.imageView,
//                                position,
//                                strollViewModel.mGetClothesItemInfoBean!!.coverUrls as List<Any>?,
//                                false,
//                                false,
//                                -1,
//                                -1,
//                                -1,
//                                false,
//                                -1,
//                                { popupView, position -> },
//                                SmartGlideImageLoader()
//                            ) { popupView, position -> }.show()
//                    }
                }
            })
        }

        banner.indicator.indicatorView.visibility = View.GONE
//        banner.setOnBannerListener(object : OnBannerListener<String> {
//            override fun OnBannerClick(data: String?, position: Int) {
//
//
//            }
//        })

        banner.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                Log.e("onPageSelected", position.toString())
                setItemInit(position + 1)
            }
        })

    }

    private fun setViewpagerNet() {

    }

    private fun initViewPagerData() {

    }

    /**
     * 2获取服装单品详情
     */
    private fun setDetails() {
        setDetailsInitView()
        setDetailsNet()
    }

    private lateinit var mShareImg: ImageView
    private var backImg: ImageView? = null
    private var dIcon: ImageView? = null
    private var dName: TextView? = null
    private var dTitle: TextView? = null
    private var dTryDressNum: TextView? = null

    private var dIconLarge: ImageView? = null
    private var dNameLarge: TextView? = null
    private var dCareImg: ImageView? = null

    private var dLaudNum: TextView? = null
    private var dWorksNum: TextView? = null
    private var dFansNum: TextView? = null

    private var dClothesRecyclerView: RecyclerView? = null
    private var mClothesAdapter: SingleClothesAdapter? = null


    private var dTryTv: TextView? = null

    private var dCollectLayout: View? = null//收藏布局
    private var collectTv: TextView? = null//文本
    private var dCollectImg: ImageView? = null//收藏图标

    private var mClothesList: MutableList<GetClothesItemInfoBean.Clothes>? = ArrayList()

    private fun setDetailsInitView() {
        backImg = findViewById(R.id.back_img)
        backImg!!.setOnClickListener {
            finish()
        }
        dTryTv = findViewById(R.id.d_try_tv)

        dIcon = findViewById(R.id.d_icon)
        dName = findViewById(R.id.d_title)
        dTitle = findViewById(R.id.d_name)
        dTryDressNum = findViewById(R.id.d_try_dress_num)

        dIconLarge = findViewById(R.id.d_icon_large)
        dNameLarge = findViewById(R.id.d_name_large)
        dCareImg = findViewById(R.id.d_care_img)

        dLaudNum = findViewById(R.id.d_laud_num)
        dWorksNum = findViewById(R.id.d_works_num)
        dFansNum = findViewById(R.id.d_fans_num)
        mShareImg = findViewById(R.id.share_img)

        //服装列表适配器
        dClothesRecyclerView = findViewById(R.id.d_clothes_recyclerView)
        val mClothesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dClothesRecyclerView!!.layoutManager = mClothesLayoutManager
        mClothesAdapter = SingleClothesAdapter(this, mClothesList!!)
        dClothesRecyclerView!!.adapter = mClothesAdapter

        dCollectImg = findViewById(R.id.d_collect_img)


        mShareImg.singleClick {
            shareViewModel.getRecommendShareUserList()
        }
        initShareData()

    }

    private fun setDetailsNet() {
        //回调
        strollViewModel.getClothesItemInfoBeanState.observe(this) {
            when (it) {
                is GetClothesItemInfoBeanState.Success -> {
                    //设置详情
                    //评论头像
                    Glide.with(this)
                        .asBitmap()
                        .dontAnimate()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .error(R.drawable.temp_icon)
                        .load(
                            Tool().decodePicUrls(
                                strollViewModel.mGetClothesItemInfoBean!!.coverUrls[0],
                                "0",
                                true
                            )
                        )
                        .into(dIcon!!)

                    //字段是否有误
                    dName!!.text = strollViewModel.mGetClothesItemInfoBean!!.name
                    dTitle!!.text = strollViewModel.mGetClothesItemInfoBean!!.svName

                    dTryDressNum!!.text = "试穿数：${
                        Singleton.setNumRuler(
                            strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.tryOnCount,
                            "0"
                        )
                    }"


                    //点击试穿
                    dTryTv!!.setOnClickListener {
                        Singleton.isNewScene = true
                        val intent = Intent(this, AndroidBridgeActivity::class.java)
                        intent.putExtra("entryType", "EnterTrySingleProcuct")
                        intent.putExtra(
                            "trySingleClothes",
                            strollViewModel.mGetClothesItemInfoBean!!.id.toString()
                        )
                        startActivity(intent)
                    }

                    Glide.with(this)
                        .asBitmap()
                        .dontAnimate()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .error(R.drawable.temp_icon)
                        .load(
                            Tool().decodePicUrls(
                                strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.avatarUrl,
                                "0",
                                true
                            )
                        )
                        .into(dIconLarge!!)

                    dNameLarge = findViewById(R.id.d_name_large)
                    dNameLarge!!.text =
                        strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.nickName


                    //关注按钮
                    if (strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.id==Singleton.getUserInfo().userId){
                        dCareImg!!.visibility = View.GONE
                    }else{
                        /**添加*/
                        when(strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.relation){
                            0->{
                                dCareImg!!.visibility = View.VISIBLE
                            }
                            1->{
                                dCareImg!!.visibility = View.GONE
                            }
                            2->{
                                dCareImg!!.visibility = View.VISIBLE
                            }
                            3->{
                                dCareImg!!.visibility = View.GONE
                            }
                        }

                        dCareImg!!.setOnClickListener {
                            //关注   0:未关注 1:我关注他 2:他关注我 3:互相关注
//                            if (strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.relation != null) {
//                                when (strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.relation) {
//                                    0 -> {
//
//                                        strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.relation = 1
//                                        followTvEd.visibility = View.VISIBLE
//                                        //关注请求
//                                        commonViewModel.getFollowUser(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)
//
//
//
//                                    }
//
//                                    1 -> {
//                                        val pop: ShowNoFollowPop = ShowNoFollowPop()
//                                        pop.showPop(this)
//                                        pop.setShowNoFollowPopCallBack(object : ShowNoFollowPop.InnerInterface{
//                                            override fun onclick(state: Int) {
//                                                when(state){
//                                                    0->{}
//                                                    1->{//操作在这里
//                                                        strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.relation = 0
//                                                        followLl.visibility = View.VISIBLE
//                                                        //取消关注请求
//                                                        commonViewModel.getUnfollowUser(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)
//                                                    }
//                                                }
//                                            }
//                                        })
//                                    }
//
//                                    2 -> {
//                                        strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.relation = 3
//                                        followTvEd.visibility = View.VISIBLE
//                                        //关注请求
//                                        commonViewModel.getFollowUser(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)
//                                    }
//
//                                    3 -> {
//                                        val pop: ShowNoFollowPop = ShowNoFollowPop()
//                                        pop.showPop(this)
//                                        pop.setShowNoFollowPopCallBack(object : ShowNoFollowPop.InnerInterface{
//                                            override fun onclick(state: Int) {
//                                                when(state){
//                                                    0->{}
//                                                    1->{//操作在这里
//                                                        strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.relation = 2
//                                                        followLl.visibility = View.VISIBLE
//                                                        //取消关注请求
//                                                        commonViewModel.getUnfollowUser(postDetailsViewModel.mFeedDetailsBean!!.userInfo.id)
//                                                    }
//                                                }
//                                            }
//                                        })
//
//
//                                    }
//                                }
//                            }
                        }
                    }


                    dLaudNum!!.text = Singleton.setNumRuler(
                        strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.laudCount,
                        "0"
                    )
                    dWorksNum!!.text = Singleton.setNumRuler(
                        strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.worksCount,
                        "0"
                    )
                    dFansNum!!.text = Singleton.setNumRuler(
                        strollViewModel.mGetClothesItemInfoBean!!.userInfo!!.followerCount,
                        "0"
                    )

                    // TODO:  是否有加载更多
                    var mClothesList: MutableList<GetClothesItemInfoBean.Clothes> = ArrayList()
                    for (i in 0 until strollViewModel.mGetClothesItemInfoBean!!.clothesList!!.size) {
                        mClothesList.add(strollViewModel.mGetClothesItemInfoBean!!.clothesList!![i])
                        mClothesList[i].picUrl =
                            Tool().decodePicUrls(mClothesList[i].picUrl, "0", true)
                    }

                    mClothesAdapter!!.setData(mClothesList, true)

                    //无数据隐藏
                    // TODO: 或有隐患
                    if (mClothesList.size ==0){
                        dClothesRecyclerView!!.visibility=View.GONE
                    }else{
                        dClothesRecyclerView!!.visibility=View.VISIBLE
                    }


                    if (strollViewModel.mGetClothesItemInfoBean!!.isCollect == 1) {
                        dCollectImg!!.setImageResource(R.drawable.home_collect_ed)
                    } else {
                        dCollectImg!!.setImageResource(R.drawable.home_collect)
                    }
                    //收藏逻辑 和点击事件
                    dCollectImg!!.setOnClickListener {
                        Toast.makeText(this@SingleDetailsActivity, "点击了收藏", Toast.LENGTH_SHORT)
                            .show()
                    }


                    //banner设置
                    imageUrls = strollViewModel.mGetClothesItemInfoBean!!.coverUrls
                    when (imageUrls!!.size) {
                        0 -> {
                            item1!!.visibility = View.GONE
                            item2!!.visibility = View.GONE
                            item3!!.visibility = View.GONE
                            item4!!.visibility = View.GONE
                        }

                        1 -> {
                            item1!!.visibility = View.GONE
                            item2!!.visibility = View.GONE
                            item3!!.visibility = View.GONE
                            item4!!.visibility = View.GONE
                        }

                        2 -> {
                            item1!!.visibility = View.VISIBLE
                            item2!!.visibility = View.VISIBLE
                            item3!!.visibility = View.GONE
                            item4!!.visibility = View.GONE
                        }

                        3 -> {
                            item1!!.visibility = View.VISIBLE
                            item2!!.visibility = View.VISIBLE
                            item3!!.visibility = View.VISIBLE
                            item4!!.visibility = View.GONE
                        }

                        4 -> {
                            item1!!.visibility = View.VISIBLE
                            item2!!.visibility = View.VISIBLE
                            item3!!.visibility = View.VISIBLE
                            item4!!.visibility = View.VISIBLE
                        }
                    }

                    singleShareCover = imageUrls!!.first()

                    for (i in 0 until imageUrls!!.size) {
                        imageUrls!![i] = Tool().decodePicUrls(imageUrls!![i], "0", true)
                    }
                    setViewpager()


                    //这里加载潮流精选
                    setFashionWear()


                }

                is GetClothesItemInfoBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)

                    //这里加载潮流精选
                    setFashionWear()
                }
            }
        }

        Log.e("mId", mId.toString())

        // TODO: 2021/4/27 0027
        strollViewModel.getClothesItemInfo(mId)
    }

    /**
     * 3设置潮流精选
     */
    private fun setFashionWear() {
        setFashionWearInitView()
        setFashionWearNet()
    }

    private var mFashionWearList: MutableList<WaterfallFeedBean.Item.Info>? = ArrayList()
    private var dFashionWearRecyclerView: RecyclerView? = null
    private var mFashionWearAdapter: SingleFashionWearAdapter? = null
    private var dFashionWearMoreLayout: View? = null

    private fun setFashionWearInitView() {
        dFashionWearRecyclerView = findViewById(R.id.d_fashion_wear_recyclerView)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        dFashionWearRecyclerView!!.layoutManager = layoutManager
        mFashionWearAdapter = SingleFashionWearAdapter(this, mFashionWearList!!, 169.0f, 375.0f)
        dFashionWearRecyclerView!!.adapter = mFashionWearAdapter


        //点击加载更多
        dFashionWearMoreLayout = findViewById(R.id.d_fashion_wear_more_layout)
        dFashionWearMoreLayout!!.setOnClickListener {
            val intent = Intent(this, SingleFeedActivity::class.java)//帖子详情页
            intent.putExtra("targetId", mId)
            startActivity(intent)
        }
    }

    var fashionWearListState = 0
    private fun setFashionWearNet() {
        strollViewModel.getFeedListByCvIdBeanState.observe(this) {
            when (it) {
                is GetFeedListByCvIdBeanState.Success -> {

                    when (fashionWearListState) {
                        0 -> {

                            mFashionWearList!!.clear()//清空集合
                            mFashionWearList =
                                Singleton.changeList(strollViewModel.mGetFeedListByCvIdBean!!)//加载请求后的数据
                            mFashionWearAdapter!!.setData(mFashionWearList!!, true)//设置数据 更新适配器

                            if (mFashionWearList!!.size==0){//没有数据
                                findViewById<View>(R.id.d_fashion_wear_layout).visibility=View.GONE
                            }
                        }

                        1 -> {
                            mFashionWearAdapter!!.setData(
                                Singleton.changeList(strollViewModel.mGetFeedListByCvIdBean!!)!!,
                                false
                            )//设置数据 更新适配器
                        }
                    }

                    //这里加载相似推荐
                    setSimilarWear()
                }

                is GetFeedListByCvIdBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)

                    //这里加载相似推荐
                    setSimilarWear()
                }
            }
        }

        // TODO: 请求有问题
        //请求
        strollViewModel.getFeedListByCvId(
            mId,//targetID
            4,
            null
        )
    }

    /**
     * 4设置相似穿搭
     */
    private fun setSimilarWear() {
//        setSimilarWearInitView()
        setSimilarWearNet()
    }


    private var dSimilarViewPager: ViewPager? = null
    private var fragments: MutableList<Fragment> = arrayListOf()
    private lateinit var mViewPagerFragmentAdapter: ViewPagerFragmentAdapter//banner适配器
    private var mSimilarWearListState = 0

    private var p1: View? = null
    private var p2: View? = null
    private var p3: View? = null
    private var p4: View? = null
    private var p5: View? = null
    private fun setSimilarWearInitView() {
        //banner设置
        dSimilarViewPager = findViewById(R.id.d_similar_viewPager)//通过viewPager加载fragment的方式实现切换

        p1 = findViewById(R.id.p1)
        p2 = findViewById(R.id.p2)
        p3 = findViewById(R.id.p3)
        p4 = findViewById(R.id.p4)
        p5 = findViewById(R.id.p5)

        initPoint(0)



        (0..4).forEach {
            val mSimilarBannerFragment = SimilarBannerFragment()
            val bundle = Bundle()
            bundle.putInt("key", it)
            mSimilarBannerFragment.arguments = bundle
            fragments.add(mSimilarBannerFragment)
        }

        //banner设置适配器
        mViewPagerFragmentAdapter = ViewPagerFragmentAdapter(supportFragmentManager, fragments)
        dSimilarViewPager!!.adapter = mViewPagerFragmentAdapter
        dSimilarViewPager!!.offscreenPageLimit = 5//设置缓存的页面数量


        dSimilarViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                initPoint(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


        // 点击加载更多
//        dSimilarWearMoreLayout= findViewById(R.id.d_similar_more_layout)
//        dSimilarWearMoreLayout!!.setOnClickListener {
//            // TODO: 请求需要修改
//            if (strollViewModel.mGetSimilarRecommendBean!=null){
//                if (strollViewModel.mGetSimilarRecommendBean!!.hasNext==1){
//                    mSimilarWearListState=1
//                    strollViewModel.getSimilarRecommend(mId, 10)
//                }else{
//                    //隐藏这个加载更多
//                    dSimilarWearMoreLayout!!.visibility= View.GONE
//                }
//            }
//        }
    }

    //初始化原点
    private fun initPoint(tag: Int) {
        p1!!.background = resources.getDrawable(R.drawable.bg_point_e6e6e6)
        p2!!.background = resources.getDrawable(R.drawable.bg_point_e6e6e6)
        p3!!.background = resources.getDrawable(R.drawable.bg_point_e6e6e6)
        p4!!.background = resources.getDrawable(R.drawable.bg_point_e6e6e6)
        p5!!.background = resources.getDrawable(R.drawable.bg_point_e6e6e6)

        when (tag) {
            0 -> {
                p1!!.background = resources.getDrawable(R.drawable.bg_point_222222)
            }

            1 -> {
                p2!!.background = resources.getDrawable(R.drawable.bg_point_222222)
            }

            2 -> {
                p3!!.background = resources.getDrawable(R.drawable.bg_point_222222)
            }

            3 -> {
                p4!!.background = resources.getDrawable(R.drawable.bg_point_222222)
            }

            4 -> {
                p5!!.background = resources.getDrawable(R.drawable.bg_point_222222)
            }
        }
    }


    private fun setSimilarWearNet() {
        strollViewModel.getSimilarRecommendBeanState.observe(this) {
            when (it) {
                is GetSimilarRecommendBeanState.Success -> {
                    when (mSimilarWearListState) {
                        0 -> {
                            Singleton.mSimilarWearList!!.clear()
                            Singleton.mSimilarWearList =
                                strollViewModel.mGetSimilarRecommendBean!!.list//加载请求后的数据

                            Log.e("mSimilarWearList", Singleton.mSimilarWearList.toString())
                            setSimilarWearInitView()

                            when (Singleton.mSimilarWearList!!.size) {
                                0 -> {
                                    if (Singleton.mSimilarWearList != null && Singleton.mSimilarWearList!!.size <= 6) {
                                        p1!!.visibility = View.GONE
                                        p2!!.visibility = View.GONE
                                        p3!!.visibility = View.GONE
                                        p4!!.visibility = View.GONE
                                        p5!!.visibility = View.GONE
                                    }
                                }

                                1 -> {
                                    if (Singleton.mSimilarWearList != null && Singleton.mSimilarWearList!!.size <= 12) {
                                        p1!!.visibility = View.VISIBLE
                                        p2!!.visibility = View.VISIBLE
                                        p3!!.visibility = View.GONE
                                        p4!!.visibility = View.GONE
                                        p5!!.visibility = View.GONE
                                    }
                                }

                                2 -> {
                                    if (Singleton.mSimilarWearList != null && Singleton.mSimilarWearList!!.size <= 18) {
                                        p1!!.visibility = View.VISIBLE
                                        p2!!.visibility = View.VISIBLE
                                        p3!!.visibility = View.VISIBLE
                                        p4!!.visibility = View.GONE
                                        p5!!.visibility = View.GONE
                                    }
                                }

                                3 -> {
                                    if (Singleton.mSimilarWearList != null && Singleton.mSimilarWearList!!.size <= 24) {
                                        p1!!.visibility = View.VISIBLE
                                        p2!!.visibility = View.VISIBLE
                                        p3!!.visibility = View.VISIBLE
                                        p4!!.visibility = View.VISIBLE
                                        p5!!.visibility = View.GONE
                                    }
                                }

                                4 -> {
                                    if (Singleton.mSimilarWearList != null && Singleton.mSimilarWearList!!.size <= 30) {
                                        p1!!.visibility = View.VISIBLE
                                        p2!!.visibility = View.VISIBLE
                                        p3!!.visibility = View.VISIBLE
                                        p4!!.visibility = View.VISIBLE
                                        p5!!.visibility = View.VISIBLE
                                    }
                                }
                            }


                        }

                        1 -> {
                            //无加载
                        }
                    }
                }

                is GetSimilarRecommendBeanState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                }
            }
        }
        // TODO: 请求有问题
        //请求
        strollViewModel.getSimilarRecommend(
            mId,//targetID
            30
        )
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
                            strollViewModel.mGetClothesItemInfoBean!!.name
                        }在腰果上发布了一条超棒的单品点击链接直接打开"
                        ClipboardUtils.copyText(content)
                        ToastUtils.showShort("复制成功，快去分享给好友吧")

                    } else if (it.shareType == TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_SINGLE) {
                        XPopup.Builder(this@SingleDetailsActivity)
                            .isDestroyOnDismiss(true)
                            .asCustom(
                                ShareSinglePop(
                                    mContext = this@SingleDetailsActivity,
                                    mSingleGoods = strollViewModel.mGetClothesItemInfoBean!!,
                                    shareType = SHARE_CLICK_TYPE_CREATE_SINGLE,
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

        val shareAllPop = ShareAllPop(
            mContext = this,
            shareType = TIMCommonConstants.SHARE_TYPE_EDIT_NAME,
            shareOnClick = object : ShareAllPop.ShareOnClick {
                override fun onClickCreatePost() {

                    shareViewModel.getShortUrl(
                        mId.toString(),
                        TIMCommonConstants.SHARE_TYPE_SINGLE,
                        TIMCommonConstants.SHARE_CLICK_TYPE_CREATE_SINGLE
                    )

                }

                override fun onClickCopyLink() {
                    shareViewModel.getShortUrl(
                        mId.toString(),
                        TIMCommonConstants.SHARE_TYPE_SINGLE,
                        TIMCommonConstants.SHARE_CLICK_TYPE_COPY
                    )
                }

                override fun onClickTipOff() {

                }

                override fun onClickShare(shareType: Int) {
                    shareViewModel.getShortUrl(
                        mId.toString(),
                        TIMCommonConstants.SHARE_TYPE_SINGLE,
                        shareType
                    )
                }

                override fun onSendMsg(
                    userListSel: List<UserInfo>,
                    content: String,
                    shareType: Int,
                ) {
                    val customData: MutableMap<String, ShareSingleEntity> =
                        mutableMapOf()
                    val gson = Gson()

                    var singleEntity = shareSingleEntity()

                    customData.put(
                        TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY,
                        singleEntity
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
                            LoadingPop(mContext = this@SingleDetailsActivity).setTitle("发送中")
                                .setStyle(LoadingPop.Style.Spinner)
                        )
                        .show()
                        .delayDismissWith(1500) {
                            ToastUtils.showShort("发送成功")
                        }

                }

                //去私信好友
                override fun onClickToFriend() {
                    val customData: MutableMap<String, ShareSingleEntity> =
                        mutableMapOf()
                    val gson = Gson()
                    var singleEntity = shareSingleEntity()
                    customData[TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY] =
                        singleEntity
                    ShareToActivity.start(mContext, gson.toJson(customData))
                }

                //wq
                override fun onClickEdit() {

                }

                override fun onClickChangeName() {
                    showEditPop(mId)
                }

                override fun onClickDelete() {
                    showDelPop(mId)
                }

                override fun onClickPrivate() {
                    changeClothesItemSeeLimit(mId, 0)
                }

                override fun onClickPublic() {
                    changeClothesItemSeeLimit(mId, 1)
                }
            })

        shareViewModel.recomendUserList.observe(this) {
            when (it) {
                is ShareUseRecomendListState.Success -> {
                    if (it.recommendUserListBean != null)
                        shareAllPop.userList = it.recommendUserListBean

                    XPopup.Builder(this)
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

    private fun shareSingleEntity(): ShareSingleEntity {


        var singleEntity = ShareSingleEntity(
            coverUrl = singleShareCover,
            name = strollViewModel.mGetClothesItemInfoBean!!.name,
            id = mId.toString(),
        )
        return singleEntity
    }

    private fun share(shareType: Int, shortUrl: String) {

        var content = ShareWebContent()
        content.title = strollViewModel.mGetClothesItemInfoBean!!.name
        content.description = "万千好物一键上身，美好生活即刻分享"


        Thread {
            AndroidDownloadManager(
                this@SingleDetailsActivity,
                strollViewModel.mGetClothesItemInfoBean!!.coverUrls.first()
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
                        ToastUtils.showShort("图片下载失败，请重新下载！")
                        Log.e("downloadVideo", "onFailed", throwable)
                    }
                }).download()
        }.start()

    }

    private fun shareCallback(
        shareType: Int,
        content: ShareWebContent,
    ) {
        Social.share(this@SingleDetailsActivity,
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


    /**弹出编辑弹窗*/
    fun showEditPop(singleId: Long) {
        val inflate = LayoutInflater.from(this).inflate(R.layout.dialog_single_dress, null)
        val tvConfirm = inflate.findViewById<TextView>(R.id.tv_confirm)

        val etName = inflate.findViewById<AppCompatEditText>(R.id.et_name)
        val tvNicknameCount = inflate.findViewById<TextView>(R.id.tv_nickname_count)


        val blockCharacterSet = " \n" // 定义要阻止输入的字符集合

        /**禁止输入换行和空格*/
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // 不需要实现
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                // 每次用户输入字符时进行检查
                if (count > 0 && blockCharacterSet.indexOf(s[start]) >= 0) {
                    // 如果用户输入的字符是空格或换行符，则立即删除该字符
                    val newText =
                        s.subSequence(0, start).toString() + s.subSequence(start + count, s.length)
                            .toString()
                    etName.setText(newText)
                    etName.setSelection(start) // 设置光标位置
                }
            }

            override fun afterTextChanged(s: Editable) {
                // 不需要实现
                tvNicknameCount.text = "${s?.length}"
            }
        })


        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        /**能被键盘顶起来*/
        mPopupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        mPopupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

        //点击确定
        tvConfirm.setOnClickListener {
            /**
             * 修改单品名字  请求
             */
            if (etName.text.toString().isEmpty()){
                Singleton.centerToast(this,"请为你的单品取一个闪亮的名字吧")
            }else{
                strollViewModel.changeClothesItemName(singleId, etName.text.toString())
                mPopupWindow.dismiss()
                newName=etName.text.toString()
            }
        }


        //动画
        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            Singleton.bgAlpha(this, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                Singleton.bgAlpha(this, 1f) //恢复透明度
            }
        }


        //在pause时关闭
        Singleton.lifeCycleSet(this, mPopupWindow)

    }


    /**
     * 修改单品可见状态  请求
     */
    private fun changeClothesItemSeeLimit(
        id: Long,//单品id
        state: Int,//0-私密，1-公开
    ) {
        strollViewModel.changeClothesItemSeeLimit(id, state)
    }


    /**删除单品弹窗*/
    private fun showDelPop(delId: Long) {
        val inflate = LayoutInflater.from(this).inflate(R.layout.home_dialog_ok_cancel, null)
//        val tips = inflate.findViewById<TextView>(R.id.tips)
//        tips.visibility = View.VISIBLE
//        tips.text = "退出登录"
        val content = inflate.findViewById<TextView>(R.id.content)
        content.text = "删除以后不能恢复哦，确认是否删除"

        val confirm = inflate.findViewById<TextView>(R.id.confirm)
        confirm.text = "确认删除"
        confirm.setTextColor(Color.parseColor("#EA4359"))

        val cancel = inflate.findViewById<TextView>(R.id.cancel)
//        cancel.text="取消"

        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        /**点击确定*/
        confirm.setOnClickListener {
            mPopupWindow.dismiss()
            /**删除单品  请求*/
            strollViewModel.deleteClothesItem(delId)
        }


        /**点击取消*/
        cancel.setOnClickListener {
            mPopupWindow.dismiss()
        }

        //动画
        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.CENTER, 0, 0)
            Singleton.bgAlpha(this, 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                Singleton.bgAlpha(this, 1f) //恢复透明度
            }
        }

        //在pause时关闭
        Singleton.lifeCycleSet(this, mPopupWindow)
    }

    /**网络请求回调 优先在initview中加载*/

    var newName=""  //用于成功之后修改
    private fun setNetCallback() {
        /**
         * 删除单品  请求回调
         */
        strollViewModel.deleteClothesItemState.observe(this) {
            when (it) {
                is DeleteClothesItemState.Success -> {
                    if (strollViewModel.mDeleteClothesItemBean != null) {
                        when (strollViewModel.mDeleteClothesItemBean!!.state) {
                            0 -> {
                                Singleton.centerToast(this, "删除失败")
                            }

                            1 -> {
                                Singleton.centerToast(this, "删除成功")
                                finish()
                            }
                        }
                    }
                }

                is DeleteClothesItemState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }
                }
            }
        }


        /**
         * 修改单品可见状态  请求回调
         */
        strollViewModel.changeClothesItemSeeLimitState.observe(this) {
            when (it) {
                is ChangeClothesItemSeeLimitState.Success -> {
                    if (strollViewModel.mChangeClothesItemSeeLimitBean != null) {
                        when (strollViewModel.mChangeClothesItemSeeLimitBean!!.state) {
                            0 -> {
                                Singleton.centerToast(this, "修改失败")
                            }

                            1 -> {
                                Singleton.centerToast(this, "修改成功")
                            }
                        }
                    }
                }

                is ChangeClothesItemSeeLimitState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }
                }
            }
        }


        /**
         * 修改单品名字  请求回调
         */
        strollViewModel.changeClothesItemNameState.observe(this) {
            when (it) {
                is ChangeClothesItemNameState.Success -> {
                    if (strollViewModel.mChangeClothesItemNameBean != null) {
                        when (strollViewModel.mChangeClothesItemNameBean!!.state) {
                            0 -> {
                                Singleton.centerToast(this, "修改失败")
                            }

                            1 -> {
                                Singleton.centerToast(this, "修改成功")
                                dName!!.text=newName
                            }
                        }
                    }
                }

                is ChangeClothesItemNameState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }
                }
            }
        }
    }


}