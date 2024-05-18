package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.base.bean.BasePageResult
import cn.dreamfruits.yaoguo.module.main.stroll.inner.singledetails.bean.GetSimilarRecommendBean
import cn.dreamfruits.yaoguo.repository.bean.CommonStateIntBean
import cn.dreamfruits.yaoguo.repository.bean.diy.GetStyleVersionListByTypeBean
import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesBean
import cn.dreamfruits.yaoguo.repository.bean.selectclothes.ClothesCategoryBean
import cn.dreamfruits.yaoguo.repository.bean.stroll.GetClothesItemInfoBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ClothesApi {

    /**
     * 获取版型分类
     * @param position 一级分类
     * @param gender 性别 0=女 1=男
     * @param size 分页大小
     */
    @FormUrlEncoded
    @POST("/app/api/getClothesVersionList")
    fun getClothesCategory(
        @Field("position") position: Int,
        @Field("gender") gender: Int,
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?
    ): Observable<BaseResult<BasePageResult<ClothesCategoryBean>>>


    /**
     * 获取单品
     * @param cvId 版型id
     * @param size 分页大小
     */
    @FormUrlEncoded
    @POST("/app/api/getSingleProductByCvId")
    fun getClothesList(
        @Field("cvId") cvId: Long,
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?
    ): Observable<BaseResult<BasePageResult<ClothesBean>>>


    /**
     * 根据类型获取版型列表  逛逛
     */
    @FormUrlEncoded
    @POST("/app/api/getStyleVersionListByType")
    fun getStyleVersionListByType(
        @Field("type") type: Int,//1-上装，2-下装，3-连衣裙，4-袜子，5-鞋子，6-配饰
        @Field("size") size: Int,
        @Field("gender") gender: Int?=null,//0-女，1-男
        @Field("lastTime") lastTime: Long?=null,
        @Field("subType") subType: Int?=null//从逛逛页获取数据不用传，从DIY页获取数据传1
    ): Observable<BaseResult<GetStyleVersionListByTypeBean>>

    /**
     * 根据类型获取版型列表2  逛逛
     */
    @FormUrlEncoded
    @POST("/model/api/getStyleVersionTypeList")
    fun getStyleVersionTypeListStroll(
        @Field("type") type: Long,//1-一级分类，2-二级分类，3-三级分类
        @Field("id") id: Long?=null//上一级分类的id 可选
    ): Observable<BaseResult<GetStyleVersionListByTypeBean>>


    /**
     * 根据二级分类获取服装列表 逛逛   api
     */
    @FormUrlEncoded
    @POST("/app/api/getClothesItemListByType")
    fun getClothesItemListByTypeStroll(

        @Field("gender") gender: Int,//0-女，1-男
        @Field("type") type: Long,//二级分类id
        @Field("size") size: Int,
        @Field("LastTime")lastTime: Long?=null

    ): Observable<BaseResult<GetStyleVersionListByTypeBean>>




    /**
     * 根据版型id获取商品  逛逛也用这个接口
     */
    @FormUrlEncoded
    @POST("/app/api/getClothesItemListBySvId")
    fun getClothesItemListBySvId(
        @Field("svId") svId: Long,
        @Field("size") size: Int,
        @Field("gender") gender: Int,//0-女，1-男
        @Field("isOpen") isOpen: Int,//0-私密，1-公开，2-全部
        @Field("lastTime") lastTime: Long?=null,
    ): Observable<BaseResult<GetStyleVersionListByTypeBean>>


    /**
     * 获取我的DIY列表
     */
    @FormUrlEncoded
    @POST("/app/api/getMyDiyList")
    fun getMyDiyList(
        @Field("size") size: Int,
        @Field("isOpen") isOpen: Int,//0-私密，1-公开，2-全部
        @Field("lastTime") lastTime: Long?=null,
    ): Observable<BaseResult<GetStyleVersionListByTypeBean>>


    /**
     * 获取服装单品详情
     */
    @FormUrlEncoded
    @POST("/app/api/getClothesItemInfo")
    fun getClothesItemInfo(
        @Field("id") id: Long//服装单品详情id
    ): Observable<BaseResult<GetClothesItemInfoBean>>



    /**
     * 潮流精选
     */
    @FormUrlEncoded
    @POST("/app/api/getFeedListByCvId")
    fun getFeedListByCvId(
        @Field("targetId") targetId: Long,//服装单品主键id
        @Field("size") size: Int,//请求数目，默认4
        @Field("lastTime") lastTime: Long?=null
    ): Observable<BaseResult<WaterfallFeedBean>>


    /**
     * 相似穿搭
     */
    @FormUrlEncoded
    @POST("/app/api/getSimilarRecommend")
    fun getSimilarRecommend(
        @Field("targetId") targetId: Long,//服装单品主键id
        @Field("size") size: Int//请求数目，默认30
    ): Observable<BaseResult<GetSimilarRecommendBean>>


    /**
     * 获取版型分类列表APP（目前用于diy）
     */
    @FormUrlEncoded
    @POST("/model/api/getStyleVersionTypeListDiy")
    fun getStyleVersionTypeList(
        @Field("type") type: Long,//1-一级分类，2-二级分类，3-三级分类
        @Field("id") id: Long?=null//上一级分类的id 可选
    ): Observable<BaseResult<GetStyleVersionListByTypeBean>>

    /**
     * 获取我的服装列表
     */
    @FormUrlEncoded
    @POST("/app/api/getMyClothesItem")
    fun getMyClothesItem(
        @Field("size") size: Int,
        @Field("type") isOpen: Int,//0-我收藏的，1-我试穿的，2-我浏览的，3-我DIY的
        @Field("lastTime") lastTime: Long?=null,
    ): Observable<BaseResult<BasePageResult<ClothesBean>>>

   /**
     * 搜索服装列表
     */
    @FormUrlEncoded
    @POST("/app/api/searchClothesItem")
    fun searchClothesItem(
        @Field("key") key: String,
        @Field("page") page: Int,
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?=null,
    ): Observable<BaseResult<BasePageResult<ClothesBean>>>

    /**
     * 根据分类id获取版型列表（目前用于diy）
     */
    @FormUrlEncoded
    @POST("/model/api/getStyleVersionListByType")
    fun getStyleVersionListByType(
        @Field("id") id: Long,//三级分类id
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?=null
    ): Observable<BaseResult<GetStyleVersionListByTypeBean>>


    /**
     * 修改单品可见状态   api
     */
    @FormUrlEncoded
    @POST("/app/api/changeClothesItemSeeLimit")
    fun changeClothesItemSeeLimit(

        @Field("id") id: Long,//单品id
        @Field("state") state: Int,//0-私密，1-公开

    ): Observable<BaseResult<CommonStateIntBean>>

    /**
     * 修改单品名字   api
     */
    @FormUrlEncoded
    @POST("/app/api/changeClothesItemName")
    fun changeClothesItemName(

        @Field("id") id: Long,//单品id
        @Field("str") str: String,//单品名字

    ): Observable<BaseResult<CommonStateIntBean>>

    /**
     * 删除单品   api
     */
    @FormUrlEncoded
    @POST("/app/api/deleteClothesItem")
    fun deleteClothesItem(

        @Field("id") id: Long,//单品id

    ): Observable<BaseResult<CommonStateIntBean>>

    /**
     * 重新上传制作服装单品   api
     */
    @FormUrlEncoded
    @POST("/app/api/remakeClothesItem")
    fun remakeClothesItem(

        @Field("id") id: Long,//单品id

    ): Observable<BaseResult<CommonStateIntBean>>


}