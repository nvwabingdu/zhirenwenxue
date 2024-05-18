package cn.dreamfruits.yaoguo.module.main.message

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.adapter.base.BaseQuickAdapter
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemChildClickListener
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemClickListener
import cn.dreamfruits.yaoguo.adapter.base.listener.OnItemLongClickListener
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.module.login.ImUserInfoState
import cn.dreamfruits.yaoguo.module.login.MsgRevokedState
import cn.dreamfruits.yaoguo.module.login.TextMsgState
import cn.dreamfruits.yaoguo.module.main.home.labeldetails.LabelDetailsActivity
import cn.dreamfruits.yaoguo.module.main.home.postdetails.PostDetailsActivity
import cn.dreamfruits.yaoguo.module.main.home.vlistvideo.ListVideoActivity
import cn.dreamfruits.yaoguo.module.main.message.adapter.ChartConversationAdapter
import cn.dreamfruits.yaoguo.module.main.message.chart.manage.FaceManager
import cn.dreamfruits.yaoguo.module.main.message.chart.utils.MediaPlayerUtils
import cn.dreamfruits.yaoguo.module.main.message.chart.view.InputView
import cn.dreamfruits.yaoguo.module.main.message.chart.view.InputView.ChatInputHandler.*
import cn.dreamfruits.yaoguo.module.main.message.chart.view.InputView.MessageHandler
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.module.main.message.modle.ChartViewModel
import cn.dreamfruits.yaoguo.module.main.message.pop.ChartMsgDealPop
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.SingleDetailsActivity
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMMessage.*
import com.tencent.imsdk.v2.V2TIMValueCallback
import org.json.JSONObject


/**
 * @author Lee
 * @createTime 2023-06-27 20 GMT+8
 * @desc : 私聊
 */
class ChartActivity : BaseActivity() {

    lateinit var mInputView: InputView
    lateinit var mTvTitle: TextView
    lateinit var mIvSpeakStatus: ImageView
    lateinit var mIvSet: ImageView
    lateinit var mRvContent: RecyclerView
    lateinit var mRefreshLayout: SmartRefreshLayout

    private val chartViewModel by viewModels<ChartViewModel>()

    lateinit var mAdapter: ChartConversationAdapter
    lateinit var mLayoutManager: LinearLayoutManager
//    lateinit var mV2TIMConversation: V2TIMConversation

    var userId = ""

    companion object {
        @JvmStatic
        fun start(context: Context, userId: String) {
            val starter = Intent(context, ChartActivity::class.java)
                .putExtra("userId", userId)
            context.startActivity(starter)
        }
    }

    override fun layoutResId(): Int = R.layout.activity_chart

