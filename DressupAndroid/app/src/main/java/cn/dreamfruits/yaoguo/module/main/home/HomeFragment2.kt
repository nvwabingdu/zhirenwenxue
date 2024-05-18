package cn.dreamfruits.yaoguo.module.main.home

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemChildClickListener
import cn.dreamfruits.yaoguo.adapter.base.viewholder.BaseViewHolder
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.global.FeedPublishGlobal
import cn.dreamfruits.yaoguo.module.main.home.child.AttentionFragment
import cn.dreamfruits.yaoguo.module.main.home.child.LabelFragment
import cn.dreamfruits.yaoguo.module.main.home.child.RecommendFragment
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.FeedPublishStateAdapter
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.ViewPagerFragmentAdapter
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.HomeSearchDetailsActivity
import cn.dreamfruits.yaoguo.module.main.home.searchhistory.HomeSearchHistoryActivity
import cn.dreamfruits.yaoguo.module.main.home.state.HomeRecommendLabelListState
import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity
import cn.dreamfruits.yaoguo.repository.MMKVRepository
import cn.dreamfruits.yaoguo.repository.bean.label.GetRecommendLabelListBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment2 : Fragment(), NetworkUtils.OnNetworkStatusChangedListener {
    private var viewPager: ViewPager? = null
    private var magicIndicator: MagicIndicator? = null
    var fragments: MutableList<Fragment> = arrayListOf()//fragment集合
    var mRecommendLabelList: MutableList<String> = arrayListOf()//标题集合
    private val homeViewModel by viewModels<HomeViewModel>()

    private var mPublishStateAdapter: FeedPublishStateAdapter? = null
    private lateinit var mPublishListRv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment_home, container, false)

        //跳转到unity我的衣橱
        view.findViewById<ImageView>(R.id.start_unity).setOnClickListener {
            Singleton.isNewScene = true
            val intent = Intent(requireActivity(), AndroidBridgeActivity::class.java)
            intent.putExtra("entryType", "EnterMyWardrobe")
            requireActivity().startActivity(intent)
        }

        //点击搜索
        view.findViewById<ImageView>(R.id.home_search).setOnClickListener {
            val intent = Intent(activity, HomeSearchHistoryActivity::class.java)
            intent.putExtra("SearchData", "")
            startActivity(intent)
        }

        //沉浸状态栏重叠问题
        view.findViewById<View>(cn.dreamfruits.yaoguo.R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BarUtils.getStatusBarHeight()
            )

        //联动模块
        /*viewPager=view.findViewById<NoPreloadViewPager>(cn.dreamfruits.yaoguo.R.id.view_pager)*/
        viewPager = view.findViewById(R.id.view_pager)

        magicIndicator =
            view.findViewById<MagicIndicator>(R.id.magic_indicator)

        mPublishListRv = view.findViewById(R.id.rv_publish_list)

        //初始化something
        init()
        initFeedPublishList()
        addFeedPublishListener()

        return view
    }


    /**
     * 初始化
     */
    private fun initFeedPublishList() {
        mPublishStateAdapter = FeedPublishStateAdapter()
        mPublishListRv.layoutManager = LinearLayoutManager(context)
        mPublishListRv.adapter = mPublishStateAdapter

        mPublishStateAdapter!!.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int,
            ) {
                when (view.id) {
                    R.id.iv_retry -> {
                        FeedPublishGlobal.feedPublishTryAgain(mPublishStateAdapter!!.data[position].taskId)
                    }
                    R.id.tv_cancel_publish -> {
                        FeedPublishGlobal.cancelPublish(mPublishStateAdapter!!.data[position].taskId)
                    }
                }
            }

        })


    }

    /**
     * 添加发布监听器
     */
    private fun addFeedPublishListener() {
        FeedPublishGlobal.addObserve(
            this,
            requireContext(),
            onStart = {
                mPublishStateAdapter?.addData(it)
            },
            onSuccess = { taskId ->

                (fragments[0] as AttentionFragment).refreshData()

                activity?.runOnUiThread {
                    mPublishStateAdapter?.data?.indexOfFirst {
                        it.taskId == taskId
                    }?.let {
                        mPublishStateAdapter?.data?.removeAt(it)
                        //延迟两秒移除
                        mPublishListRv.postDelayed({
                            mPublishStateAdapter?.notifyItemRemoved(it)
                        }, 1000)
                    }
                }
            },
            onProgress = { taskId, progress ->
                activity?.runOnUiThread {
                    mPublishStateAdapter?.data?.indexOfFirst {
                        it.taskId == taskId
                    }?.let {
                        val itemView = mPublishListRv.findViewHolderForAdapterPosition(it) as?
                                BaseViewHolder
                        itemView?.getView<TextView>(R.id.tv_uploading_progress)?.text =
                            "${progress}%"
                        itemView?.getView<ProgressBar>(R.id.pb_uploading_progress)?.progress =
                            progress
                    }
                }
            },
            onFail = { taskId, msg ->
                activity?.runOnUiThread {
                    mPublishStateAdapter?.data?.indexOfFirst {
                        it.taskId == taskId
                    }?.let {
                        val bean = mPublishStateAdapter?.data?.get(it)
                        bean?.publishFail = true
                        bean?.msg = msg
                        mPublishStateAdapter?.notifyItemChanged(it)
                    }
                }
            },
            onCancel = { taskId ->
                activity?.runOnUiThread {
                    mPublishStateAdapter?.data?.indexOfFirst {
                        it.taskId == taskId
                    }?.let {
                        mPublishStateAdapter?.remove(it)
                    }
                }
            },
            onTryAgain = { taskId ->
                activity?.runOnUiThread {
                    mPublishStateAdapter?.data?.indexOfFirst {
                        it.taskId == taskId
                    }?.let {
                        val bean = mPublishStateAdapter?.data?.get(it)
                        bean?.publishFail = false
                        mPublishStateAdapter?.notifyItemChanged(it)
                    }
                }
            },
            onTryAll = { taskId ->
                activity?.runOnUiThread {
                    mPublishStateAdapter?.data?.indexOfFirst {
                        it.taskId == taskId
                    }?.let {
                        val bean = mPublishStateAdapter?.data?.get(it)
                        bean?.publishFail = false
                        mPublishStateAdapter?.notifyItemChanged(it)
                    }
                }
            }
        )
        NetworkUtils.registerNetworkStatusChangedListener(this)
    }

    override fun onDisconnected() {

    }

    override fun onConnected(networkType: NetworkUtils.NetworkType?) {
        FeedPublishGlobal.feedPublishTryAll()
    }

    /**
     * 初始化
     */
    private fun init() {
        //前两个是关注和推荐
        if (mRecommendLabelList.size == 0) {
            mRecommendLabelList.add("关注")
            mRecommendLabelList.add("推荐")
        }

        if (fragments.size == 0) {
            val mAttentionFragment = AttentionFragment()
            val bundle = Bundle()
            bundle.putLong("id", -2)
            bundle.putString("name", "关注")
            bundle.putInt("viewCount", 0)
            mAttentionFragment.arguments = bundle
            fragments.add(mAttentionFragment)


            val mRecommendFragment = RecommendFragment()
            val bundle2 = Bundle()
            bundle2.putLong("id", -1)
            bundle2.putString("name", "推荐")
            bundle2.putInt("viewCount", 0)
            mRecommendFragment.arguments = bundle2
            fragments.add(mRecommendFragment)
        }

        /**
         * 没有网络的时候，且数据为空的时候，使用上一次的缓存数据
         */
        try {
            if (!NetworkUtilsJava.isNetworkConnected(context) || mRecommendLabelList.size == 2) {//没网，且数据只有两个的时候
//            if (!NetworkUtilsJava.isNetworkConnected(context)) {//没网，且没有数据的时候
                //从缓存获取数据
                val json = MMKVRepository.getCommonMMKV()
                    .decodeString(MMKVConstants.RECOMMEND_LABEL_LIST, "")//推荐动态json
                Log.e("zqr----", json.toString())
                val bean = GsonUtils.fromJson(json, GetRecommendLabelListBean::class.java)
                //设置数据
                setData(bean)
                return
            }
        } catch (e: Exception) {

        }

        homeViewModel.getRecommendLabelList(0)//请求 话题tab
        //请求状态
        homeViewModel.getRecommendLabelListBean.observe(this) {
            when (it) {
                is HomeRecommendLabelListState.Success -> {
                    //设置数据
                    homeViewModel.mGetRecommendLabelListBean?.let { it1 -> setData(it1) }
                }
                is HomeRecommendLabelListState.Fail -> {

                }
            }
        }
    }

    /**
     * 设置数据
     */
    private fun setData(mGetRecommendLabelListBean: GetRecommendLabelListBean) {
        for (i in 0 until mGetRecommendLabelListBean.list.size) {
            mGetRecommendLabelListBean.list[i].name.let { it ->
                mRecommendLabelList.add(it)
            }
            mGetRecommendLabelListBean.list[i].let { it ->
                val mLabelFragment = LabelFragment()
                val bundle = Bundle()
                bundle.putLong("id", it.id)
                bundle.putString("name", it.name)
                bundle.putInt("viewCount", it.viewCount)
                mLabelFragment.arguments = bundle
                fragments.add(mLabelFragment)
            }
        }
        viewPager!!.adapter = ViewPagerFragmentAdapter(childFragmentManager, fragments)
        magicIndicator!!.bindViewPager(viewPager!!, mRecommendLabelList)
        // 设置默认选中的Tab位置
        magicIndicator!!.onPageSelected(1)
        viewPager!!.currentItem = 1
    }

    /**
     * 指示器绑定viewpager
     */
    fun MagicIndicator.bindViewPager(
        viewPager: ViewPager,
        mStringList: List<String> = arrayListOf(),
        action: (index: Int) -> Unit = {},
    ) {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mStringList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return HomeSearchDetailsActivity.ScaleTransitionPagerTitleView(activity!!).apply {
                    //设置文本
                    text = mStringList[index].toHtml()
                    //设置为粗体
                    //background=resources.getDrawable()

                    //字体大小
                    textSize = 16.0f
                    //不缩放效果
                    minScale = 1.0f
                    //未选中颜色
                    normalColor = ContextCompat.getColor(
                        getContext(),
                        R.color.color_pitch2
                    )
                    //选中颜色
                    selectedColor = ContextCompat.getColor(getContext(), R.color.color_pitch_ed2)

                    //点击事件
                    setOnClickListener {
                        viewPager.currentItem = index
                        action.invoke(index)
                    }

                }
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    //mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    //线条的宽高度
                    lineHeight = UIUtil.dip2px(activity, 4.0).toFloat()
//                    lineWidth = UIUtil.dip2px(activity, 100.0).toFloat()
                    //// 相对于底部的偏移量，如果你想让直线位于title上方，设置它即可
                    yOffset = UIUtil.dip2px(activity, 14.0).toFloat()
                    //线条的圆角
                    roundRadius = UIUtil.dip2px(activity, 0.0).toFloat()
                    //设置拉长动画
                    // startInterpolator = AccelerateInterpolator()
                    // endInterpolator = DecelerateInterpolator(0.5f)
                    //线条的颜色
                    setColors(
                        ContextCompat.getColor(
                            getContext(),
                            cn.dreamfruits.yaoguo.R.color.color_tab_indicator_line2
                        )
                    )
                }
            }
        }

        this.navigator = commonNavigator

        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                this@bindViewPager.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                this@bindViewPager.onPageSelected(position)
                action.invoke(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                this@bindViewPager.onPageScrollStateChanged(state)
            }

        })
    }

    /**
     * 设置标题文字
     */
    class ScaleTransitionPagerTitleView(context: Context) : ColorTransitionPagerTitleView(context) {
        //设置为粗体
        override fun setTypeface(tf: Typeface?) {
            super.setTypeface(Typeface.DEFAULT)
        }

        var minScale = 0.7f
        override fun onEnter(
            index: Int,
            totalCount: Int,
            enterPercent: Float,
            leftToRight: Boolean,
        ) {
            super.onEnter(index, totalCount, enterPercent, leftToRight)    // 实现颜色渐变
            scaleX = minScale + (1.0f - minScale) * enterPercent
            scaleY = minScale + (1.0f - minScale) * enterPercent
        }

        override fun onLeave(
            index: Int,
            totalCount: Int,
            leavePercent: Float,
            leftToRight: Boolean,
        ) {
            super.onLeave(index, totalCount, leavePercent, leftToRight)    // 实现颜色渐变
            scaleX = 1.0f + (minScale - 1.0f) * leavePercent
            scaleY = 1.0f + (minScale - 1.0f) * leavePercent
        }
    }

    fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, flag)
        } else {
            Html.fromHtml(this)
        }
    }

    fun turnToPosition(position: Int) {
        LogUtils.e(">>> viewPager = " + viewPager)
        LogUtils.e(">>> magicIndicator = " + magicIndicator)
        viewPager?.setCurrentItem(position, false)
        magicIndicator?.onPageSelected(position)

    }
}

