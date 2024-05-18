package cn.dreamfruits.yaoguo.module.main.home.searchdetails.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.customview.ZrDefaultPageView
import cn.dreamfruits.yaoguo.module.main.home.dialog.ShowNoFollowPop
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.SearchViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.FollowUserBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.SearchUserBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.UnfollowUserBeanState
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.Singleton.setRefreshResult
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout


class SearchUserFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mList: MutableList<SearchUserBean.Item>? = ArrayList()
    private val searchViewModel by viewModels<SearchViewModel>()
    private val commonViewModel by viewModels<CommonViewModel>()//公共的viewmodel
    private var mAdapter: SearchUserAdapter? = null
    private var mRootView: View? = null
    private var key = ""
    private var defaultPageView: ZrDefaultPageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mRootView = inflater.inflate(R.layout.home_fragment_search_user, container, false)

        key = arguments!!.getString("key").toString()//获取传递过来的key

        Log.e("da12121", "onCreateView: $key")

        initView()//1.初始化一些东西
        setCallback()//2.设置回调
        setRefresh() //3.设置刷新 请求数据
        refreshLayout!!.autoRefresh()//4.自动刷新

        return mRootView
    }


    /**
     * 初始化
     */
    fun initView() {

        /**缺省*/
        defaultPageView = mRootView!!.findViewById(R.id.default_page_view)
        defaultPageView!!.setData("没有找到相关搜素结果～", R.drawable.empty_share_search)

        mRecyclerView = mRootView!!.findViewById(R.id.recyclerview)
        /*mRecyclerView!!.isNestedScrollingEnabled = false//禁止滑动 解决滑动冲突*/
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = SearchUserAdapter(requireActivity(), mList!!)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun setCallback() {
        /**
         * 回调
         */
        mAdapter!!.setSearchUserAdapterListener(object :
            SearchUserAdapter.SearchUserAdapterInterface {
            //关注
            override fun onCareUser(id: Long, isCare: Boolean) {
                if (isCare) {//关注
                    commonViewModel.getFollowUser(id)
                } else {//取消关注
                    commonViewModel.getUnfollowUser(id)
                }
            }

        })


        /**
         * 请求结果   关注
         */
        commonViewModel.followUserBeanState.observe(this) {
            when (it) {
                is FollowUserBeanState.Success -> {
                    Singleton.centerToast(requireActivity(), Singleton.CARE_TEXT)
                }
                is FollowUserBeanState.Fail -> {
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
                    Singleton.centerToast(requireActivity(), Singleton.UN_CARE_TEXT)
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
    private var refreshLayout: SmartRefreshLayout? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载

    private fun setRefresh() {
        refreshLayout = mRootView!!.findViewById(R.id.refreshLayout)
        refreshLayout!!.setRefreshHeader(MRefreshHeader2(activity))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(activity))
        /**
         * 2.请求结果
         */
        searchViewModel.searchUserBeanState.observe(this) {
            when (it) {
                is SearchUserBeanState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            mList!!.clear()//清空集合
                            mList = searchViewModel.mSearchUserBean!!.list.toMutableList()//加载请求后的数据
                            mAdapter!!.setData(mList!!, true)//设置数据 更新适配器
                            setRefreshResult(refreshLayout!!, 1)

                            setDefaultPageView()
                        }
                        1, 2 -> {//加载 预加载
                            mAdapter!!.setData(
                                searchViewModel.mSearchUserBean!!.list.toMutableList(),
                                false
                            )//设置数据 更新适配器
                            setRefreshResult(refreshLayout!!, 2)

                            setDefaultPageView()
                        }
                    }
                }
                is SearchUserBeanState.Fail -> {
                    setDefaultPageView()
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
                    //2.各自的失败处理逻辑
                    when (refreshState) {
                        0 -> {//刷新
                            setRefreshResult(refreshLayout!!, 3)
                        }
                        1, 2 -> {//加载 预加载
                            setRefreshResult(refreshLayout!!, 4)
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
            refreshState = 0

            // TODO: 请求1
            //2.请求
            searchViewModel.getSearchUser(
                key,//搜索词
                1,//页码，第一次穿1，后面传入上一次接口返回得page
                10,//数量
                null,//用于分页，传入上一次接口返回lastTime
                null//过滤的用户的ids，例如：id1,id2,id3
            )//第一次请求
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            //设置标签为加载（不刷新）
            refreshState = 1
            //2.请求
            // TODO: 请求2
            if (mList!!.size != 0) {
                getLoadMoreLogic(key, null)
            }
        }
    }

    /**
     * 5.加载更多逻辑
     */
    private fun getLoadMoreLogic(key: String, withoutIds: String?) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (searchViewModel.mSearchUserBean == null) {//等于空 说明没有请求过，发出第一次请求
            searchViewModel.getSearchUser(
                key,//搜索词
                1,//页码，第一次穿1，后面传入上一次接口返回得page
                10,//数量
                null,//用于分页，传入上一次接口返回lastTime
                null//过滤的用户的ids，例如：id1,id2,id3
            )//第一次请求
        } else if (searchViewModel.mSearchUserBean != null && searchViewModel.mSearchUserBean!!.hasNext == 1) {
            searchViewModel.getSearchUser(
                key,//搜索词
                searchViewModel.mSearchUserBean!!.page,//页码，第一次穿1，后面传入上一次接口返回得page
                10,//数量
                searchViewModel.mSearchUserBean!!.lastTime,//用于分页，传入上一次接口返回lastTime
                withoutIds//过滤的用户的ids，例如：id1,id2,id3
            )//第一次请求
        } else if (searchViewModel.mSearchUserBean != null && searchViewModel.mSearchUserBean!!.hasNext == 0) {//表示没有更多数据了
            setRefreshResult(refreshLayout!!, 5)
        }
    }

    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */

    private fun setDefaultPageView() {
        if (mList!!.size == 0) {
            defaultPageView!!.visibility = View.VISIBLE
        } else {
            defaultPageView!!.visibility = View.GONE
        }
    }

}