package cn.dreamfruits.yaoguo.module.login


import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.extention.isPhone
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo
import cn.dreamfruits.yaoguo.module.bindgender.BindGenderActivity
import cn.dreamfruits.yaoguo.module.login.config.AliyunAuthUIConfig
import cn.dreamfruits.yaoguo.module.login.state.LoginState
import cn.dreamfruits.yaoguo.module.login.state.SmsState
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.ProfileState
import cn.dreamfruits.yaoguo.net.HttpResultCode
import cn.dreamfruits.yaoguo.util.SizeUtil
import cn.dreamfruits.yaoguo.view.bubble.BubbleView
import com.blankj.utilcode.util.JsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.mobile.auth.gatewayauth.*
import com.mobile.auth.gatewayauth.model.TokenRet
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONObject
import org.koin.android.ext.android.get
import java.util.concurrent.TimeUnit


/**
 * 登录页面
 */
class LoginActivity : BaseActivity() {


    private lateinit var mPhoneNumber: EditText
    private lateinit var mTvGetCaptcha: TextView
    private lateinit var mPhone: LinearLayout
    private lateinit var mCaptchaEditText: EditText
    private lateinit var mAgreePrivacy: ImageView
    private lateinit var mPrivacy: TextView
    private val mCountTime = 60
    private lateinit var mBubbleView: BubbleView

    private var mDisposable: Disposable? = null

    private var phoneNumber: String? = null

    private lateinit var mWeChatIv: ImageView
    private lateinit var mQQIv: ImageView
    private var isCanSendCaptcha: Boolean = false


    //一键登录
    private var mAuthHelper: PhoneNumberAuthHelper? = null
    private var mCheckListener: TokenResultListener? = null
    private var mOneKeyLoginBubbleView: BubbleView? = null


    private val loginViewModel by viewModels<LoginViewModel>()


