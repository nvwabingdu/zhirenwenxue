package cn.dreamfruits.yaoguo.module.login


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.dreamfruits.yaoguo.base.BaseViewModel
import cn.dreamfruits.yaoguo.module.OEMPush.services.TIMAppService
import cn.dreamfruits.yaoguo.module.login.bean.UserInfo
import cn.dreamfruits.yaoguo.module.login.signature.GenerateTestUserSig
import cn.dreamfruits.yaoguo.module.login.state.LoginState
import cn.dreamfruits.yaoguo.module.login.state.LogoutState
import cn.dreamfruits.yaoguo.module.login.state.SmsState
import cn.dreamfruits.yaoguo.module.login.state.ThirdpartyApiState
import cn.dreamfruits.yaoguo.module.main.MainActivity
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.ProfileState
import cn.dreamfruits.yaoguo.net.exception.ApiException
import cn.dreamfruits.yaoguo.repository.OauthRepository
import cn.dreamfruits.yaoguo.repository.ThridpartyRepository
import cn.dreamfruits.yaoguo.repository.UserRepository
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMManager
import org.koin.core.component.inject


class LoginViewModel() : BaseViewModel() {

    private val oauthRepository by inject<OauthRepository>()

    private val userRepository by inject<UserRepository>()

    private val thridpartyRepository by inject<ThridpartyRepository>()

    //是否点击手机号登录
    var phoneLoginClick = false

    //是否同意隐私协议
    var confirmPrivacy = false

    //一键登录隐私协议
    var onKeyLoginPrivacy = false

    private val _smsState = MutableLiveData<SmsState>()
    val smsState: LiveData<SmsState> get() = _smsState

    private val _loginState = MutableLiveData<LoginState>()

    val loginState: LiveData<LoginState> get() = _loginState

    private val _aliPhoneKey = MutableLiveData<String>()
    val aliPhoneKey: LiveData<String> get() = _aliPhoneKey

    private val _logoutState = MutableLiveData<LogoutState>()
    val logoutState: LiveData<LogoutState> get() = _logoutState

    /**
     * 个人资料
     */
    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> get() = _profileState


    private val _thirdpartyApiState = MutableLiveData<ThirdpartyApiState>()//wq
    val thirdpartyApiState: MutableLiveData<ThirdpartyApiState> get() = _thirdpartyApiState//wq

    fun sendSms(phoneNumber: String) {

        val disposable = oauthRepository.getSmsLoginCode(phoneNumber)
            .subscribe({
                _smsState.value = SmsState.Success
            }, {
                _smsState.value = SmsState.Fail(it.message)
            })

        addDisposable(disposable)
    }


    /**
     * 验证码登录
     */
    fun smsLogin(phoneNumber: String, smsCode: String) {
        val disposable = oauthRepository.smsLogin(phoneNumber, smsCode)
            .subscribe({
                _loginState.value = LoginState.Success(it)
                //如果绑定了性别
                LogUtils.e(">>> inviteCodePerfect = " + it.inviteCodePerfect)
                if (it.isPerfect == 1 && it.inviteCodePerfect != 0) {
                    getUserInfo(it.userId)
                }
            }, {
                if (it is ApiException) {
                    _loginState.value = LoginState.Fail(it.message, it.code)
                } else {
                    _loginState.value = LoginState.Fail(it.message)
                }
            })
        addDisposable(disposable)
    }

    /**
     * 登出
     */
    fun logout() {
        val disposable = oauthRepository.logout()
            .subscribe({

                V2TIMManager.getInstance().logout(object : V2TIMCallback {
                    override fun onSuccess() {
                        Log.i("imsdk", "success")
                        _logoutState.value = LogoutState.Success
//                        ToastUtils.showShort("退出登录成功")
                        OauthRepository.clearCache()
                        ActivityUtils.finishAllActivities()
                        LoginHelper.startLogin()
                    }

                    override fun onError(code: Int, desc: String) {
                        Log.i("imsdk", "failure, code:$code, desc:$desc")
                    }
                })


            }, {
                _logoutState.value = LogoutState.Fail(it.message)
            })
        addDisposable(disposable)
    }

