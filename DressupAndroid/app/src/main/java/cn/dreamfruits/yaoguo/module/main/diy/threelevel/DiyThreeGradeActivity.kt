package cn.dreamfruits.yaoguo.module.main.diy.threelevel

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionTypeListBeanState
import cn.dreamfruits.yaoguo.module.main.diy.DiyViewModel
import cn.dreamfruits.yaoguo.module.main.diy.mine.DiyMineAdapter2
import cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage.CardAdapter
import cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage.library.CardScaleHelper
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionListByTypeState
import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlin.collections.ArrayList

/**
 * @Author qiwangi
 * @Date 2023/6/19
 * @TIME 15:16
 */
class DiyThreeGradeActivity : BaseActivity() {

    private var mRecyclerView: RecyclerView? = null

    private var mList: MutableList<GetStyleVersionListByTypeBean.Item>? = ArrayList()
    private val diyViewModel by viewModels<DiyViewModel>()

    private var mAdapter: DiyMineAdapter2? = null

    private var svId: Long = 0
    private var gender: Int = 0

    private var tvTitle: TextView? = null
    private var diyBannerPageFragment: FrameLayout? = null

//    private val mFragment = DiyPageBannerFragment()

    override fun layoutResId(): Int = R.layout.activity_stroll_style_id_list

    override fun initData() {
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
    }

    override fun initView() {

        //沉浸状态栏重叠问题
        findViewById<View>(R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BarUtils.getStatusBarHeight()
            )

        svId = intent.getLongExtra("svId", 0)
        Log.e("qiwangi", "svId=" + svId)
        gender = intent.getIntExtra("gender", 0)

        tvTitle = findViewById(R.id.tv_title)

        tvTitle!!.text = intent.getStringExtra("title")

        top()//顶部
        init()//1.初始化一些东西
        setRefresh() //2.设置刷新 请求数据
        refreshLayout!!.autoRefresh()//3.自动刷新
    }

    /**
     * 顶部
     */
    fun top() {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }

    /**
     * 列表
     */
    private lateinit var handler: Handler
    fun init() {
        mRecyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = DiyMineAdapter2(this, mList!!)
        mRecyclerView!!.adapter = mAdapter

        //设置回调
        setCallBack()

        //用于显示隐藏 fragment布局
        diyBannerPageFragment = findViewById<FrameLayout>(R.id.diy_banner_page_fragment)

        /**
         * banner fragment
         */


        /**
         * 创建fragment  这里先加载出来
         */
//        val bundle = Bundle()
//        mFragment.arguments = bundle
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.diy_banner_page_fragment, mFragment)
//            .commit()

        val isOver = intent.getIntExtra("isOver", 0)//是否还有下一级 0否 1是
        Log.e("qiwangi", "isOver=" + isOver)
        Log.e("qiwangi", "svId=" + svId)


        handler = Handler(Looper.getMainLooper())

