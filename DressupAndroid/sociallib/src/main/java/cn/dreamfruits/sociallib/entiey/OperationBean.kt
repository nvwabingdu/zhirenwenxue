package cn.dreamfruits.sociallib.entiey

import android.content.Context
import cn.dreamfruits.sociallib.callback.OperationCallback
import cn.dreamfruits.sociallib.config.OperationType
import cn.dreamfruits.sociallib.config.PlatformType
import cn.dreamfruits.sociallib.entiey.content.OperationContent

/**
 * 第三方平台操作的实体
 */
data class OperationBean(
    var operationContext: Context,        // 操作上下文
    var operationPlat: PlatformType,      // 平台类型
    var operationType: OperationType,     // 操作类型
    var operationCallback: OperationCallback,   // 回调
    var operationContent: OperationContent? = null  // 平台内容
)