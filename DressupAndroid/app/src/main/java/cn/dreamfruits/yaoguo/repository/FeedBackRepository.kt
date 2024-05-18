package cn.dreamfruits.yaoguo.repository

import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.constants.MMKVConstants
import cn.dreamfruits.yaoguo.repository.bean.feedback.FeedbackBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.FeedbackFeedBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.UninterestedBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.FeedBackRemoteDataSource
import io.reactivex.rxjava3.core.Observable

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 16:25
 */
class FeedBackRepository (dataSource: FeedBackRemoteDataSource)
    : BaseRemoteRepository<FeedBackRemoteDataSource>(dataSource) {

    /**
     *意见反馈
     */
    fun getFeedback(content: String,picUrls: String): Observable<FeedbackBean> {
        return remoteDataSource.getFeedback(content,picUrls).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.FEED_BACK,
                it)
        }
    }

    /**
     *动态反馈
     */
    fun getFeedbackFeed(feedId: Long,type: Int): Observable<FeedbackFeedBean> {
        return remoteDataSource.getFeedbackFeed(feedId,type).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.FEED_BACK_FEED,
                it)
        }
    }

    /**
     *不感兴趣
     */
    fun getUninterested( targetId: Long,type: Int): Observable<UninterestedBean> {
        return remoteDataSource.getUninterested(targetId,type).doOnNext {
            MMKVConstants.initData(
                MMKVConstants.UN_INTERESTED,
                it)
        }
    }
}