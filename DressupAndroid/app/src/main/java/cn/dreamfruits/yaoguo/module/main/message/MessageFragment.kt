package cn.dreamfruits.yaoguo.module.main.message

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.module.login.ConversionAddState
import cn.dreamfruits.yaoguo.module.login.ConversionChangedState
import cn.dreamfruits.yaoguo.module.login.DeleteConversionState
import cn.dreamfruits.yaoguo.module.login.bean.UserInfo
import cn.dreamfruits.yaoguo.module.login.signature.GenerateTestUserSig
import cn.dreamfruits.yaoguo.module.main.home.state.ConversationListState
import cn.dreamfruits.yaoguo.module.main.home.state.GetSysMessageListBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.GetUnreadCountBeanState
import cn.dreamfruits.yaoguo.module.main.home.state.IsStrangerBeanState
import cn.dreamfruits.yaoguo.module.main.message.adapter.MessageAdapter
import cn.dreamfruits.yaoguo.module.main.message.into.MessageInnerPageActivity
import cn.dreamfruits.yaoguo.repository.UserRepository
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity
import cn.dreamfruits.yaoguo.repository.bean.message.GetMessageInnerPageListBean
import cn.dreamfruits.yaoguo.util.Singleton
import cn.dreamfruits.yaoguo.util.decodePicUrls
import cn.dreamfruits.yaoguo.view.swipe.SwipeMenuLayout
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.reflect.TypeToken
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMManager


open class MessageFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mList: MutableList<GetMessageInnerPageListBean.Item> = arrayListOf()
    private lateinit var mAdapter: MessageAdapter
    private var mRootView: View? = null
    private val messageViewModel by viewModels<MessageViewModel>()//公共的viewmodel

    private var messageCommentNum: TextView? = null
    private var messageLaudNum: TextView? = null
    private var messageFollowNum: TextView? = null


    private lateinit var headerView: View
    private lateinit var systemView: View
    private lateinit var strangerView: View
    private lateinit var emptyFooterView: View

    var page = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_message, container, false)
        initTop()
        initView()
        setCallback()
        setRefresh()
        refreshLayout!!.autoRefresh()//自动刷新

