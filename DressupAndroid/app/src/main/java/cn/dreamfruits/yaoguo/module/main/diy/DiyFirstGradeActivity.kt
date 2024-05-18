package cn.dreamfruits.yaoguo.module.main.diy

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.diy.mine.DiyMineActivity
import cn.dreamfruits.yaoguo.module.main.diy.threelevel.DiyThreeGradeActivity
import cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage.CardAdapter
import cn.dreamfruits.yaoguo.module.main.diy.zrbannerpage.library.CardScaleHelper
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionListByTypeState
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionTypeListBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionTypeListBeanStateLeft
import cn.dreamfruits.yaoguo.module.main.home.unity.AndroidBridgeActivity
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.isNewScene
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xiaomi.push.id
import com.xiaomi.push.it


/**
 * @Author qiwangi
 * @Date 2023/6/2
 * @TIME 17:23
 */
class DiyFirstGradeActivity : BaseActivity() {
    private val diyViewModel by viewModels<DiyViewModel>()

    override fun layoutResId() = R.layout.activity_diy_clothes

    override fun initData() {}

    override fun initView() {
        top()
        init()
        leftList()
        rightList()
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
     * --------跳到我的
     */
    private var diyMineImg: View? = null
    private fun init() {
        diyMineImg = findViewById(R.id.diy_mine_img)
        diyMineImg!!.setOnClickListener {
            //跳转到我的页面
            val intent = Intent(this, DiyMineActivity::class.java)
            intent.putExtra("key", "value")
            startActivity(intent)
        }
    }


    /**
     * ----------------------------------------左列表逻辑----------------------------------------
     */

    private var mLeftRecyclerview: RecyclerView? = null
    private var mLeftList: MutableList<GetStyleVersionListByTypeBean.Item>? = ArrayList()
    private var mLeftAdapter: DiyLeftAdapter? = null
    private fun leftList() {
        mLeftRecyclerview = findViewById(R.id.diy_left_recyclerview)
        val layoutManager = LinearLayoutManager(this)
        mLeftRecyclerview!!.layoutManager = layoutManager
        mLeftAdapter = DiyLeftAdapter(this, mLeftList!!)
        mLeftRecyclerview!!.adapter = mLeftAdapter

        /**数据请求*/
        diyViewModel.getStyleVersionTypeListLeft(1L, null)

        diyViewModel.getStyleVersionTypeListBeanStateLeft.observe(this) {
            when (it) {
                is GetStyleVersionTypeListBeanStateLeft.Success -> {
                    mLeftList!!.clear()
                    mLeftList = diyViewModel.mGetStyleVersionTypeListBeanLeft!!.list
                    //初始化之后的一些状态
                    mLeftList!![0].isSelect = true//这里需要设置第一个默认为选中状态
                    mLeftAdapter!!.setData(mLeftList!!, true)

                    // TODO:  获取版型分类列表APP（目前用于diy）  表示请求完成之后立即刷新
                    diyViewModel.getStyleVersionTypeList(
                        2,
                        getType()
                    )
                }

                is GetStyleVersionTypeListBeanStateLeft.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(this, it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(this)
                    }
                }
            }
        }

        /**左侧列表回调*/
        mLeftAdapter!!.setDiyLeftAdapterCallBack(object : DiyLeftAdapter.DiyLeftAdapterInterface {
            override fun onclick(position: Int) {
                refreshState = 0//相当于刷新

                // TODO:  获取版型分类列表APP（目前用于diy）
                diyViewModel.getStyleVersionTypeList(
                    2,
                    getType()
                )

            }
        })


        // TODO:  获取版型分类列表APP（目前用于diy）
        diyViewModel.getStyleVersionTypeList(
            2,
            getType()//1上装，2下装，3连衣裙，4袜子，5鞋子，6配饰
        )
    }


    /**
     * ----------------------------------------右列表逻辑----------------------------------------
     */
    private var mRightRecyclerview: RecyclerView? = null
    private var mRightList: MutableList<GetStyleVersionListByTypeBean.Item>? = ArrayList()
    private var mRightAdapter: DiyRightAdapter? = null

