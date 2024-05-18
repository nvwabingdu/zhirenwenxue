package cn.dreamfruits.yaoguo.module.share

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemClickListener
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.share.adapter.ShareUserAllListAdapter
import cn.dreamfruits.yaoguo.module.share.adapter.ShareUserSearchAdapter
import cn.dreamfruits.yaoguo.module.share.adapter.ShareUserSelAdapter
import cn.dreamfruits.yaoguo.module.share.bean.UserInfo
import cn.dreamfruits.yaoguo.module.share.state.ShareUserListState
import cn.dreamfruits.yaoguo.util.PinyinUtils
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.view.SideBarView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import java.text.Collator
import java.util.*


/**
 * @author Lee
 * @createTime 2023-07-06 12 GMT+8
 * @desc :
 */
class ShareToActivity : BaseActivity() {

    private val chartViewModel by viewModels<ChartViewModel>()


    private lateinit var sideTextList: MutableList<String>

    private val shareViewModel by viewModels<ShareViewModel>()
    private lateinit var mRvList: RecyclerView
    private lateinit var rv_sel_user: RecyclerView
    private lateinit var rv_search_list: RecyclerView
    private lateinit var mSideBarView: SideBarView
    private lateinit var iv_recent: ImageView
    private lateinit var iv_both: ImageView
    private lateinit var btn_send: Button
    private lateinit var et_content: EditText
    private lateinit var et_search: EditText
    private lateinit var cl_desc: ConstraintLayout
    private lateinit var mAdapter: ShareUserAllListAdapter
    private lateinit var mAdapterSelUser: ShareUserSelAdapter
    private lateinit var mAdapterSearch: ShareUserSearchAdapter
    private lateinit var indeMap: MutableMap<String, Int>

    var customData: String = ""

    private lateinit var footerView: View
    private lateinit var footerViewSearch: View
    private lateinit var emptySearchView: View
    private lateinit var emptyFriendView: View


    companion object {
        @JvmStatic
        fun start(context: Context, customData: String) {
            val starter = Intent(context, ShareToActivity::class.java)
                .putExtra("customData", customData)
            context.startActivity(starter)
        }
    }

    override fun layoutResId(): Int = R.layout.activity_share_to
    override fun initView() {

        rv_sel_user = findViewById(R.id.rv_sel_user)
        mRvList = findViewById(R.id.rv_list)
        rv_search_list = findViewById(R.id.rv_search_list)
        iv_recent = findViewById(R.id.iv_recent)
        iv_both = findViewById(R.id.iv_both)
        cl_desc = findViewById(R.id.cl_desc)
        btn_send = findViewById(R.id.btn_send)
        et_content = findViewById(R.id.et_content)
        et_search = findViewById(R.id.et_search)
        mSideBarView = findViewById<SideBarView>(R.id.sbv_view)

        footerView = View.inflate(this, R.layout.layout_empty_footer_view, null)
        footerViewSearch = View.inflate(this, R.layout.layout_empty_footer_view, null)
        emptySearchView = View.inflate(this, R.layout.layout_attch_goods_empty, null)
        emptyFriendView = View.inflate(this, R.layout.layout_attch_goods_empty, null)

        emptySearchView.findViewById<ImageView>(R.id.iv_cover)
            .setImageResource(R.drawable.empty_share_search)
        emptySearchView.findViewById<TextView>(R.id.tv_desc)
            .text = "没有找到相关搜索结果～"


        emptyFriendView.findViewById<ImageView>(R.id.iv_cover)
            .setImageResource(R.drawable.empty_share_search)
        emptyFriendView.findViewById<TextView>(R.id.tv_desc)
            .text = "您还没有好友可以分享～"



        findViewById<TextView>(R.id.tv_cancel)
            .setOnClickListener { finish() }


        mSideBarView.setOnSideItemSelectListener { position, title ->
            mRvList.scrollToPosition(indeMap[title]!!)
        }

        /**
         * 选择的好友
         */
        mAdapterSelUser = ShareUserSelAdapter(this)
        rv_sel_user.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_sel_user.adapter = mAdapterSelUser

        /**
         * 分享好友列表
         */
        mAdapter = ShareUserAllListAdapter(this)
        var mLinearLayoutManager = LinearLayoutManager(this)
        mRvList.layoutManager = mLinearLayoutManager
        mRvList.adapter = mAdapter
        mAdapter.setEmptyView(emptyFriendView)

        /**
         * 搜索好友列表
         */
        mAdapterSearch = ShareUserSearchAdapter(this)
        rv_search_list.layoutManager = LinearLayoutManager(this)
        rv_search_list.adapter = mAdapterSearch

        mAdapterSearch.addFooterView(footerViewSearch)
        mAdapter.addFooterView(footerView)
        mAdapterSearch.setEmptyView(emptySearchView)


        mRvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstIndex: Int = mLinearLayoutManager.findFirstVisibleItemPosition()
                    val firstKey: String = mAdapter.data[firstIndex].firstKey
                    val index = sideTextList.indexOf(firstKey)
                    mSideBarView.setCurrentIndex(index)
                }
            }
        })

        iv_both.setOnClickListener {
            mRvList.scrollToPosition(indeMap["互关好友"]!!)
        }
        iv_recent.setOnClickListener {
            mRvList.scrollToPosition(indeMap["最近联系"]!!)
        }

        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

                if (mAdapter.data[position].isSel) {
//                    mAdapter.data[position].isSel = false

                    val filter = mAdapter.data.filter { it.id == mAdapter.data[position].id }
                    filter.forEach { it.isSel = false }
                    mAdapterSelUser.remove(filter.first())

                } else {
                    if (mAdapterSelUser.data.size >= 9) {
                        ToastUtils.showShort("最多只能同时分享9个用户")
                        return
                    }
                    val filter = mAdapter.data.filter { it.id == mAdapter.data[position].id }
                    filter.forEach { it.isSel = true }

//                    mAdapter.data[position].isSel = true
                    mAdapterSelUser.addData(filter.first())
                }
