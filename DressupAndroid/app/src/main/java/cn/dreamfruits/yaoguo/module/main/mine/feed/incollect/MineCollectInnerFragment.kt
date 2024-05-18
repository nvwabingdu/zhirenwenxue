package cn.dreamfruits.yaoguo.module.main.mine.feed.incollect

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.MainActivity
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.FeedViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.WaterfallAdapter
import cn.dreamfruits.yaoguo.module.main.home.customview.ZrDefaultPageView
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.repository.bean.feed.GetCollectListBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.changeList

import java.util.ArrayList

class MineCollectInnerFragment : Fragment() {
    private var mRootView: View? = null
    private var userId: Long? = null
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private val collectInnerViewModel by viewModels<CollectInnerViewModel>()

    //用于三个页面的动态
    private var mRecyclerView: RecyclerView? = null

    //    private val feedViewModel by viewModels<FeedViewModel>()
    private var mMineFeedList: MutableList<WaterfallFeedBean.Item.Info>? =
        ArrayList()//个人中心页面的feed列表
    private var mAdapter: WaterfallAdapter? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载
    private var isNoMoreData = false
    private var feedTag: Int = 7 //7 帖子   收藏 帖子8  话题11       9 赞过


    //用于收藏下的话题的
    private var mMineFeedListLabel: MutableList<GetCollectListBean.Item>? =
        ArrayList()//个人中心页面的feed列表
    private var mRecyclerViewLabel: RecyclerView? = null
    private var mAdapterLabel: CollectLabelAdapter? = null
    private var refreshStateLabel: Int = 0//0 刷新  1 加载  2预加载
    private var isNoMoreDataLabel = false
    private var title = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_feed2, container, false)

        /**获取传递过来的targetId*/
        userId = arguments!!.getLong("targetId")
        Log.e("userId122221", userId.toString())

        title = arguments!!.getString("title").toString()
        Log.e("title122221", title)

        when (title) {
            "帖子" -> {
                feedTag = 8
                /**动态布局 请求 加载及回调 加载更多*/
                initLayout()

                /**请求数据  （为了符合视频的tag  这里用减法的方式将参数搞为 0帖子  1收藏  2赞过）*/
                initNet()
            }

            "话题" -> {
                feedTag = 11
                /**初始化收藏下话题tab的适配器和列表控件 以及设置回调*/
                initLayoutLabel()

                /**请求话题数据*/
                initNetLabel()
            }
        }

        /**设置缺省*/
        setDefaultPage()

        return mRootView
    }

    /**动态布局 请求 加载及回调 加载更多*/
    private fun initLayout() {//初始化一些公用的逻辑
        /**动态通用*/
        mRecyclerView = mRootView!!.findViewById(R.id.recyclerview)
        val mStaggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mStaggeredGridLayoutManager.gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_NONE//1.防止item乱跳错位
        mRecyclerView!!.layoutManager = mStaggeredGridLayoutManager
        //2.防止第一页出现空白 3.使用notifyItemRangeChanged
//        mRecyclerView!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mStaggeredGridLayoutManager.invalidateSpanAssignments()
            }
        })
        mAdapter = WaterfallAdapter(requireActivity(), 20f, mMineFeedList!!)
        mRecyclerView!!.adapter = mAdapter

        /**
         * 2.设置回调
         */
        mAdapter!!.setWaterfallAdapterListener(object : WaterfallAdapter.WaterfallAdapterInterface {
            override fun onCallBack(position: Int, id: Long) {
                //showPopupWindow(position, id)//内容反馈弹窗
            }

            override fun onPreload(position: Int) {
                if (!isNoMoreData) {
                    refreshState = 2//设置为预加载状态
                    if (mMineFeedList!!.size != 0) {//没有数据不能触发加载
                        refreshState = 1//设置为加载
                        // TODO: 请求2
                        if (collectInnerViewModel.mGetUserFeedListBean != null) {
                            getLoadMoreLogic(collectInnerViewModel.mGetUserFeedListBean!!)
                        }
                    }
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
                Singleton.videoTag = feedTag
                Singleton.mMineFeedListSingle!!.clear()
                Singleton.mMineFeedListSingle = mMineFeedList
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNet() {
        refreshState = 0//设置为刷新状态
        /**
         * 开始请求动态列表
         */
        Log.e("userId1d22221", userId.toString())
        collectInnerViewModel.getUserFeedList(
            feedTag - 7,//为了符合视频的tag  这里用减法的方式将参数搞为 0帖子  1收藏  2赞过
            userId,
            10,
            null
        )

        /**
         * 2.请求结果动态列表
         */
        collectInnerViewModel.getUserFeedListState.observe(this) {
            when (it) {
                is GetUserFeedListState.Success -> {

                    when (refreshState) {
                        0 -> {//刷新
                            mMineFeedList!!.clear()//清空集合
                            mMineFeedList =
                                changeList(collectInnerViewModel.mGetUserFeedListBean)//加载请求后的数据
                            mAdapter!!.setData(mMineFeedList!!, true)//设置数据 更新适配器
                            setDefaultPageView(mMineFeedList!!.size)
                        }

                        1, 2 -> {//加载 预加载
                            when (feedTag) {
                                7, 8, 9 -> {
                                    mAdapter!!.setData(
                                        changeList(collectInnerViewModel.mGetUserFeedListBean)!!,
                                        false
                                    )//设置数据 更新适配器

                                    setDefaultPageView(mMineFeedList!!.size)
                                }
                            }
                        }
                    }
                }

                is GetUserFeedListState.Fail -> {
                    setDefaultPageView(mMineFeedList!!.size)
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
                }
            }
        }
    }

    private fun getLoadMoreLogic(bean: WaterfallFeedBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        when (bean.hasNext) {
            0 -> {//没有更多数据了
                isNoMoreData = true
            }

            1 -> {//有更多数据
                collectInnerViewModel.getUserFeedList(
                    feedTag - 7,//为了符合视频的tag  这里用减法的方式将参数搞为 0帖子  1收藏  2赞过
                    userId,
                    10,
                    bean.lastTime
                )
            }
        }
    }


    /**收藏话题下的*/
    private fun initLayoutLabel() {
        /**收藏下的话题列表及适配器*/
        mRecyclerViewLabel = mRootView!!.findViewById(R.id.recyclerview)
        /*mRecyclerView!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突*/
        val layoutManager = LinearLayoutManager(requireActivity())
        mRecyclerViewLabel!!.layoutManager = layoutManager
        mAdapterLabel = CollectLabelAdapter(requireActivity(), mMineFeedListLabel!!)
        mRecyclerViewLabel!!.adapter = mAdapterLabel

        /**
         * 2.设置回调
         */
        mAdapterLabel!!.setCollectLabelAdapterCallBack(object : CollectLabelAdapter.InnerInterface {
            override fun onCareUser(id: Long, isCare: Boolean) {

            }

            override fun onPreload() {
                if (!isNoMoreDataLabel) {
                    refreshStateLabel = 2//设置为预加载状态
                    if (mMineFeedListLabel!!.size != 0) {//没有数据不能触发加载
                        refreshStateLabel = 1//设置为加载
                        if (collectInnerViewModel.mGetCollectListBean != null) {
                            when (collectInnerViewModel.mGetCollectListBean!!.hasNext) {
                                0 -> {//没有更多数据了
                                    isNoMoreDataLabel = true
                                }

                                1 -> {//有更多数据
                                    collectInnerViewModel.getCollectList(
                                        userId,
                                        3,//类型，0-动态，1-单品，2-穿搭方案，3-话题，4-服装单品，5-图案
                                        10,
                                        collectInnerViewModel.mGetCollectListBean!!.lastTime
                                    )
                                }
                            }
                        }
                    }
                }
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNetLabel() {
        refreshStateLabel = 0//设置为刷新状态

        /**请求话题列表*/
        collectInnerViewModel.getCollectList(
            userId,
            3,//类型，0-动态，1-单品，2-穿搭方案，3-话题，4-服装单品，5-图案
            10,
            null
        )


        /**
         * 2.请求结果
         */
        collectInnerViewModel.getCollectListState.observe(this) {
            when (it) {
                is GetCollectListState.Success -> {

                    when (refreshStateLabel) {
                        0 -> {//刷新
                            mMineFeedListLabel!!.clear()//清空集合
                            mMineFeedListLabel =
                                collectInnerViewModel.mGetCollectListBean!!.list//加载请求后的数据
                            mAdapterLabel!!.setData(mMineFeedListLabel!!, true)//设置数据 更新适配器
                            setDefaultPageView(mMineFeedListLabel!!.size)
                        }

                        1, 2 -> {//加载 预加载
                            when (feedTag) {
                                11 -> {
                                    mAdapterLabel!!.setData(
                                        collectInnerViewModel.mGetCollectListBean!!.list,
                                        false
                                    )//设置数据 更新适配器

                                    setDefaultPageView(mMineFeedListLabel!!.size)
                                }
                            }
                        }
                    }
                }

                is GetCollectListState.Fail -> {
                    setDefaultPageView(mMineFeedListLabel!!.size)
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
                }
            }
        }
    }


    /**缺省*/
    private var defaultPageView: ZrDefaultPageView? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDefaultPage() {
        defaultPageView = mRootView!!.findViewById(R.id.default_page_view)
        when (title) {//获取传递过来的key
            "帖子" -> {
                defaultPageView!!.setData(
                    getString(R.string.default_user_center_collect),
                    R.drawable.default_user_center_collect_img
                )

                try {
                    if (userId != 0L && Singleton.getUserInfo().userId == userId) {

                        Log.e(
                            "zqruserId",
                            Singleton.getUserInfo().userId.toString() + "userId" + userId
                        )

                        defaultPageView!!.setOnThisItemClickListener {
                            val ac = requireActivity() as MainActivity
                            ac.turnHome()
                        }
                    }
                } catch (e: Exception) {
                    // TODO:  这里 类转换异常
                }


            }

            "话题" -> {
                defaultPageView!!.setData(
                    getString(R.string.default_user_center_collect_label),
                    R.drawable.default_user_center_collect_label
                )

                try {
                    if (userId != 0L && Singleton.getUserInfo().userId == userId) {

                        Log.e(
                            "zqruserId",
                            Singleton.getUserInfo().userId.toString() + "userId" + userId
                        )
                        defaultPageView!!.setOnThisItemClickListener {
                            val ac = requireActivity() as MainActivity
                            ac.turnHome()
                        }
                    }
                } catch (e: Exception) {
                    // TODO:  这里 类转换异常
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDefaultPageView(size: Int) {
        when (size) {
            0 -> {
                defaultPageView!!.visibility = View.VISIBLE
            }

            else -> {
                defaultPageView!!.visibility = View.GONE
            }
        }
    }


}