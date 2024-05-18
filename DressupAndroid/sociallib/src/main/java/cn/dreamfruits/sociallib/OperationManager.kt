package cn.dreamfruits.sociallib

import cn.dreamfruits.sociallib.config.OperationType
import cn.dreamfruits.sociallib.config.OperationType.*
import cn.dreamfruits.sociallib.config.SocialConstants
import cn.dreamfruits.sociallib.entiey.OperationBean
import cn.dreamfruits.sociallib.entiey.content.AuthContent
import cn.dreamfruits.sociallib.entiey.content.OperationContent
import cn.dreamfruits.sociallib.entiey.content.ShareContent
import cn.dreamfruits.sociallib.handler.SSOHandler

/**
 * 操作管理类
 */
internal class OperationManager {

    companion object {

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            OperationManager()
        }
    }

    /**
     * 执行操作
     */
    fun perform(bean: OperationBean) {
        //平台是否可用
        if (PlatformManager.availablePlatMap[bean.operationPlat] == null) {
            bean.operationCallback.onErrors?.invoke(
                bean.operationPlat,
                SocialConstants.PLAT_NOT_INSTALL,
                "未安装${bean.operationPlat} 的app"
            )
            return
        }
        //平台是否支持该操作
        if (!PlatformManager.available4PlatAndOperation(bean.operationPlat, bean.operationType)) {
            bean.operationCallback.onErrors?.invoke(
                bean.operationPlat,
                SocialConstants.OPERATION_NOT_SUPPORT,
                "${bean.operationPlat} 不支持 ${bean.operationType}"
            )
            return
        }

        val handler = PlatformManager
            .getPlatHandler(bean.operationContext, bean.operationPlat)

        if (handler == null) {
            bean.operationCallback.onErrors
                ?.invoke(
                    bean.operationPlat, SocialConstants.PLAT_HANDLER_ERROR,
                    "获取 ${bean.operationPlat}的handler失败"
                )
            return
        }

        when (bean.operationType) {
            AUTH -> {
                handler.authorize(
                    bean.operationPlat,
                    bean.operationCallback,
                    bean.operationContent as AuthContent
                )
            }

            //.....
            PAY -> TODO()
            SHARE -> {
                handler.share(
                    bean.operationPlat,
                    bean.operationContent as ShareContent,
                    bean.operationCallback

                )
            }
        }
    }

    /**
     * 部分sdk 需要调用activity 的onActivityResult
     */
    fun performActivityResult(handler: SSOHandler, content: OperationContent) {
        handler.onActivityResult(content)
    }


}