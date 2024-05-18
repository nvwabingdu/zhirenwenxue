package cn.dreamfruits.yaoguo.module.main.message.modle

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.login.*
import cn.dreamfruits.yaoguo.module.main.message.adapter.ChartConversationAdapter
import cn.dreamfruits.yaoguo.module.main.message.bean.SharePostEntity
import cn.dreamfruits.yaoguo.module.main.message.bean.ShareSingleEntity
import cn.dreamfruits.yaoguo.module.main.message.bean.ShareUserEntity
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.QuoteBean
import cn.dreamfruits.yaoguo.module.main.message.chart.bean.QuoteMessageBean
import cn.dreamfruits.yaoguo.module.main.message.constants.TIMCommonConstants
import cn.dreamfruits.yaoguo.repository.bean.message.ConversionEntity
import cn.dreamfruits.yaoguo.repository.datasource.MessageRepository
import cn.dreamfruits.yaoguo.util.decodePicUrls
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.tencent.imsdk.v2.*
import org.json.JSONObject
import org.koin.core.component.inject


/**
 * @author Lee
 * @createTime 2023-06-29 20 GMT+8
 * @desc :
 */
class ChartViewModel : BaseViewModel() {

    private val _textMsgState = MutableLiveData<TextMsgState>()
    val textMsgState: LiveData<TextMsgState> get() = _textMsgState

    private val _msgRevokedState = MutableLiveData<MsgRevokedState>()
    val msgRevokedState: LiveData<MsgRevokedState> get() = _msgRevokedState

    private val _userInfoState = MutableLiveData<ImUserInfoState>()
    val userInfoState: LiveData<ImUserInfoState> get() = _userInfoState

    private val _userMsgCountState = MutableLiveData<UserMsgCountState>()
    val userMsgCountState: LiveData<UserMsgCountState> get() = _userMsgCountState

    private val messageRepository by inject<MessageRepository>()


    /**
     * 发送单聊文本消息
     */
    fun sendMsg(msg: V2TIMMessage, userId: String) {
        //消息已读回执
        msg.isNeedReadReceipt = false
        V2TIMManager.getMessageManager().sendMessage(
            msg,
            userId,
            null,
            V2TIMMessage.V2TIM_PRIORITY_NORMAL,
            false,
            null,
            object :
                V2TIMSendCallback<V2TIMMessage> {
                override fun onSuccess(p0: V2TIMMessage) {
                    _textMsgState.value = TextMsgState.Success(p0)
                }

                override fun onError(code: Int, desc: String) {
                    _textMsgState.value = TextMsgState.Fail(code, desc, msg)
                    LogUtils.e(">>> code = " + code, " desc = " + desc)
                }

                override fun onProgress(p0: Int) {

                }

            })

    }

    /**
     * 消息接收监听
     */
    fun onReciveMsg() {
        V2TIMManager.getMessageManager()
            .addAdvancedMsgListener(object : V2TIMAdvancedMsgListener() {
                override fun onRecvNewMessage(msg: V2TIMMessage) {
                    super.onRecvNewMessage(msg)
                    _textMsgState.value = TextMsgState.Success(msg)
                }

                /**
                 * 对方撤回了一条消息
                 */
                override fun onRecvMessageRevoked(msgID: String) {
                    super.onRecvMessageRevoked(msgID)
                    _msgRevokedState.value = MsgRevokedState.Success(msgID)
                }
            })
    }

    /**
     * 获取Im用户信息
     */
    fun getUserInfo(userId: String) {

        var listId: MutableList<String> = arrayListOf()
        listId.add(userId)

        V2TIMManager.getInstance()
            .getUsersInfo(listId, object : V2TIMValueCallback<List<V2TIMUserFullInfo>> {
                override fun onSuccess(p0: List<V2TIMUserFullInfo>?) {
                    if (!p0.isNullOrEmpty())
                        _userInfoState.value = ImUserInfoState.Success(p0.first())
                    else {
                        _userInfoState.value = ImUserInfoState.Fail(-1, "未找到用户")
                    }
                }

                override fun onError(p0: Int, p1: String) {
                    _userInfoState.value = ImUserInfoState.Fail(p0, p1)
                }
            })
    }

    fun insertC2CMessageToLocalStorage(
        message: String,
        receiver_userID: String,
        sender_userID: String,
    ) {
        // 创建一条消息
        val msg = V2TIMManager.getMessageManager().createTextMessage(message)
        // 插入到本地存储
        V2TIMManager.getMessageManager().insertC2CMessageToLocalStorage(
            msg,
            receiver_userID,
            sender_userID,
            object : V2TIMValueCallback<V2TIMMessage> {
                override fun onSuccess(message: V2TIMMessage) {
                    _textMsgState.value = TextMsgState.Success(message)
                    // 插入 C2C 消息成功
                }

                override fun onError(code: Int, desc: String) {
                    // 插入 C2C 消息失败
                }
            })
    }

