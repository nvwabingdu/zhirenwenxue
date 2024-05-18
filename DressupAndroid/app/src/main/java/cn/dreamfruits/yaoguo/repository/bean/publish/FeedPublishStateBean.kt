package cn.dreamfruits.yaoguo.repository.bean.publish

/**
 * 发布帖子状态
 */
data class FeedPublishStateBean(
    val taskId: Long,
    var cover: String,
    var publishFail: Boolean = false,
    var msg: String = ""
)