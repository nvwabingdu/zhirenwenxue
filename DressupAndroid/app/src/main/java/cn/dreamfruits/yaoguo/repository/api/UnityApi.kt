package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.unity.GetStyleVersionListBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/5/31
 * @TIME 10:49
 */
interface UnityApi {
    /**
     * 搜索动态
     */
    @FormUrlEncoded
    @POST("/app/api/getStyleVersionList")
    fun getStyleVersionList(
        @Field("size") size: Int,//每页数量
        @Field("lastTime") lastTime: Long?,//分页用，传入接口返回的该值
    ): Observable<BaseResult<GetStyleVersionListBean>>

}