    fun checkMessageCount(
        userId: String,
    ) {
        val disposable = messageRepository.checkMessageCount(userId)
            .subscribe({
                _userMsgCountState.value = UserMsgCountState.Success(it)
            }, {
                _userMsgCountState.value = UserMsgCountState.Fail(-1, it.message!!)
            })
        addDisposable(disposable)
    }

    /**
     * 消息处理
     */
    fun handleMsg(msg: V2TIMMessage): ConversionEntity {


        var conversation = ConversionEntity()

        if (msg.isSelf) {
            when (msg.elemType) {
                V2TIMMessage.V2TIM_ELEM_TYPE_TEXT -> {
                    buildConversationType(
                        msg,
                        conversation,
                        TIMCommonConstants.MESSAGE_TYPE_RIGHT_TEXT
                    )
                }
                V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO -> {
                    buildConversationType(
                        msg,
                        conversation,
                        TIMCommonConstants.MESSAGE_TYPE_RIGHT_VIDEO
                    )
                }
                V2TIMMessage.V2TIM_ELEM_TYPE_SOUND -> {
                    buildConversationType(
                        msg,
                        conversation,
                        TIMCommonConstants.MESSAGE_TYPE_RIGHT_SOUND
                    )
                }
                V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM -> {

                    var jsonObject: JSONObject = JSONObject(String(msg.customElem.data))
                    if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY)) {
                        buildConversationType(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_POST
                        )
                        parseUserData(msg, conversation, TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY)
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY)) {
                        buildConversationType(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_USER
                        )
                        parseUserData(msg, conversation, TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY)
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY)) {
                        buildConversationType(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_SINGLE
                        )
                        parseUserData(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY
                        )
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY)) {
                        buildConversationType(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_POST
                        )
                        parseUserData(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY
                        )
                    }

                }
                else -> {
                    conversation.itemType = TIMCommonConstants.MESSAGE_TYPE_RIGHT_IMAGE
                }
            }
        } else {
            when (msg.elemType) {
                V2TIMMessage.V2TIM_ELEM_TYPE_TEXT -> {
                    buildConversationType(
                        msg,
                        conversation,
                        TIMCommonConstants.MESSAGE_TYPE_LEFT_TEXT
                    )
                }
                V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO -> {
                    buildConversationType(
                        msg,
                        conversation,
                        TIMCommonConstants.MESSAGE_TYPE_LEFT_VIDEO
                    )
                }
                V2TIMMessage.V2TIM_ELEM_TYPE_SOUND -> {
                    buildConversationType(
                        msg,
                        conversation,
                        TIMCommonConstants.MESSAGE_TYPE_LEFT_SOUND
                    )
                }
                V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM -> {

                    var jsonObject: JSONObject = JSONObject(String(msg.customElem.data))
                    if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY)) {
                        buildConversationType(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_POST
                        )
                        parseUserData(msg, conversation, TIMCommonConstants.MESSAGE_CUSTOM_POST_KEY)
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY)) {
                        buildConversationType(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_USER
                        )
                        parseUserData(msg, conversation, TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY)
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY)) {
                        buildConversationType(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_TYPE_RIGHT_CUSTOM_SINGLE
                        )
                        parseUserData(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY
                        )
                    } else if (jsonObject.has(TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY)) {
                        buildConversationType(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_TYPE_LEFT_CUSTOM_POST
                        )
                        parseUserData(
                            msg,
                            conversation,
                            TIMCommonConstants.MESSAGE_CUSTOM_TOPIC_KEY
                        )
                    }
                }
                else -> {
                    conversation.itemType = TIMCommonConstants.MESSAGE_TYPE_LEFT_IMAGE
                }
            }
        }

        conversation.headerImgUrl = msg.faceUrl.decodePicUrls()
        conversation.message = msg

        return conversation
    }

    private fun parseUserData(msg: V2TIMMessage, conversation: ConversionEntity, keyType: String) {
        val gson = Gson()
        val postHashMap: HashMap<*, *> = gson.fromJson<HashMap<*, *>>(
            String(msg.customElem.data),
            HashMap::class.java
        )

        if (keyType == TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY) {
            val userContentObj =
                postHashMap[TIMCommonConstants.MESSAGE_CUSTOM_USER_KEY]
            var userDetails: ShareUserEntity
            if (userContentObj is Map<*, *>) {
                userDetails = gson.fromJson(
                    gson.toJson(userContentObj),
                    ShareUserEntity::class.java
                )
                userDetails.avatarUrl = userDetails.avatarUrl.decodePicUrls()
                userDetails.backgroundUrl = userDetails.backgroundUrl.decodePicUrls()

                conversation.shareUserEntity = userDetails
            }
        } else if (keyType == TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY) {
            val userContentObj =
                postHashMap[TIMCommonConstants.MESSAGE_CUSTOM_SINGLE_KEY]
            var userDetails: ShareSingleEntity
            if (userContentObj is Map<*, *>) {
                userDetails = gson.fromJson(
                    gson.toJson(userContentObj),
                    ShareSingleEntity::class.java
                )
                userDetails.coverUrl = userDetails.coverUrl.decodePicUrls()
                conversation.shareSingleEntity = userDetails
            }
        } else {
            val userContentObj =
                postHashMap[keyType]
            var userDetails: SharePostEntity
            if (userContentObj is Map<*, *>) {
                userDetails = gson.fromJson(
                    gson.toJson(userContentObj),
                    SharePostEntity::class.java
                )
                userDetails.avatarUrl = userDetails.avatarUrl.decodePicUrls()
                userDetails.picUrls.forEach { it.url = it.url.decodePicUrls() }
                userDetails.videoUrls.forEach { it.url = it.url.decodePicUrls() }
                conversation.sharePostEntity = userDetails
            }
        }
    }

    private fun buildConversationType(
        msg: V2TIMMessage,
        conversation: ConversionEntity,
        itemType: Int,
    ) {
        when (msg.status) {
            //导入到本地的消息,插入的一条本地消息
            V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_IMPORTED -> {
                conversation.itemType =
                    TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_IMPORTED
                //消息被删除
            }
            V2TIMMessage.V2TIM_MSG_STATUS_HAS_DELETED -> {
                conversation.itemType =
                    TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_DELETE
                //消息被撤回
            }
            V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_REVOKED -> {
                conversation.itemType =
                    TIMCommonConstants.MESSAGE_TYPE_CENTER_HINT_REVOKED
                //正常消息
            }
            else -> {
                conversation.itemType = itemType
            }
        }
    }


    /**
     * isDelete = false 撤回一条消息
     * isDelete = true  删除一条消息
     */
    fun deleteOrRollBackMsg(
        msg: V2TIMMessage,
        isDelete: Boolean = true,
        mAdapter: ChartConversationAdapter,
    ) {

        var listFind: MutableList<ConversionEntity> = arrayListOf()

        mAdapter.data.forEach { conver ->
            if (!TextUtils.isEmpty(conver.message!!.cloudCustomData)) {
                val quoteMsg = mAdapter.getQuoteMsg(
                    JSONObject(conver.message!!.cloudCustomData),
                    conver.message!!.cloudCustomData
                )
                if (quoteMsg != null) {
                    if (quoteMsg.message != null)
                        if (msg.msgID == quoteMsg.message!!.msgID) {
                            listFind.add(conver)
                        }
                }
            }
        }

        if (!listFind.isEmpty()) {
            listFind.forEach { findQuote ->
                val quoteMsg = mAdapter.getQuoteMsg(
                    JSONObject(findQuote.message!!.cloudCustomData),
                    findQuote.message!!.cloudCustomData
                )
                LogUtils.e(">>> quoteMsg = " + quoteMsg.toString())

                if (quoteMsg != null) {
                    if (isDelete) {
                        quoteMsg.message!!.messageStatus = V2TIMMessage.V2TIM_MSG_STATUS_HAS_DELETED
                    } else {
                        quoteMsg.message!!.messageStatus =
                            V2TIMMessage.V2TIM_MSG_STATUS_LOCAL_REVOKED
                    }
                    var originMessage = findQuote.message

                    val cloudData: MutableMap<String, QuoteMessageBean> = HashMap()
                    val gson = Gson()
                    cloudData[TIMCommonConstants.MESSAGE_REPLY_KEY] = quoteMsg

                    LogUtils.e(">>>> quoteMsg = " + quoteMsg.toString())

                    originMessage!!.cloudCustomData = gson.toJson(cloudData)

                    LogUtils.e(">>>> quoteMsg = " + quoteMsg.toString())

                    LogUtils.e(">>>> originMessage = " + originMessage.toString())

                    modifyMessage(originMessage, mAdapter)
                }
            }
        }
    }

    private fun modifyMessage(
        originMessage: V2TIMMessage?,
        mAdapter: ChartConversationAdapter? = null,
    ) {
        V2TIMManager.getMessageManager().modifyMessage(
            originMessage
        ) { code, desc, message ->
            // 修改消息完成，message 为修改之后的消息对象
            LogUtils.e(
                ">> 消息修改完成 code = " + code,
                "desc = " + desc,
                "message = " + message.toString()
            )
            mAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * 给多个用户发送自定义消息
     */
    fun sendCustomMsg(byteArray: ByteArray, userIdList: List<String>, content: String) {
        userIdList.forEach {
            var message = V2TIMManager.getMessageManager().createCustomMessage(byteArray)
            sendMsg(message, it)
            if (!TextUtils.isEmpty(content)) {
                var message = V2TIMManager.getMessageManager().createTextMessage(content)
                sendMsg(message, it)
            }
        }
    }


}