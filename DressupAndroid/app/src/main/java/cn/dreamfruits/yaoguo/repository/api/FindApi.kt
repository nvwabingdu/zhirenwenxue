package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.attention.FollowUserBean
import cn.dreamfruits.yaoguo.repository.bean.find.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/3/30
 * @TIME 14:18
 */
interface FindApi {

    /**
     *发现用户
     */
    @FormUrlEncoded
    @POST("/app/api/getDiscoverUserList")
    fun getDiscoverUserList(
        @Field("temp") temp: Int//无字段 拼接
    ): Observable<BaseResult<DiscoverUserListBean>>



    /**
     *感兴趣的人
     */
    @FormUrlEncoded
    @POST("/app/api/getHomeRecommendUserList")
    fun getHomeRecommendUserList(
        @Field("size") size: Int//
    ): Observable<BaseResult<HomeRecommendUserListBean>>


    /**
     *忽略感兴趣的人
     */
    @FormUrlEncoded
    @POST("/app/api/ignoreRecommendUser")
    fun ignoreRecommendUser(
        @Field("id") id: Long,//忽略的用户ID
        @Field("type") type: Int//推荐用户的类型
    ): Observable<BaseResult<IgnoreRecommendUserBean>>



    /**
     *发现穿搭
     */
    @FormUrlEncoded
    @POST("/app/api/findOutfit")
    fun findOutfit(
        @Field("temp") temp: Int//无字段 拼接
    ): Observable<BaseResult<FindOutfitBean>>


    /**
     *发现话题
     */
    @FormUrlEncoded
    @POST("/app/api/findLabel")
    fun findLabel(
        @Field("temp") temp: Int//无字段 拼接
    ): Observable<BaseResult<FindLabelBean>>


}