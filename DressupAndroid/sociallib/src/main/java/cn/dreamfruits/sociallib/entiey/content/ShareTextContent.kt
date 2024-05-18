package cn.dreamfruits.sociallib.entiey.content

/**
 * description: 文字分享
 */
data class ShareTextContent(
    override var description: String? = null,   //描述
    override var url: String? = null,   // 连接
    var atUser: String? = null
) : ShareContent()