    override fun initStatus() {
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(true)
        }
    }


    override fun layoutResId(): Int = R.layout.activity_login

    override fun initView() {

        mPhoneNumber = findViewById(R.id.et_phone_number)
        mPhone = findViewById(R.id.ll_phone)
        mCaptchaEditText = findViewById(R.id.et_security_code)
        mAgreePrivacy = findViewById(R.id.iv_agree_privacy)
        mPrivacy = findViewById(R.id.tv_privacy)
        mBubbleView = findViewById(R.id.bubble_view)
        mWeChatIv = findViewById(R.id.iv_wechat)
        mQQIv = findViewById(R.id.iv_qq)
        mTvGetCaptcha = findViewById(R.id.tv_get_captcha)
        mCaptchaEditText.isEnabled = false
        mPhoneNumber.addTextChangedListener(onTextChanged = { text, _, _, _ ->

            if (text!!.length == 11) {
                mTvGetCaptcha.setTextColor(Color.BLACK)
            } else {
                mTvGetCaptcha.setTextColor(Color.parseColor("#CFCDCE"))
            }
        })
        mCaptchaEditText.addTextChangedListener(afterTextChanged = { text ->
            if (mCaptchaEditText.text.length == 6 && mPhoneNumber.text.length == 11) {
                if (!loginViewModel.confirmPrivacy) {
                    startAnim()
                    KeyboardUtils.hideSoftInput(this)
                    return@addTextChangedListener
                }

                if (phoneNumber == null) {
                    ToastUtils.showShort("请获取验证码")
                    return@addTextChangedListener
                }

                KeyboardUtils.hideSoftInput(this)
                loginViewModel.smsLogin(phoneNumber!!, mCaptchaEditText.text.toString())
            }
        })

        mAgreePrivacy.setOnClickListener {
            if (loginViewModel.confirmPrivacy) {
                mAgreePrivacy.setBackgroundResource(R.drawable.ic_no_select)
                loginViewModel.confirmPrivacy = false
            } else {
                mAgreePrivacy.setBackgroundResource(R.drawable.ic_selected)
                loginViewModel.confirmPrivacy = true
                mBubbleView.visibility = View.GONE
            }
        }

        mTvGetCaptcha.setOnClickListener {

            if (mPhoneNumber.text.toString().isEmpty()) {
                ToastUtils.showShort("请输入手机号码")
                return@setOnClickListener
            }
            if (!loginViewModel.confirmPrivacy) {
                startAnim()
                KeyboardUtils.hideSoftInput(this)
                return@setOnClickListener
            }


            if (mPhoneNumber.text.toString().isPhone()) {
                phoneNumber = mPhoneNumber.text.toString()

                loginViewModel.sendSms(phoneNumber!!)
                startCountDown()
            } else {
                ToastUtils.showShort("手机号格式错误")
            }
        }

        mWeChatIv.setOnClickListener {

            if (!loginViewModel.confirmPrivacy) {
                startAnim()
                return@setOnClickListener
            }
            startThirdPartyLogin(1, 111)
        }

        mQQIv.setOnClickListener {
            if (!loginViewModel.confirmPrivacy) {
                startAnim()
                return@setOnClickListener
            }
            startThirdPartyLogin(2, 111)
        }

        bottomUserAgreement()
    }

    /**
     * 跳转到三方登录页面
     * @param type 微信 =1 qq =2
     * @param infoType 普通登录 = 111 一键登录 = 112
     */
    private fun startThirdPartyLogin(type: Int, infoType: Int) {
        val intent = Intent(this, ThirdPartyLoginActivity::class.java)
        intent.putExtra(RouterConstants.ThirdPartyLogin.KEY_PLATFORM_TYPE, type)
        intent.putExtra(RouterConstants.ThirdPartyLogin.KEY_INTO_TYPE, infoType)
        startActivity(intent)
    }


    /**
     * 播放抖动动画
     */
    private fun startAnim() {
        if (mBubbleView.visibility == View.GONE) {
            mBubbleView.visibility = View.VISIBLE
        }
        val translateAnimation: Animation = TranslateAnimation(0f, 10f, 0f, 10f)
        translateAnimation.interpolator = CycleInterpolator(3f)
        translateAnimation.duration = 500
        mBubbleView.startAnimation(translateAnimation)
    }


    /**
     * 开始倒计时
     */
    private fun startCountDown() {

        Observable.interval(0, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(mCountTime.toLong())
            .map { mCountTime - (it + 1) }
            .doOnSubscribe {
                isCanSendCaptcha = true
                mPhoneNumber.clearFocus()
                mTvGetCaptcha.setTextColor(Color.parseColor("#CFCDCE"))
                if (!KeyboardUtils.isSoftInputVisible(this)) {
                    KeyboardUtils.showSoftInput()
                }
                mCaptchaEditText.isEnabled = true
                mCaptchaEditText.requestFocus()
                mTvGetCaptcha.isEnabled = false
            }
            .subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(t: Long) {
                    mTvGetCaptcha.text = "重新发送($t)"

                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {
                    mTvGetCaptcha.isEnabled = true
                    mTvGetCaptcha.text = "获取验证码"
                    isCanSendCaptcha = false
                    if (mPhoneNumber.text.length == 11)
                        mTvGetCaptcha.setTextColor(Color.BLACK)
                    else mTvGetCaptcha.setTextColor(Color.parseColor("#CFCDCE"))
                }
            })

    }

    @SuppressWarnings("all")
    override fun initData() {

        loginViewModel.smsState.observe(this) {
            when (it) {
                is SmsState.Success -> {

                }

                is SmsState.Fail -> {
                    resetTimer()
                    Toast.makeText(this, "${it.errorMsg}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginViewModel.loginState.observe(this) { loginState ->
            when (loginState) {
                is LoginState.Success -> {
                    if (loginState.loginResultBean?.inviteCodePerfect == 0) {
                        BindInviteCodeActivity.start(mContext, loginState.loginResultBean.isPerfect)
                        return@observe
                    }
                    if (loginState.loginResultBean?.isPerfect == 0) {
                        val intent = Intent(this, BindGenderActivity::class.java)
                        startActivity(intent)
                    }
                }

                is LoginState.Fail -> {

                    if (loginState.errorCode == HttpResultCode.NEED_BIND_GENDER) {

                        val userId = JsonUtils.getLong(loginState.errorMsg, "userId")

                        val intent = Intent(this, BindGenderActivity::class.java)
                        intent.putExtra(RouterConstants.BindGender.KEY_USER_ID, userId)
                        startActivity(intent)

                    } else {
                        ToastUtils.showShort(loginState.errorMsg)
                    }
                }
            }
        }

        //用户信息
        loginViewModel.profileState.observe(this) { state ->
            when (state) {

                is ProfileState.Success -> {
                    loginViewModel.getTencentCDNSecretKey()//wq 请求腾讯sdn
//                    //跳转首页
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    ActivityUtils.finishToActivity(MainActivity::class.java, false)

                    loginViewModel.loginIm(this, state.data!!.userId.toString())

                }

                is ProfileState.Fail -> {
                    ToastUtils.showShort("获取用户信息失败")
                }
            }
        }


        loginViewModel.aliPhoneKey.observe(this) { aliKey ->
            sdkInit(aliKey)
        }

        loginViewModel.getAliPhoneKey()
    }

    /**
     * 重置计时器
     *
     */
    private fun resetTimer() {
        mDisposable?.dispose()
//        mLogin.text = "获取短信验证码"
//        mLogin.setBackgroundResource(R.drawable.bg_black)
//        mLogin.isEnabled = true

    }

    /**
     * 底部隐私协议
     */
    private fun bottomUserAgreement() {

        val spanned = SpannableStringBuilder()
        spanned.append(getString(R.string.user_agreement))

        val usageClick = object : ClickableSpan() {

            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.parseColor("#0066FF")
            }

            override fun onClick(widget: View) {
                ToastUtils.showShort("用户协议")
            }
        }

        val privacyClick = object : ClickableSpan() {

            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.parseColor("#0066FF")

            }

            override fun onClick(widget: View) {
                ToastUtils.showShort("隐私条款")
            }
        }

        spanned.setSpan(usageClick, 11, 17, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        spanned.setSpan(
            privacyClick,
            17,
            getString(R.string.user_agreement).length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        mPrivacy.apply {
            text = spanned
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }


    }

    /**
     * 初始化一键登录sdk 并拉起一键登录页面
     */
    private fun sdkInit(aliKey: String) {
        mCheckListener = object : TokenResultListener {

            override fun onTokenSuccess(p0: String?) {
                Log.i("TAG111", "onTokenSuccess: $p0")
                val tokenRet: TokenRet
                try {
                    Log.i("TAG111", "CODE_SUCCESS:")
                    tokenRet = TokenRet.fromJson(p0)
                    if (ResultCode.CODE_ERROR_ENV_CHECK_SUCCESS == tokenRet.code) {

                    }
                    //获取login token 成功
                    if (ResultCode.CODE_SUCCESS == tokenRet.code) {
                        if (ThirdPartyInfo.type != null) {
                            loginViewModel.rapidLogin(
                                tokenRet.token,
                                ThirdPartyInfo.type!!,
                                ThirdPartyInfo.code
                            )
                        } else {
                            loginViewModel.rapidLogin(tokenRet.token, 0)
                        }

                        mAuthHelper?.setAuthListener(null)
                        mAuthHelper?.quitLoginPage()
                    }

                } catch (e: Exception) {

                }
            }

            override fun onTokenFailed(p0: String?) {
                mAuthHelper?.quitLoginPage()
                mAuthHelper?.setAuthListener(null)
            }
        }
        mAuthHelper =
            PhoneNumberAuthHelper.getInstance(get<Context>(), mCheckListener).apply {
                setAuthSDKInfo(aliKey)
                reporter.setLoggerEnable(true)
                checkEnvAvailable(PhoneNumberAuthHelper.SERVICE_TYPE_LOGIN)
            }

        configAuthPage()

        mAuthHelper!!.getLoginToken(get(), 5000)

    }


    /**
     * 配置一键登录页面
     */
    private fun configAuthPage() {

        mAuthHelper!!.apply {

            setUIClickListener { code, context, jsonString ->

                val jsonObj = try {
                    JSONObject(jsonString)
                } catch (e: Exception) {
                    JSONObject()
                }
                Log.i("TAG111", "configAuthPage: $code $jsonString")
                when (code) {
                    //用户选择其他登录方式
                    ResultCode.CODE_ERROR_USER_SWITCH -> {

                    }
                    //点击一键登录按钮会发出此回调
                    ResultCode.CODE_ERROR_USER_LOGIN_BTN -> {
                        if (!jsonObj.optBoolean("isChecked")) {
                            mOneKeyLoginBubbleView?.visibility = View.VISIBLE
                        }
                    }
                    //checkbox状态改变触发此回调
                    ResultCode.CODE_ERROR_USER_CHECKBOX -> {
                        if (jsonObj.optBoolean("isChecked")) {
                            loginViewModel.onKeyLoginPrivacy = true
                            mOneKeyLoginBubbleView?.visibility = View.GONE
                        } else {
                            loginViewModel.onKeyLoginPrivacy = false
                        }
                    }
                    //物理返回键
                    ResultCode.CODE_ERROR_USER_CONTROL_CANCEL_BYKEY -> {
                        quitLoginPage()
                        this@LoginActivity.finish()
                    }

                }
            }

            removeAuthRegisterXmlConfig()
            removeAuthRegisterViewConfig()
            //登录页配置自定义icon布局
            addAuthRegisterXmlConfig(
                AuthRegisterXmlConfig.Builder()
                    .setLayout(R.layout.switch_icon, object : AbstractPnsViewDelegate() {
                        override fun onViewCreated(p0: View?) {
                            findViewById(R.id.iv_wechat).setOnClickListener {
                                if (!loginViewModel.onKeyLoginPrivacy) {

                                    mOneKeyLoginBubbleView?.visibility = View.VISIBLE
                                    return@setOnClickListener
                                }
                                startThirdPartyLogin(1, 112)
                                //
                            }
                            findViewById(R.id.iv_weibo).setOnClickListener {
                                //
                            }
                            findViewById(R.id.iv_qq).setOnClickListener {
                                if (!loginViewModel.onKeyLoginPrivacy) {

                                    mOneKeyLoginBubbleView?.visibility = View.VISIBLE
                                    return@setOnClickListener
                                }
                                startThirdPartyLogin(2, 112)
                                //
                            }

                            val iconContainer = findViewById(R.id.container_bottom)
                            val layoutParams =
                                iconContainer.layoutParams as RelativeLayout.LayoutParams
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                            layoutParams.bottomMargin = SizeUtil.dp2px(81f)

                        }
                    }).build()
            )
            //一键登录页自定义配置 同意隐私气泡弹窗
            addAuthRegisterXmlConfig(
                AuthRegisterXmlConfig.Builder()
                    .setLayout(R.layout.layout_bubble, object : AbstractPnsViewDelegate() {
                        override fun onViewCreated(p0: View?) {

                            mOneKeyLoginBubbleView = findViewById(R.id.bubble_view) as BubbleView
                            val layoutParams =
                                mOneKeyLoginBubbleView!!.layoutParams as RelativeLayout.LayoutParams
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                            layoutParams.bottomMargin = SizeUtil.dp2px(70f)
                            layoutParams.leftMargin = SizeUtil.dp2px(82f)
                        }
                    }).build()
            )

            var authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            if (Build.VERSION.SDK_INT == 26) {
                authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND
            }

            setAuthUIConfig(AliyunAuthUIConfig.getUIConfig(authPageOrientation))

            //用户控制返回键及左上角返回按钮效果
            userControlAuthPageCancel()
        }
    }


    /**复写back键*/
    override fun onBackPressed() {
        //禁用返回键
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable?.dispose()
        mOneKeyLoginBubbleView = null
    }


}