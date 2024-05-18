package cn.dreamfruits.yaoguo.net.exception

/**
 * 登录异常
 */
class LoginException(code: Int, msg: String) : ApiException(code, msg)