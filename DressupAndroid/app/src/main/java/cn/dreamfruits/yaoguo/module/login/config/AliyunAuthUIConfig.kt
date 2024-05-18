package cn.dreamfruits.yaoguo.module.login.config

import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.mobile.auth.gatewayauth.AuthUIConfig

/**
 * 一键登录UI配置
 * https://help.aliyun.com/document_detail/144231.html
 */
class AliyunAuthUIConfig {

    companion object {


        fun getUIConfig(orientation:Int):AuthUIConfig {

            return AuthUIConfig.Builder()
                //配置底部隐私协议
                .setAppPrivacyOne("《用户协议》", "https://www.baidu.com")
                .setAppPrivacyTwo("《隐私条款》", "https://www.baidu.com")
                .setAppPrivacyColor(Color.GRAY, Color.parseColor("#0066FF"))
                .setPrivacyConectTexts(arrayOf("\n和", " "))
                .setPrivacyOffsetY_B(32)
                .setPrivacyTextSize(12)
                .setProtocolGravity(Gravity.START)
                .setCheckedImgPath("ic_selected")
                .setUncheckedImgPath("ic_no_select")
                .setCheckBoxHeight(24)
                .setCheckBoxWidth(24)

                //隐藏默认Toast
                .setLogBtnToastHidden(true)

                //配置状态栏
                .setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                .setLightColor(true)

                //隐藏导航栏
                .setNavHidden(true)
                .setBottomNavColor(Color.TRANSPARENT)

                //设置logo
                .setLogoImgPath("bg1_login")
                .setLogoWidth(SizeUtils.px2dp(415f))
                .setLogoHeight(SizeUtils.px2dp(415f))
                .setLogoOffsetY(SizeUtils.px2dp(120f))

                //设置号码样式
                .setNumFieldOffsetY(350)
                .setNumberSizeDp(26)

                //设置slogan  中国移动提供认证服务
                .setSloganOffsetY(386)
                .setSloganTextSizeDp(12)

                //设置登录按钮样式
                .setLogBtnTextColor(Color.WHITE)
                .setLogBtnOffsetY(442)
                .setLogBtnTextSizeDp(18)
                .setLogBtnHeight(44)
                .setLogBtnWidth(295)
                .setLogBtnBackgroundPath("bg_black")

                //设置其他手机号码登录
                .setSwitchOffsetY(506)
                .setSwitchAccTextSizeDp(14)
                .setSwitchAccTextColor(Color.BLACK)
                .setSwitchAccText("其他手机号登录")
                .setHiddenLoading(true)

                //设置页面背景
                .setPageBackgroundPath("bg_login")
                .setScreenOrientation(orientation)
                .create()
        }

    }

}