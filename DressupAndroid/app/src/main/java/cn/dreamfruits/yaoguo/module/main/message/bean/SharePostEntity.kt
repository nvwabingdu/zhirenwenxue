package cn.dreamfruits.yaoguo.module.main.message.bean

import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean

/**
 * @author Lee
 * @createTime 2023-07-07 16 GMT+8
 * @desc :
 */
data class SharePostEntity(
    var picUrls: List<WaterfallFeedBean.Item.Info.PicUrl> = arrayListOf(),
    var videoUrls: List<WaterfallFeedBean.Item.Info.VideoUrl> = arrayListOf(),
    var title: String = "",
    var avatarUrl: String = "",
    var avatarUrlX: String = "",
    var nickName: String = "",
    var userId: String = "",
    var id: String = "",
    var type: Int = 0,
    var coverHeight: Int = 0,
    var coverWidth: Int = 0,
)

data class ShareUserEntity(
    var avatarUrl: String = "",
    var avatarUrlX: String = "",
    var nickName: String = "",
    var userId: String = "",
    var descript: String = "",
    var followerCount: Long = 0,
    var feedCount: Int = 0,
    var backgroundUrl: String = "",
)
data class ShareSingleEntity(
    var coverUrl: String = "",
    var name: String = "",
    var id: String = "",
)

