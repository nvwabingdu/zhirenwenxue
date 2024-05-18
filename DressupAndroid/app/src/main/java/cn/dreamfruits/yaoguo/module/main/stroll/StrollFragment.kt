package cn.dreamfruits.yaoguo.module.main.stroll

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionTypeListStrollState
import cn.dreamfruits.yaoguo.module.main.home.state.StyleVersionListByTypeBeanState
import cn.dreamfruits.yaoguo.module.main.diy.DiyLeftAdapter
import cn.dreamfruits.yaoguo.module.main.diy.bean.DiyLeftBean
import cn.dreamfruits.yaoguo.module.main.home.state.GetSearchWordBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetStyleVersionTypeListStrollStateLeft
import cn.dreamfruits.yaoguo.module.main.stroll.inner.StrollStyleListActivity
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

open class StrollFragment : Fragment() {
    private var mRootView: View? = null
    private val strollViewModel by viewModels<StrollViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_stroll, container, false)

        top()

        leftList()

        rightList()

        return mRootView
    }

    /**
     * 男女选项卡
     */
    private var diyTopGender: View? = null
    private var genderState: Int = 0//0女  1男   默认是女

    private var diyTopWoman: View? = null
    private var diyTopWomanTv: TextView? = null
    private var diyTopWomanLine: View? = null

    private var diyTopMan: View? = null
    private var diyTopManTv: TextView? = null
    private var diyTopManLine: View? = null

    /**顶部男女选项卡是否隐藏*/
    var height: Int? = null
    var topIsOpen: Boolean = false//性别选项卡 true收缩  false展开

    /**
     * 顶部
     */
    fun top() {
        // TODO: 搜索  后续添加
        mRootView!!.findViewById<ImageView>(R.id.stroll_search).setOnClickListener {
            Toast.makeText(requireActivity(), "搜索", Toast.LENGTH_SHORT).show()
        }

        // 男女选项卡
        diyTopGender = mRootView!!.findViewById(R.id.diy_top_gender)

        diyTopWoman = mRootView!!.findViewById(R.id.diy_top_woman)
        diyTopWomanTv = mRootView!!.findViewById(R.id.diy_top_woman_tv)
        diyTopWomanLine = mRootView!!.findViewById(R.id.diy_top_woman_line)

        diyTopMan = mRootView!!.findViewById(R.id.diy_top_man)
        diyTopManTv = mRootView!!.findViewById(R.id.diy_top_man_tv)
        diyTopManLine = mRootView!!.findViewById(R.id.diy_top_man_line)

        diyTopWoman!!.setOnClickListener {
            initGender(0)
            // TODO: 添加请求
        }

        diyTopMan!!.setOnClickListener {
            initGender(1)
            // TODO: 添加请求
        }

        // TODO:  性别选项卡初始展开还是收缩

//        /**延时初始化*/
//        delayedAction(1000) {
//            setTopGenderVisibility()
//            initGender(0)
//        }
    }
    private fun setTopGenderVisibility(tag: Int) {
        if (height == null) {
            height = diyTopGender!!.height
        }

        // 创建属性动画对象，设置动画属性为“height”，从控件高度到0
        val anim1: ValueAnimator = ValueAnimator.ofInt(height!!, 0)
        anim1.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val value = animation.getAnimatedValue() as Int
                diyTopGender!!.layoutParams.height = value
                diyTopGender!!.requestLayout()
            }
        })

        // 创建属性动画对象，设置动画属性为“height”，从0到控件高度
        val anim2: ValueAnimator = ValueAnimator.ofInt(0, height!!)
        anim2.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val value = animation.getAnimatedValue() as Int
                diyTopGender!!.layoutParams.height = value
                diyTopGender!!.requestLayout()
            }
        })

        when (tag) {
            0 -> {
                Log.e("TAG123", "收缩")
                // 如果需要收缩，则播放第一个动画
                anim1.duration = 300
                anim1.start()
                topIsOpen = true
            }

            1 -> {
                Log.e("TAG123", "展开")
                // 如果需要展开，则播放第二个动画
                anim2.duration = 300
                anim2.start()
                topIsOpen = false
            }
        }
    }
    private fun initGender(tag: Int) {
        diyTopWomanTv!!.setTextColor(Color.parseColor("#80202020"))
        diyTopWomanLine!!.visibility = View.INVISIBLE

        diyTopManTv!!.setTextColor(Color.parseColor("#80202020"))
        diyTopManLine!!.visibility = View.INVISIBLE

        when (tag) {
            0 -> {
                diyTopWomanTv!!.setTypeface(null, Typeface.BOLD)
                diyTopWomanTv!!.setTextColor(Color.parseColor("#202020"))
                diyTopWomanLine!!.visibility = View.VISIBLE
                genderState = 0
            }

            1 -> {
                diyTopManTv!!.setTypeface(null, Typeface.BOLD)
                diyTopManTv!!.setTextColor(Color.parseColor("#202020"))
                diyTopManLine!!.visibility = View.VISIBLE
                genderState = 1
            }
        }
    }
    //======================延时======================VV
    private val handler = Handler()
    private fun delayedAction(delayMillis: Long, action: () -> Unit) {
        handler.postDelayed(action, delayMillis)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)// 在 Fragment 销毁前，取消所有还未执行的延时操作
    }
    //======================延时======================^^

    /**
     * ----------------------------------------左列表逻辑----------------------------------------
     */
    private var mLeftRecyclerview: RecyclerView? = null
    private var mLeftList: MutableList<GetStyleVersionListByTypeBean.Item>? = ArrayList()
    private var mLeftAdapter: DiyLeftAdapter? = null
    private fun leftList() {
        /**加载数据*/
        mLeftRecyclerview = mRootView!!.findViewById(R.id.diy_left_recyclerview)
        val layoutManager = LinearLayoutManager(requireActivity())
        mLeftRecyclerview!!.layoutManager = layoutManager
        mLeftAdapter = DiyLeftAdapter(requireActivity(), mLeftList!!)
        mLeftRecyclerview!!.adapter = mLeftAdapter

        /**请求左侧列表数据  级数是1*/
        strollViewModel.getStyleVersionTypeListStrollLeft(1, null)

        strollViewModel.getStyleVersionTypeListStrollStateLeft.observe(this) {
            when (it) {
                is GetStyleVersionTypeListStrollStateLeft.Success -> {
                    mLeftList!!.clear()
                    mLeftList = strollViewModel.mGetStyleVersionListByTypeStrollBeanLeft!!.list

                    //初始化之后的一些状态
                    mLeftList!![0].isSelect = true//这里需要设置第一个默认为选中状态
                    if (mLeftList!![0].name.equals("精选特辑")) {
                        setListViewState(0)
                    }else{
                        setListViewState(1)
                    }

                    mLeftAdapter!!.setData(mLeftList!!, true)

                    //立即请求第右侧列表
                    when (mLeftList!![0].name) {
                        "精选特辑" -> {
                            //设置为精选特辑刷新
                            refreshStateGreatestHits=0

                            //隐藏性别选项卡
//                        if (topIsOpen){
//                            setTopGenderVisibility(0)
//                        }

                            //精选特辑的列表显示
                            setListViewState(0)

                            // TODO:  精选特辑请求数据 待做


                        }
                        else -> {
                            //设置为其他标签页刷新
                            refreshState=0

//                        //展开性别选项卡
//                        if (!topIsOpen){
//                            setTopGenderVisibility(1)
//                        }

                            //其他的列表
                            setListViewState(1)

                            /**请求二级列表数据  这里是逛逛*/
                            strollViewModel.getStyleVersionTypeListStroll(
                                2,
                                getType(),)
                        }
                    }

                }

                is GetStyleVersionTypeListStrollStateLeft.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(requireActivity(), it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(requireActivity())
                    }
                }
            }
        }

        /**左侧列表回调*/
        mLeftAdapter!!.setDiyLeftAdapterCallBack(object : DiyLeftAdapter.DiyLeftAdapterInterface {
            override fun onclick(position: Int) {
                when (mLeftList!![position].name) {
                    "精选特辑" -> {
                        //设置为精选特辑刷新
                        refreshStateGreatestHits=0

                        //隐藏性别选项卡
//                        if (topIsOpen){
//                            setTopGenderVisibility(0)
//                        }

                        //精选特辑的列表显示
                        setListViewState(0)

                        // TODO:  精选特辑请求数据 待做


                    }
                    else -> {
                        //设置为其他标签页刷新
                        refreshState=0

//                        //展开性别选项卡
//                        if (!topIsOpen){
//                            setTopGenderVisibility(1)
//                        }

                        //其他的列表
                        setListViewState(1)

                        /**请求二级列表数据  这里是逛逛*/
                        strollViewModel.getStyleVersionTypeListStroll(
                            2,
                            getType(),)
                    }
                }
            }
        })
    }
    fun setListViewState(tag: Int){
        when(tag){
            0->{
                mRightRecyclerview!!.visibility = View.VISIBLE
                mRightRecyclerviewTwo!!.visibility = View.GONE
            }
            1->{
                mRightRecyclerview!!.visibility = View.GONE
                mRightRecyclerviewTwo!!.visibility = View.VISIBLE
            }
        }
    }

    /**
     * ----------------------------------------右列表逻辑1----------------------------------------
     */
    private var mRightRecyclerview: RecyclerView? = null
    private var mRightRecyclerviewTwo: RecyclerView? = null//非精选特辑列表
    private var mRightList: MutableList<String>? = ArrayList()
    private var mRightAdapter: StrollRightAdapter? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载
    private var refreshStateGreatestHits: Int = 0//0 刷新  1 加载  2预加载    针对于精选特辑

    //第二种列表
    private var mRightListTwo: MutableList<GetStyleVersionListByTypeBean.Item>? = ArrayList()
    private var mRightAdapterTwo: StrollRightTwoAdapter? = null//第二种适配器  这里复用diy的

    private fun rightList() {
        //精选特辑列表
        mRightRecyclerview = mRootView!!.findViewById<RecyclerView>(R.id.diy_right_recyclerview)
        val layoutManager = LinearLayoutManager(requireActivity())
        mRightRecyclerview!!.layoutManager = layoutManager

        //伪数据
        (0..6).forEach {
            mRightList!!.add("111")
        }

        mRightAdapter = StrollRightAdapter(requireActivity(), mRightList!!)
        mRightRecyclerview!!.adapter = mRightAdapter

        //回调
        mRightAdapter!!.setStrollRightAdapterCallBack(object :
            StrollRightAdapter.StrollRightAdapterInterface {
            override fun onclick(id: Long) {
//                val intent = Intent(this@DiyClothesActivity, AndroidBridgeActivity::class.java)
//                intent.putExtra("entryType", "EnterDIY")
//                intent.putExtra("data", id.toString())
//                startActivity(intent)

//                val intent: Intent = Intent(this@DiyClothesActivity, UnityPlayerActivitySingleton.getInstance().javaClass)
//                intent.putExtra("entryType", "EnterDIY")
//                intent.putExtra("data", id.toString())
//                startActivity(intent)
            }
        })

        //其他列表
        mRightRecyclerviewTwo = mRootView!!.findViewById<RecyclerView>(R.id.diy_right_two_recyclerview)
        val layoutManagerTwo = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRightRecyclerviewTwo!!.layoutManager = layoutManagerTwo
        mRightAdapterTwo = StrollRightTwoAdapter(requireActivity(), mRightListTwo!!)
        mRightRecyclerviewTwo!!.adapter = mRightAdapterTwo

        //回调
        mRightAdapterTwo!!.setStrollRightTwoAdapterCallBack(object :
            StrollRightTwoAdapter.InnerInterface {
            override fun onclick(svId: Long, name: String) {
                // TODO:  先从这里跳到 单品id页面
                val intent = Intent(requireActivity(), StrollStyleListActivity::class.java)//帖子详情页
                intent.putExtra("svId", svId)
                intent.putExtra("gender", genderState)
                intent.putExtra("title", name)
                startActivity(intent)
            }
        })

        setRefreshRight() //2.设置刷新 请求数据

        strollViewModel.getStyleVersionTypeListStroll(
            2,
            getType(),
        )
    }

    private fun setRefreshRight() {
        refreshLayout = mRootView!!.findViewById(R.id.refreshLayout)
        //refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(requireActivity()))
        refreshLayout!!.setEnableRefresh(false)
        /**
         * 精选特辑 请求返回
         */
        // TODO:  精选特辑


        /**
         * 其他 上衣 下装...请求返回
         */
        strollViewModel.getStyleVersionTypeListStrollState.observe(this) {
            when (it) {
                is GetStyleVersionTypeListStrollState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            mRightListTwo!!.clear()//清空集合
                            mRightListTwo =
                                strollViewModel.mGetStyleVersionListByTypeStrollBean!!.list//加载请求后的数据
                            mRightAdapterTwo!!.setData(mRightListTwo!!, true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }

                        1, 2 -> {//加载 预加载
                            mRightAdapterTwo!!.setData(
                                strollViewModel.mGetStyleVersionListByTypeStrollBean!!.list,
                                false
                            )//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                        }
                    }
                }

                is GetStyleVersionTypeListStrollState.Fail -> {
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
            when (getLeftName()) {
                "精选特辑" -> {//精选特辑
                    if (mRightList!!.size != 0) {
                        refreshStateGreatestHits = 1//设置为加载
                        // TODO: 精选特辑加载请求
                    }
                }

                else -> {//其他  上衣 下装
                    if (mRightListTwo!!.size != 0) {
                        refreshState = 1//设置为加载
                        getLoadMoreLogicTwo(strollViewModel.mGetStyleVersionListByTypeStrollBean!!)
                    }
                }
            }
        }
    }

    /**
     * 精选特辑 加载更多
     */
    private fun getLoadMoreLogic(bean: GetStyleVersionListByTypeBean, key: String) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean != null && bean.hasNext == 1) {//加载更多

        } else if (bean != null && bean.hasNext == 0) {//表示没有更多数据了
            Singleton.setRefreshResult(refreshLayout!!, 5)
        }
    }

    /**
     * 其他 上衣 下装...
     */
    private fun getLoadMoreLogicTwo(bean: GetStyleVersionListByTypeBean) {
        if (bean != null && bean.hasNext == 1) {//加载更多

            strollViewModel.getStyleVersionTypeListStroll(
                2,
                getType(),
            )

        } else if (bean != null && bean.hasNext == 0) {//表示没有更多数据了
            Singleton.setRefreshResult(refreshLayout!!, 5)
        }


    }


    /**
     * 返回对应的类型
     */
    private fun getType(): Long {
        var typeId = 0L
        mLeftList!!.forEach {
            if (it.isSelect) {
                typeId = it.id
            }
        }
        return typeId
    }


    /**通过id 获取对应的name*/
    private fun getLeftName(): String {
        var name = ""
        mLeftList!!.forEach {
            if (it.isSelect) {
                name = it.name
            }
        }
        return name
    }

}





