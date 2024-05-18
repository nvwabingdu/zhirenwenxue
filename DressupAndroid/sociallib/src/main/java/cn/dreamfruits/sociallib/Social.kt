package cn.dreamfruits.sociallib

import android.content.Context
import android.content.Intent
import android.util.Log
import cn.dreamfruits.sociallib.callback.AuthCallback
import cn.dreamfruits.sociallib.callback.ShareCallback
import cn.dreamfruits.sociallib.config.OperationType
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.OperationBean
import cn.dreamfruits.sociallib.entiey.content.*
import cn.dreamfruits.sociallib.entiey.platform.PlatformConfig

/**
 * 入口类
 */
object Social {

    private const val TAG = "Social"
    private const val FAST_CLICK_DELAY_TIME = 3000


    /**
     * 上次处理的时间
     */
    private var mLastDuration = 0L


    /**
     * 初始化各个平台的配置
     * 判断当前配置的平台 哪些是否是可用状态
     * @param configs 平台配置
     */
    fun init(context: Context, vararg configs: PlatformConfig) {
        for (config in configs) {
            if (!config.appkey.isNullOrEmpty()) {
                if (!PlatformManager.initPlat(context, config)) {
                    Log.e(TAG, "${config.name} 初始化失败")
                }
            }
        }
    }

    /**
     * 判断平台是否可用
     * @param type 平台类型
     * @return true 为安装了， false 为未安装
     */
    fun available4Plat(type: PlatformType): Boolean {
        return PlatformManager.availablePlatMap[type] != null
    }


    /**
     * 授权
     * @param context 上下文
     * @param type 授权平台
     * @param aliAuthToken 支付宝授权信息
     * @param onSuccess 成功回调
     * @param onError 失败回调
     * @param onCancel 取消回调
     */
    fun auth(
        context: Context,
        type: PlatformType,
        aliAuthToken: String? = null,
        onSuccess: ((type: PlatformType, data: Map<String, String?>?) -> Unit)? = null,
        onError: ((type: PlatformType, errorCode: Int, errorMsg: String?) -> Unit)? = null,
        onCancel: ((type: PlatformType) -> Unit)? = null,
    ) {
        val callback = AuthCallback(
            onSuccess = onSuccess,
            onErrors = onError,
            onCancel = onCancel
        )
        var content = AuthContent()
        aliAuthToken?.let {
            content = AliAuthContent(it)
        }
        val bean = OperationBean(context, type, OperationType.AUTH, callback, content)
        performOperation(bean)
    }

    fun share(
        context: Context,
        type: PlatformType,
        content: ShareContent,
        onSuccess: ((type: PlatformType) -> Unit)? = null,
        onError: ((type: PlatformType, errorCode: Int, errorMsg: String?) -> Unit)? = null,
        onCancel: ((type: PlatformType) -> Unit)? = null,
    ) {
        val callback = ShareCallback(
            onSuccess = onSuccess,
            onErrors = onError,
            onCancel = onCancel
        )
        Log.e(">>>", "share type = " + type)
        val bean = OperationBean(context, type, OperationType.SHARE, callback, content)
        performOperation(bean)
    }


    private fun performOperation(bean: OperationBean) {
        // 两次处理的时间间隔不能小于3s
        if (System.currentTimeMillis() - mLastDuration < FAST_CLICK_DELAY_TIME) {
            Log.d(
                "AimySocial",
                "$TAG 重复处理 上次处理时间$mLastDuration 本次处理时间 ${System.currentTimeMillis()}"
            )
            return
        }
        mLastDuration = System.currentTimeMillis()
        OperationManager.instance.perform(bean)
    }


    /**
     * 授权回调
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val content: OperationContent
        //
        if (requestCode == 1) {
            content = NewIntentContent(data)
        } else {
            content = ActivityResultContent(requestCode, resultCode, data)
        }
        PlatformManager.currentHandler?.let {
            Log.d("sociallib1", PlatformManager.currentHandler.toString())
            OperationManager.instance.performActivityResult(it, content)
        }
    }
}