package cn.dreamfruits.yaoguo.base.bean

/**
 * 分页数据基类
 */
data class BasePageResult<out T>(
    val lastTime: Long,
    val hasNext: Int,
    val list: List<T>? = emptyList()
)