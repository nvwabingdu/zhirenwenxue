package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.feed.GetCollectListBean
import cn.dreamfruits.yaoguo.repository.bean.collect.CollectBean
import cn.dreamfruits.yaoguo.repository.bean.collect.UncollectBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/4/7
 * @TIME 15:15
 */
interface CollectApi {
    /**
     * 收藏
     */
    @FormUrlEncoded
    @POST("/app/api/collect")
    fun getCollect(
        @Field("type") type: Long,//类型，0-动态，1-单品，2-穿搭方案
        @Field("targetId") targetId: Long,//目标id(动态id | 单品id | 穿搭id)
        @Field("outfitStr") outfitStr: String?=null//穿搭json，创建动态
    ): Observable<BaseResult<CollectBean>>


    /**
     * 取消收藏
     */
    @FormUrlEncoded
    @POST("/app/api/uncollect")
    fun getUnCollect(
        @Field("targetId") targetId: Long,//目标id(动态id | 单品id | 穿搭id)
        @Field("type") type: Long//类型，0-动态，1-单品，2-穿搭方案
    ): Observable<BaseResult<UncollectBean>>


    /**
     * 收藏列表
     */
    @FormUrlEncoded
    @POST("/app/api/getCollectList")
    fun getCollectList(
        @Field("targetId") targetId: Long?=null,
        @Field("type") type: Long,//类型，0-动态，1-单品，2-穿搭方案，3-套装
        @Field("size") size: Long,//每页数量
        @Field("lastTime") lastTime: Long?=null//第一页不传，第二页开始使用上一页返回的lastTime
    ): Observable<BaseResult<GetCollectListBean>>



}