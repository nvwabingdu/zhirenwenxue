package cn.dreamfruits.yaoguo.repository


import android.content.Context
import cn.dreamfruits.baselib.repository.BaseRemoteRepository
import cn.dreamfruits.yaoguo.repository.bean.oauth.LoginResultBean
import cn.dreamfruits.yaoguo.repository.datasource.remote.OauthRemoteDataSource
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.core.Observable

/**
 * 授权相关的仓库
 */
class OauthRepository(
    dataSource: OauthRemoteDataSource,

    ) : BaseRemoteRepository<OauthRemoteDataSource>(dataSource) {

    companion object {

        private const val KEY_CACHE_OAUTH_BEAN = "key_cache_oauth_bean"

        /**
         * 用户没登录的时候的token
         */
        private const val BASIC_TOKEN = "aMBI%%jZUHaf5p&h!2MT!&tKxP56^yR5"


        var token = BASIC_TOKEN
            private set

        private var refreshToken: String = ""
        private var userId: Long = 0L
        private var tempLoginResultBean: LoginResultBean? = null


        fun init() {
            val json = MMKVRepository.getCommonMMKV().decodeString(KEY_CACHE_OAUTH_BEAN, "")
            if (json!!.isNotBlank()) {
                try {
                    val bean = GsonUtils.fromJson(json, LoginResultBean::class.java)
                    initData(bean)
                } catch (e: Exception) {

                }
            }
        }

        private fun initData(bean: LoginResultBean) {
            token = bean.token
            refreshToken = bean.refreshToken
            userId = bean.userId
        }

        /**
         * 更新token
         */
        fun updateToken(newToken: LoginResultBean) {
            tempLoginResultBean = newToken
            cacheLoginResultBean()
            initData(newToken)
        }


        /**
         * 将token数据缓存
         */
         fun cacheLoginResultBean() {
            tempLoginResultBean?.let {
                val json = GsonUtils.toJson(it)
                MMKVRepository.getCommonMMKV().encode(KEY_CACHE_OAUTH_BEAN, json)
            }
        }

        /**
         * 清除缓存数据
         */
        fun clearCache() {
            token = BASIC_TOKEN
            refreshToken = ""
            userId = 0L
            MMKVRepository.getCommonMMKV().removeValueForKey(KEY_CACHE_OAUTH_BEAN)
        }

        /**
         * 是否登录
         */
        fun isLogin(): Boolean {
            return token.isNotEmpty() && token != BASIC_TOKEN && userId != 0L
        }

        /*
         *获取用户的授权token放在请求头中
         */
        fun getAuthorization() = token

        /**
         * 获取默认的Authorization
         */
        fun getBasicAuthorization() = BASIC_TOKEN


        fun getUserId() = userId

        fun getExpireTime() = tempLoginResultBean?.expireTime ?: 0L

    }

    fun getSmsLoginCode(phoneNumber: String): Observable<Any> {
        return remoteDataSource.sendSmsCode(phoneNumber)
    }


    /**
     * 验证码登录
     */
    fun smsLogin(phoneNumber: String, smsCode: String): Observable<LoginResultBean> {
        clearCache()
        return remoteDataSource.smsLogin(phoneNumber, smsCode)
            .doOnNext {
                initData(it)
                tempLoginResultBean = it
                LogUtils.e(">>> inviteCodePerfect = " + it.inviteCodePerfect)
                if (it.isPerfect == 1 && it.inviteCodePerfect != 0) {
                    LogUtils.e(">>> cacheLoginResultBean = ")
                    cacheLoginResultBean()
                }
            }
    }

    /**
     * 登出
     */
    fun logout(): Observable<Any> {
        return remoteDataSource.logout()
            .doOnNext {
                clearCache()
            }
    }

    /**
     * qq 微信 三方登录
     */
    fun thirdLogin(
        loginType: String,
        code: String,
        qqOpenId: String?,
    ): Observable<LoginResultBean> {
        return remoteDataSource.thirdLogin(loginType, code, qqOpenId)
            .doOnNext {
                initData(it)
                tempLoginResultBean = it
                if (it.isPerfect == 1 && it.inviteCodePerfect != 0) {
                    cacheLoginResultBean()
                }
            }
    }

    /**
     * 刷新 token
     */
    fun refreshToken(): Observable<LoginResultBean> {
        return remoteDataSource.refreshToken(refreshToken)
            .doOnNext {
                initData(it)
                tempLoginResultBean = it
                cacheLoginResultBean()
            }
    }

    /**
     * 获取阿里云key
     */
    fun getAliPhoneKey(): Observable<Map<String, String>> {
        return remoteDataSource.getAliPhoneKey()
    }

    /**
     * 一键登录
     */
    fun rapidLogin(token: String, type: Int, code: String?): Observable<LoginResultBean> {
        return remoteDataSource.rapidLogin(token, type, code)
            .doOnNext {
                initData(it)
                tempLoginResultBean = it
                if (it.isPerfect == 1 && it.inviteCodePerfect != 0) {
                    cacheLoginResultBean()
                }
            }
    }

    /**
     * 绑定手机号
     */
    fun bindPhone(
        type: Int,
        phoneNumber: String,
        captcha: String,
        code: String,
    ): Observable<LoginResultBean> {
        return remoteDataSource.bindPhone(type, phoneNumber, captcha, code)
            .doOnNext {
                initData(it)
                tempLoginResultBean = it
                cacheLoginResultBean()
            }
    }

    /**
     * 解除绑定三方
     */
    fun unbindThirdParty(type: Int): Observable<Any> {
        return remoteDataSource.unbindThirdParty(type)
    }
}