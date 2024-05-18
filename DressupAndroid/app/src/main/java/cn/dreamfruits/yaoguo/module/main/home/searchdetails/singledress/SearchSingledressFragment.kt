package cn.dreamfruits.yaoguo.module.main.home.searchdetails.singledress

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.CommonViewModel
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.SearchViewModel
import cn.dreamfruits.yaoguo.module.main.home.searchdetails.user.SearchUserAdapter
import cn.dreamfruits.yaoguo.module.main.home.state.SearchSingleProductBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.SearchUserBeanState
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchSingleProductBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class SearchSingledressFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mList: MutableList<WaterfallFeedBean.Item.Info.Single>?=ArrayList()
    private val searchViewModel by viewModels<SearchViewModel>()
    private var mAdapter: SearchSingleDressAdapter? = null
    private var mRootView:View?=null
    private var key=""
    private lateinit var mClEmpty: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView =inflater.inflate(R.layout.home_fragment_search_singledress, container, false)

        key = arguments!!.getString("key").toString()//获取传递过来的key
        Log.e("da12121", "onCreateView: $key")

        initView()//1.初始化一些东西
        setRefresh() //2.设置刷新 请求数据
        refreshLayout!!.autoRefresh()//3.自动刷新

        return mRootView
    }

    /**
     * 初始化
     */
    fun initView(){
        mRecyclerView = mRootView!!.findViewById<RecyclerView>(R.id.recyclerview)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = SearchSingleDressAdapter(requireActivity(),mList!!)
        mRecyclerView!!.adapter = mAdapter

        mClEmpty = mRootView!!.findViewById(R.id.cl_empty)
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
        searchViewModel.searchSingleProductBeanState.observe(this){
            when (it) {
                is SearchSingleProductBeanState.Success -> {

                    if (searchViewModel.mSearchSingleProductBean == null){
                        mClEmpty.visibility = View.VISIBLE
                        return@observe
                    }

                    when(refreshState){
                        0->{//刷新
                            mList!!.clear()//清空集合
                            mList= searchViewModel.mSearchSingleProductBean!!.list.toMutableList()//加载请求后的数据
                            mAdapter!!.setData(mList!!,true)//设置数据 更新适配器
                            if (mAdapter!!.dataList.isEmpty()){
                                mClEmpty.visibility = View.VISIBLE
                            }else{
                                mClEmpty.visibility = View.GONE
                            }
                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }
                        1,2->{//加载 预加载
                            mAdapter!!.setData(searchViewModel.mSearchSingleProductBean!!.list.toMutableList(), false)//设置数据 更新适配器
                            if (mAdapter!!.dataList.isEmpty()){
                                mClEmpty.visibility = View.VISIBLE
                            }else{
                                mClEmpty.visibility = View.GONE
                            }
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                        }
                    }
                }
                is SearchSingleProductBeanState.Fail->{
                    mClEmpty.visibility = View.VISIBLE
                    //1.是否有断网提示
                    Singleton.isNetConnectedToast(requireActivity())
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
            // TODO: 请求1
            //2.请求


            searchViewModel.getSearchSingleProduct(
                key,//搜索词
                1,//页码，第一次穿1，后面传入上一次接口返回得page
                10,//数量
                null//用于分页，传入上一次接口返回lastTime
            )
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            refreshState=1//设置为加载

            if (mList!!.size!=0){
                // TODO: 请求2
                getLoadMoreLogic(searchViewModel.mSearchSingleProductBean!!,key)
            }
        }
    }

    /**
     * 5.请求逻辑
     */
    private fun getLoadMoreLogic(bean:SearchSingleProductBean,key: String) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
     if (bean != null && bean.hasNext == 1) {//加载更多
            searchViewModel.getSearchSingleProduct(
                key,//搜索词
                1,//页码，第一次穿1，后面传入上一次接口返回得page
                10,//数量
                bean.lastTime//用于分页，传入上一次接口返回lastTime
            )
        } else if (bean != null && bean.hasNext == 0) {//表示没有更多数据了
            Singleton.setRefreshResult(refreshLayout!!, 5)
        }
    }
    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */
}