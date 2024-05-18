package cn.dreamfruits.yaoguo.module.main.mine.feed

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.selector.PictureConstants
import cn.dreamfruits.selector.PublishPickerActivity
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.MainActivity
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.FeedViewModel
import cn.dreamfruits.yaoguo.module.main.home.child.adapter.WaterfallAdapter
import cn.dreamfruits.yaoguo.module.main.home.customview.ZrDefaultPageView
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.changeList
import com.permissionx.guolindev.PermissionX

import java.util.ArrayList

class MinePostFragment : Fragment() {
    private var mRootView: View? = null
    private var targetId: Long? = null
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private val mineViewModel by viewModels<MineViewModel>()

    //用于三个页面的动态
    private var mRecyclerView: RecyclerView? = null
    private val feedViewModel by viewModels<FeedViewModel>()
    private var mMineFeedList: MutableList<WaterfallFeedBean.Item.Info>? =
        ArrayList()//个人中心页面的feed列表
    private var mAdapter: WaterfallAdapter? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载
    private var isNoMoreData = false
    private var feedTag: Int = 7 //7 帖子   收藏 帖子8  话题11       9 赞过

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_feed2, container, false)

        /**获取传递过来的targetId*/
        targetId = arguments!!.getLong("targetId")
        val title = arguments!!.getString("title").toString()
        val collectTag = arguments!!.getString("collectKey").toString()


        Log.e("da12da121dd", "帖子和获赞   " + targetId)
        /**用标题进行判断  设置feedTag  如果是收藏就展示上面的tab 并设置响应的点击事件*/
        inTitleSetLayout(title, collectTag)

        /**初始化公共的viewmodel*/
        initLayout()

        /**设置缺省*/
        setDefaultPage(title, collectTag)

        /**请求数据  （为了符合视频的tag  这里用减法的方式将参数搞为 0帖子  1收藏  2赞过）*/
        initNet()

        return mRootView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inTitleSetLayout(tag: String, collectTag: String) {
        Log.e("da12121", arguments!!.getString("key").toString())
        Log.e("da12121 ", arguments!!.getString("collectKey").toString())
        when (tag) {//获取传递过来的key
            "帖子" -> {
                feedTag = 7
            }

            "赞过" -> {
                feedTag = 9
            }
        }
    }

    /**缺省*/
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDefaultPage(tag: String, collectTag: String) {
        defaultPageView = mRootView!!.findViewById(R.id.default_page_view)
        when (tag) {//获取传递过来的key
            "帖子" -> {
                defaultPageView!!.setData(
                    getString(R.string.default_user_center_post),
                    R.drawable.default_user_center_post_img
                )
                if (targetId != 0L && Singleton.getUserInfo().userId == targetId) {
                    Log.e(
                        "zqruserId",
                        Singleton.getUserInfo().userId.toString() + "帖子targetId" + targetId
                    )

                    defaultPageView!!.setOnThisItemClickListener {

//                   AlertDialog.Builder(requireActivity())
//                       .setTitle("删除")
//                       .setMessage("确定要跳转到发布吗？")
//                       .setNegativeButton("确定") { dialog, which ->
//                           dialog.dismiss()
//
//                           // TODO: 2021/4/27  跳转到发布需要修改
//                           //确定操作
//                           //回调这里做点击事件
//                           PermissionX.init(this)
//                               .permissions(
//                                   Manifest.permission.CAMERA,
//                                   Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                   Manifest.permission.READ_EXTERNAL_STORAGE
//                               )
//                               .request { allGranted, _, deniedList ->
//                                   if (allGranted) {
//                                       val intent =
//                                           Intent(
//                                               requireActivity(),
//                                               PublishPickerActivity::class.java
//                                           )
//                                       startActivityForResult(
//                                           intent,
//                                           PictureConstants.IMAGE_RESULT_CODE
//                                       )
//                                   } else {
//                                       val list: MutableList<String> = mutableListOf()
//                                       deniedList.forEach {
//                                           if (it == Manifest.permission.CAMERA) {
//                                               list.add("相机")
//                                           }
//                                           if (it == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
//                                               list.add("读取内存")
//                                           }
//                                           if (it == Manifest.permission.READ_EXTERNAL_STORAGE) {
//                                               list.add("写入内存")
//                                           }
//                                       }
//
//                                       Singleton.centerToast(
//                                           requireActivity(),
//                                           "未开启" + list.toString() + "权限,请前往设置开启"
//                                       )
//                                   }
//                               }
//                       }
//                       .show()

                    }
                }
            }

            "赞过" -> {
                defaultPageView!!.setData(
                    getString(R.string.default_user_center_laud),
                    R.drawable.default_user_center_laud_img
                )

                try {
                    if (targetId != 0L && Singleton.getUserInfo().userId == targetId) {
                        Log.e(
                            "zqruserId",
                            Singleton.getUserInfo().userId.toString() + "获赞targetId" + targetId
                        )
                        defaultPageView!!.setOnThisItemClickListener {
                            val ac = requireActivity() as MainActivity
                            ac.turnHome()
                        }
                    }
                } catch (e: Exception) {
                    // TODO:  这里类转换异常
                }


            }
        }
    }

    private var defaultPageView: ZrDefaultPageView? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDefaultPageView(size: Int) {
        //7 帖子   收藏 帖子8  话题11       9 赞过
        when (feedTag) {//获取传递过来的key
            7 -> {
                defaultPageView!!.setData(
                    getString(R.string.default_user_center_post),
                    R.drawable.default_user_center_post_img
                )
            }

            9 -> {
                defaultPageView!!.setData(
                    getString(R.string.default_user_center_laud),
                    R.drawable.default_user_center_laud_img
                )
            }
        }

        when (size) {
            0 -> {
                defaultPageView!!.visibility = View.VISIBLE
            }

            else -> {
                defaultPageView!!.visibility = View.GONE
            }
        }
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
                        if (feedViewModel.mGetUserFeedListBean != null) {
                            getLoadMoreLogic(feedViewModel.mGetUserFeedListBean!!)
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
        feedViewModel.getUserFeedList(
            feedTag - 7,//为了符合视频的tag  这里用减法的方式将参数搞为 0帖子  1收藏  2赞过
            targetId,
            10,
            null
        )

        /**
         * 2.请求结果动态列表
         */
        feedViewModel.getUserFeedListState.observe(this) {
            when (it) {
                is GetUserFeedListState.Success -> {

                    when (refreshState) {
                        0 -> {//刷新
                            mMineFeedList!!.clear()//清空集合
                            mMineFeedList = changeList(feedViewModel.mGetUserFeedListBean)//加载请求后的数据
                            mAdapter!!.setData(mMineFeedList!!, true)//设置数据 更新适配器
                            setDefaultPageView(mMineFeedList!!.size)
                        }

                        1, 2 -> {//加载 预加载
                            when (feedTag) {
                                7, 8, 9 -> {
                                    mAdapter!!.setData(
                                        changeList(feedViewModel.mGetUserFeedListBean)!!,
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
                feedViewModel.getUserFeedList(
                    feedTag - 7,//为了符合视频的tag  这里用减法的方式将参数搞为 0帖子  1收藏  2赞过
                    targetId,
                    10,
                    bean.lastTime
                )
            }
        }
    }

}