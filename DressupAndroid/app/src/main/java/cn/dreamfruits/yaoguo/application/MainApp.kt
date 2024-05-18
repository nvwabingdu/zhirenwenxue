package cn.dreamfruits.yaoguo.application


import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDex
import cn.dreamfruits.baselib.BaseApplication
import cn.dreamfruits.yaoguo.module.main.home.refresh.MRefreshHeader2
import cn.dreamfruits.yaoguo.view.swipeback.BGASwipeBackHelper
import com.blankj.utilcode.util.LogUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.imsdk.v2.*


class MainApp : BaseApplication() {


    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        CrashReport.initCrashReport(getApplicationContext(), "950aaaf3ed", true);

        // 2. 初始化 config 对象。
        val config = V2TIMSDKConfig()
// 3. 指定 log 输出级别。
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO)
        config.logListener = object : V2TIMLogListener() {
            override fun onLog(logLevel: Int, logContent: String) {
                // logContent 为 SDK 日志内容
                LogUtils.e("im_content", logContent)
            }
        }

// 4. 添加 V2TIMSDKListener 的事件监听器，sdkListener 是 V2TIMSDKListener 的实现类，如果您不需要监听 IM SDK 的事件，这个步骤可以忽略。
        V2TIMManager.getInstance().addIMSDKListener(object : V2TIMSDKListener() {

            override fun onUserSigExpired() {
                super.onUserSigExpired()
                LogUtils.e("im  >>> onUserSigExpired")
            }

            override fun onKickedOffline() {
                super.onKickedOffline()
                LogUtils.e("im  >>> onKickedOffline")
            }

            override fun onConnecting() {
                super.onConnecting()
                LogUtils.e("im  >>> onConnecting")
            }

            override fun onConnectFailed(code: Int, error: String?) {
                super.onConnectFailed(code, error)
                LogUtils.e("im  >>> onConnectFailed", "code = $code", "error = $error")
            }

            override fun onConnectSuccess() {
                super.onConnectSuccess()
                LogUtils.e("im  >>> onConnectSuccess")
            }

            override fun onSelfInfoUpdated(info: V2TIMUserFullInfo?) {
                super.onSelfInfoUpdated(info)
                LogUtils.e("im  >>> info = " + info.toString())
            }

            override fun onUserStatusChanged(userStatusList: MutableList<V2TIMUserStatus>?) {
                super.onUserStatusChanged(userStatusList)

                LogUtils.e("im  >>> onUserStatusChanged = " + userStatusList.toString())

            }

        })
        // 5. 初始化 IM SDK，调用这个接口后，可以立即调用登录接口。
        V2TIMManager.getInstance().initSDK(this, TENCENT_IM_APP_ID, config)

        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null)

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null
        var TENCENT_IM_APP_ID = 1400715776

        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { con, layout ->
                val materialHeader = MRefreshHeader2(mContext)
                materialHeader
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { con, layout ->
                val materialFooter = ClassicsFooter(mContext)
                materialFooter
            }
        }
    }

}