    /**
     * 第三方登录
     */
    fun loginByThird(loginType: String, code: String, qqOpenId: String? = null) {
        val disposable = oauthRepository.thirdLogin(loginType, code, qqOpenId)
            .subscribe({
                _loginState.value = LoginState.Success(it)
                //如果绑定了性别
                if (it.isPerfect == 1 && it.inviteCodePerfect != 0) {
                    getUserInfo(it.userId)
                }
            }, {
                if (it is ApiException) {
                    _loginState.value = LoginState.Fail(it.message, it.code)
                } else {
                    _loginState.value = LoginState.Fail(it.message)
                }
            })
        addDisposable(disposable)
    }


    /**
     * 获取阿里云一键登录 key
     */
    fun getAliPhoneKey() {
        val disposable = oauthRepository.getAliPhoneKey()
            .subscribe({
                _aliPhoneKey.value = it["key"]
            }, {

            })

        addDisposable(disposable)
    }

    /**
     * 一键登录 ||三方登录绑定手机号
     * @param token 阿里云key
     * @param type 登录类型 0==手机号 1==微信 2 ==QQ
     *
     */
    fun rapidLogin(token: String, type: Int, code: String? = null) {
        val disposable = oauthRepository.rapidLogin(token, type, code)
            .subscribe({
                _loginState.value = LoginState.Success(it)
                //如果绑定了性别
                if (it.isPerfect == 1 && it.inviteCodePerfect != 0) {
                    getUserInfo(it.userId)
                }
            }, {
                if (it is ApiException) {
                    _loginState.value = LoginState.Fail(it.message, it.code)
                } else {
                    _loginState.value = LoginState.Fail(it.message)
                }
            })
        addDisposable(disposable)
    }


    /**
     * 绑定手机号
     */
    fun bindPhone(type: Int, phoneNumber: String, captcha: String, code: String) {
        val disposable = oauthRepository.bindPhone(type, phoneNumber, captcha, code)
            .subscribe({
                _loginState.value = LoginState.Success(it)
                //如果绑定了性别
                if (it.isPerfect == 1 && it.inviteCodePerfect != 0) {
                    getUserInfo(it.userId)
                }
            }, {
                if (it is ApiException) {
                    _loginState.value = LoginState.Fail(it.message, it.code)
                } else {
                    _loginState.value = LoginState.Fail(it.message)
                }
            })
        addDisposable(disposable)
    }

    /**
     * 获取用户信息
     */
    public fun getUserInfo(userId: Long) {
        val disposable = userRepository.getUserInfo(userId)
            .subscribe({
                _profileState.value = ProfileState.Success(it)
                UserRepository.userInfo = it
            }, {
                _profileState.value = ProfileState.Fail
            })
        addDisposable(disposable)
    }


    /**
     * 获取CDN的key和随机数
     */
    fun getTencentCDNSecretKey() {
        val disposable = thridpartyRepository.getTencentCDNSecretKey()
            .subscribe({

                _thirdpartyApiState.value = ThirdpartyApiState.Success
                Log.e("zqr", "thirdpartyApiState请求成功：$it")
            }, {
                _thirdpartyApiState.value = ThirdpartyApiState.Fail(it.message)
                Log.e("zqr", "thirdpartyApiState请求失败：$it")
            })
        addDisposable(disposable)
    }

    fun loginIm(mContext: Context, userId: String) {

        LogUtils.e(">>>> userId = " + userId)
        UserInfo.getInstance().setUserId(userId)

//        val userSig: String = GenerateTestUserSig.genTestUserSig(userId)
//        UserInfo.getInstance().setUserSig(userSig)

        thridpartyRepository.getTencentUserSig()
            .subscribe({

                V2TIMManager.getInstance()
                    .login(userId, it.usersig, object : V2TIMCallback {
                        override fun onSuccess() {

                            hideLoading()
                            /**
                             * 注册推送
                             */
                            TIMAppService.getInstance().registerPushManually()
                            OauthRepository.cacheLoginResultBean()
                            //跳转首页
                            val intent = Intent(mContext, MainActivity::class.java)
                            mContext.startActivity(intent)
                            ActivityUtils.finishToActivity(MainActivity::class.java, false)

                        }

                        override fun onError(p0: Int, p1: String?) {
                            hideLoading()
                            LogUtils.e(">>> p0 = " + p0, "p1 = " + p1)
                            ToastUtils.showShort("[${p0}],登录失败[${p1}],请重试")
                        }

                    })
            }, {
                hideLoading()
                ToastUtils.showShort(it.message)
            })

    }


}