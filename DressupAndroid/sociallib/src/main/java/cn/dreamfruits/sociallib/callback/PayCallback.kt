package cn.dreamfruits.sociallib.callback

import cn.dreamfruits.sociallib.config.PlatformType

/**
 * 支付的callback
 */
data class PayCallback(
    var onSuccess: ((type: PlatformType) -> Unit)? = null,
    override var onErrors: ((type: PlatformType, errorCode: Int, errorMsg: String?) -> Unit)? = null,
    var onCancel: ((type: PlatformType) -> Unit)? = null
) : OperationCallback