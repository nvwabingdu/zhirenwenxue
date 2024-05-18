package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.label.GetRecommendLabelListBean
import cn.dreamfruits.yaoguo.repository.bean.label.LabelDetailsBean
import cn.dreamfruits.yaoguo.repository.bean.oauth.LoginResultBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/3/20
 * @TIME 10:53
 */
interface LabelApi {


    /**
     * 首页tab-推荐话题  发布用
     */
    @FormUrlEncoded
    @POST("/app/api/getRecommendLabelList")
    fun getRecommendLabelList(
        @Field("count") count: Int
    ): Observable<BaseResult<GetRecommendLabelListBean>>


    /**
     * 首页tab-推荐话题
     */
    @FormUrlEncoded
    @POST("/app/api/getHomeRecommendLabelList")
    fun getRecommendLabelList2(
        @Field("count") count: Int
    ): Observable<BaseResult<GetRecommendLabelListBean>>


    /**
     * 话题 详情页
     */
    // TODO: 话题传id？ 
    @FormUrlEncoded
    @POST("/app/api/getLabelDetail")
    fun getLabelDetail(
        @Field("id") id: Long?
    ): Observable<BaseResult<LabelDetailsBean>>

}