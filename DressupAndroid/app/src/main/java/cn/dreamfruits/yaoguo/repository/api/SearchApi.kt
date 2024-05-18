package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.mention.TopicBean
import cn.dreamfruits.yaoguo.repository.bean.search.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/3/10
 * @TIME 17:40
 */
interface SearchApi {
    /**
     * 搜索动态
     */
    @FormUrlEncoded
    @POST("/app/api/searchFeed")
    fun getSearchFeed(
    @Field("key") key: String,//搜索词
    @Field("type") type: Int,//1-综合 2-最新 3-最热
    @Field("page") page: Int,//页码 第一页为1  后面传入接口返回的page字段值
    @Field("size") size: Int,//每页数量
    @Field("lastTime") lastTime: Long?,//分页用，传入接口返回的该值
    ): Observable<BaseResult<WaterfallFeedBean>>


    /**
     * 搜索用户
     */
    @FormUrlEncoded
    @POST("/app/api/searchUser")
    fun getSearchUser(
        @Field("key") key: String,//搜索词
        @Field("page") page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        @Field("size") size: Int,//数量
        @Field("lastTime") lastTime: Long?,//用于分页，传入上一次接口返回lastTime
        @Field("withoutIds") withoutIds: String?//过滤的用户的ids，例如：id1,id2,id3
    ): Observable<BaseResult<SearchUserBean>>



    /**
     * 搜索单品
     */
    @FormUrlEncoded
    @POST("/app/api/searchSingleProduct")
    fun getSearchSingleProduct(
        @Field("key") key: String,//搜索词
        @Field("page") page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        @Field("size") size: Int,//数量
        @Field("lastTime") lastTime: Long?//用于分页，传入上一次接口返回lastTime
    ): Observable<BaseResult<SearchSingleProductBean>>

    /**
     * 获取腰果热榜   api
     */
    @POST("/app/api/getYgHotFeedList")
    fun getYgHotFeedList(
    ): Observable<BaseResult<GetYgHotFeedListBean>>

    /**
     * 猜你想搜   api
     */
    @FormUrlEncoded
    @POST("/app/api/getGuessHotWords")
    fun getGuessHotWords(

        @Field("size") size: Int,

        ): Observable<BaseResult<GetGuessHotWordsBean>>




    /**
     * 搜索穿搭
     */
    @FormUrlEncoded
    @POST("/app/api/searchOutfit")
    fun getSearchOutfit(
        @Field("key") key: String,//搜索词
        @Field("type") type: Int,//1-综合 2-最新 3-最热
        @Field("page") page: Int,//页码，第一次穿1，后面传入上一次接口返回得page
        @Field("size") size: Int,//数量
        @Field("lastTime") lastTime: Long//用于分页，传入上一次接口返回lastTime
    ): Observable<BaseResult<SearchOutfitBean>>


   /**
     * 获取滚动热搜词
     */
    @FormUrlEncoded
    @POST("/app/api/getScrollHotWords")
    fun getScrollHotWords(
       @Field("size") size: Int//默认10
   ): Observable<BaseResult<SearchScrollHotWordsBean>>

    /**
     * 搜索模糊匹配   api
     */
    @FormUrlEncoded
    @POST("/es/api/searchLabel")
    fun searchWord(
        @Field("key") key: String,
        @Field("page") page: Int,
        @Field("size") size: Int,
        @Field("LastTime")lastTime: Long?=null
    ): Observable<BaseResult<SearchWordBean>>


    /**
     * 搜索话题
     */
    @FormUrlEncoded
    @POST("/es/api/searchLabel")
    fun searchTopic(
        @Field("key") key: String,
        @Field("page") page: Int,
        @Field("size") size: Int,
        @Field("LastTime")lastTime: Long?
    ):Observable<BaseResult<BasePageResult<TopicBean>>>
}