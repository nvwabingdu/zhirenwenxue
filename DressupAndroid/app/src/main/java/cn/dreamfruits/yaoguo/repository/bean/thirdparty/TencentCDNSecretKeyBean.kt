package cn.dreamfruits.yaoguo.repository.bean.thirdparty

data class TencentCDNSecretKeyBean(
    val randStr: String, // sign
    val secondSecretKey: String, // 5v4n14xTOz73I5Us89K028m89MgFyh
    val secretKey: String, // CyrNUuz8J8HZXQ1q6874z6tTzsx2G2zN
    val usersig: String //
)