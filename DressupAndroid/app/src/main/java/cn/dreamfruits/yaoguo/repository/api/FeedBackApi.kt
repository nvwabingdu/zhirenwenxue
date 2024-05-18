package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.feedback.FeedbackBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.FeedbackFeedBean
import cn.dreamfruits.yaoguo.repository.bean.feedback.UninterestedBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 16:29
 */
interface FeedBackApi {


    /**
     *意见反馈
     */
    @FormUrlEncoded
    @POST("/app/api/feedback")
    fun getFeedback(
        @Field("content") content: String,//目标id
        @Field("picUrls") picUrls: String//图片数组，["a","b"]
    ): Observable<BaseResult<FeedbackBean>>



    /**
     *动态反馈
     */
    @FormUrlEncoded
    @POST("/app/api/feedbackFeed")
    fun getFeedbackFeed(
        @Field("feedId") feedId: Long,//动态id
        @Field("type") type: Int//1-广告，2-内容重复，3-内容不适，4-色情低俗
    ): Observable<BaseResult<FeedbackFeedBean>>




    /**
     *不感兴趣
     */
    @FormUrlEncoded
    @POST("/app/api/uninterested")
    fun getUninterested(
        @Field("targetId") targetId: Long,//动态id
        @Field("type") type: Int//1-不喜欢该内容，2-不喜欢该作者
    ): Observable<BaseResult<UninterestedBean>>


}