package cn.dreamfruits.sociallib.callback

import cn.dreamfruits.sociallib.config.PlatformType

/**
 * 授权的call back
 */
data class AuthCallback(
    var onSuccess: ((type: PlatformType, data: Map<String, String?>?) -> Unit)? = null,
    override var onErrors: ((type: PlatformType, errorCode: Int, errorMsg: String?) -> Unit)? = null,
    var onCancel: ((type: PlatformType) -> Unit)? = null
) : OperationCallback