//                mAdapter.notifyItemChanged(position)
                mAdapter.notifyDataSetChanged()

                if (mAdapterSelUser.data.isEmpty()) {
                    cl_desc.visibility = View.GONE
                } else {
                    cl_desc.visibility = View.VISIBLE
                }
            }
        })
        et_search.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_ENTER ||
                        keyCode == KeyEvent.KEYCODE_SPACE)
                && event.action == KeyEvent.ACTION_DOWN) {
                // 拦截换行符和空格输入
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        et_search.addTextChangedListener {
            if (TextUtils.isEmpty(it.toString())) {
                rv_search_list.visibility = View.GONE
                mAdapterSearch.data.clear()
            } else {
                var searchKey = it.toString()

                val filter = mAdapter.data.filter {
                    it.firstKey.equals(searchKey, ignoreCase = true)
                            || it.nickName.contains(searchKey)
                            || PinyinUtils.ccs2Pinyin(it.nickName).contains(searchKey)
                }

                if (filter.isNullOrEmpty()) {
                    rv_search_list.visibility = View.VISIBLE
                    mAdapterSearch.data.clear()
                    mAdapterSearch.isUseEmpty = true
                } else {
                    rv_search_list.visibility = View.VISIBLE
                    mAdapterSearch.setList(filter)
                    mAdapterSearch.isUseEmpty = false
                }
            }
        }

        mAdapterSearch.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

                if (mAdapterSearch.data[position].isSel) {

                    val filter =
                        mAdapterSearch.data.filter { it.id == mAdapterSearch.data[position].id }
                    filter.forEach { it.isSel = false }
//                    mAdapterSearch.data[position].isSel = false

                    mAdapterSelUser.remove(filter.first())

                } else {
                    if (mAdapterSelUser.data.size >= 9) {
                        ToastUtils.showShort("最多只能同时分享9个用户")
                        return
                    }
                    val filter =
                        mAdapterSearch.data.filter { it.id == mAdapterSearch.data[position].id }
                    filter.forEach { it.isSel = true }

//                    mAdapterSearch.data[position].isSel = true
                    mAdapterSelUser.addData(filter.first())
                }

                mAdapter.notifyDataSetChanged()

                mAdapterSearch.notifyDataSetChanged()

                if (mAdapterSelUser.data.isEmpty()) {
                    cl_desc.visibility = View.GONE
                } else {
                    cl_desc.visibility = View.VISIBLE
                }
            }
        })


        btn_send.setOnClickListener {

            var content = et_content.text.toString()

            var userIdList: MutableList<String> = arrayListOf()
            mAdapterSelUser.data.forEach {
                userIdList.add(it.id)
            }
            chartViewModel.sendCustomMsg(
                customData.toByteArray(),
                userIdList,
                content
            )

            XPopup.Builder(mContext)
                .asLoading("发送中")
                .show()
                .delayDismissWith(1500) {
                    ToastUtils.showShort("发送成功")
                    finish()
                }
        }
    }

    override fun initData() {

        customData = intent.getStringExtra("customData")!!

        shareViewModel.getShareUserList()

        shareViewModel.shareUserList.observe(this) {
            when (it) {
                is ShareUserListState.Success -> {

                    var list: MutableList<UserInfo> = arrayListOf()
//
                    //最近联系的好友
                    if (!it.shareUserListBean.recentList.isNullOrEmpty()) {
                        iv_recent.visibility = View.VISIBLE
                        val first = it.shareUserListBean.recentList!!.first()
                        first.firstKey = "最近联系"
                        first.isHeader = true
                        list.addAll(it.shareUserListBean.recentList!!)
                    } else {
                        iv_recent.visibility = View.GONE
                    }

                    //互相关注的好友
                    if (!it.shareUserListBean.bothFollowList.isNullOrEmpty()) {
                        iv_both.visibility = View.VISIBLE
                        val first = it.shareUserListBean.bothFollowList!!.first()
                        first.firstKey = "互关好友"
                        first.isHeader = true
                        list.addAll(it.shareUserListBean.bothFollowList!!)
                    } else {
                        iv_both.visibility = View.GONE
                    }

                    //所有好友
                    if (!it.shareUserListBean.allFollowList.isNullOrEmpty()) {

                        it.shareUserListBean.allFollowList!!.forEachIndexed { index, userInfo ->
                            userInfo.firstKey = PinyinUtils.getPinyinFirstLetter(userInfo.nickName)
                        }

                        // 根据首字母进行排序
                        val comparator = Collator.getInstance(Locale.CHINA)
                        //排序
                        it.shareUserListBean.allFollowList!!.sortWith { p0, p1 ->
                            comparator.compare(
                                p0.firstKey,
                                p1.firstKey
                            )
                        }

                        //设置显示头
                        it.shareUserListBean.allFollowList!!.forEachIndexed { index, userInfo ->
                            if (index == 0) {
                                userInfo.isHeader = true
                            } else {
                                userInfo.isHeader =
                                    userInfo.firstKey.uppercase() != it.shareUserListBean.allFollowList!![index - 1].firstKey.uppercase()
                            }
                        }

                        list.addAll(it.shareUserListBean.allFollowList!!)

                        LogUtils.json(Gson().toJson(list))

                    }

                    list.forEach { it.avatarUrlX = it.avatarUrl.decodePicUrls() }

                    mAdapter.setList(list)

                    mAdapter.isUseEmpty = mAdapter.data.isEmpty()

                    sideTextList = arrayListOf()
                    indeMap = mutableMapOf()
                    list.forEachIndexed { index, userInfo ->
                        if (userInfo.isHeader) {
                            sideTextList.add(userInfo.firstKey)
                            indeMap.put(userInfo.firstKey, index)
                        }
                    }
                    mSideBarView.setSideText(sideTextList)

                }

                is ShareUserListState.Fail -> {
                    ToastUtils.showShort(it.msg)
                }

            }
        }

    }
}

