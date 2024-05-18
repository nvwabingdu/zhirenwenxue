package cn.dreamfruits.yaoguo.module.main.mine.fans

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
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
import cn.dreamfruits.yaoguo.module.main.home.state.GetCareListState
import cn.dreamfruits.yaoguo.module.main.home.state.SearchCareUserState
import cn.dreamfruits.yaoguo.module.main.home.state.UnfollowUserBeanState
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.setRefreshResult
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class CareFragment : Fragment() {
    private var mRecyclerView: RecyclerView?=null
    private var mList: MutableList<SearchUserBean.Item>?=ArrayList()
    private val mineViewModel by viewModels<MineViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private var mAdapter: FansAndCareAdapter? = null
    private var mRootView:View?=null
    private var key=""
    private var userId=0L

    private var defaultPageView: ZrDefaultPageView? = null

    private var tempLayout: RelativeLayout? = null
    private var searchEt: EditText? = null
    private var searchDel: ImageView? = null
    private var searchIv: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView=inflater.inflate(R.layout.home_fragment_search_user, container, false)

        key=arguments!!.getString("key").toString()

        userId=arguments!!.getLong("userId")

        Log.e("da12121", "onCreateView: ${key}${userId}")

        initView()//1.初始化一些东西
        setCallback()//2.设置回调
        setRefresh() //3.设置刷新 请求数据
        refreshLayout!!.autoRefresh()//4.自动刷新

        /**缺省*/
        defaultPageView = mRootView!!.findViewById(R.id.default_page_view)
        defaultPageView!!.setData( getString(R.string.default_user_center_follow) ,R.drawable.def_fllow)

        searchLayout()//搜索功能布局

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

    /**搜索功能布局*/
    private fun searchLayout(){
        tempLayout = mRootView!!.findViewById(R.id.temp_layout)
        tempLayout!!.visibility = View.VISIBLE
        searchEt = mRootView!!.findViewById(R.id.search_et)
        searchDel = mRootView!!.findViewById(R.id.search_del)
        searchIv = mRootView!!.findViewById(R.id.search_iv)

        /**设置hint文本*/
        searchEt!!.hint=" 搜索我的关注"

        /** 键盘上的搜素按钮 把回车修改为搜索并实现监听和隐藏键盘*/
        searchEt!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCareUser()//开始搜索
                return@OnEditorActionListener true
            }
            false
        })

        /**搜索接口回调*/
        mineViewModel.searchCareUserState.observe(this){
            when (it) {
                is SearchCareUserState.Success -> {
                    //搜索请求成功
                    when(refreshState){
                        0->{//刷新
                            mList!!.clear()//清空集合
                            mList= mineViewModel.mSearchCareUserBean!!.list.toMutableList()//加载请求后的数据
                            mAdapter!!.setData(mList!!,true)//设置数据 更新适配器
                            setRefreshResult(refreshLayout!!,1)
                            //缺省
                            setDefaultPageView()
                        }
                        1,2->{//加载 预加载
                            mAdapter!!.setData(mineViewModel.mSearchCareUserBean!!.list.toMutableList(), false)//设置数据 更新适配器
                            setRefreshResult(refreshLayout!!,2)
                            //缺省
                            setDefaultPageView()
                        }
                    }

                }
                is SearchCareUserState.Fail->{
                    //缺省
                    setDefaultPageView()
                    //2.各自的失败处理逻辑
                    when(refreshState){
                        0->{//刷新
                            setRefreshResult(refreshLayout!!,3)
                        }
                        1,2->{//加载 预加载
                            setRefreshResult(refreshLayout!!,4)
                        }
                    }
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
                }
            }
        }


        /**删除文本*/
        searchDel!!.setOnClickListener {
            searchEt!!.setText("")
            searchEt!!.hint=" 搜索我的关注"
            isCurrentSearch=false
        }

        /**搜索按钮*/
        searchIv!!.setOnClickListener {
            searchCareUser()//开始搜索
        }


        /**搜索框监听*/
        searchEt!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                    if (!searchEt!!.text.toString().trim().isNotEmpty()){
                        isCurrentSearch =false
                        searchDel!!.visibility=View.GONE
                    }else{
                        isCurrentSearch =true
                        searchDel!!.visibility=View.VISIBLE
                    }
            }
        })


    }


    private var isCurrentSearch=false//是否当前是搜索
    /**点击搜索之后 开始搜索用户*/
    private fun searchCareUser(){
        if (searchEt!!.text.toString().trim().isEmpty()){
            Singleton.centerToast(requireActivity(),"请输入搜索内容")
            return
        }
        /**设置当前是搜索*/
        isCurrentSearch=true

        /**开始刷新*/
        refreshLayout!!.autoRefresh()//自动刷新
    }


    /**回调*/
    private fun setCallback(){
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
       mineViewModel.getCareListState.observe(this){
           when (it) {
               is GetCareListState.Success -> {
                   when(refreshState){
                       0->{//刷新
                           mList!!.clear()//清空集合
                           mList= mineViewModel.mGetCareListBean!!.list.toMutableList()//加载请求后的数据
                           mAdapter!!.setData(mList!!,true)//设置数据 更新适配器
                           setRefreshResult(refreshLayout!!,1)

                           //缺省
                           setDefaultPageView()
                       }
                       1,2->{//加载 预加载
                           mAdapter!!.setData(mineViewModel.mGetCareListBean!!.list.toMutableList(), false)//设置数据 更新适配器
                           setRefreshResult(refreshLayout!!,2)

                           //缺省
                           setDefaultPageView()
                       }
                   }
               }
               is GetCareListState.Fail->{
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
           if (isCurrentSearch){
               refreshState=0
               /**
                * 请求搜索关注列表
                */
               mineViewModel.searchFollow(
                   10,
                   searchEt!!.text.toString(),
                   null,//第一页不传，第二页开始传上次请求接口返回的lastTime
               )
           }else{
               //1.设置标签为刷新
               refreshState=0
               /**
                * 2.请求关注列表
                */
               mineViewModel.getUserFollowList(
                   10,
                   null,
                   userId
               )
           }
       }

       /**
        * 4.上拉加载
        */
       refreshLayout?.setOnLoadMoreListener { refreshlayout ->
           if (isCurrentSearch){
               refreshState=1
               /**
                * 请求搜索关注列表
                */
               if (mList!!.size!=0){
                   getLoadMoreLogicSearch()
               }
           }else{
               //设置标签为加载（不刷新）
               refreshState=1
               //2.请求
               // TODO: 请求2
               if (mList!!.size!=0){
                   getLoadMoreLogic()
               }
           }

       }
    }

    /**
     * 5.加载更多逻辑
     */
    private fun getLoadMoreLogic() {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (mineViewModel.mGetCareListBean == null) {//等于空 说明没有请求过，发出第一次请求
            mineViewModel.getUserFollowList(
                10,
                null,
                userId
            )
        } else if (mineViewModel.mGetCareListBean != null && mineViewModel.mGetCareListBean!!.hasNext == 1) {
            mineViewModel.getUserFollowList(
                10,
                mineViewModel.mGetCareListBean!!.lastTime,
                userId
            )
        } else if (mineViewModel.mGetCareListBean != null && mineViewModel.mGetCareListBean!!.hasNext == 0) {//表示没有更多数据了
            setRefreshResult(refreshLayout!!,5)
        }
    }

    /**
     * 5.加载更多逻辑  搜索
     */
    private fun getLoadMoreLogicSearch() {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (mineViewModel.mSearchCareUserBean == null) {//等于空 说明没有请求过，发出第一次请求
            mineViewModel.searchFollow(
                10,
                searchEt!!.text.toString(),
                null,//第一页不传，第二页开始传上次请求接口返回的lastTime
            )
        } else if (mineViewModel.mSearchCareUserBean != null && mineViewModel.mSearchCareUserBean!!.hasNext == 1) {
            mineViewModel.searchFollow(
                10,
                searchEt!!.text.toString(),
                mineViewModel.mSearchCareUserBean!!.lastTime,//第一页不传，第二页开始传上次请求接口返回的lastTime
            )
        } else if (mineViewModel.mSearchCareUserBean != null && mineViewModel.mSearchCareUserBean!!.hasNext == 0) {//表示没有更多数据了
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