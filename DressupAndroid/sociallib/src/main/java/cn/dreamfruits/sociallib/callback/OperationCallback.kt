package cn.dreamfruits.sociallib.callback

import cn.dreamfruits.sociallib.config.PlatformType

/**
 * 操作的回调
 */
interface OperationCallback{

    /**
     * type : 平台类型
     * errorCode : 错误码
     * errorMsg : 错误信息
     */
    var onErrors: ((type: PlatformType, errorCode: Int, errorMsg:String?) -> Unit)?

}