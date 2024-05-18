package cn.dreamfruits.yaoguo.repository.bean.media

/**
 * 图片bean
 * @param url 图片地址
 * @param width 宽度
 * @param height 高度
 */
data class PicBean(
    var httpUrl: String,
    var url: String,
    var width: Int,
    var height: Int
)