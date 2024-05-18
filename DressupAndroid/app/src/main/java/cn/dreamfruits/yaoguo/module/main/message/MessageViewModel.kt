package cn.dreamfruits.yaoguo.module.main.message

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.login.BlackListServerState
import cn.dreamfruits.yaoguo.module.login.ConversionAddState
import cn.dreamfruits.yaoguo.module.login.ConversionChangedState
import cn.dreamfruits.yaoguo.module.login.DeleteConversionState
import cn.dreamfruits.yaoguo.module.main.home.state.*
import cn.dreamfruits.yaoguo.repository.bean.message.*
import cn.dreamfruits.yaoguo.repository.datasource.MessageRepository
import cn.dreamfruits.yaoguo.util.Tool
import com.blankj.utilcode.util.LogUtils
import com.tencent.imsdk.v2.*
import org.koin.core.component.inject


/**
 * @Author qiwangi
 * @Date 2023/5/23
 * @TIME 15:03
 */
class MessageViewModel : BaseViewModel() {

    private val messageRepository by inject<MessageRepository>()

    /**
     *获取点赞|收藏|引用 消息列表
     */
    var mGetLaudMessageListBean: GetMessageInnerPageListBean? = null
    private val _getLaudMessageListBeanState = MutableLiveData<GetLaudMessageListBeanState>()
    val getLaudMessageListBeanState: MutableLiveData<GetLaudMessageListBeanState> get() = _getLaudMessageListBeanState


    private val _conversationItem = MutableLiveData<ConversationListState>()
    val conversation: LiveData<ConversationListState> get() = _conversationItem

    private val _blackListServer = MutableLiveData<BlackListServerState>()
    val blackListServer: LiveData<BlackListServerState> get() = _blackListServer

    private val _deleteConversation = MutableLiveData<DeleteConversionState>()
    val deleteConversation: LiveData<DeleteConversionState> get() = _deleteConversation

    private val _conversationChaned = MutableLiveData<ConversionChangedState>()
    val conversationChaned: LiveData<ConversionChangedState> get() = _conversationChaned

    private val _conversationAdd = MutableLiveData<ConversionAddState>()
    val conversationAdd: LiveData<ConversionAddState> get() = _conversationAdd

