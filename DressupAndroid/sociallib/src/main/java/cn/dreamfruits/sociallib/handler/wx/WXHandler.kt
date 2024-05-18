package cn.dreamfruits.sociallib.handler.wx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import cn.dreamfruits.sociallib.PlatformManager
import cn.dreamfruits.sociallib.R
import cn.dreamfruits.sociallib.callback.AuthCallback
import cn.dreamfruits.sociallib.callback.OperationCallback
import cn.dreamfruits.sociallib.callback.ShareCallback
import cn.dreamfruits.sociallib.config.OperationType
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.config.SocialConstants
import cn.dreamfruits.sociallib.entiey.content.AuthContent
import cn.dreamfruits.sociallib.entiey.content.ShareContent
import cn.dreamfruits.sociallib.entiey.content.ShareImageContent
import cn.dreamfruits.sociallib.entiey.content.ShareWebContent
import cn.dreamfruits.sociallib.entiey.platform.PlatformConfig
import cn.dreamfruits.sociallib.handler.SSOHandler
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.ByteArrayOutputStream

/**
 * 微信 handler
 */
class WXHandler(context: Context, config: PlatformConfig) : SSOHandler() {

    companion object {
        const val TAG = "WXHandler"
        private val opList = listOf(
            OperationType.PAY,
            OperationType.AUTH,
            OperationType.SHARE
        )
    }

    private val mScope = "snsapi_userinfo"
    private val mState = "wechat_sdk_微信登录"
    private lateinit var mCallback: OperationCallback
    private lateinit var mShareType: PlatformType

    var wxAPI: IWXAPI? = WXAPIFactory.createWXAPI(context, config.appkey, true)
        private set
    var wxEventHandler: IWXAPIEventHandler
        private set


    init {
        wxAPI?.registerApp(config.appkey)
        wxEventHandler = object : IWXAPIEventHandler {

            override fun onReq(rsq: BaseReq?) {

            }

            override fun onResp(resp: BaseResp?) {
                Log.i(TAG, "onResp: ${resp?.type}")
                val type = resp?.type ?: -1
                when (type) {
                    //授权返回
                    ConstantsAPI.COMMAND_SENDAUTH -> this@WXHandler.onAuthCallback(resp as SendAuth.Resp)
                    ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> this@WXHandler.onShareAuthCallback(
                        resp as SendMessageToWX.Resp
                    )

                    //.....

                    else -> {
                        Log.i("social", "$TAG : wxEventHandler 回调为null")
                    }
                }
            }
        }

    }

    override val isInstalled: Boolean
        get() {
            if (wxAPI == null) {
                return false
            }
            return wxAPI!!.isWXAppInstalled
        }


    override fun authorize(type: PlatformType, callback: OperationCallback, content: AuthContent?) {
        setPlatCurrentHandler(callback)
        if (wxAPI == null) {
            callback.onErrors?.invoke(type, SocialConstants.AUTH_ERROR, "$TAG : wxAPI 为null")
            release()
            return
        }
        val req = SendAuth.Req().apply {
            scope = mScope
            state = mState
        }

        wxAPI?.let {
            if (!it.sendReq(req)) {
                callback.onErrors?.invoke(
                    type,
                    SocialConstants.LOGIN_ERROR,
                    "$TAG : 微信授权请求出错"
                )
                release()
            }
        }
    }