    override fun initView() {

        // 加载默认 emoji 小表情
        FaceManager.loadEmojis()

        mRvContent = findViewById(R.id.rv_content)

        mInputView = findViewById(R.id.input_view)

        mTvTitle = findViewById(R.id.tv_title)

        mIvSpeakStatus = findViewById(R.id.iv_speak_status)

        mRefreshLayout = findViewById(R.id.refresh_layout)

        mIvSet = findViewById(R.id.iv_set)

        mIvSet.setOnClickListener {
            ChartSetActiivity.start(
                mContext,
                userId,
                ""
            )
        }

        findViewById<ImageView>(R.id.iv_back)
            .setOnClickListener { onBackPressed() }

        mInputView.setChatInputHandler(object : InputView.ChatInputHandler {
            override fun onInputAreaClick() {
            }

            override fun onRecordStatusChanged(status: Int) {
                when (status) {
                    RECORD_START -> {
                        mIvSpeakStatus.visibility = View.VISIBLE
                        mIvSpeakStatus.setImageResource(R.drawable.chart_is_speaking)
                    }
                    RECORD_STOP -> {
                        mIvSpeakStatus.visibility = View.GONE
                    }
                    RECORD_CANCEL -> {
                        mIvSpeakStatus.setImageResource(R.drawable.chart_cancel_speak)
                    }
                    RECORD_TOO_SHORT -> {
                        mIvSpeakStatus.visibility = View.VISIBLE
                        mIvSpeakStatus.setImageResource(R.drawable.chart_speak_too_short)
                        mIvSpeakStatus.postDelayed({
                            mIvSpeakStatus.visibility = View.GONE
                        }, 1000L)
                    }
                    RECORD_FAILED -> {
                        mIvSpeakStatus.visibility = View.GONE
                        ToastUtils.showShort("录制失败")
                    }
                }
            }


            override fun onUserTyping(status: Boolean, time: Long) {

            }
        })


        mInputView.setMessageHandler(object : MessageHandler {
            override fun sendMessage(msg: V2TIMMessage) {
                chartViewModel.sendMsg(msg, userId)
                mAdapter.addData(0, chartViewModel.handleMsg(msg))
                mRvContent.smoothScrollToPosition(0)
            }

//            override fun sendImageMsg(imagePath: String) {
//                chartViewModel.insertC2CMessageToLocalStorage(
//                    "对方关注或回复你之前，24小时内最多只能发一条消息哟", userId,
//                    V2TIMManager.getInstance().loginUser
//                )
//            }

            override fun scrollToEnd() {
                mRvContent.smoothScrollToPosition(0)
            }
        })

        chartViewModel.textMsgState.observe(this) { ser ->
            when (ser) {
                is TextMsgState.Success -> {

                    val find =
                        mAdapter.data.indexOfFirst { it.message!!.msgID == ser.message.msgID }
                    if (find != -1) {
                        mAdapter.data[find].message = ser.message
                        mAdapter.notifyItemChanged(find)
                    } else {
                        mAdapter.addData(0, chartViewModel.handleMsg(ser.message))
                        mRvContent.smoothScrollToPosition(0)
                    }

                }
                is TextMsgState.Fail -> {
                    if (ser.errorMsg == "invalid receiver userid") {
                        ToastUtils.showShort("用户不存在")
                    }
                    LogUtils.e(">>>> originMsg = ", ser.originMsg.toString())
                    /**
                     * 消息发送失败
                     */
//                    mAdapter.addData(0, chartViewModel.handleMsg(it.originMsg))
//                    mRvContent.smoothScrollToPosition(0)

                    val find =
                        mAdapter.data.indexOfFirst { it.message!!.msgID == ser.originMsg.msgID }
                    if (find != -1) {
                        mAdapter.data[find].message = ser.originMsg
                        mAdapter.notifyItemChanged(find)
                    } else {
                        mAdapter.addData(0, chartViewModel.handleMsg(ser.originMsg))
                        mRvContent.smoothScrollToPosition(0)
                    }
                }
            }
        }

        mRefreshLayout.setEnableLoadMore(false)

        mRefreshLayout.setOnRefreshListener {
            getHistoryMsg()
        }

        /**
         * 监听消息接收
         */
        chartViewModel.onReciveMsg()
    }


