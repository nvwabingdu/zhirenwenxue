package cn.dreamfruits.yaoguo.repository.bean.feed

data class GetUserFeedListBean(
    val hasNext: Int, // 1
    val lastTime: Long, // 1660737100012
    val list: List<Item>
) {
    data class Item(
        val address: Any, // null
        val atUser: String,
        val cityAdCode: Any, // null
        val config: String, // {"topic":{"紧身":308331070320096}}
        val content: String, // 伟哥你背后有人#紧身 
        val createTime: Long, // 1660737205283
        val id: Long, // 313429240065504
        val isLaud: Int, // 0
        val latitude: Any, // null
        val laudCount: Int, // 0
        val longitude: Any, // null
        val outfitId: Any, // null
        val picUrls: List<PicUrl>,
        val provinceAdCode: Any, // null
        val relation: Int, // 0
        val state: Int, // 1
        val title: String, // 伟哥摸摸摸鱼
        val type: Int, // 1
        val updateTime: Long, // 1660737205283
        val userInfo: UserInfo,
        val videoUrl: String
    ) {
        data class PicUrl(
            val height: Int, // 800
            val url: String, // https://devfile.dreamfruits.cn/admin/10002/89f6999043d7c19aec1b6c7186422757.JPG
            val width: Int // 1000
        )

        data class UserInfo(
            val avatarUrl: String, // https://devfile.dreamfruits.cn/app/243242437692896/4c689f5de49f7616f56fbdd809e81914
            val id: Long, // 243242437692896
            val nickName: String // 欧诺
        )
    }
}