    override fun share(type: PlatformType, content: ShareContent, callback: OperationCallback) {
        setPlatCurrentHandler(callback)
        Log.e(">>>> ", "share PlatformType = " + type)
        if (wxAPI == null) {
            callback.onErrors?.invoke(type, SocialConstants.AUTH_ERROR, "$TAG : wxAPI 为null")
            release()
            return
        }

        if (content is ShareWebContent) {
            val webpage = WXWebpageObject()
            webpage.webpageUrl = content.webPageUrl
            val msg = WXMediaMessage(webpage)
            msg.title = content.title
            msg.description = content.description
            msg.thumbData =
                bmpToByteArray(Bitmap.createScaledBitmap(content.img!!, 150, 150, true), true)

            val req = SendMessageToWX.Req().apply {
                message = msg
                transaction = buildTransaction("webpage");
                scene = if (type == PlatformType.WEIXIN) SendMessageToWX.Req.WXSceneSession
                else SendMessageToWX.Req.WXSceneTimeline
            }
            wxAPI?.let {
                if (!it.sendReq(req)) {
                    callback.onErrors?.invoke(
                        type,
                        SocialConstants.LOGIN_ERROR,
                        "$TAG : 微信分享请求出错"
                    )
                    release()
                }
            }
        } else if (content is ShareImageContent) {
            val imgObj = WXImageObject(content.img)
            val msg = WXMediaMessage()
            msg.mediaObject = imgObj

            val thumbBmp = Bitmap.createScaledBitmap(
                content.img!!,
                150,
                150,
                true
            )
            content.img!!.recycle()
            msg.thumbData = bmpToByteArray(thumbBmp, true)

            val req = SendMessageToWX.Req().apply {
                message = msg
                transaction = buildTransaction("img");
                scene = if (type == PlatformType.WEIXIN) SendMessageToWX.Req.WXSceneSession
                else SendMessageToWX.Req.WXSceneTimeline
            }
            wxAPI?.let {
                if (!it.sendReq(req)) {
                    callback.onErrors?.invoke(
                        type,
                        SocialConstants.LOGIN_ERROR,
                        "$TAG : 微信分享请求出错"
                    )
                    release()
                }
            }
        }


    }

    /**
     *  设置PlatformManager.currentHandler,用于回调
     */
    private fun setPlatCurrentHandler(callback: OperationCallback) {
        this.mCallback = callback
        PlatformManager.currentHandler = this
    }


    /**
     * 授权回调
     */
    private fun onAuthCallback(resp: SendAuth.Resp) {
        Log.i(TAG, "onAuthCallback: ${resp}")
        if (mCallback !is AuthCallback) {
            mCallback.onErrors
                ?.invoke(
                    PlatformType.WEIXIN,
                    SocialConstants.AUTH_ERROR,
                    "$TAG : callback 类型错误"
                )
            return
        }
        val callback = mCallback as AuthCallback
        when (resp.errCode) {
            //授权成功
            BaseResp.ErrCode.ERR_OK -> {
                val data = mutableMapOf<String, String?>()
                data["code"] = resp.code
                callback.onSuccess?.invoke(PlatformType.WEIXIN, data)
            }

            //授权取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                callback.onCancel?.invoke(PlatformType.WEIXIN)
            }

            //授权失败
            else -> {
                callback.onErrors
                    ?.invoke(
                        PlatformType.WEIXIN,
                        SocialConstants.AUTH_ERROR,
                        "$TAG : 授权失败"
                    )
            }
        }
        release()
    }

    private fun onShareAuthCallback(resp: SendMessageToWX.Resp) {
        Log.i(">>>> ", "onShareCallback: ${resp}")
        if (mCallback !is ShareCallback) {
            mCallback.onErrors
                ?.invoke(
                    PlatformType.WEIXIN,
                    SocialConstants.AUTH_ERROR,
                    "$TAG : callback 类型错误"
                )
            return
        }
        val callback = mCallback as ShareCallback
        Log.i(">>>> ", "onShareCallback: ${resp}")
        Log.i(">>>> ", "onShareCallback: ${resp.errCode}")
        when (resp.errCode) {
            //授权成功
            BaseResp.ErrCode.ERR_OK -> {
                callback.onSuccess?.invoke(PlatformType.WEIXIN)
            }

            //授权取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                callback.onCancel?.invoke(PlatformType.WEIXIN)
            }

            //授权失败
            else -> {
                callback.onErrors
                    ?.invoke(
                        PlatformType.WEIXIN,
                        SocialConstants.AUTH_ERROR,
                        "$TAG : 授权失败"
                    )
            }
        }
        release()
    }


    /**
     * 获取对应平台支持的操作
     */
    fun getAvailableOperations(): List<OperationType> {
        return opList
    }


    override fun release() {
        wxAPI?.unregisterApp()
        wxAPI?.detach()
        wxAPI = null

        PlatformManager.currentHandler = null
    }

    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray? {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun buildTransaction(type: String?): String? {
        return if (type == null) System.currentTimeMillis()
            .toString() else type + System.currentTimeMillis()
    }

}