        Log.e("zqr diy选择", "isOver=" + isOver)
        if (isOver == 1) {
            //显示fragment
            diyBannerPageFragment!!.visibility = View.VISIBLE

//            val timer = Timer()
//            timer.schedule(object : TimerTask() {
//                override fun run() {
//                    // 发送消息到主线程
//                    handler.post {
//                        mFragment.upDateAdapter(svId, 2)
//                    }
//                }
//            }, 500)

        }

//        //当前fragemnt回调
//        mFragment.setDiyPageBannerFragmentCallBack(object : DiyPageBannerFragment.InnerInterface {
//            override fun onConfirm() {
//                //暂无操作
//            }
//
//            override fun onBack() {
//                if (diyBannerPageFragment!!.visibility == View.GONE) {
//                    diyBannerPageFragment!!.visibility = View.VISIBLE
//                } else {
//                    diyBannerPageFragment!!.visibility = View.GONE
//                }
//            }
//
//            override fun upDate() {
//                //这里用于fragment 更新数据
//            }
//        })
    }


    /**
     * 回调
     */
    private fun setCallBack() {
        mAdapter!!.setDiyMineAdapterCallBack(object : DiyMineAdapter2.InnerInterface {
            override fun onclick(id: Long) {
//                val intent = Intent(this@DiyStyleListActivity, DiyPageBannerActivity2::class.java)//帖子详情页
//                intent.putExtra("id", id)
//                startActivity(intent)

                Log.e("zqr diy选择", "id=" + id)
                //调用fragment的方法
//                mFragment.upDateAdapter(id, 3)

                showBannerPop(id)

//                //显示DiyPageBannerFragment
//                if (diyBannerPageFragment!!.visibility == View.GONE) {
//                    diyBannerPageFragment!!.visibility = View.VISIBLE
//                } else {
//                    diyBannerPageFragment!!.visibility = View.GONE
//                }


            }
        })
    }

    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */
    private var refreshLayout: SmartRefreshLayout? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载

    private fun setRefresh() {
        refreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))
        /**
         * 2.请求结果
         */
        diyViewModel.getStyleVersionTypeListBeanState.observe(this) {
            when (it) {
                is GetStyleVersionTypeListBeanState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            mList!!.clear()//清空集合
                            mList = diyViewModel.mGetStyleVersionTypeListBean!!.list//加载请求后的数据
                            mAdapter!!.setData(mList!!, true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }

                        1, 2 -> {//加载 预加载
                            mAdapter!!.setData(
                                diyViewModel.mGetStyleVersionTypeListBean!!.list,
                                false
                            )//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                        }
                    }
                }

                is GetStyleVersionTypeListBeanState.Fail -> {
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
         * 3.下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            refreshState = 0//设置为刷新
            // TODO: 需要修改
//            //2.请求
//            strollViewModel.getClothesItemListBySvId(
//                svId,
//                10,
//                gender,
//                2,//0-私密，1-公开，2-全部
//                null,
//            )

            // TODO:  获取版型分类列表APP（目前用于diy）
            diyViewModel.getStyleVersionTypeList(
                3,
                svId//1上装，2下装，3连衣裙，4袜子，5鞋子，6配饰
            )
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            refreshState = 1//设置为加载

            if (mList!!.size != 0) {
                // TODO: 请求2 版型id
                getLoadMoreLogic(diyViewModel.mGetStyleVersionTypeListBean!!)
            }
        }
    }

    /**
     * 5.请求逻辑
     */
    private fun getLoadMoreLogic(bean: GetStyleVersionListByTypeBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean != null && bean.hasNext == 1) {//加载更多
//            // TODO:  需要修改
//            strollViewModel.getClothesItemListBySvId(
//                svId,
//                10,
//                gender,
//                2,//0-私密，1-公开，2-全部
//                bean.lastTime,
//            )
            // TODO:  获取版型分类列表APP（目前用于diy）
            diyViewModel.getStyleVersionTypeList(
                3,
                svId//1上装，2下装，3连衣裙，4袜子，5鞋子，6配饰
            )
        } else if (bean != null && bean.hasNext == 0) {//表示没有更多数据了
            Singleton.setRefreshResult(refreshLayout!!, 5)
        }
    }


    /**用帧布局实现banner弹窗*/
    fun showBannerPop(mId: Long) {
        val inflate = LayoutInflater.from(this).inflate(R.layout.dialog_banner_page, null)
        var requestState = 0 //0 刷新  1 加载  2预加载
        var mBannerList: MutableList<GetStyleVersionListByTypeBean.Item>? = ArrayList()

        val mBannerRecyclerView = inflate.findViewById<View>(R.id.banner_recyclerView) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBannerRecyclerView.layoutManager = linearLayoutManager
        val cardAdapter = CardAdapter(this, mBannerList!!)
        mBannerRecyclerView.adapter = cardAdapter

        // mRecyclerView绑定scale效果
        val mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper.currentItemPos = 0
        mCardScaleHelper.attachToRecyclerView(mBannerRecyclerView)

        /**pop弹窗*/
        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )

        /**加载banner数据请求回调*/
        diyViewModel.getStyleVersionListByTypeState.observe(this) {
            when (it) {
                is GetStyleVersionListByTypeState.Success -> {
                    when (requestState) {
                        0 -> {
                            mBannerList!!.clear()
                            mBannerList = diyViewModel.mGetStyleVersionListByTypeBean!!.list
                            cardAdapter!!.setData(mBannerList!!, true)
                        }

                        1 -> {
                            cardAdapter!!.setData(
                                diyViewModel.mGetStyleVersionListByTypeBean!!.list,
                                true
                            )
                        }
                    }
                }

                is GetStyleVersionListByTypeState.Fail -> {
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.各自的失败处理逻辑
                }
            }
        }

        /**请求数据*/
        diyViewModel.getStyleVersionListByType(mId, 10, null)

        /**点击X*/
        inflate.findViewById<ImageView>(R.id.diy_page_back).setOnClickListener {
            mPopupWindow.dismiss()
        }

        /**点击确定跳转unity*/
        inflate.findViewById<ImageView>(R.id.diy_page_confirm).setOnClickListener {
            if (mBannerList!!.size != 0) {
                Singleton.isNewScene = true
                val intent = Intent(this@DiyThreeGradeActivity, AndroidBridgeActivity::class.java)
                intent.putExtra("entryType", "EnterDIY")
                intent.putExtra("data", mBannerList!![mCardScaleHelper!!.currentItemPos].id.toString())
                startActivity(intent)
            }
        }

//        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                    Log.e("zqr176543", "mLastPos"+mLastPos)
//                    Log.e("zqr176543", "mCardScaleHelper!!.currentItemPos"+mCardScaleHelper!!.currentItemPos)
//                    Log.e("zqr176543", "mBannerList[mCardScaleHelper!!.currentItemPos]"+mBannerList!![mCardScaleHelper!!.currentItemPos])
//
//                }
//            }
//        })

        /**recycler回调*/
        cardAdapter.setCardAdapterCallBack(object : CardAdapter.InnerInterface {
            override fun onclick(id: Long, position: Int) {
                Log.e("zqr176543", "id" + id + "" + mBannerList!![position].name)
                /**跳转unity*/
                if (mBannerList!!.size != 0) {
                    Singleton.isNewScene = true
                    val intent = Intent(this@DiyThreeGradeActivity, AndroidBridgeActivity::class.java)
                    intent.putExtra("entryType", "EnterDIY")
                    intent.putExtra("data", id.toString())
                    startActivity(intent)
                }
            }

            /**预加载*/
            override fun onPreload() {
                if (mBannerList!!.size != 0) {
                    if (diyViewModel.mGetStyleVersionListByTypeBean != null) {
                        if (diyViewModel.mGetStyleVersionListByTypeBean!!.hasNext == 1) {
                            /**加载数据*/
                            requestState = 1
                            diyViewModel.getStyleVersionListByType(
                                mId,
                                10,
                                diyViewModel.mGetStyleVersionListByTypeBean!!.lastTime
                            )
                        }
                    }
                }
            }
        })

        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.CENTER, 0, 0)
            Singleton.bgAlpha(this, 0.3f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                Singleton.bgAlpha(this, 1f) //恢复透明度
            }
        }
    }

}