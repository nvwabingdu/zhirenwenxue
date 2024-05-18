package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.message.bean.UserMsgCountEntity
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.MessageApi
import cn.dreamfruits.yaoguo.repository.bean.message.*
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/5/23
 * @TIME 14:50
 */
class MessageRemoteDataSource : BaseRemoteDataSource() {

    private val messageApi: MessageApi = retrofitService(MessageApi::class.java)

    /**
     *获取点赞|收藏|引用 消息列表
     */
    fun getLaudMessageList(
        size: Int,
        lastTime: Long?,
    ): Observable<GetMessageInnerPageListBean> {
        return messageApi.getLaudMessageList(size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     *获取系统消息列表
     */
    fun getSysMessageList(
        size: Int,
        lastTime: Long?,
    ): Observable<GetMessageInnerPageListBean> {
        return messageApi.getSysMessageList(size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     *获取关注消息列表
     */
    fun getFollowMessageList(
        size: Int,
        lastTime: Long?,
    ): Observable<GetMessageInnerPageListBean> {
        return messageApi.getFollowMessageList(size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     *获取评论消息列表
     */
    fun getCommentMessageList(
        size: Int,
        lastTime: Long?,
    ): Observable<GetMessageInnerPageListBean> {
        return messageApi.getCommentMessageList(size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 获取消息未读数（整合）
     */
    fun getUnreadCount(
    ): Observable<GetUnreadCountBean> {
        return messageApi.getUnreadCount().compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 检查可发消息数量
     */
    fun getCheckMessageCount(
        id: Long,
    ): Observable<CheckMessageCountBean> {
        return messageApi.getCheckMessageCount(id)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 未读更新为已读
     */
    fun getUpdateUnreadToRead(
        type: Long,
    ): Observable<UpdateUnreadToReadBean> {
        return messageApi.getUpdateUnreadToRead(type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 是否陌生人
     */
    fun getIsStranger(
        ids: String,//id的数组字符串，例如：["111","222","333"]
    ): Observable<IsStrangerBean> {
        return messageApi.getIsStranger(ids)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    fun operateBlackList(
        ids: String,
        operation: Int,
    ): Observable<BlackListBean> {
        return messageApi.operateBlackList(ids, operation)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    fun getBlackList(
        page: Int,
        size: Int,
        lastTime: Long?,
    ): Observable<BasePageResult<BlackListBean>> {
        return messageApi.getBlackList(page, size, lastTime)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    fun checkMessageCount(
        ids: String,
    ): Observable<UserMsgCountEntity> {
        return messageApi.checkMessageCount(ids)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


}