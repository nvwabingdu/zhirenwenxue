package cn.dreamfruits.yaoguo.repository.bean.thirdparty

/**
 * 腾讯 对象存储临时密钥
 */
data class TencentTmpSecretKeyBean(
    val tmpSecretId: String,
    val tmpSecretKey: String,
    val sessionToken: String,
    val allowKey: String,
    val startTime: Long,
    val expireTime: Long,
    val host: String,
    val cdnDomain: String,
    val bucket: String,
    val region:String
)