    override fun initData() {
        userId = intent.getStringExtra("userId")!!

        mAdapter = ChartConversationAdapter(this)
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.reverseLayout = true
        mRvContent.layoutManager = mLayoutManager
        mRvContent.setHasFixedSize(false)
        mLayoutManager.stackFromEnd = true

        mRvContent.adapter = mAdapter

        mAdapter.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int,
            ): Boolean {
                KeyboardUtils.hideSoftInput(this@ChartActivity)
                when (mAdapter.data[position].itemType) {

                    TIMCommonConstants.MESSAGE_TYPE_LEFT_VIDEO -> {
                        showDealPop(view.findViewById(R.id.iv_left_cover), position)
                    }
                    TIMCommonConstants.MESSAGE_TYPE_RIGHT_VIDEO -> {
                        showDealPop(view.findViewById(R.id.iv_right_cover), position)
                    }
                    TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_POST,
                    TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_USER,
                    TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_POST,
                    TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_USER,
                    -> {
                        showDealPop(view.findViewById(R.id.cl_child_content), position)
                    }
                    TIMCommonConstants.MESSAGE_TYPE_LEFT_TEXT,
                    TIMCommonConstants.MESSAGE_TYPE_RIGHT_TEXT,
                    TIMCommonConstants.MESSAGE_TYPE_LEFT_SOUND,
                    TIMCommonConstants.MESSAGE_TYPE_RIGHT_SOUND,
                    -> {
                        showDealPop(view.findViewById(R.id.tv_content), position)
                    }
                }
                return false
            }
        })

        mAdapter.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int,
            ) {
                when (view.id) {
                    R.id.iv_fail -> {
                        chartViewModel.sendMsg(mAdapter.data[position].message!!, userId)
                        mAdapter.removeAt(position)
                    }
                    R.id.tv_content -> {

                        if (MediaPlayerUtils.getInstance().isPlaying()) {

                        } else
                            mAdapter.data[position].message!!.soundElem.getUrl(object :
                                V2TIMValueCallback<String> {
                                override fun onSuccess(p0: String?) {
                                    MediaPlayerUtils.getInstance().playMedia(this@ChartActivity,
                                        p0!!,
                                        {

                                        },
                                        {

                                        }
                                    ) { p0, p1, p2 -> true }
                                }

                                override fun onError(p0: Int, p1: String?) {
                                }
                            })

                    }
                }
            }
        })

        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (mAdapter.data[position].message!!.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {

                } else if (mAdapter.data[position].message!!.elemType == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
                    var jsonObject: JSONObject =
                        JSONObject(String(mAdapter.data[position].message!!.customElem.data))
                    if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY)) {
                        val jsonObject1 =
                            jsonObject.getJSONObject(TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY)

                        var id = jsonObject1.getString("id")
                        var type = jsonObject1.optInt("type")

                        var coverHeight = jsonObject1.optInt("coverHeight")
                        var coverWidth = jsonObject1.optInt("coverWidth")

                        if (type == 0) {
                            val intent =
                                Intent(this@ChartActivity, PostDetailsActivity::class.java)//帖子详情页
                            intent.putExtra("feedId", id.toLong())
                            intent.putExtra("coverHeight", coverHeight)
                            intent.putExtra("coverWidth", coverWidth)
                            ActivityUtils.startActivity(intent)
                        } else {
                            val intent =
                                Intent(this@ChartActivity, ListVideoActivity::class.java)//视频列表页面
                            intent.putExtra("feedId", id.toLong())
//                            intent.putExtra("position", position)//位置
                            ActivityUtils.startActivity(intent)
                        }

                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY)) {
                        val jsonObject1 =
                            jsonObject.getJSONObject(TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY)
                        var id = jsonObject1.getString("userId")
                        Singleton.startOtherUserCenterActivity(mContext, id.toLong())

                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY)) {
                        val jsonObject1 =
                            jsonObject.getJSONObject(TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY)
                        var id = jsonObject1.getString("id")

                        val intent =
                            Intent(this@ChartActivity, SingleDetailsActivity::class.java)//帖子详情页
                        intent.putExtra("id", id.toLong())
                        startActivity(intent)

                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY)) {
                        val jsonObject1 =
                            jsonObject.getJSONObject(TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY)
                        var id = jsonObject1.getString("id")
                        val intent = Intent(mContext, LabelDetailsActivity::class.java)
                        intent.putExtra("id", id) //string的id
                        mContext.startActivity(intent)
//                        536337863683360
                    }
                }
            }
        })




        chartViewModel.getUserInfo(userId)
        chartViewModel.userInfoState.observe(this)
        {
            when (it) {
                is ImUserInfoState.Success -> {
                    mTvTitle.text = it.message.nickName
                }
                is ImUserInfoState.Fail -> {

                }
            }
        }

        /**
         * 对方撤回了消息
         */
        chartViewModel.msgRevokedState.observe(this) { msgId ->
            when (msgId) {
                is MsgRevokedState.Success -> {
                    val find = mAdapter.data.find {
                        it.message!!.msgID == msgId.msgId
                    }

                    if (find != null) {
                        find.itemType = TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_REVOKED
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
        getHistoryMsg()
    }


    override fun onCreateBefore() {

    }

    override fun initStatus() {

    }


    private fun showDealPop(attchView: View, position: Int) {
        var pop: ChartMsgDealPop = ChartMsgDealPop(this, mAdapter.data[position], object :
            ChartMsgDealPop.OnChartPopClick {
            override fun onDelete(mConversation: ConversionEntity) {
                // selectedMessageList 为用户选中待删除的消息列表
                var selectedMessageList: MutableList<V2TIMMessage> = arrayListOf()
                selectedMessageList.add(mAdapter.data[position].message!!)
                deleteMsg(selectedMessageList)

                /**
                 * 消息删除后，如果正在引用当前消息，则退出消息引用
                 */
                if (mInputView.replyPreviewBean != null) {
                    if (mInputView.replyPreviewBean.message!!.msgID == mAdapter.data[position].message!!.msgID) {
                        mInputView.exitQuote()
                    }
                }
            }

            override fun onQuote(mConversation: ConversionEntity) {
                mInputView.quoteMsg(mConversation)
            }

            override fun onTranspond(mConversation: ConversionEntity) {
            }

            override fun onRollBack(mConversation: ConversionEntity) {

                LogUtils.e(">>> " + mConversation.message!!.toString())

                V2TIMManager.getMessageManager()
                    .revokeMessage(mConversation.message!!, object : V2TIMCallback {
                        override fun onError(code: Int, desc: String) {
                            // 撤回消息失败
                            LogUtils.e(">>> code = " + code, "desc = " + desc)
                        }

                        override fun onSuccess() {
                            // 撤回消息成功
                            mAdapter.data[position].itemType =
                                TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_REVOKED
                            chartViewModel.deleteOrRollBackMsg(
                                mConversation.message!!,
                                isDelete = false,
                                mAdapter
                            )
                            mAdapter.notifyItemChanged(position)

                        }
                    })

            }
        })

        XPopup.Builder(this)
            .isTouchThrough(true)
            .isDestroyOnDismiss(true)
            .atView(attchView)
            .isCenterHorizontal(false)
            .hasShadowBg(false)
            .asCustom(pop)
            .show()

    }

    private fun deleteMsg(selectedMessageList: MutableList<V2TIMMessage>) {
        V2TIMManager.getMessageManager()
            .deleteMessages(selectedMessageList, object : V2TIMCallback {
                override fun onSuccess() {
                    // 删除云端消息成功
                    selectedMessageList.forEach { msg ->
                        val find = mAdapter.data.find { msg.msgID == it.message!!.msgID }
                        if (find != null) {
                            find.itemType = TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_DELETE
                        }
                        chartViewModel.deleteOrRollBackMsg(msg, isDelete = true, mAdapter)
                    }
                    mAdapter.notifyDataSetChanged()
                }

                override fun onError(code: Int, desc: String) {
                    // 删除云端消息失败
                }
            })
    }


    var lastTIMMsg: V2TIMMessage? = null

    private fun getHistoryMsg() {

        V2TIMManager.getMessageManager()
            .getC2CHistoryMessageList(userId, 20, lastTIMMsg, object :
                V2TIMValueCallback<List<V2TIMMessage>> {
                override fun onSuccess(p0: List<V2TIMMessage>) {


                    if (p0.isNullOrEmpty()) {
                        mRefreshLayout.finishRefreshWithNoMoreData()
                        mRefreshLayout.setEnableRefresh(false)
                        return
                    }

                    var list: MutableList<ConversionEntity> = arrayListOf()
                    p0.forEach { msg ->
                        list.add(chartViewModel.handleMsg(msg))
                    }
                    mAdapter.addData(list)
                    if (lastTIMMsg == null) {
                        mLayoutManager.scrollToPositionWithOffset(0, 0)
                    }
                    mRefreshLayout.finishRefresh()

                    lastTIMMsg = p0.last()
                }

                override fun onError(p0: Int, p1: String?) {
                    LogUtils.e(">>> p0 = " + p0, "p1 = " + p1)
                }
            })
    }


    private fun setMsgRead(message: V2TIMMessage) {
        if (!message.isSelf() && message.isNeedReadReceipt) {
            val messageList: MutableList<V2TIMMessage> = ArrayList()
            messageList.add(message)
            V2TIMManager.getMessageManager()
                .sendMessageReadReceipts(messageList, object : V2TIMCallback {
                    override fun onSuccess() {
                        // 发送消息已读回执成功
                    }

                    override fun onError(code: Int, desc: String) {
                        // 发送消息已读回执失败
                    }
                })
        }

    }

    //                val V2TIM_MSG_STATUS_SENDING = 1消息发送中
//                val V2TIM_MSG_STATUS_SEND_SUCC = 2消息发送成功
//                val V2TIM_MSG_STATUS_SEND_FAIL = 3消息发送失败
//                val V2TIM_MSG_STATUS_HAS_DELETED = 4消息被删除
//                val V2TIM_MSG_STATUS_LOCAL_IMPORTED = 5导入到本地的消息
//                val V2TIM_MSG_STATUS_LOCAL_REVOKED = 6被撤销的消息

}
