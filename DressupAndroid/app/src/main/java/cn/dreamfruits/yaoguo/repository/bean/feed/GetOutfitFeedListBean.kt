package cn.dreamfruits.yaoguo.repository.bean.feed

data class GetOutfitFeedListBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 1661509272662
    val list: List<Item>,
    val page: Int, // 2
    val size: Int // 1
) {
    data class Item(
        val address: Any, // null
        val atUser: String,
        val cityAdCode: Any, // null
        val config: String,
        val content: String,
        val createTime: Long, // 1661346198900
        val id: Long, // 318418115300832
        val isLaud: Int, // 0
        val latitude: Any, // null
        val laudCount: Int, // 0
        val longitude: Any, // null
        val outfitId: Long, // 318418115407328
        val picUrls: List<PicUrl>,
        val provinceAdCode: Any, // null
        val relation: Int, // 0
        val state: Int, // 1
        val title: String, // 来来来
        val type: Int, // 1
        val updateTime: Long, // 1661346198900
        val userInfo: UserInfo,
        val videoUrl: String
    ) {
        data class PicUrl(
            val height: Int, // 720
            val url: String, // https://devfile.dreamfruits.cn/app/243242437692896/90a201ab85f7ce33fd9c188535c5d3f5
            val width: Int // 720
        )

        data class UserInfo(
            val avatarUrl: String, // https://devfile.dreamfruits.cn/app/243242437692896/4c689f5de49f7616f56fbdd809e81914
            val id: Long, // 243242437692896
            val nickName: String // 欧诺
        )
    }
}