    private fun rightList() {
        mRightRecyclerview = findViewById<RecyclerView>(R.id.diy_right_recyclerview)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRightRecyclerview!!.layoutManager = layoutManager
        mRightAdapter = DiyRightAdapter(this, mRightList!!)
        mRightRecyclerview!!.adapter = mRightAdapter

        setRefreshRight() //2.设置刷新 请求数据

        rightCallBack()
    }

    private var refreshLayout: SmartRefreshLayout? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载
    private fun setRefreshRight() {
        refreshLayout = findViewById(R.id.refreshLayout)
//        refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))
        refreshLayout!!.setEnableRefresh(false)
        /**
         * 2.请求结果
         */
        diyViewModel.getStyleVersionTypeListBeanState.observe(this) {
            when (it) {
                is GetStyleVersionTypeListBeanState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            mRightList!!.clear()//清空集合
                            mRightList = diyViewModel.mGetStyleVersionTypeListBean!!.list//加载请求后的数据
                            mRightAdapter!!.setData(mRightList!!, true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }

                        1, 2 -> {//加载 预加载
                            mRightAdapter!!.setData(
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


//        /**
//         * 3.下拉刷新
//         */
//        refreshLayout?.setOnRefreshListener { refreshlayout ->
//            refreshState=0//设置为刷新
        // TODO: 请求1
        //2.请求
//            searchViewModel.getSearchSingleProduct(
//                key,//搜索词
//                1,//页码，第一次穿1，后面传入上一次接口返回得page
//                10,//数量
//                null//用于分页，传入上一次接口返回lastTime
//            )
//        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            if (mRightList!!.size != 0) {
                refreshState = 1//设置为加载
                // TODO: 请求2
                getLoadMoreLogic(diyViewModel.mGetStyleVersionTypeListBean!!)
            }

        }
    }


    /**
     * 右边列表回调  跳转unity
     */
    private fun rightCallBack() {
        mRightAdapter!!.setDiyRightAdapterCallBack(object :
            DiyRightAdapter.DiyRightAdapterInterface {
            override fun onclick(id: Long, name: String, isOver: Int) {
                when (isOver) {//是否还有下一级 0否 1是
                    1 -> {
                        showBannerPop(id)
                    }

                    0 -> {
                        val intent = Intent(
                            this@DiyFirstGradeActivity,
                            DiyThreeGradeActivity::class.java
                        )//帖子详情页
                        intent.putExtra("svId", id)
                        intent.putExtra("title", name)
                        intent.putExtra("isOver", isOver)
                        startActivity(intent)
                    }
                }
            }
        })
    }

    private fun getLoadMoreLogic(bean: GetStyleVersionListByTypeBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean != null && bean.hasNext == 1) {//加载更多
//            diyViewModel.getStyleVersionListByType(
//                getType(),//1-上装，2-下装，3-连衣裙，4-袜子，5-鞋子，6-配饰
//                10,
//                null,
//                bean.lastTime,
//                1//从逛逛页获取数据不用传，从DIY页获取数据传1
//            )

            // TODO:  获取版型分类列表APP（目前用于diy）
            diyViewModel.getStyleVersionTypeList(
                2,
                getType()//1上装，2下装，3连衣裙，4袜子，5鞋子，6配饰
            )

        } else if (bean != null && bean.hasNext == 0) {//表示没有更多数据了
            Singleton.setRefreshResult(refreshLayout!!, 5)
        }
    }

    /**
     * 返回对应的类型
     */
    fun getType(): Long {
        var typeId = 1L
        mLeftList!!.forEach {
            if (it.isSelect) {
                typeId = it.id
            }
        }
        return typeId
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
                val intent = Intent(this@DiyFirstGradeActivity, AndroidBridgeActivity::class.java)
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
                    val intent = Intent(this@DiyFirstGradeActivity, AndroidBridgeActivity::class.java)
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