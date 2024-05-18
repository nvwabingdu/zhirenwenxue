package cn.dreamfruits.yaoguo.repository.bean.oauth

//登录成功响应结果
data class LoginResultBean(
    val userId: Long,
    val token: String,
    val expireTime: Long,
    val refreshToken: String,
    val refreshTokenExpireTime: Long,
    val isPerfect: Int,//是否有性别
    val inviteCodePerfect: Int,//是否完善邀请码 0-否，1-是
)