package cn.dreamfruits.yaoguo.repository.datasource.remote

import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.FeedBackApi
import cn.dreamfruits.yaoguo.repository.bean.feed.DeleteFeedBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.FeedbackBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.FeedbackFeedBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.UninterestedBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 16:24
 */
class FeedBackRemoteDataSource : BaseRemoteDataSource(){
    private val feedBackApi: FeedBackApi = retrofitService(FeedBackApi::class.java)



    /**
     * 意见反馈
     */
    fun getFeedback(
         content: String,//目标id
        picUrls: String//图片数组，["a","b"]
    ): Observable<FeedbackBean> {
        return feedBackApi.getFeedback(content,picUrls)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }



    /**
     * 动态反馈
     */
    fun getFeedbackFeed(
        feedId: Long,//动态id
        type: Int//1-广告，2-内容重复，3-内容不适，4-色情低俗
    ): Observable<FeedbackFeedBean> {
        return feedBackApi.getFeedbackFeed(feedId,type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 不感兴趣
     */
    fun getUninterested(
        targetId: Long,//动态id
        type: Int//1-不喜欢该内容，2-不喜欢该作者
    ): Observable<UninterestedBean> {
        return feedBackApi.getUninterested(targetId,type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }
}