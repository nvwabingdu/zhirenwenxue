package cn.dreamfruits.yaoguo.net

/**
 * 服务器返回的错误码
 */
interface HttpResultCode {

    companion object{
        //token 过期
        const val TOKEN_EXPIRED = 401
        //参数不全
        const val PARAMETER_DEFICIENCY = 400
        //需要登录
        const val NEED_LOGIN = 402
        //请求成功
        const val SUCCESS = 200

        //需要绑定手机号
        const val NEED_BIND_PHONE = 111

        //需要绑定性别
        const val NEED_BIND_GENDER = 120

    }





}