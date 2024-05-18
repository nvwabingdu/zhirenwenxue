package cn.dreamfruits.yaoguo.module.bindphone


import android.content.Intent
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import cn.dreamfruits.yaoguo.R
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.extention.isPhone
import cn.dreamfruits.yaoguo.module.bindgender.BindGenderActivity
import cn.dreamfruits.yaoguo.module.login.BindInviteCodeActivity
import cn.dreamfruits.yaoguo.module.main.mine.editprofile.ProfileState
import cn.dreamfruits.yaoguo.module.login.LoginViewModel
import cn.dreamfruits.yaoguo.module.login.state.LoginState
import cn.dreamfruits.yaoguo.module.login.state.SmsState
import cn.dreamfruits.yaoguo.module.main.MainActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 绑定手机号
 */
class BindPhoneActivity : BaseActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()

    private lateinit var mPhoneNumber: EditText
    private lateinit var mCaptcha: EditText
    private lateinit var mBindPhone: TextView
    private lateinit var mGetCaptcha: TextView
    private lateinit var mClose: ImageView

    private var mType: Int? = null
    private var mCode: String? = null
    private var isCanSendCaptcha: Boolean = false
    private var isSendCaptcha: Boolean = false

    //倒计时
    private val mTime = 61
    private var mDisposable: Disposable? = null


    override fun layoutResId() = R.layout.activity_bind_phone

    override fun initView() {
        mPhoneNumber = findViewById(R.id.et_phone_number)
        mCaptcha = findViewById(R.id.et_captcha)
        mGetCaptcha = findViewById(R.id.tv_get_captcha)
        mBindPhone = findViewById(R.id.tv_bind_phone)
        mClose = findViewById(R.id.iv_close)


//        BarUtils.transparentStatusBar(this)

        mGetCaptcha.setOnClickListener {
            val phoneNumber = mPhoneNumber.text.toString()
            if (!phoneNumber.isPhone()) {
                ToastUtils.showShort("手机号格式不正确")
                return@setOnClickListener
            }
            loginViewModel.sendSms(mPhoneNumber.text.toString())
            startCountDown()
        }

        mBindPhone.setOnClickListener {
            val phoneNumber = mPhoneNumber.text.toString()
            val captcha = mCaptcha.text.toString()
            if (!phoneNumber.isPhone()) {
                ToastUtils.showShort("手机号格式不正确")
                return@setOnClickListener
            }
            loginViewModel.bindPhone(mType!!, phoneNumber, captcha, mCode!!)
        }

        mCaptcha.addTextChangedListener {
            if (it!!.length == 6) {
                if (mPhoneNumber.length() < 11) {
                    return@addTextChangedListener
                }
                if (isSendCaptcha) {
                    mBindPhone.setBackgroundResource(R.drawable.bg_black)
                    mBindPhone.isEnabled = true
                }
            } else {
                mBindPhone.setBackgroundResource(R.drawable.bg_f6f6f6)
                mBindPhone.isEnabled = false
            }
        }
        mPhoneNumber.addTextChangedListener {
            if (it!!.length == 11) {
                if (!isCanSendCaptcha) {
                    mGetCaptcha.setTextColor(resources.getColor(R.color.black_222222))
                }
                if (mCaptcha.text.length < 6) {
                    return@addTextChangedListener
                }
                if (isSendCaptcha) {
                    mBindPhone.setBackgroundResource(R.drawable.bg_black)
                    mBindPhone.isEnabled = true
                }
            } else {
                mGetCaptcha.setTextColor(resources.getColor(R.color.gray_B3B3B3))
                mBindPhone.setBackgroundResource(R.drawable.bg_f6f6f6)
                mBindPhone.isEnabled = false
            }
        }

        mClose.setOnClickListener { finish() }
    }

    /**
     * 开启倒计时
     */
    private fun startCountDown() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(mTime.toLong())
            .map {
                mTime - (it + 1)
            }
            .doOnSubscribe {
                isCanSendCaptcha = true
                isSendCaptcha = true
                mGetCaptcha.isEnabled = false
                mGetCaptcha.setTextColor(resources.getColor(R.color.gray_AFADAD))
            }
            .subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(timer: Long) {
                    mGetCaptcha.text = "重新发送($timer)"
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {
                    isCanSendCaptcha = false
                    mGetCaptcha.isEnabled = true
                    mGetCaptcha.text = "获取验证码"
                    mGetCaptcha.setTextColor(resources.getColor(R.color.black_222222))
                }
            })

    }

    /*
     *重置倒计时
     */
    private fun resetTimer() {
        mDisposable?.dispose()
        mGetCaptcha.text = "获取短信验证码"
        mGetCaptcha.setTextColor(resources.getColor(R.color.black_222222))
        mGetCaptcha.isEnabled = true
    }

    @SuppressWarnings("all")
    override fun initData() {
        mType = intent.getIntExtra(RouterConstants.BindPhone.KEY_BIND_TYPE, -1)
        mCode = intent.getStringExtra(RouterConstants.BindPhone.KEY_BIND_CODE)

        loginViewModel.smsState.observe(this) { smsState ->
            when (smsState) {

                is SmsState.Success -> {

                }

                is SmsState.Fail -> {
                    resetTimer()
                    ToastUtils.showShort("发送验证码失败")
                }
            }
        }

        loginViewModel.loginState.observe(this) { loginState ->
            when (loginState) {
                is LoginState.Success -> {
                    if (loginState.loginResultBean?.inviteCodePerfect == 0) {
                        BindInviteCodeActivity.start(mContext,loginState.loginResultBean.isPerfect)
                        return@observe
                    }
                    if (loginState.loginResultBean?.isPerfect == 0) {
                        val intent = Intent(this, BindGenderActivity::class.java)
                        startActivity(intent)
                    }
                }

                is LoginState.Fail -> {
                    ToastUtils.showShort("${loginState.errorMsg}")
                }
            }
        }

        //个人资料
        loginViewModel.profileState.observe(this) { state ->
            when (state) {

                is ProfileState.Success -> {
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

    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable?.dispose()
    }

    override fun onCreateBefore() {
//        super.onCreateBefore()

    }

    override fun initStatus() {
//        super.initStatus()
    }

}