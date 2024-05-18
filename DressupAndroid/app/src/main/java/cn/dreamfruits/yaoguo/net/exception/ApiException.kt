package cn.dreamfruits.yaoguo.net.exception


/**
 * api异常
 */
open class ApiException(
    val code: Int = -1,
    override val message: String = "undefined",
) : Exception(message)