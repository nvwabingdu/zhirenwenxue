package cn.dreamfruits.yaoguo.repository.datasource.remote


import cn.dreamfruits.baselib.repository.BaseRemoteDataSource
import cn.dreamfruits.yaoguo.net.rx.RxGlobalHttpHandleUtil
import cn.dreamfruits.yaoguo.repository.api.OauthApi
import cn.dreamfruits.yaoguo.repository.bean.oauth.LoginResultBean
import io.reactivex.rxjava3.core.Observable

/**
 *description: 登录相关的接口请求.
 */
class OauthRemoteDataSource : BaseRemoteDataSource() {

    private val oauthApi: OauthApi = retrofitService(OauthApi::class.java)


    fun sendSmsCode(phoneNumber: String): Observable<Any> {
        return oauthApi.sendSmsCode(phoneNumber)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 短信验证码登录
     */
    fun smsLogin(
        phoneNumber: String,
        smsCode: String,
    ): Observable<LoginResultBean> {
        return oauthApi.login("0", phoneNumber, smsCode, null, null)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    fun logout (): Observable<Any> {
        return oauthApi.logout()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 三方登录
     */
    fun thirdLogin(loginType:String,code:String,qqOpenId:String?):Observable<LoginResultBean>{

        return oauthApi.login(loginType, null, null, code, qqOpenId)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     *
     */
    fun refreshToken(refreshToken: String): Observable<LoginResultBean> {
        return oauthApi.refreshToken(refreshToken)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())

    }

    /**
     * 获取阿里一键登录key
     */
    fun getAliPhoneKey(): Observable<Map<String, String>> {
        return oauthApi.getAliPhoneKey()
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }

    /**
     * 一键登录
     */
    fun rapidLogin(token: String, type: Int,code: String?): Observable<LoginResultBean> {
        return oauthApi.rapidLogin(token, type,code)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 绑定手机号
     */
    fun bindPhone(type:Int,phoneNumber:String,captcha:String,code:String):Observable<LoginResultBean>{
        return oauthApi.bindPhone(type, phoneNumber, captcha, code)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }


    /**
     * 解除绑定第三方
     */
    fun unbindThirdParty(type: Int):Observable<Any>{
        return oauthApi.unbindThirdParty(type)
            .compose(RxGlobalHttpHandleUtil.globalHttpHandle())
    }
}