    fun getLaudMessageList(size: Int, lastTime: Long?) {
        val disposable = messageRepository.getLaudMessageList(size, lastTime)
            .subscribe({
                mGetLaudMessageListBean = Tool().toUrlGetMessageInnerPageListBean(it)

                _getLaudMessageListBeanState.value = GetLaudMessageListBeanState.Success
                Log.e("zqr", "_getLaudMessageListBeanState请求成功：$it")
            }, {

                _getLaudMessageListBeanState.value = GetLaudMessageListBeanState.Fail(it.message)
                Log.e("zqr", "_getLaudMessageListBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     *获取系统消息列表
     */
    var mGetSysMessageListBean: GetMessageInnerPageListBean? = null
    private val _getSysMessageListBeanState = MutableLiveData<GetSysMessageListBeanState>()
    val getSysMessageListBeanState: MutableLiveData<GetSysMessageListBeanState> get() = _getSysMessageListBeanState
    fun getSysMessageList(size: Int, lastTime: Long?) {

        val disposable = messageRepository.getSysMessageList(size, lastTime)
            .subscribe({
                mGetSysMessageListBean = Tool().toUrlGetMessageInnerPageListBean(it)

                _getSysMessageListBeanState.value = GetSysMessageListBeanState.Success
                Log.e("zqr", "_getSysMessageListBeanState请求成功：$it")
            }, {

                _getSysMessageListBeanState.value = GetSysMessageListBeanState.Fail(it.message)
                Log.e("zqr", "_getSysMessageListBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     *获取关注消息列表
     */
    var mGetFollowMessageListBean: GetMessageInnerPageListBean? = null
    private val _getFollowMessageListBeanState = MutableLiveData<GetFollowMessageListBeanState>()
    val getFollowMessageListBeanState: MutableLiveData<GetFollowMessageListBeanState> get() = _getFollowMessageListBeanState
    fun getFollowMessageList(size: Int, lastTime: Long?) {
        val disposable = messageRepository.getFollowMessageList(size, lastTime)
            .subscribe({
                mGetFollowMessageListBean = Tool().toUrlGetMessageInnerPageListBean(it)

                _getFollowMessageListBeanState.value = GetFollowMessageListBeanState.Success
                Log.e("zqr", "_getFollowMessageListBeanState请求成功：$it")
            }, {

                _getFollowMessageListBeanState.value =
                    GetFollowMessageListBeanState.Fail(it.message)
                Log.e("zqr", "_getFollowMessageListBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     *获取评论消息列表
     */
    var mGetCommentMessageListBean: GetMessageInnerPageListBean? = null
    private val _getCommentMessageListBeanState = MutableLiveData<GetCommentMessageListBeanState>()
    val getCommentMessageListBeanState: MutableLiveData<GetCommentMessageListBeanState> get() = _getCommentMessageListBeanState
    fun getCommentMessageList(size: Int, lastTime: Long?) {
        val disposable = messageRepository.getCommentMessageList(size, lastTime)
            .subscribe({
                mGetCommentMessageListBean = Tool().toUrlGetMessageInnerPageListBean(it)

                _getCommentMessageListBeanState.value = GetCommentMessageListBeanState.Success
                Log.e("zqr", "_getCommentMessageListBeanState请求成功：$it")
            }, {

                _getCommentMessageListBeanState.value =
                    GetCommentMessageListBeanState.Fail(it.message)
                Log.e("zqr", "_getCommentMessageListBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 获取消息未读数（整合）
     */
    var mGetUnreadCountBean: GetUnreadCountBean? = null
    private val _getUnreadCountBeanState = MutableLiveData<GetUnreadCountBeanState>()
    val getUnreadCountBeanState: MutableLiveData<GetUnreadCountBeanState> get() = _getUnreadCountBeanState
    fun getUnreadCount() {
        val disposable = messageRepository.getUnreadCount()
            .subscribe({
                mGetUnreadCountBean = it

                _getUnreadCountBeanState.value = GetUnreadCountBeanState.Success
                Log.e("zqr", "_getUnreadCountBeanState请求成功：$it")
            }, {

                _getUnreadCountBeanState.value = GetUnreadCountBeanState.Fail(it.message)
                Log.e("zqr", "_getUnreadCountBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 检查可发消息数量
     */
    var mCheckMessageCountBean: CheckMessageCountBean? = null
    private val _checkMessageCountBeanState = MutableLiveData<CheckMessageCountBeanState>()
    val checkMessageCountBeanState: MutableLiveData<CheckMessageCountBeanState> get() = _checkMessageCountBeanState
    fun getCheckMessageCount(id: Long) {
        val disposable = messageRepository.getCheckMessageCount(id)
            .subscribe({
                mCheckMessageCountBean = it

                _checkMessageCountBeanState.value = CheckMessageCountBeanState.Success
                Log.e("zqr", "_checkMessageCountBeanState请求成功：$it")
            }, {

                _checkMessageCountBeanState.value = CheckMessageCountBeanState.Fail(it.message)
                Log.e("zqr", "_checkMessageCountBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 未读更新为已读
     */
    var mUpdateUnreadToReadBean: UpdateUnreadToReadBean? = null
    private val _updateUnreadToReadBeanState = MutableLiveData<UpdateUnreadToReadBeanState>()
    val updateUnreadToReadBeanState: MutableLiveData<UpdateUnreadToReadBeanState> get() = _updateUnreadToReadBeanState
    fun getUpdateUnreadToRead(type: Long) {
        val disposable = messageRepository.getUpdateUnreadToRead(type)
            .subscribe({
                mUpdateUnreadToReadBean = it

                _updateUnreadToReadBeanState.value = UpdateUnreadToReadBeanState.Success
                Log.e("zqr", "_updateUnreadToReadBeanState请求成功：$it")
            }, {

                _updateUnreadToReadBeanState.value = UpdateUnreadToReadBeanState.Fail(it.message)
                Log.e("zqr", "_updateUnreadToReadBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }


    /**
     * 是否陌生人
     */
    var mIsStrangerBean: IsStrangerBean? = null
    private val _isStrangerBeanState = MutableLiveData<IsStrangerBeanState>()
    val isStrangerBeanState: MutableLiveData<IsStrangerBeanState> get() = _isStrangerBeanState
    fun getIsStranger(ids: String, list: MutableList<ConversionEntity>, isAdd: Boolean = false) {
        val disposable = messageRepository.getIsStranger(ids)
            .subscribe({
                mIsStrangerBean = it
                _isStrangerBeanState.value = IsStrangerBeanState.Success(it, list, isAdd)
                Log.e("zqr", "_isStrangerBeanState请求成功：$it")
            }, {

                _isStrangerBeanState.value = IsStrangerBeanState.Fail(it.message)
                Log.e("zqr", "_isStrangerBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    var lastTime: Long? = null
    fun getBlackList(page: Int, size: Int) {
        val disposable = messageRepository.getBlackList(page, size, lastTime)
            .subscribe({
                lastTime = it.lastTime
                _blackListServer.value = BlackListServerState.Success(it)
                Log.e("zqr", "_isStrangerBeanState请求成功：$it")
            }, {
                _blackListServer.value = BlackListServerState.Fail(it.message!!)
                Log.e("zqr", "_isStrangerBeanState请求失败：$it")
            })
        addDisposable(disposable)
    }

    var nextSeq = 0L
    fun getConversationList(page: Int) {
        val filter = V2TIMConversationListFilter()
        filter.conversationType = V2TIMConversation.V2TIM_C2C //拉取单聊会话
// filter.setConversationType(V2TIMConversation.V2TIM_GROUP); //拉取群聊会话

        LogUtils.e(">>> " + nextSeq)
        V2TIMManager.getConversationManager().getConversationListByFilter(
            filter,
            nextSeq,
            page,
            object : V2TIMValueCallback<V2TIMConversationResult> {
                override fun onSuccess(v2TIMConversationResult: V2TIMConversationResult) {
                    nextSeq = v2TIMConversationResult.nextSeq
                    // 获取会话列表成功
                    _conversationItem.value = ConversationListState.Success(v2TIMConversationResult)
                }

                override fun onError(code: Int, desc: String) {
                    // 获取会话列表失败
//                    LogUtils.e(">>>> code = $code", "desc = $desc")
                    _conversationItem.value = ConversationListState.Fail(code, desc)
                }
            })

    }

    /**
     * 删除会话消息
     */
    fun deleteConversation(conversationID: String, position: Int) {
        V2TIMManager.getConversationManager()
            .deleteConversation(conversationID, object : V2TIMCallback {
                override fun onSuccess() {
                    // 删除会话成功
                    _deleteConversation.value = DeleteConversionState.Success(position)
                }

                override fun onError(code: Int, desc: String) {
                    // 删除会话失败
                    _deleteConversation.value = DeleteConversionState.Fail(code, desc)
                }
            })

    }

    /**
     * 置顶会话
     * isPinned 参数为 true，表示置顶会话，否则，表示取消置顶。
     */
    fun pinConversation(conversationID: String, isPinned: Boolean, position: Int) {
        V2TIMManager.getConversationManager()
            .pinConversation(conversationID, isPinned, object : V2TIMCallback {
                override fun onSuccess() {
                    Log.i("imsdk", "success")
                }

                override fun onError(code: Int, desc: String) {
                    Log.i("imsdk", "failure, code:$code, desc:$desc")
                }
            })
    }


    fun addConversationListener() {

        val conversationListener: V2TIMConversationListener = object : V2TIMConversationListener() {
            //            同步服务器会话开始
            override fun onSyncServerStart() {
                Log.i("imsdk", "onSyncServerStart")
            }

            //            同步服务器会话完成
            override fun onSyncServerFinish() {
                Log.i("imsdk", "onSyncServerFinish")
            }

            //            同步服务器会话失败
            override fun onSyncServerFailed() {
                Log.i("imsdk", "onSyncServerFailed")
            }

            //            有会话新增
            override fun onNewConversation(conversationList: List<V2TIMConversation>) {
                Log.i("imsdk", "onNewConversation")
                _conversationAdd.value = ConversionAddState.Success(conversationList)
            }

            //            有会话更新
            override fun onConversationChanged(conversationList: List<V2TIMConversation>) {
                Log.i("imsdk", "onConversationChanged")
                _conversationChaned.value = ConversionChangedState.Success(conversationList)
            }

            //            有会话被删除
            override fun onConversationDeleted(conversationIDList: List<String>) {
                Log.i("imsdk", "onConversationDeleted")
            }

            //            会话未读总数变更通知
            override fun onTotalUnreadMessageCountChanged(totalUnreadCount: Long) {
                Log.i("imsdk", "onTotalUnreadMessageCountChanged")
            }

            //            过滤条件下未读总数变更通知
            override fun onUnreadMessageCountChangedByFilter(
                filter: V2TIMConversationListFilter,
                totalUnreadCount: Long,
            ) {
                Log.i("imsdk", "onUnreadMessageCountChangedByFilter")
            }
        }

        V2TIMManager.getConversationManager().addConversationListener(conversationListener);
    }
}