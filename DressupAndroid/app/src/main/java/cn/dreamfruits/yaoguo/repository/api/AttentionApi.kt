package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.attention.*
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/3/21
 * @TIME 17:29
 */
interface AttentionApi {
    /**
     *关注用户
     */
    @FormUrlEncoded
    @POST("/usercenter/api/followUser")
    fun getFollowUser(
        @Field("targetId") targetId: Long//要关注用户的id
    ): Observable<BaseResult<FollowUserBean>>



    /**
     *取消关注
     */
    @FormUrlEncoded
    @POST("/usercenter/api/unfollowUser")
    fun getUnfollowUser(
        @Field("targetId") targetId: Long//要取消关注用户的id
    ): Observable<BaseResult<UnfollowUserBean>>



    /**
     *获取关注列表
     */
    @FormUrlEncoded
    @POST("/usercenter/api/getUserFollowList")
    fun getUserFollowList(
        @Field("size") size: Int,//每页数量
        @Field("lastTime") lastTime: Long,//第一页不传，第二页开始传上次请求接口返回的lastTime
        @Field("targetUid") targetUid: Long//目标用户id
    ): Observable<BaseResult<UserFollowListBean>>



    /**
     *获取粉丝列表
     */
    @FormUrlEncoded
    @POST("/usercenter/api/getUserFollowerList")
    fun getUserFollowerList(
        @Field("size") size: Int,//每页数量
        @Field("lastTime") lastTime: Long,//第一页不传，第二页开始传上次请求接口返回的lastTime
        @Field("targetUid") targetUid: Long//目标用户id
    ): Observable<BaseResult<UserFollowFansListBean>>


    /**
     *推荐关注
     */
    @FormUrlEncoded
    @POST("/usercenter/api/getRecommendUserList")
    fun getRecommendUserList(
        @Field("page") page: Int,//页数，从1开始
        @Field("size") size: Int//大于等于1
    ): Observable<BaseResult<RecommendUserListBean>>


    /**
     *获取at列表
     */
    @FormUrlEncoded
    @POST("/usercenter/api/getAtUserList")
    fun getAtUserList(
        @Field("size") size: Int,// 大于等于1
        @Field("lastTime") lastTime: Long?,//第一页不传，第二页开始使用上一页返回的lastTime    api上面是number类型？？？
        @Field("withoutIds") withoutIds: String?//需要过滤的id组成的字符串，使用英文逗号拼接在一起，例如：id1,id2,id3
    ): Observable<BaseResult<SearchUserBean>>


}

