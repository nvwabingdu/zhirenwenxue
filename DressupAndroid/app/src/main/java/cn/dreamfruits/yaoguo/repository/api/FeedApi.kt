package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.feed.*
import cn.dreamfruits.yaoguo.repository.bean.label.GetLabelListBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @Author qiwangi
 * @Date 2023/3/14
 * @TIME 15:47
 */
interface FeedApi {

    /**
     * 创建动态
     */
    @FormUrlEncoded
    @POST("/app/api/createFeed")
    fun createFeed(
        @Field("title") title: String?,//示例值： 测试创建动态标题1 动态标题
        @Field("content") content: String?,//示例值： 测试创建动态标题1 动态内容
        @Field("type") type: Int,//类型：0-图文动态；1-视频动态
        @Field("picUrls") picUrls: String?,//[{"url":"https://devfile.dreamfruits.cn/admin/10002/89f6999043d7c19aec1b6c7186422757.JPG","width":1000,"height":800},{"url":"https://devfile.dreamfruits.cn/admin/10002/de3afd4ccbda949ee8edec6816640d7a.JPG","width":1000,"height":800}]
        @Field("seeLimit") seeLimit: Int,//动态私密性：0-公开，1-好友可见，2-私密
        @Field("videoUrls") videoUrl: String?,//视频
        @Field("topicIds") topicIds: String?,//示例值： [282184828546528,282231699115488] 话题ids
        @Field("atUser") atUser: String?,//at用户ids
        @Field("provinceAdCode") provinceAdCode: String?,//省码
        @Field("cityAdCode") cityAdCode: String?,//城市码
        @Field("longitude") longitude: Double?,//经度
        @Field("latitude") latitude: Double?,//维度
        @Field("address") address: String?,//地址
        @Field("singleIds")singleIds: String?//单品id
    ): Observable<BaseResult<FeedDetailsBean>>



    /**
     * 删除动态   api
     */
    @FormUrlEncoded
    @POST("/app/api/deleteFeed")
    fun deleteFeed(
        @Field("id") id: Long,//目标id 必须
    ): Observable<BaseResult<DeleteFeedBean>>


    /**
     * 动态详情
     */
    @FormUrlEncoded
    @POST("/app/api/feedDetail")
    fun getFeedDetail(
        @Field("id") id: Long//动态id
    ): Observable<BaseResult<WaterfallFeedBean.Item.Info>>


    /**
     *获取推荐动态列表
     */
    @FormUrlEncoded
    @POST("/app/api/getRecommendFeedList")
    fun getRecommendFeedList(
        @Field("size") size: Int,//请求数目，默认10
        @Field("type") type: Int//类型：0-图文动态；1-视频动态
    ): Observable<BaseResult<WaterfallFeedBean>>


    /**
     *获取关注动态列表
     */
    @FormUrlEncoded
    @POST("/app/api/getCareFeedList")
    fun getCareFeedList(
        @Field("size") size: Int,//请求数目，默认10
        @Field("lastTime") lastTime: Long//用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入
    ): Observable<BaseResult<WaterfallFeedBean>>


    /**
     *获取用户动态列表
     */
    @FormUrlEncoded
    @POST("/app/api/getUserFeedList")
    fun getUserFeedList(
        @Field("type") type: Int,//0-用户动态列表 1-用户收藏动态列表 2-用户浏览过得动态列表
        @Field("targetId") targetId: Long?=null,//查询自己的动态列表不用传入，查询他人的需要传入该用户id
        @Field("size") size: Int,//请求数目，默认10
        @Field("lastTime") lastTime: Long?=null//用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入 不传查询自己的动态列表
    ): Observable<BaseResult<WaterfallFeedBean>>


    /**
     *获取话题动态列表
     */
    @FormUrlEncoded
    @POST("/app/api/getLabelFeedList")
    fun getLabelFeedList(
        @Field("labelId") labelId: Long,
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long//用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入 不传查询自己的动态列表
    ): Observable<BaseResult<WaterfallFeedBean>>


    /**
     * 指定单品层级相关帖子
     */
    @FormUrlEncoded
    @POST("/app/api/getFeedListBySpId")
    fun getFeedListBySpId(
        @Field("singleProductId") singleProductId: Long,//单品id
        @Field("floor") floor: Int,//层级
        @Field("page") page: Int,
        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long//用于分页，当上一次接口请求返回hasNext为1时，将上一次返回的lastTime传入 不传查询自己的动态列表
    ): Observable<BaseResult<GetFeedListBySpIdBean>>


    /**
     * 获取穿搭方案同款帖子
     */
    @FormUrlEncoded
    @POST("/app/api/getOutfitFeedList")
    fun getOutfitFeedList(
        @Field("outfitId") outfitId: Long,//穿搭id
        @Field("page") page: Int,
        @Field("size") size: Int,//一页条数
        @Field("lastTime") lastTime: Long//用于分页
    ): Observable<BaseResult<GetOutfitFeedListBean>>


    /**
     * 点赞
     */
    @FormUrlEncoded
    @POST("/app/api/laudFeed")
    fun getLaudFeed(
        @Field("id") id: Long
    ): Observable<BaseResult<LaudFeedBean>>


    /**
     * 取消点赞
     */
    @FormUrlEncoded
    @POST("/app/api/unLudFeed")
    fun getUnLudFeed(
        @Field("id") id: Long
    ): Observable<BaseResult<UnLudFeedBean>>


    /**
     * 获取赞过的帖子
     */
    @FormUrlEncoded
    @POST("/app/api/getLaudList")
    fun getLaudList(
        @Field("size") size: Int,//一页条数
        @Field("lastTime") lastTime: Long,//用于分页
        @Field("targetUid") targetUid: Long//他人的用户id
    ): Observable<BaseResult<GetLaudListBean>>


    /**
     * 更改动态的可见状态
     */
    @FormUrlEncoded
    @POST("/app/api/changeFeedSeeLimit")
    fun getChangeFeedSeeLimit(
        @Field("id") id: Long,//一页条数
        @Field("seeLimit") seeLimit: Int//0-公开，1-好友可见，2-私密
    ): Observable<BaseResult<ChangeFeedSeeLimitBean>>

}