//fun getRandomCode(): String {
//    var randomcode = ""
//    // 用字符数组的方式随机
//    val model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ（）&%！"
//    val m = model.toCharArray()
//    var j = 0
//    while (j < 6) {
//        val c = m[(Math.random() * 41).toInt()]
//        // 保证六位随机数之间没有重复的
//        if (randomcode.contains(c.toString())) {
//            j--
//            j++
//            continue
//        }
//        randomcode = randomcode + c
//        j++
//    }
//    return randomcode
//}
//var listFake: MutableList<UserInfo> = arrayListOf()
//for (i in 0 until 100) {
//    var info = UserInfo(
//        firstKey = "",
//        id = "535528593032896",
//        nickName = getRandomCode(),
//        avatarUrl = "https://devfile.dreamfruits.cn/app/1008611/936cda026ef96e6225648cf5a3571b671.png",
//        isHeader = false
//    );
//    info.firstKey = PinyinUtils.getPinyinFirstLetter(info.nickName)
//    listFake.add(info)
//}
//
//listFake.sortWith { p0, p1 ->
//    comparator.compare(
//        p0.firstKey,
//        p1.firstKey
//    )
//}
//listFake.forEachIndexed { index, userInfo ->
//    if (index == 0) {
//        userInfo.isHeader = true
//    } else {
//        userInfo.isHeader =
//            userInfo.firstKey.uppercase() != listFake[index - 1].firstKey.uppercase()
//    }
//}
//
//list.addAll(listFake)