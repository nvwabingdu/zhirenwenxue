package cn.dreamfruits.yaoguo.repository.api


import cn.dreamfruits.yaoguo.base.bean.BaseResult
import cn.dreamfruits.yaoguo.repository.bean.oauth.LoginResultBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface OauthApi {

    /**
     * 发送验证码
     */
    @FormUrlEncoded
    @POST("/usercenter/api/getCaptcha")
    fun sendSmsCode(@Field("phoneNumber") phoneNumber: String): Observable<BaseResult<Any>>


    /**
     * 刷新token
     */
    @FormUrlEncoded
    @POST("/usercenter/api/refreshToken")
    fun refreshToken(@Field("refreshToken") refreshToken: String): Observable<BaseResult<LoginResultBean>>


    /**
     * 登录接口
     * @param loginType 0手机号 1微信 2QQ 4微博
     * @param phoneNumber 手机号 loginType为 0 时必传
     * @param captcha 验证码
     * @param code 微信登录的code || qq的token
     * @param qqOpenId qq的openid
     */
    @FormUrlEncoded
    @POST("/usercenter/api/login")
    fun login(
        @Field("loginType") loginType: String,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("captcha") captcha: String?,
        @Field("code") code: String?,
        @Field("qqOpenId") qqOpenId: String?,
    ): Observable<BaseResult<LoginResultBean>>

    /**
     * 退出登录
     */
    @POST("/usercenter/api/logout")
    fun logout(): Observable<BaseResult<Any>>

    /**
     * 获取阿里云一键登录key
     */
    @POST("/thirdparty/api/getAliPhoneKey")
    fun getAliPhoneKey(): Observable<BaseResult<Map<String, String>>>


    /**
     * 一键登录
     * @param token 阿里云 token
     * @param type 0-一键登录
     * @param code 微信unionId || qqopenId
     */
    @FormUrlEncoded
    @POST("/usercenter/api/rapidLogin")
    fun rapidLogin(
        @Field("token") token: String,
        @Field("type") type: Int,
        @Field("code") code: String?
    ): Observable<BaseResult<LoginResultBean>>


    /**
     * 绑定手机号
     */
    @FormUrlEncoded
    @POST("/usercenter/api/bindPhone")
    fun bindPhone(
        @Field("type") type: Int,
        @Field("phoneNumber") phoneNumber: String,
        @Field("captcha") captcha: String,
        @Field("code") code: String
    ): Observable<BaseResult<LoginResultBean>>


    /**
     * 解除绑定第三方
     * @param type 1=微信 2=QQ
     */
    @FormUrlEncoded
    @POST("/usercenter/api/unbindThirdParty")
    fun unbindThirdParty(@Field("type")type: Int):Observable<BaseResult<Any>>
}