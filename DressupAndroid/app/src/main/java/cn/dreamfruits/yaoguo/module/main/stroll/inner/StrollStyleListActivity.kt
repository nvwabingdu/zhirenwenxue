package cn.dreamfruits.yaoguo.module.main.stroll.inner

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.GetClothesItemListByTypeStrollStrollState
import cn.dreamfruits.yaoguo.module.main.diy.mine.StrollInnerAdapter
import cn.dreamfruits.yaoguo.module.main.stroll.StrollViewModel
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.SingleDetailsActivity
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.BarUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @Author qiwangi
 * @Date 2023/6/9
 * @TIME 15:13
 */
class StrollStyleListActivity : BaseActivity(){

    private var mRecyclerView: RecyclerView? = null

    private var mList: MutableList<GetStyleVersionListByTypeBean.Item>?=ArrayList()
    private val strollViewModel by viewModels<StrollViewModel>()
    private var mAdapter: StrollInnerAdapter? = null

    private var svId: Long=0
    private var gender: Int=0

    private var tvTitle: TextView? = null

    override fun layoutResId(): Int = R.layout.activity_stroll_style_id_list

    override fun initData() {}

    override fun initView() {

        //沉浸状态栏重叠问题
        findViewById<View>(R.id.status_bar).layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight())

        svId= intent.getLongExtra("svId",0)
        Log.e("qiwangi","svId="+svId)
        gender=intent.getIntExtra("gender",0)

        tvTitle=findViewById(R.id.tv_title)

        tvTitle!!.text=intent.getStringExtra("title")
        top()
        init()//1.初始化一些东西
        setRefresh() //2.设置刷新 请求数据
        refreshLayout!!.autoRefresh()//3.自动刷新
    }

    /**
     * 顶部
     */
    fun top(){
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }

    /**
     * 列表
     */
    fun init(){
        mRecyclerView =findViewById<RecyclerView>(R.id.recyclerview)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = StrollInnerAdapter(this,mList!!)
        mRecyclerView!!.adapter = mAdapter

        //设置回调
        setCallBack()
    }

    /**
     * 回调
     */
    private fun setCallBack(){
        mAdapter!!.setStrollInnerAdapterCallBack(object : StrollInnerAdapter.InnerInterface{
            override fun onclick(id: Long) {
                    val intent = Intent(this@StrollStyleListActivity, SingleDetailsActivity::class.java)//帖子详情页
                    intent.putExtra("id", id)
                    startActivity(intent)
            }
        })
    }


    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */
    private var refreshLayout: SmartRefreshLayout?= null
    private var refreshState:Int=0//0 刷新  1 加载  2预加载

    private fun  setRefresh(){
        refreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))
        /**
         * 2.请求结果
         */
        strollViewModel.getClothesItemListByTypeStrollStrollState.observe(this){
            when (it) {
                is GetClothesItemListByTypeStrollStrollState.Success -> {
                    when(refreshState){
                        0->{//刷新
                            mList!!.clear()//清空集合
                            mList= strollViewModel.mGetStyleVersionListByTypeBean!!.list//加载请求后的数据
                            mAdapter!!.setData(mList!!,true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }
                        1,2->{//加载 预加载
                            mAdapter!!.setData(strollViewModel.mGetStyleVersionListByTypeBean!!.list, false)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                        }
                    }
                }
                is GetClothesItemListByTypeStrollStrollState.Fail->{
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(this)
                    //2.各自的失败处理逻辑
                    when(refreshState){
                        0->{//刷新
                            Singleton.setRefreshResult(refreshLayout!!, 3)
                        }
                        1,2->{//加载 预加载
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
            refreshState=0//设置为刷新
            // TODO: 需要修改
            //2.请求
//            strollViewModel.getClothesItemListBySvId(
//                svId,
//                10,
//                gender,
//                2,//0-私密，1-公开，2-全部
//                null,
//            )

            strollViewModel.getClothesItemListByTypeStroll(
                gender,
                svId,
                10,
                null
            )
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            refreshState=1//设置为加载

            if (mList!!.size!=0){
                // TODO: 请求2 版型id
                getLoadMoreLogic(strollViewModel.mGetStyleVersionListByTypeBean!!)
            }
        }
    }

    /**
     * 5.请求逻辑
     */
    private fun getLoadMoreLogic(bean: GetStyleVersionListByTypeBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean != null && bean.hasNext == 1) {//加载更多
            // TODO:  需要修改
//            strollViewModel.getClothesItemListBySvId(
//                svId,
//                10,
//                gender,
//                2,//0-私密，1-公开，2-全部
//                bean.lastTime,
//            )
//

            strollViewModel.getClothesItemListByTypeStroll(
                gender,
                svId,
                10,
                bean.lastTime
            )


        } else if (bean != null && bean.hasNext == 0) {//表示没有更多数据了
            Singleton.setRefreshResult(refreshLayout!!, 5)
        }
    }


}