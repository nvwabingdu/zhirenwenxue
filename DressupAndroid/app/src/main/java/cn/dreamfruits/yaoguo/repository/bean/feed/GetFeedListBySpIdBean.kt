package cn.dreamfruits.yaoguo.repository.bean.feed

data class GetFeedListBySpIdBean(
    val hasNext: Int, // 1
    val lastTime: Long, // 1661509358609
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
        val createTime: Long, // 1661333069641
        val id: Long, // 318310560468448
        val isLaud: Int, // 0
        val latitude: Any, // null
        val laudCount: Int, // 0
        val longitude: Any, // null
        val outfitId: Long, // 318310560525792
        val picUrls: List<PicUrl>,
        val provinceAdCode: Any, // null
        val relation: Int, // 0
        val state: Int, // 1
        val title: String, // 带穿搭方案
        val type: Int, // 1
        val updateTime: Long, // 1661333069641
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