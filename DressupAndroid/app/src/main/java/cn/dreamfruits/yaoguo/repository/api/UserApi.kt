package cn.dreamfruits.yaoguo.repository.api

import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.CommonStateBean
import cn.dreamfruits.yaoguo.repository.bean.search.SearchUserBean
import cn.dreamfruits.yaoguo.repository.bean.set.BindThreeAccountBean
import cn.dreamfruits.yaoguo.repository.bean.set.BindThreeAccountBean2
import cn.dreamfruits.yaoguo.repository.bean.set.GetBindRelationBean
import cn.dreamfruits.yaoguo.repository.bean.set.GetNotificationBean
import cn.dreamfruits.yaoguo.repository.bean.set.GetUserHomePageSettingBean
import cn.dreamfruits.yaoguo.repository.bean.set.NullBean
import cn.dreamfruits.yaoguo.repository.bean.set.SetUserHomePageSettingBean
import cn.dreamfruits.yaoguo.repository.bean.user.UserInfoBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * 用户相关api
 */
interface UserApi {


    /**
     * 获取用户信息
     */
    @FormUrlEncoded
    @POST("/usercenter/api/getUserInfo")
    fun getUserInfo(@Field("userId")userId:Long?=null):Observable<BaseResult<UserInfoBean>>



    /**
     * 更新用户信息
     * @param gender 性别 0=女 1=男
     * @param nickName 昵称
     * @param avatarUrl 用户头像
     * @param description 简介
     * @param ygId 腰果id |微酸号
     * @param birthday 用户生日 毫秒
     * @param country 国家
     * @param province 省
     * @param city 城市
     */
    @FormUrlEncoded
    @POST("/usercenter/api/updateUserInfo")
    fun updateUserInfo(
        @Field("gender") gender: Int?,
        @Field("nickName") nickName:String?,
        @Field("avatarUrl") avatarUrl:String?,
        @Field("description") description:String?,
        @Field("ygId") ygId:String?,
        @Field("birthday")birthday:Long?,
        @Field("country") country:String?,
        @Field("province")province:String?,
        @Field("city") city:String?,
        @Field("inviteCode") inviteCode:String?
    ): Observable<BaseResult<UserInfoBean>>


    /**
     * 设置用户消息通知设置   api
     */
    @FormUrlEncoded
    @POST("/app/api/setUserNotificationSetting")
    fun setUserNotificationSetting(
        @Field("type") type: Int,//0-点赞和收藏，1-新粉丝，2-评论，3-@，4-单聊，5-关注人发动态，6-官方
        @Field("isOpen") isOpen: Int,//0-关闭，1-开起
    ): Observable<BaseResult<NullBean>>


    /**
     * 获取用户消息通知设置列表   api
     */
    @POST("/app/api/getUserNotificationSettingList")
    fun getUserNotificationSettingList(
    ): Observable<BaseResult<GetNotificationBean>>

    /**
     * 获取绑定关系   api
     */
    @POST("/usercenter/api/getBindRelation")
    fun getBindRelation(
    ): Observable<BaseResult<GetBindRelationBean>>


    /**
     * 绑定三方   api
     */
    @FormUrlEncoded
    @POST("/usercenter/api/bindThirdParty")
    fun bindThirdParty(
        @Field("type") type: Int,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
        @Field("code") code: String?=null,//微信登录的code，qq登录的token
        @Field("openId") openId: String?=null,//qq的openId
    ): Observable<BaseResult<BindThreeAccountBean2>>

    /**
     * 解除绑定三方   api
     */
    @FormUrlEncoded
    @POST("/usercenter/api/unbindThirdParty")
    fun unbindThirdParty(
        @Field("type") type: Int,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
    ): Observable<BaseResult<BindThreeAccountBean2>>

    /**
     * 意见反馈   api
     */
    @FormUrlEncoded
    @POST("/app/api/feedback")
    fun feedback(

        @Field("content") content: String,//目标id
        @Field("picUrls") picUrls: String?=null,//可选 图片数组，["a","b"]

    ): Observable<BaseResult<CommonStateBean>>


    /**
     * 获取用户个人主页展示设置   api
     */
    @POST("/usercenter/api/getUserHomePageSetting")
    fun getUserHomePageSetting(



    ): Observable<BaseResult<GetUserHomePageSettingBean>>

    /**
     * 设置用户个人主页展示设置   api
     */
    @FormUrlEncoded
    @POST("/usercenter/api/setUserHomePageSetting")
    fun setUserHomePageSetting(

        @Field("type") type: Int,//0:生日，1:地区
        @Field("isShow") isShow: Int,//是否显示；0-不展示，1-展示
        @Field("subType") subType: Int?=null,//当type为0时，此字段必填；0:年龄，1-星座

    ): Observable<BaseResult<SetUserHomePageSettingBean>>

    /**
     * 获取关注列表   api
     */
    @FormUrlEncoded
    @POST("/usercenter/api/getUserFollowList")
    fun getUserFollowList(

        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?=null,//第一页不传，第二页开始传上次请求接口返回的lastTime
        @Field("targetUid") targetUid: Long?=null,//目标用户id

    ): Observable<BaseResult<SearchUserBean>>

    /**
     * 获取粉丝列表   api
     */
    @FormUrlEncoded
    @POST("/usercenter/api/getUserFollowerList")
    fun getUserFollowerList(

        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?=null,//第一页不传，第二页开始传上次请求接口返回的lastTime
        @Field("targetUid") targetUid: Long?=null,//目标用户id

    ): Observable<BaseResult<SearchUserBean>>

    /**
     * 搜索关注用户   api
     */
    @FormUrlEncoded
    @POST("/usercenter/api/searchFollow")
    fun searchFollow(

        @Field("size") size: Int,
        @Field("keyWord") keyWord: String,//需要过滤的id组成的字符串，使用英文逗号拼接在一起，例如：id1,id2,id3
        @Field("lastTime") lastTime: Long?=null,//第一页不传，第二页开始传上次请求接口返回的lastTime

    ): Observable<BaseResult<SearchUserBean>>


    /**
     * 获取黑名单列表   api
     */
    @FormUrlEncoded
    @POST("/app/api/getBlackList")
    fun getBlackList(

        @Field("size") size: Int,
        @Field("lastTime") lastTime: Long?=null,//第一页不传，第二页开始传上次请求接口返回的lastTime

    ): Observable<BaseResult<SearchUserBean>>

    /**
     * 操作黑名单   api
     */
    @FormUrlEncoded
    @POST("/app/api/operateBlackList")
    fun operateBlackList(

        @Field("operation") operation: Int,//0-拉黑， 1-取消拉黑
        @Field("ids") ids: String,//被拉黑用户id组成的字符串，用英文逗号隔开，例如：id1,id2,id3

    ): Observable<BaseResult<CommonStateBean>>


}