package cn.dreamfruits.yaoguo.repository.bean.message

data class GetUnreadCountBean(
    val commentCount: Int, // 0
    val followCount: Int, // 1
    val laudCount: Int, // 2
    val sysCount: Int // 0
)