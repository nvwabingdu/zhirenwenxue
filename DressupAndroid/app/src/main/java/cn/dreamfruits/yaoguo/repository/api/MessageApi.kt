package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.module.main.message.bean.UserMsgCountEntity
import cn.dreamfruits.yaoguo.repository.bean.message.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/5/23
 * @TIME 14:38
 */
interface MessageApi {
    /**
     *获取点赞|收藏|引用 消息列表
     */
    @FormUrlEncoded
    @POST("/app/api/getLaudMessageList")
    fun getLaudMessageList(
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?,
    ): Observable<BaseResult<GetMessageInnerPageListBean>>

    /**
     *获取系统消息列表
     */
    @FormUrlEncoded
    @POST("/app/api/getSysMessageList")
    fun getSysMessageList(
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?,
    ): Observable<BaseResult<GetMessageInnerPageListBean>>


    /**
     *获取关注消息列表
     */
    @FormUrlEncoded
    @POST("/app/api/getFollowMessageList")
    fun getFollowMessageList(
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?,
    ): Observable<BaseResult<GetMessageInnerPageListBean>>


    /**
     *获取评论消息列表
     */
    @FormUrlEncoded
    @POST("/app/api/getCommentMessageList")
    fun getCommentMessageList(
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?,
    ): Observable<BaseResult<GetMessageInnerPageListBean>>


    /**
     * 获取消息未读数（整合）
     */
    @POST("/app/api/getUnreadCount")
    fun getUnreadCount(

    ): Observable<BaseResult<GetUnreadCountBean>>

    /**
     * 检查可发消息数量
     */
    @FormUrlEncoded
    @POST("/app/api/checkMessageCount")
    fun getCheckMessageCount(
        @Field("id") id: Long,
    ): Observable<BaseResult<CheckMessageCountBean>>

    /**
     * 未读更新为已读
     */
    @FormUrlEncoded
    @POST("/app/api/updateUnreadToRead")
    fun getUpdateUnreadToRead(
        @Field("type") type: Long,//0-赞|收藏和引用，1-新增关注，2-评论消息，4-系统消息
    ): Observable<BaseResult<UpdateUnreadToReadBean>>


    /**
     * 是否陌生人
     */
    @FormUrlEncoded
    @POST("/app/api/isStranger")
    fun getIsStranger(
        @Field("ids") ids: String,//id的数组字符串，例如：["111","222","333"]
    ): Observable<BaseResult<IsStrangerBean>>

    /**
     * 拉黑用户
     */
    @FormUrlEncoded
    @POST("/app/api/operateBlackList")
    fun operateBlackList(
        @Field("ids") ids: String,
        @Field("operation") operation: Int,
    ): Observable<BaseResult<BlackListBean>>

    /**
     * 获取黑名单列表
     */
    @FormUrlEncoded
    @POST("/app/api/getBlackList")
    fun getBlackList(
        @Field("page") page: Int,
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?,
    ): Observable<BaseResult<BasePageResult<BlackListBean>>>

    @FormUrlEncoded
    @POST("/app/api/operateBlackList")
    fun checkMessageCount(
        @Field("id") ids: String,
    ): Observable<BaseResult<UserMsgCountEntity>>

}