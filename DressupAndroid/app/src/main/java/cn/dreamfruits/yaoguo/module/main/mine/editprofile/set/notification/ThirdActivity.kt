package cn.dreamfruits.yaoguo.module.main.mine.editprofile.set.notification

import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import cn.dreamfruits.sociallib.Social
import cn.dreamfruits.sociallib.config.PlatformType.*
import cn.dreamfruits.sociallib.utils.CallbackDataUtil
import cn.dreamfruits.yaoguo.base.BaseActivity
import cn.dreamfruits.yaoguo.constants.RouterConstants
import cn.dreamfruits.yaoguo.global.ThirdPartyInfo
import cn.dreamfruits.yaoguo.module.main.home.state.BindThirdPartyState
import cn.dreamfruits.yaoguo.module.main.mine.MineViewModel
import cn.dreamfruits.yaoguo.util.Singleton
import com.blankj.utilcode.util.ToastUtils

/**
 * 三方登录
 * 透明主题activity
 */
class ThirdActivity : BaseActivity() {
    private val mineViewModel by viewModels<MineViewModel>()

    /**
     * 平台 微信 = 1 QQ =2
     */
    private var mType = -1

    /**
     * 从什么页面进入的
     * 普通登录= 111  一键登录 =112
     */
    private var mIntoType = -1

    //QQopenId
    private var mQQOpenId = ""

    override fun layoutResId(): Int = 0


    override fun initView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun initData() {
        mType = intent.getIntExtra(RouterConstants.ThirdPartyLogin.KEY_PLATFORM_TYPE, -1)
        mIntoType = intent.getIntExtra(RouterConstants.ThirdPartyLogin.KEY_INTO_TYPE, -1)


//        loginViewModel.loginState.observe(this) { loginState ->
//
//            when (loginState) {
//                is LoginState.Success -> {
//
//                    if (loginState.loginResultBean?.isPerfect ==0){
//                        val intent = Intent(this,BindGenderActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//                is LoginState.Fail -> {
//                    //需要绑定手机号
//                    if (loginState.errorCode == HttpResultCode.NEED_BIND_PHONE) {
//
//                        if (mType == 1){
//                            val unionid = JsonUtils.getString(loginState.errorMsg,"unionid")
//                            ThirdPartyInfo.type = 1
//                            ThirdPartyInfo.code = unionid
//
//                        }else if (mType == 2){
//                            ThirdPartyInfo.type = 2
//                            ThirdPartyInfo.code = mQQOpenId
//                        }
//
//                        //如果不是一键绑定
//                        if (mIntoType == 111){
//                            val intent = Intent(this, BindPhoneActivity::class.java)
//                            intent.putExtra(RouterConstants.BindPhone.KEY_BIND_TYPE, ThirdPartyInfo.type)
//                            intent.putExtra(RouterConstants.BindPhone.KEY_BIND_CODE, ThirdPartyInfo.code)
//                            startActivity(intent)
//                            this.finish()
//                        }else{
//                            //如果是一键绑定
//                            val activityList =
//                                ActivityUtils.getActivityList().filterIsInstance<LoginAuthActivity>()
//                            val loginAuthActivity = activityList.first()
//
//                            val clazz = loginAuthActivity.javaClass
//                            val field = clazz.getDeclaredField("mNumberPhone")
//                            val field2 = clazz.getDeclaredField("mSlogan")
//                            val filed3 = clazz.getDeclaredField("mLoginRL")
//                            field.isAccessible = true
//                            field2.isAccessible = true
//                            filed3.isAccessible = true
//
//                            OneKeyBindPhoneDialog.Builder(this)
//                                .setPhoneNumber(field[loginAuthActivity] as String)
//                                .setSlogan(field2[loginAuthActivity] as String)
//                                .setOnBindPhoneClick {
//                                    (filed3[loginAuthActivity] as RelativeLayout).performClick()
//                                    finish()
//                                }
//                                .setOnBindOtherPhoneListener{
//
//                                    val intent = Intent(this, BindPhoneActivity::class.java)
//                                    intent.putExtra(RouterConstants.BindPhone.KEY_BIND_TYPE, ThirdPartyInfo.type)
//                                    intent.putExtra(RouterConstants.BindPhone.KEY_BIND_CODE, ThirdPartyInfo.code)
//                                    startActivity(intent)
//                                    finish()
//                                }
//                                .setOnCloseListener{
//                                    finish()
//                                }
//                                .build()
//                                .show()
//
//                        }
//                    }else{
//                        finish()
//                    }
//                }
//            }
//        }

//        //个人资料
//        loginViewModel.profileState.observe(this){state ->
//            when(state){
//
//                is ProfileState.Success ->{
//                    //跳转首页
//                    val intent = Intent(this,MainActivity::class.java)
//                    startActivity(intent)
//                    ActivityUtils.finishToActivity(MainActivity::class.java,false)
//                }
//
//                is ProfileState.Fail ->{
//                    ToastUtils.showShort("获取用户信息失败")
//                }
//            }
//        }


        Social.auth(this,
            if (mType == 1) WEIXIN else QQ,
            onSuccess = { type, data ->
                data?.let {

                    val id = CallbackDataUtil.getID(it, "")
                    val token = CallbackDataUtil.getAccessToken(it, "")

                    when (type) {
                        WEIXIN -> {
                            Log.e("ThirdActivity", "微信登录成功" + id+ "token" + token)
                            mineViewModel.bindThirdParty(
                                1,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
                                id,//微信登录的code，qq登录的token
                                null
                            )

                            mineViewModel.bindThirdPartyState.observe(this) {
                                when (it) {
                                    is BindThirdPartyState.Success -> {
                                        if (mineViewModel.mBindThreeAccountBean!!.state==1){
                                            Singleton.centerToast(this,"绑定成功")
                                        }else{
                                            Singleton.centerToast(this,"绑定失败")
                                        }
                                        finish()
                                    }

                                    is BindThirdPartyState.Fail -> {
                                        //1.是否有断网提示
                                        Singleton.isNetConnectedToast(this)
                                        finish()
                                    }
                                }
                            }
                        }

                        QQ -> {
                            Log.e("ThirdActivity", "QQ登录成功" + id + "token" + token)
                            mineViewModel.bindThirdParty(
                                2,//第三方类型 1:微信 2:QQ 3:苹果 4:微博
                                token,//微信登录的code，qq登录的token
                                id
                            )

                            mineViewModel.bindThirdPartyState.observe(this) {
                                when (it) {
                                    is BindThirdPartyState.Success -> {
                                        if (mineViewModel.mBindThreeAccountBean!!.state==1){
                                            Singleton.centerToast(this,"绑定成功")
                                        }else{
                                            Singleton.centerToast(this,"绑定失败")
                                        }
                                        finish()
                                    }

                                    is BindThirdPartyState.Fail -> {
                                        //1.是否有断网提示
                                        Singleton.isNetConnectedToast(this)
                                        finish()
                                    }
                                }
                            }
                        }

                        WEIXIN_CIRCLE -> TODO()
                        QQ_ZONE -> TODO()
                        SINA_WEIBO -> TODO()
                        ALI -> TODO()
                    }
                }
            },
            onCancel = {

                ThirdPartyInfo.type = null
                ThirdPartyInfo.code = null
                ToastUtils.showShort("用户取消")

                this.finish()
            },
            onError = { _, _, msg ->
                ThirdPartyInfo.type = null
                ThirdPartyInfo.code = null
                ToastUtils.showShort("$msg")

                this.finish()
            }
        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Social.onActivityResult(requestCode, resultCode, data)
    }

}