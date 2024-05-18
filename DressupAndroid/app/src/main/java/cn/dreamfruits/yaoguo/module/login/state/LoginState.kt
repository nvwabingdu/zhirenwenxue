package cn.dreamfruits.yaoguo.module.login.state

import cn.dreamfruits.yaoguo.repository.bean.oauth.LoginResultBean


sealed class SmsState {

    object Success : SmsState()

    class Fail(val errorMsg: String?) : SmsState()
}

sealed class LoginState {

    class Success(val loginResultBean: LoginResultBean? = null) : LoginState()

    class Fail(val errorMsg: String?, val errorCode: Int? = -1) : LoginState()
}

sealed class LogoutState {

    object Success : LogoutState()

    class Fail(val errorMsg: String?) : LogoutState()
}

sealed class ThirdpartyApiState {

    object Success : ThirdpartyApiState()

    class Fail(val errorMsg: String?) : ThirdpartyApiState()
}

/**
 * 上报通讯录-全量  状态接口类
 */
sealed class UploadAllContactsState {

    object Success : UploadAllContactsState()

    class Fail(val errorMsg: String?) : UploadAllContactsState()
}

/**
 * 上报通讯录-差量  状态接口类
 */
sealed class UploadContactsState {

    object Success : UploadContactsState()

    class Fail(val errorMsg: String?) : UploadContactsState()
}

/**
 * 获取通讯录列表  状态接口类
 */
sealed class GetContactsListState {

    object Success : GetContactsListState()

    class Fail(val errorMsg: String?) : GetContactsListState()
}

/**
 * 获取短链接  状态接口类
 */
sealed class GetShortUrlState {

    object Success : GetShortUrlState()

    class Fail(val errorMsg: String?) : GetShortUrlState()
}

/**
 * 获取短链接  状态接口类
 */
sealed class GetShortUrlGetState {

    object Success : GetShortUrlGetState()

    class Fail(val errorMsg: String?) : GetShortUrlGetState()
}