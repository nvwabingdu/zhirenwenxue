package cn.dreamfruits.yaoguo.repository.datasource

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo.type
import cn.dreamfruits.yaoguo.module.main.message.bean.UserMsgCountEntity
import cn.dreamfruits.yaoguo.repository.bean.message.*
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.MessageRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/5/23
 * @TIME 14:49
 */
class MessageRepository(dataSource: MessageRemoteDataSource) :
    BaseRemoteRepository<MessageRemoteDataSource>(dataSource) {


    /**
     *获取点赞|收藏|引用 消息列表
     */
    fun getLaudMessageList(
        size: Int,
        lastTime: Long?,
    ): Observable<GetMessageInnerPageListBean> {
        return remoteDataSource.getLaudMessageList(size, lastTime).doOnNext {
        }
    }

    /**
     *获取系统消息列表
     */
    fun getSysMessageList(
        size: Int,
        lastTime: Long?,
    ): Observable<GetMessageInnerPageListBean> {
        return remoteDataSource.getSysMessageList(size, lastTime).doOnNext {
        }
    }

    /**
     *获取关注消息列表
     */
    fun getFollowMessageList(
        size: Int,
        lastTime: Long?,
    ): Observable<GetMessageInnerPageListBean> {
        return remoteDataSource.getFollowMessageList(size, lastTime).doOnNext {
        }
    }

    /**
     *获取评论消息列表
     */
    fun getCommentMessageList(
        size: Int,
        lastTime: Long?,
    ): Observable<GetMessageInnerPageListBean> {
        return remoteDataSource.getCommentMessageList(size, lastTime).doOnNext {
        }
    }


    /**
     * 获取消息未读数（整合）
     */
    fun getUnreadCount(
    ): Observable<GetUnreadCountBean> {
        return remoteDataSource.getUnreadCount().doOnNext {
        }
    }

    /**
     * 检查可发消息数量
     */
    fun getCheckMessageCount(
        id: Long,
    ): Observable<CheckMessageCountBean> {
        return remoteDataSource.getCheckMessageCount(id).doOnNext {
        }
    }


    /**
     * 未读更新为已读
     */
    fun getUpdateUnreadToRead(
        type: Long,
    ): Observable<UpdateUnreadToReadBean> {
        return remoteDataSource.getUpdateUnreadToRead(type).doOnNext {
        }
    }


    /**
     * 是否陌生人
     */
    fun getIsStranger(
        ids: String,//id的数组字符串，例如：["111","222","333"]
    ): Observable<IsStrangerBean> {
        return remoteDataSource.getIsStranger(ids).doOnNext {
        }
    }

    /**
     * operation 0-拉黑， 1-取消拉黑
     * 被拉黑用户id组成的字符串，用英文逗号隔开，例如：id1,id2,id3
     */
//    /app/api/operateBlackList
    fun operateBlackList(
        ids: String,
        operation: Int,
    ): Observable<BlackListBean> {
        return remoteDataSource.operateBlackList(ids, operation).doOnNext {
        }
    }

    /**
     * 获取黑名单列表
     */
    fun getBlackList(
        page: Int,
        size: Int,
        lastTime: Long?,
    ): Observable<BasePageResult<BlackListBean>> {
        return remoteDataSource.getBlackList(page, size, lastTime).doOnNext {
        }
    }

    fun checkMessageCount(
        ids: String,
    ): Observable<UserMsgCountEntity> {
        return remoteDataSource.checkMessageCount(ids).doOnNext {
        }
    }

}