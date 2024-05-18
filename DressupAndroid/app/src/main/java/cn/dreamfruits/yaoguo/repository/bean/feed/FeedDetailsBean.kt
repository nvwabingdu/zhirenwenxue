package cn.dreamfruits.yaoguo.repository.bean.feed

import cn.dreamfruits.yaoguo.util.Singleton

data class FeedDetailsBean(
    val address: Any, // null
    val atUser: List<Singleton.AtUser>,
    val cityAdCode: Any, // null
    var collectCount: Int, // 0
    val commentCount: Int, // 0
    val commentList: Any, // null
    val config: List<Singleton.Config>,
    val content: String, // string
    val createTime: Long, // 0
    val id: Long, // 0
    var isCollect: Int, // 0
    var isLaud: Int, // 0
    val latitude: Any, // null
    var laudCount: Int, // 0
    val longitude: Any, // null
    val outfitId: Any, // null
    val picUrls: List<PicUrl>,
    val provinceAdCode: Any, // null
    val relation: Int, // 0
    val seeLimit: Int, // 0
    val singleList: List<Single>,
    val state: Int, // 0
    val title: String, // string
    val type: Int, // 0
    val updateTime: Long, // 0
    val userInfo: UserInfo,
    val videoUrls: List<VideoUrl>,
    val wearCount: Int // 0
) {
//    data class AtUser(
//        val id: Long, // 0
//        val index: Int, // 0
//        val len: Int, // 0
//        val name: String // string
//    )

//    data class Config(
//        val id: Long, // 0
//        val index: Int, // 0
//        val len: Int, // 0
//        val name: String // string
//    )

    data class PicUrl(
        val height: Int, // 0
        var url: String, // string
        val width: Int // 0
    )

    data class Single(
        val appResourceUrl: String, // string
        val cLength: Int, // 0
        val collectCount: Int, // 0
        val createTime: Long, // 0
        val id: Long, // 0
        val isCollect: Int, // 0
        val labelsList: List<Labels>,
        val modelList: List<Model>,
        val name: String, // string
        val picUrls: List<String>,
        val position: Int, // 0
        val state: Int, // 0
        val tryOnCount: Int, // 0
        val videoUrls: String // string
    ) {
        data class Labels(
            val id: Long, // 0
            val name: String, // string
            val viewCount: Int // 0
        )

        data class Model(
            val floor: Int, // 0
            val id: Long, // 0
            val resourceUrl: String, // string
            val singleProductId: Long, // 0
            val status: Int // 0
        )
    }

    data class UserInfo(
        var avatarUrl: String, // string
        val id: Long, // 0
        val nickName: String // string
    )

    data class VideoUrl(
        val height: Int, // 0
        var url: String, // string
        val width: Int // 0
    )
}