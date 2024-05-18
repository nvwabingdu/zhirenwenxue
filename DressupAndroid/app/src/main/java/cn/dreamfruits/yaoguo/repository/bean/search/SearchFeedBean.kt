package cn.dreamfruits.yaoguo.repository.bean.search

import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean

data class SearchFeedBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 1660794741255
    val list: List<WaterfallFeedBean.Item.Info>,
    val page: Int, // 2
    val size: Int // 10
)
//{
//    data class Item(
//        val atUser: String,
//        val cityCode: String,
//        val config: String, // {"topic":{"紧身":308331070320096}}
//        val content: String, // 伟哥你背后有人#紧身
//        val createTime: Long, // 1660737059893
//        val id: Long, // 313428049038816
//        val isLaud: Int, // 0
//        val latitude: String,
//        val laudCount: Int, // 0
//        val longitude: String,
//        val outfitId: Int, // 0
//        val picUrls: List<PicUrl>,
//        val relation: Int, // 0
//        val state: Int, // 1
//        val title: String, // 伟哥摸摸摸鱼
//        val type: Int, // 1
//        val updateTime: Long, // 1660737059893
//        val userInfo: UserInfo,
//        val videoUrl: String
//    ) {
//        data class PicUrl(
//            val height: Int, // 800
//            val url: String, // https://devfile.dreamfruits.cn/admin/10002/89f6999043d7c19aec1b6c7186422757.JPG
//            val width: Int // 1000
//        )
//
//        data class UserInfo(
//            val avatarUrl: String, // https://devfile.dreamfruits.cn/app/243242437692896/4c689f5de49f7616f56fbdd809e81914
//            val id: Long, // 243242437692896
//            val nickName: String // 欧诺
//        )
//    }
//}