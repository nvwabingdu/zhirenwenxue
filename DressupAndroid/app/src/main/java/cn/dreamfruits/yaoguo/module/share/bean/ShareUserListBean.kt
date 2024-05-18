package cn.dreamfruits.yaoguo.module.share.bean

import cn.dreamfruits.yaoguo.adapter.base.entity.SectionEntity

/**
 * @author Lee
 * @createTime 2023-07-06 15 GMT+8
 * @desc :
 */

data class ShareUserListBean(
    var allFollowList: MutableList<UserInfo>?,
    var bothFollowList: MutableList<UserInfo>?,
    var recentList: MutableList<UserInfo>?,
)

data class UserInfo(
    var avatarUrl: String = "",
    var avatarUrlX: String = "",
    var id: String = "",
    var nickName: String = "",
    var firstKey: String = "",
    var isSel: Boolean = false,
    override var isHeader: Boolean = false,


    ) : SectionEntity {
    override fun toString(): String {
        return "UserInfo(avatarUrl='$avatarUrl', id='$id', nickName='$nickName', firstKey='$firstKey', isHeader=$isHeader)"
    }
}


data class ShortUrlEntity(
    var shortUrl: String = "",
)