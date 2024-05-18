package cn.dreamfruits.yaoguo.repository.bean.mention

/**
 * 话题
 */
data class TopicBean(
    /**
     * 话题id
     */
    val id: Long,
    /**
     * 话题名称
     */
    val name: String,
    /**
     * 浏览数
     */
    val viewCount: Long
)