//        refreshLayout!!.setEnableRefresh(false)
//            .setEnableLoadMore(false)

        return mRootView
    }


    /**
     * 0-赞|收藏和引用，1-新增关注，2-评论消息，4-系统消息
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTop() {

        headerView = View.inflate(context, R.layout.layout_message_header, null)
        systemView = View.inflate(context, R.layout.item_message_, null)
        strangerView = View.inflate(context, R.layout.item_message_, null)
        emptyFooterView = View.inflate(context, R.layout.layout_attch_goods_empty, null)

        emptyFooterView.findViewById<ImageView>(R.id.iv_cover)
            .setImageResource(R.drawable.empty_message)
        emptyFooterView.findViewById<TextView>(R.id.tv_desc).text = "暂无消息喔～"

        messageCommentNum = headerView.findViewById(R.id.message_comment_num)
        messageLaudNum = headerView.findViewById(R.id.message_laud_num)
        messageFollowNum = headerView.findViewById(R.id.message_follow_num)

        headerView.findViewById<View>(R.id.message_laud_layout).setOnClickListener {
            messageViewModel.getUpdateUnreadToRead(0)//请求接口 设置为已读
            val intent = Intent(requireActivity(), MessageInnerPageActivity::class.java)//帖子详情页
            intent.putExtra("title", "赞和收藏和引用")
            intent.putExtra("tag", 0)
            requireActivity().startActivity(intent)
            Singleton.setMessageNum(messageLaudNum!!, 0)
        }


        headerView.findViewById<View>(R.id.message_follow_layout).setOnClickListener {
            messageViewModel.getUpdateUnreadToRead(1)//请求接口 设置为已读
            val intent = Intent(requireActivity(), MessageInnerPageActivity::class.java)//帖子详情页
            intent.putExtra("title", "新增关注")
            intent.putExtra("tag", 1)
            requireActivity().startActivity(intent)
            Singleton.setMessageNum(messageFollowNum!!, 0)
        }

        headerView.findViewById<View>(R.id.message_comment_layout).setOnClickListener {
            messageViewModel.getUpdateUnreadToRead(2)//请求接口 设置为已读
            val intent = Intent(requireActivity(), MessageInnerPageActivity::class.java)//帖子详情页
            intent.putExtra("title", "评论和@")
            intent.putExtra("tag", 2)
            requireActivity().startActivity(intent)
            Singleton.setMessageNum(messageCommentNum!!, 0)
        }

    }

    fun initView() {
        mRecyclerView = mRootView!!.findViewById(R.id.recyclerView)
        mRecyclerView!!.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.layoutManager = layoutManager

        mAdapter = MessageAdapter(context!!, object : MessageAdapter.OnChildItemClick {

            override fun onDelete(item: ConversionEntity, position: Int) {
                LogUtils.e(">>>> item = " + " --- pos = " + position)
//                mAdapter.removeAt(position - 1)
                messageViewModel.deleteConversation(
                    item.conversation!!.conversationID,
                    position - 1
                )
            }

            override fun onToTop(item: ConversionEntity, position: Int) {
                LogUtils.e(">>>> item = " + " --- pos = " + position)
                messageViewModel.pinConversation(
                    item.conversation!!.conversationID,
                    !item.conversation!!.isPinned,
                    position
                )
            }

            override fun onCancelTop(item: ConversionEntity, position: Int) {
                LogUtils.e(">>>> item = " + " --- pos = " + position)
                messageViewModel.pinConversation(
                    item.conversation!!.conversationID,
                    !item.conversation!!.isPinned,
                    position
                )
            }

            override fun onContent(item: ConversionEntity, position: Int) {
                LogUtils.e(">>>> item111 = " + " --- pos = " + position)

                if (item.conversation!!.userID == "administrator") {

                    return
                }

                if (item.conversation!!.unreadCount == 0) {
                    ChartActivity.start(context!!, item.conversation!!.userID)
                } else {
                    val conversationID = "c2c_" + item.conversation!!.userID
                    V2TIMManager.getConversationManager().cleanConversationUnreadMessageCount(
                        conversationID,
                        0,
                        0,
                        object : V2TIMCallback {
                            override fun onSuccess() {
                                ChartActivity.start(context!!, item.conversation!!.userID)
                            }

                            override fun onError(code: Int, desc: String) {

                            }
                        })

                }
            }
        })


        mRecyclerView!!.adapter = mAdapter
        mAdapter.addHeaderView(headerView)

        systemView.findViewById<SwipeMenuLayout>(R.id.sml_all).isSwipeEnable = false
        strangerView.findViewById<SwipeMenuLayout>(R.id.sml_all).isSwipeEnable = false

        strangerView.findViewById<ConstraintLayout>(R.id.cl_content).setOnClickListener {
            StrangerMsgActivity.start(requireActivity())
        }

        systemView.findViewById<ImageView>(R.id.message_icon)
            .setImageResource(R.drawable.message_system)
        systemView.findViewById<TextView>(R.id.message_name)
            .text = "系统消息"
        systemView.findViewById<ImageView>(R.id.message_set_top_img)
            .visibility = View.GONE
        systemView.findViewById<ImageView>(R.id.message_mute_img)
            .visibility = View.GONE
        systemView.findViewById<TextView>(R.id.message_message_count)
            .visibility = View.GONE
        systemView.findViewById<View>(R.id.view_red_dot)
            .visibility = View.GONE

        systemView.findViewById<ConstraintLayout>(R.id.cl_content).setOnClickListener {

        }
    }


    private fun setCallback() {

    }

    private var refreshLayout: SmartRefreshLayout? = null
    private var refreshState: Int = 0//0 刷新  1 加载  2预加载
    private var isNoMoreData = false
    private var listConversation: MutableList<ConversionEntity> = arrayListOf()
    private var listStranger: MutableList<ConversionEntity> = arrayListOf()
    private var listSystem: MutableList<ConversionEntity> = arrayListOf()
    private fun setRefresh() {
        refreshLayout = mRootView!!.findViewById(R.id.refreshLayout)
        /**
         * 获取消息未读数（整合）
         */
        messageViewModel.getUnreadCountBeanState.observe(this) {
            when (it) {
                is GetUnreadCountBeanState.Success -> {
                    when (refreshState) {
                        0 -> {//刷新
                            //
                            Singleton.setMessageNum(
                                messageCommentNum!!,
                                messageViewModel.mGetUnreadCountBean!!.commentCount
                            )
                            Singleton.setMessageNum(
                                messageLaudNum!!,
                                messageViewModel.mGetUnreadCountBean!!.laudCount
                            )
                            Singleton.setMessageNum(
                                messageFollowNum!!,
                                messageViewModel.mGetUnreadCountBean!!.followCount
                            )

                            Singleton.setRefreshResult(refreshLayout!!, 1)
                        }
                        1, 2 -> {//加载 预加载
                            Singleton.setRefreshResult(refreshLayout!!, 2)
                        }
                    }
                }
                is GetUnreadCountBeanState.Fail -> {
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
         * 系统消息
         */
        messageViewModel.getSysMessageListBeanState.observe(this) {
            when (it) {
                is GetSysMessageListBeanState.Success -> {
                    mList.clear()
                    mList = messageViewModel.mGetSysMessageListBean!!.list
                    mAdapter.removeHeaderView(systemView)
                    if (mList.isNotEmpty()) {
                        mAdapter.addHeaderView(systemView, 1)
                    }
                }
                is GetSysMessageListBeanState.Fail -> {
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
            refreshState = 0 //1.设置标签为刷新
            messageViewModel.getUnreadCount()//获取消息整合数
            /**
             * 获取会话消息
             */
            messageViewModel.nextSeq = 0L
            messageViewModel.getConversationList(page)
        }

        /**
         * 4.上拉加载
         */
        refreshLayout?.setOnLoadMoreListener { refreshlayout ->
            refreshState = 1 //设置标签为加载（不刷新）
//            messageViewModel.getSysMessageList(1, null) //请求系统消息
            /**
             * 获取会话消息
             */
            messageViewModel.getConversationList(page)
        }

        /**
         * 获取到的会话消息列表
         */
        messageViewModel.conversation.observe(this) {
            when (it) {
                is ConversationListState.Success -> {
//                    LogUtils.e(">>> conversion list " + it.v2TIMConversationResult.conversationList.size)
//                    LogUtils.e(">>> conversion = " + it.v2TIMConversationResult.conversationList.toString())

                    if (it.v2TIMConversationResult.isFinished) {
                        refreshLayout!!.finishLoadMoreWithNoMoreData()
                    }

                    handleMsgList(it.v2TIMConversationResult.conversationList, false)
                }
                is ConversationListState.Fail -> {
                    LogUtils.e(">>>> code = ${it.code}", "desc = ${it.errorMsg}")
                }
            }
        }

        /**
         * 根据服务器进行判断是否会话在陌生人列表中
         */
        messageViewModel.isStrangerBeanState.observe(this) {
            when (it) {
                is IsStrangerBeanState.Success -> {

                    if (refreshState == 0) {
                        listConversation.clear()
                        listStranger.clear()
                    }

                    mAdapter.removeFooterView(emptyFooterView)
                    if (TextUtils.isEmpty(it.isStrangerBean.ids)) {

                        listConversation.addAll(it.list)
                        mAdapter.setList(listConversation)
                    } else {
                        val type = object : TypeToken<MutableList<String>>() {}.type
                        var list: MutableList<String> =
                            GsonUtils.fromJson(it.isStrangerBean.ids, type)

                        it.list.forEach { conversion ->
                            var find = list.find { conversion.conversation!!.userID == it }
                            if (find == null) {
                                listConversation.add(conversion)
                            } else {
                                listStranger.add(conversion)
                            }
                        }

                        mAdapter.setList(listConversation)

                        mAdapter.removeHeaderView(strangerView)
                        if (listStranger.isNotEmpty()) {
                            mAdapter.addHeaderView(strangerView)
                            setStrangerViewData()
                        }
                    }
                    sort()
                    if (mAdapter.data.isEmpty() && mList.isEmpty() && listStranger.isEmpty()) {
                        mAdapter.addFooterView(emptyFooterView)
                    }

                    Singleton.setRefreshResult(refreshLayout!!, 1)
                    Singleton.setRefreshResult(refreshLayout!!, 2)
                }
                is IsStrangerBeanState.Fail -> {

                }

            }
        }

        //删除会话结果
        messageViewModel.deleteConversation.observe(this) {
            when (it) {
                is DeleteConversionState.Success -> {
                    mAdapter.removeAt(it.position)
                    listConversation.removeAt(it.position)

                    LogUtils.e(">>> " + mAdapter.data.size)
                    LogUtils.e(">>> " + listConversation.size)

                    mAdapter.removeAllFooterView()
                    if (mAdapter.data.isEmpty() && listStranger.isEmpty() && mList.isEmpty()) {
                        mAdapter.addFooterView(emptyFooterView)
                    }

                }
                is DeleteConversionState.Fail -> {
                    ToastUtils.showShort("[${it.code}],错误：" + it.errorMsg)
                }
            }
        }

        /**
         * 会话消息更新
         */
        messageViewModel.conversationChaned.observe(this) {
            when (it) {
                is ConversionChangedState.Success -> {
                    //有会话消息被改变了，如会话置顶，消息变更等 例如未读计数发生变化、最后一条消息被更新等，此时可以重新对会话列表做排序。
                    it.conversationList.forEach { v2Server ->
                        mAdapter.data.forEach { local ->
                            if (v2Server.conversationID == local.conversation!!.conversationID) {
                                local.conversation = v2Server
                            }
                        }
                        listStranger.forEach { local ->
                            if (v2Server.conversationID == local.conversation!!.conversationID) {
                                local.conversation = v2Server
                            }
                        }
                    }
                    sort()
                }
            }
        }
        /**
         * 会话消息新增
         */
        messageViewModel.conversationAdd.observe(this) {
            when (it) {
                is ConversionAddState.Success -> {
                    //有会话消息被改变了，如会话置顶，消息变更等 例如未读计数发生变化、最后一条消息被更新等，此时可以重新对会话列表做排序。
                    refreshState = 1
                    handleMsgList(it.conversationList, true)
                }
            }
        }
        /**
         * 会话监听
         */
        messageViewModel.addConversationListener()

        val loginUserID = V2TIMManager.getInstance().loginUser

        LogUtils.e("im 已登陆>>> loginUserID = " + loginUserID)

        // TODO: 测试
        if (TextUtils.isEmpty(loginUserID)) {
            UserRepository.userInfo?.let { userInfo ->
                loginIm(userInfo.userId.toString())
            }
        }
        //        else
//            messageViewModel.getConversationList(100)
    }

    private fun handleMsgList(conversationList: List<V2TIMConversation>, isAdd: Boolean) {
        val list: MutableList<ConversionEntity> = arrayListOf()
        val listSystem: MutableList<ConversionEntity> = arrayListOf()
        val listId: MutableList<String> = arrayListOf()
        conversationList.forEach {
            if (it.userID == "administrator") {
                val item = ConversionEntity()
                item.headerImgUrl = it.faceUrl.decodePicUrls()
                item.conversation = it
                listSystem.add(item)
            } else {
                listId.add(it.userID)
                val item = ConversionEntity()
                item.headerImgUrl = it.faceUrl.decodePicUrls()
                item.conversation = it
                list.add(item)
            }
        }

        messageViewModel.getSysMessageList(1, null) //请求系统消息

        if (list.isNotEmpty())
            messageViewModel.getIsStranger(GsonUtils.toJson(listId), list, isAdd = isAdd)
    }

    private fun setStrangerViewData() {
        var message_icon =
            strangerView.findViewById<ImageView>(R.id.message_icon)
        var message_time =
            strangerView.findViewById<TextView>(R.id.message_time)
        var message_name =
            strangerView.findViewById<TextView>(R.id.message_name)
        var message_details =
            strangerView.findViewById<TextView>(R.id.message_details)
        var message_set_top_img =
            strangerView.findViewById<ImageView>(R.id.message_set_top_img)
        var message_mute_img =
            strangerView.findViewById<ImageView>(R.id.message_mute_img)
        var view_red_dot =
            strangerView.findViewById<View>(R.id.view_red_dot)
        var message_message_count =
            strangerView.findViewById<TextView>(R.id.message_message_count)

        message_message_count.visibility = View.GONE
        message_mute_img.visibility = View.GONE
        message_set_top_img.visibility = View.GONE
        message_name.text = "陌生人消息"
        message_time.text =
            TimeUtils.getFriendlyTimeSpanByNow(listStranger.first().conversation!!.lastMessage.timestamp * 1000)
        mAdapter.buildMsg(
            message_details,
            listStranger.first().conversation!!.lastMessage
        )
        message_icon.setImageResource(R.drawable.icon_msg_stranger)
        var unReadCount = 0
        listStranger.forEach {
            unReadCount += it.conversation!!.unreadCount
        }
        if (unReadCount == 0) {
            view_red_dot.visibility = View.GONE
        } else {
            view_red_dot.visibility = View.VISIBLE
        }
    }

    /**
     * 对会话列表进行重新排序
     */
    private fun sort() {

//        mAdapter.data.sortWith(kotlin.Comparator { thisCon, other ->
//            if (thisCon.conversation!!.isPinned && !other.conversation!!.isPinned) {
//                -1
//            } else if (!thisCon.conversation!!.isPinned && other.conversation!!.isPinned) {
//                1
//            } else {
//                val thisOrderKey: Long = thisCon.conversation!!.orderKey
//                val otherOrderKey: Long = other.conversation!!.orderKey
//                if (thisOrderKey > otherOrderKey) {
//                    -1
//                } else if (thisOrderKey == otherOrderKey) {
//                    0
//                } else {
//                    1
//                }
//            }
//        })

        if (listStranger.isNotEmpty()) {
            listStranger.sortWith(kotlin.Comparator { thisCon, other ->
                if (thisCon.conversation!!.isPinned != other.conversation!!.isPinned) {
                    other.conversation!!.isPinned.compareTo(thisCon.conversation!!.isPinned)
                } else {
                    other.conversation!!.orderKey.compareTo(thisCon.conversation!!.orderKey)
                }
            })

            setStrangerViewData()
        }

        mAdapter.data.sortWith(kotlin.Comparator { thisCon, other ->
            if (thisCon.conversation!!.isPinned != other.conversation!!.isPinned) {
                other.conversation!!.isPinned.compareTo(thisCon.conversation!!.isPinned)
            } else {
                other.conversation!!.orderKey.compareTo(thisCon.conversation!!.orderKey)
            }
        })

        mAdapter.notifyDataSetChanged()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            messageViewModel.getUnreadCount()
        }
    }


    /**
     * 加载更多系统消息 需要进入系统消息页面再处理  之后再处理
     * messageViewModel.mGetLaudMessageListBean
     */
    private fun getLoadMore(bean: GetMessageInnerPageListBean) {
        //用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入  表示有下一页
        if (bean == null) {
            messageViewModel.getLaudMessageList(10, 0)
        } else {
            when (bean.hasNext) {
                0 -> {//没有更多数据了
                    Singleton.setRefreshResult(refreshLayout!!, 5)
                    isNoMoreData = true
                }
                1 -> {//有更多数据
                    messageViewModel.getLaudMessageList(10, bean.lastTime)
                }
            }
        }
    }

    fun loginIm(userId: String) {

        LogUtils.e(">>>> userId = " + userId)
        UserInfo.getInstance().userId = userId
        val userSig: String = GenerateTestUserSig.genTestUserSig(userId)
        UserInfo.getInstance().setUserSig(userSig)
        V2TIMManager.getInstance()
            .login(userId, userSig, object : V2TIMCallback {
                override fun onSuccess() {
                    val loginUserID = V2TIMManager.getInstance().loginUser
                    LogUtils.e(">>>> onSuccess userId = " + userId)
                    messageViewModel.getConversationList(page)
                }

                override fun onError(p0: Int, p1: String?) {
                    LogUtils.e(">>> p0 = " + p0, "p1 = " + p1)
                }
            })
    }

}