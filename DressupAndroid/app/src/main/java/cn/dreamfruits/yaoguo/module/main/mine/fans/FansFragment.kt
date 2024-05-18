package cn.dreamfruits.yaoguo.module.main.mine.fans

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.customview.ZrDefaultPageView
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.FollowUserBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetFansListState
import cn.dreamfruits.yaoguo.module.main.home.state.UnfollowUserBeanState
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.setRefreshResult
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class FansFragment : Fragment() {
    private var mRecyclerView: RecyclerView?=null
    private var mList: MutableList<SearchUserBean.Item>?=ArrayList()
    private val mineViewModel by viewModels<MineViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private var mAdapter: FansAndCareAdapter? = null
    private var mRootView:View?=null
    private var key=""
    private var userId=0L

    private var fansText: TextView? = null
    private var defaultPageView: ZrDefaultPageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView=inflater.inflate(R.layout.home_fragment_search_user, container, false)

        key=arguments!!.getString("key").toString()

        userId=arguments!!.getLong("userId")

        Log.e("da12121", "onCreateView: ${key}${userId}")

        fansText = mRootView!!.findViewById(R.id.fans_text)
        fansText!!.visibility=View.VISIBLE

        initView()//1.初始化一些东西
        setCallback()//2.设置回调
        setRefresh() //3.设置刷新 请求数据
        refreshLayout!!.autoRefresh()//4.自动刷新

        /**缺省*/
        defaultPageView = mRootView!!.findViewById(R.id.default_page_view)
        defaultPageView!!.setData( getString(R.string.default_user_center_fans) ,R.drawable.def_fans)

        return mRootView
    }

    /**
     * 初始化
     */
    fun initView(){
        mRecyclerView = mRootView!!.findViewById(R.id.recyclerview)
        /*mRecyclerView!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突*/
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = FansAndCareAdapter(requireActivity(),mList!!)
        mRecyclerView!!.adapter = mAdapter
    }

   private fun  setCallback(){
       /**
        * 回调
        */
       mAdapter!!.setFansAndCareAdapterCallBack(object :FansAndCareAdapter.InnerInterface{
           //关注
           override fun onCareUser(id: Long, isCare: Boolean) {
               if (isCare){//关注
                   commonViewModel.getFollowUser(id)
               }else{//取消关注
                   commonViewModel.getUnfollowUser(id)
               }
           }

       })


       /**
        * 请求结果   关注
        */
       commonViewModel.followUserBeanState.observe(this){
           when (it) {
               is FollowUserBeanState.Success -> {
                   Singleton.centerToast(requireActivity(),Singleton.CARE_TEXT)
               }
               is FollowUserBeanState.Fail->{
                   //1.是否有断网提示
                   Singleton.isNetConnectedToast(requireActivity())
               }
           }
       }


       /**
        * 请求结果  取消关注
        */
       commonViewModel.unfollowUserBeanState.observe(this) {
           when (it) {
               is UnfollowUserBeanState.Success -> {
                   Singleton.centerToast(requireActivity(),Singleton.UN_CARE_TEXT)
               }
               is UnfollowUserBeanState.Fail -> {
                   //1.是否有断网提示
                   Singleton.isNetConnectedToast(requireActivity())
               }
           }
       }
   }

    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */
    private var refreshLayout: SmartRefreshLayout?= null
    private var refreshState:Int=0//0 刷新  1 加载  2预加载

    private fun  setRefresh(){
       refreshLayout = mRootView!!.findViewById(R.id.refreshLayout)
       refreshLayout!!.setRefreshHeader(MRefreshHeader2(activity))
       refreshLayout!!.setRefreshFooter(ClassicsFooter(activity))
       /**
        * 2.请求结果
        */
        mineViewModel.getFansListState.observe(this){
           when (it) {
               is GetFansListState.Success -> {
                   Log.e("da12121", "粉丝数量=====: ${mineViewModel.mGetFansListBean!!.total}")
                   fansText!!.text="我的粉丝（${Singleton.setNumRuler(mineViewModel.mGetFansListBean!!.total,"0")}）"
                   when(refreshState){
                       0->{//刷新
                           mList!!.clear()//清空集合
                           mList= mineViewModel.mGetFansListBean!!.list.toMutableList()//加载请求后的数据
                           mAdapter!!.setData(mList!!,true)//设置数据 更新适配器
                           setRefreshResult(refreshLayout!!,1)

                           //缺省
                           setDefaultPageView()
                       }
                       1,2->{//加载 预加载
                           mAdapter!!.setData(mineViewModel.mGetFansListBean!!.list.toMutableList(), false)//设置数据 更新适配器
                           setRefreshResult(refreshLayout!!,2)

                           //缺省
                           setDefaultPageView()
                       }
                   }
               }
               is GetFansListState.Fail->{
                   //缺省
                   setDefaultPageView()
                   //1.是否有断网提示
                   Singleton.isNetConnectedToast(requireActivity())
                   //2.各自的失败处理逻辑
                   when(refreshState){
                       0->{//刷新
                           setRefreshResult(refreshLayout!!,3)
                       }
                       1,2->{//加载 预加载
                           setRefreshResult(refreshLayout!!,4)
                       }
                   }
               }
           }
       }

       /**
        * 3.下拉刷新
        */
       refreshLayout?.setOnRefreshListener { refreshlayout ->
           //1.设置标签为刷新
           refreshState=0

           /**
            * 请求粉丝列表
            */
           mineViewModel.getUserFollowerList(
               10,
               null,
               userId
           )
       }

       /**
        * 4.上拉加载
        */
       refreshLayout?.setOnLoadMoreListener { refreshlayout ->
           //设置标签为加载（不刷新）
           refreshState=1
           //2.请求
           // TODO: 请求2
           if (mList!!.size!=0){
               getLoadMoreLogic(key,null)
           }
       }
    }

    /**
     * 5.加载更多逻辑
     */
    private fun getLoadMoreLogic(key: String,withoutIds: String?) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (mineViewModel.mGetFansListBean == null) {//等于空 说明没有请求过，发出第一次请求
            mineViewModel.getUserFollowerList(
                10,
                null,
                userId
            )
        } else if (mineViewModel.mGetFansListBean  != null && mineViewModel.mGetFansListBean !!.hasNext == 1) {
            mineViewModel.getUserFollowerList(
                10,
                mineViewModel.mGetFansListBean!!.lastTime,
                userId
            )
        } else if (mineViewModel.mGetFansListBean != null && mineViewModel.mGetFansListBean!!.hasNext == 0) {//表示没有更多数据了
            setRefreshResult(refreshLayout!!,5)
        }
    }

    /**
     * 设置缺省
     */
    private fun setDefaultPageView(){
        if (mList!!.size==0){
            defaultPageView!!.visibility=View.VISIBLE
        }else{
            defaultPageView!!.visibility=View.GONE
        }
    }
    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */

}