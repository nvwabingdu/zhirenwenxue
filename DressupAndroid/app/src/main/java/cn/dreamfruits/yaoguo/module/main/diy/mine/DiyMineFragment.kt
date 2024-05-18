package cn.dreamfruits.yaoguo.module.main.diy.mine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.module.main.home.state.MyDiyListBeanBeanState
import cn.dreamfruits.yaoguo.module.main.diy.DiyViewModel
import cn.dreamfruits.yaoguo.module.main.home.state.DeleteClothesItemState
import cn.dreamfruits.yaoguo.module.main.home.state.RemakeClothesItem
import cn.dreamfruits.yaoguo.module.main.stroll.StrollViewModel
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.SingleDetailsActivity
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.util.Singleton
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xiaomi.push.it

class DiyMineFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mList: MutableList<GetStyleVersionListByTypeBean.Item>? = ArrayList()
    private val diyViewModel by viewModels<DiyViewModel>()
    private val strollViewModel by viewModels<StrollViewModel>()
    private var mAdapter: DiyMineAdapter? = null
    private var mRootView: View? = null
    private lateinit var cl_empty: ConstraintLayout
    private var key = 2//0-私密，1-公开，2-全部

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.home_fragment_search_singledress, container, false)

        when (arguments!!.getString("key").toString()) {
            "私密" -> {
                key = 0
            }

            "公开" -> {
                key = 1
            }

            "全部" -> {
                key = 2
            }
        }

        initView()//1.初始化一些东西
        setRefresh() //2.设置刷新 请求数据

        setNetCallBack()//删除或者重新制作的回调
        return mRootView
    }

    /**设置*/
    override fun onResume() {
        super.onResume()
        refreshLayout!!.autoRefresh()//3.自动刷新
    }

    /**
     * 初始化
     */
    fun initView() {
        mRecyclerView = mRootView!!.findViewById<RecyclerView>(R.id.recyclerview)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView!!.layoutManager = layoutManager
        mAdapter = DiyMineAdapter(requireActivity(), mList!!)
        mRecyclerView!!.adapter = mAdapter


        cl_empty = mRootView!!.findViewById(R.id.cl_empty)

        //跳转单品详情页
        mAdapter!!.setDiyMineAdapterCallBack(object : DiyMineAdapter.InnerInterface {
            override fun onclick(id: Long) {
                val intent = Intent(requireActivity(), SingleDetailsActivity::class.java)//帖子详情页
                intent.putExtra("id", id)
                startActivity(intent)
            }

            override fun delete(id: Long, position: Int) {
                showDelOrRemake(id,View.GONE)
                delPosition=position
            }

            override fun remake(id: Long, position: Int) {
                showDelOrRemake(id,View.VISIBLE)
                remakePosition=position
            }
        })
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
        diyViewModel.myDiyListBeanBeanState.observe(this) {
            when (it) {
                is MyDiyListBeanBeanState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            mList!!.clear()//清空集合
                            mList = diyViewModel.mMyDiyListBean!!.list//加载请求后的数据
                            mAdapter!!.setData(mList!!, true)//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 1)

                            if (mAdapter!!.dataList.isEmpty()) {
                                cl_empty.visibility = View.VISIBLE
                            } else {
                                cl_empty.visibility = View.GONE
                            }
                        }

                        1, 2 -> {//加载 预加载
                            mAdapter!!.setData(
                                diyViewModel.mMyDiyListBean!!.list,
                                false
                            )//设置数据 更新适配器
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                            if (mAdapter!!.dataList.isEmpty()) {
                                cl_empty.visibility = View.VISIBLE
                            } else {
                                cl_empty.visibility = View.GONE
                            }
                        }
                    }
                }

                is MyDiyListBeanBeanState.Fail -> {
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

        /**
         * 3.下拉刷新
         */
        refreshLayout?.setOnRefreshListener { refreshlayout ->
            refreshState = 0//设置为刷新
            //2.请求
            diyViewModel.getMyDiyList(
                10,
                key,//0-私密，1-公开，2-全部
                null
            )
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            refreshState = 1//设置为加载

            if (mList!!.size != 0) {
                // TODO: 请求2 版型id
                getLoadMoreLogic(diyViewModel.mMyDiyListBean!!, key)
            }
        }
    }

    /**
     * 5.请求逻辑
     */
    private fun getLoadMoreLogic(bean: GetStyleVersionListByTypeBean, key: Int) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean != null && bean.hasNext == 1) {//加载更多
            diyViewModel.getMyDiyList(
                10,
                key,//0-私密，1-公开，2-全部
                null
            )
        } else if (bean != null && bean.hasNext == 0) {//表示没有更多数据了
            Singleton.setRefreshResult(refreshLayout!!, 5)
        }
    }
    /**
     * ----------------------------------------刷新逻辑----------------------------------------
     */



    /**
     * ----------------------------------------------------删除 或者  重制删除  弹窗
     */
    fun showDelOrRemake(dressId:Long,llReportVisibility: Int) {
        val inflate =
            LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_dress_del_remake, null)

        val llReport = inflate.findViewById<View>(R.id.ll_report)//重新制作
        llReport.visibility =llReportVisibility
        val llDel = inflate.findViewById<View>(R.id.ll_del)//删除
        val cancel = inflate.findViewById<TextView>(R.id.cancel)//取消

        var mPopupWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        /**重制*/
        llReport.setOnClickListener {
            mPopupWindow.dismiss()
            /**
             * 重新上传制作服装单品  请求
             */
            diyViewModel.remakeClothesItem(dressId)
        }

        /**删除*/
        llDel.setOnClickListener {
            mPopupWindow.dismiss()
            /**
             * 删除服装单品  请求
             */
            strollViewModel.deleteClothesItem(dressId)
        }

        /**取消*/
        cancel.setOnClickListener {
            mPopupWindow.dismiss()
        }

        //动画
        mPopupWindow!!.animationStyle = R.style.BottomDialogAnimation

        if (mPopupWindow.isShowing) {//如果正在显示，关闭弹窗。
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isTouchable = true
            mPopupWindow.isFocusable = true
            mPopupWindow.showAtLocation(inflate, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            Singleton.bgAlpha(requireActivity(), 0.5f) //设置透明度0.5
            mPopupWindow.setOnDismissListener {
                Singleton.bgAlpha(requireActivity(), 1f) //恢复透明度
            }
        }

        //在pause时关闭
        Singleton.lifeCycleSet(requireActivity(), mPopupWindow)
    }



    private var delPosition=-1
    private var remakePosition=-1

    /**上面的接口回调 直接放在初始化*/
    private fun setNetCallBack(){
        /**
         * 重新上传制作服装单品  请求回调
         */
        diyViewModel.remakeClothesItemState.observe(requireActivity()) {
            when (it) {
                is RemakeClothesItem.Success -> {
                    //       diyViewModel.mCommonStateIntBean
                    if (diyViewModel.mRemakeClothesItemBean != null) {
                        when (diyViewModel.mRemakeClothesItemBean!!.state) {
                            0 -> {
//                                Singleton.centerToast(requireActivity(), "重新制作失败")
                            }

                            1 -> {
//                                Singleton.centerToast(requireActivity(), "重新制作成功")
                                if (remakePosition!=-1){
                                    mAdapter!!.delItem(remakePosition)
                                }
                            }
                        }
                    }
                    remakePosition=-1
                }
                is RemakeClothesItem.Fail -> {
                    if (it.errorMsg!=null&&it.errorMsg!=""){
                        Singleton.centerToast(requireActivity(), it.errorMsg.toString())
                    }else{
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(requireActivity())
                    }
                    remakePosition=-1
                }
            }
        }


        /**
         * 删除单品  请求回调
         */
        strollViewModel.deleteClothesItemState.observe(requireActivity()) {
            when (it) {
                is DeleteClothesItemState.Success -> {
                    if (strollViewModel.mDeleteClothesItemBean != null) {
                        when (strollViewModel.mDeleteClothesItemBean!!.state) {
                            0 -> {
//                                Singleton.centerToast(requireActivity(), "删除失败")
                            }

                            1 -> {
                                Singleton.centerToast(requireActivity(), "删除成功")
                                if (delPosition!=-1){
                                    mAdapter!!.delItem(delPosition)
                                }
                            }
                        }
                    }
                    delPosition=-1
                }

                is DeleteClothesItemState.Fail -> {
                    if (it.errorMsg != null && it.errorMsg != "") {
                        Singleton.centerToast(requireActivity(), it.errorMsg.toString())
                    } else {
                        //1.是否有断网提示
                        Singleton.isNetConnectedToast(requireActivity())
                    }
                    delPosition=-1
                }
            }
        }


    }


}