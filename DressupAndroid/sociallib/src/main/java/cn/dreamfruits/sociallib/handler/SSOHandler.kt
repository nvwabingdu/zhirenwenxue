package cn.dreamfruits.sociallib.handler

import cn.dreamfruits.sociallib.callback.OperationCallback
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.AuthContent
import cn.dreamfruits.sociallib.entiey.content.OperationContent
import cn.dreamfruits.sociallib.entiey.content.PayContent
import cn.dreamfruits.sociallib.entiey.content.ShareContent

/**
 *
 */
abstract class SSOHandler {


    /**
     * 判断是否安装平台
     */
    abstract val isInstalled: Boolean


    /**
     * 重写onActivity
     */
    open fun onActivityResult(content: OperationContent) {

    }

    /**
     *  支付
     */
    open fun pay(type: PlatformType, content: PayContent, callback: OperationCallback) {

    }

    /**
     * 分享
     */
    open fun share(type: PlatformType, content: ShareContent, callback: OperationCallback) {

    }

    /**
     * 授权
     */
    open fun authorize(
        type: PlatformType,
        callback: OperationCallback,
        content: AuthContent? = null
    ) {
    }


    /**
     * 资源释放
     */
    abstract fun release()

}