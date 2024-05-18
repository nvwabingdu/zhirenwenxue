package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.comment.ChildCommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentDetailBean
import cn.dreamfruits.yaoguo.repository.bean.comment.CommentBean
import cn.dreamfruits.yaoguo.repository.bean.comment.DelCommentItemBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/4/8
 * @TIME 17:41
 */
interface CommentApi {

    /**
     * 获取评论详情
     */
    @FormUrlEncoded
    @POST("/app/api/getCommentDetail")
    fun getCommentDetail(
        @Field("commentId") commentId: Long,//评论id
        @Field("ids") ids: String//子评论id，例如，11111,22222
    ): Observable<BaseResult<CommentDetailBean>>



    /**
     * 获取评论列表
     */
    @FormUrlEncoded
    @POST("/app/api/getCommentList")
    fun getCommentList(
        @Field("targetId") targetId: Long,//动态id，一级评论id
        @Field("type") type: Int,//0-动态 1-评论
        @Field("size") size: Long,//每页条数
        @Field("lastTime") lastTime: Long?=null,//上一页返回的lastTime，第一页不传
        @Field("ids") ids: String?=null,//排除的id，例如：11111111,22222222
        @Field("commentIds") commentIds: String?=null//专用于消息页面，例如：11111111,22222222
    ): Observable<BaseResult<CommentBean>>



    /**
     * 发布评论
     */
    @FormUrlEncoded
    @POST("/app/api/publishComment")
    fun getPublishComment(//发布评论
        @Field("targetId") targetId: Long,//动态id，一级评论id
        @Field("type") type: Int,//0-动态 1-评论
        @Field("content") content: String,//评论内容
        @Field("replyId") replyId: Long?=null,//回复的评论id
        @Field("atUser") atUser: String,//at的用户字符串,用英文逗号","隔开，例如：id1,id2,id3
        @Field("config") config: String//配置字符串
    ): Observable<BaseResult<CommentBean.Item>>



    /**
     * ===================================获取评论列表 子级
     */
    @FormUrlEncoded
    @POST("/app/api/getCommentList")
    fun getCommentListChild(
        @Field("targetId") targetId: Long,//动态id，一级评论id
        @Field("type") type: Int,//0-动态 1-评论
        @Field("size") size: Long,//每页条数
        @Field("lastTime") lastTime: Long?=null,//上一页返回的lastTime，第一页不传
        @Field("ids") ids: String?=null//排除的id，例如：11111111,22222222
    ): Observable<BaseResult<ChildCommentBean>>

    /**
     * ===================================发布评论 子级
     */
    @FormUrlEncoded
    @POST("/app/api/publishComment")
    fun getPublishCommentChild(//发布评论
        @Field("targetId") targetId: Long,//动态id，一级评论id
        @Field("type") type: Int,//0-动态 1-评论
        @Field("content") content: String,//评论内容
        @Field("replyId") replyId: Long?=null,//回复的评论id
        @Field("atUser") atUser: String,//at的用户字符串,用英文逗号","隔开，例如：id1,id2,id3
        @Field("config") config: String//配置字符串
    ): Observable<BaseResult<ChildCommentBean.ChildItem>>


    /**
     * 点赞评论
     */
    @FormUrlEncoded
    @POST("/app/api/laudComment")
    fun getLaudComment(
        @Field("id") id: Long//评论id
    ): Observable<BaseResult<Any>>


    /**
     * 取消点赞评论
     */
    @FormUrlEncoded
    @POST("/app/api/unLudComment")
    fun getUnLaudComment(
        @Field("id") id: Long//评论id
    ): Observable<BaseResult<Any>>



    /**
     * 删除评论
     */
    @FormUrlEncoded
    @POST("/app/api/deleteComment")
    fun getDeleteComment(
        @Field("id") id: String//评论id
    ): Observable<BaseResult<DelCommentItemBean>>





}