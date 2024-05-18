package cn.dreamfruits.sociallib.handler.qq

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import cn.dreamfruits.sociallib.PlatformManager
import cn.dreamfruits.sociallib.callback.AuthCallback
import cn.dreamfruits.sociallib.callback.OperationCallback
import cn.dreamfruits.sociallib.callback.ShareCallback
import cn.dreamfruits.sociallib.config.OperationType
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.config.SocialConstants
import cn.dreamfruits.sociallib.entiey.content.*
import cn.dreamfruits.sociallib.entiey.platform.PlatformConfig
import cn.dreamfruits.sociallib.handler.SSOHandler
import cn.dreamfruits.sociallib.utils.AppUtils
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QQShare
import com.tencent.tauth.DefaultUiListener
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject


/**
 * QQ处理
 */
class QQHandler(context: Context, config: PlatformConfig) : SSOHandler() {

    companion object {
        private const val TAG = "QQHandler"
        private val opList = listOf(
            OperationType.AUTH,
            OperationType.SHARE
        )
    }

    private var mContext: Context = context

    private val mTencent: Tencent by lazy {
        Tencent.setIsPermissionGranted(true)
        Tencent.createInstance(config.appkey, context.applicationContext)
    }

    private lateinit var mAuthCallback: AuthCallback
    private lateinit var mShareCallback: ShareCallback


    override val isInstalled: Boolean
        get() {
            return (AppUtils.isAppInstalled("com.tencent.mobileqq", mContext) // qq手机
                    || AppUtils.isAppInstalled("com.tencent.qqlite", mContext)//
                    || AppUtils.isAppInstalled("com.tencent.tim", mContext))     // tim
        }


    /**
     * 登录监听回调
     */
    private val authListener = object : IUiListener {

        override fun onComplete(callbackMap: Any?) {
            if (null == callbackMap) {
                Log.e(TAG, "onSuccess but response=null")
                mAuthCallback.onErrors
                    ?.invoke(
                        PlatformType.QQ,
                        SocialConstants.AUTH_ERROR,
                        "$TAG :授权回调的map为null"
                    )
                return
            }
            if (callbackMap is JSONObject) {
                initOpenidAndToken(callbackMap)
                mAuthCallback.onSuccess?.invoke(PlatformType.QQ, jsonToMap(callbackMap))
            } else {
                mAuthCallback.onErrors
                    ?.invoke(
                        PlatformType.QQ,
                        SocialConstants.AUTH_ERROR,
                        "$TAG : 授权回调的数据转换为map失败"
                    )
            }

            release()
        }

        override fun onError(uiError: UiError) {
            mAuthCallback.onErrors
                ?.invoke(
                    PlatformType.QQ,
                    SocialConstants.AUTH_ERROR,
                    "$TAG :授权失败${uiError.errorCode},${uiError.errorMessage},${uiError.errorDetail}"
                )
            release()
        }

        override fun onCancel() {
            mAuthCallback.onCancel?.invoke(PlatformType.QQ)
            release()
        }

        override fun onWarning(p0: Int) {
            Log.i(TAG, "onWarning: ")
        }
    }


    override fun authorize(type: PlatformType, callback: OperationCallback, content: AuthContent?) {
        if (callback !is AuthCallback) {
            mAuthCallback.onErrors
                ?.invoke(
                    PlatformType.QQ,
                    SocialConstants.CALLBACK_CLASSTYPE_ERROR,
                    "$TAG: callback 类型错误"
                )
            return
        }
        mAuthCallback = callback

        // parameter as activity or fragment can be access only for mTencent's login()
        if (mContext !is Activity) {
            mAuthCallback.onErrors
                ?.invoke(
                    PlatformType.QQ,
                    SocialConstants.AUTH_ERROR,
                    "$TAG: context 不是activity或者fragment"
                )
            return
        }

        PlatformManager.currentHandler = this
        val activity: Activity = mContext as Activity
        mTencent.login(activity, "all", authListener)
    }


    override fun share(type: PlatformType, content: ShareContent, callback: OperationCallback) {

        if (callback !is ShareCallback) {
            mAuthCallback.onErrors
                ?.invoke(
                    PlatformType.QQ,
                    SocialConstants.CALLBACK_CLASSTYPE_ERROR,
                    "$TAG: callback 类型错误"
                )
            return
        }
        mShareCallback = callback

        if (content is ShareWebContent) {
            val params = Bundle()
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
            params.putString(QQShare.SHARE_TO_QQ_TITLE, content.title)
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content.description)
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, content.webPageUrl)




            params.putString(
                QQShare.SHARE_TO_QQ_IMAGE_URL,
                content.url
            )
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "微酸")
            mTencent.shareToQQ(mContext as Activity?, params, qqShareListener)

        } else if (content is ShareImageContent) {
            val params = Bundle()
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
            params.putString(
                QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,
                content.description
            )
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "微酸")
            mTencent.shareToQQ(mContext as Activity?, params, qqShareListener)
        }

    }

    var qqShareListener: IUiListener = object : DefaultUiListener() {
        override fun onCancel() {
            mShareCallback.onCancel?.invoke(PlatformType.QQ)
        }

        override fun onComplete(response: Any) {
            mShareCallback.onSuccess?.invoke(PlatformType.QQ)
        }

        override fun onError(e: UiError) {
            mShareCallback.onErrors?.invoke(PlatformType.QQ, e.errorCode, e.errorMessage)
        }

        override fun onWarning(code: Int) {
            if (code == Constants.ERROR_NO_AUTHORITY) {
//                mShareCallback.onErrors?.invoke(PlatformType.QQ, code, "请授权手Q访问分享的文件的读取权限!")
            } else {
                mShareCallback.onErrors?.invoke(PlatformType.QQ, code, "")
            }
        }
    }

    /**
     * 要初始化open_id和token
     */
    private fun initOpenidAndToken(jsonObject: JSONObject) {
        try {
            val token =
                jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
            val expires =
                jsonObject.getString(Constants.PARAM_EXPIRES_IN)
            val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)

            mTencent.setAccessToken(token, expires)
            mTencent.openId = openId
        } catch (e: Exception) {
            e.message
        }
    }

    private fun jsonToMap(jsonObj: JSONObject): MutableMap<String, String?> {
        val map = mutableMapOf<String, String?>()

        val iterator = jsonObj.keys()

        while (iterator.hasNext()) {
            val var4 = iterator.next()
            map[var4] = jsonObj.opt(var4).toString() + ""
        }
        return map
    }

    override fun onActivityResult(content: OperationContent) {
        val activityResultContent = content as ActivityResultContent
        when (activityResultContent.request) {

            Constants.REQUEST_LOGIN -> {
                Tencent.onActivityResultData(
                    activityResultContent.result,
                    activityResultContent.result,
                    activityResultContent.data,
                    authListener
                )
            }
            else -> {}
        }

    }


    /**
     * 获取该平台支持的操作
     */
    fun getAvailableOperation(): List<OperationType> {
        return opList
    }


    override fun release() {
        PlatformManager.currentHandler = null
    }
}