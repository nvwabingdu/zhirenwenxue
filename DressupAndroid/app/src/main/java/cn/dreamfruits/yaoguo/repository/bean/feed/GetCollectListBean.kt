package cn.dreamfruits.yaoguo.repository.bean.feed

data class GetCollectListBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 0
    val list: MutableList<Item>,
    val totalCount: Int // 3
    ) {
        data class Item(
            val feedCount: Int, // 14
            val feedPics: MutableList<FeedPic>,
            val id: Long, // 357269849612848
            val name: String, // OOTD
            val outfitCount: Int, // 0
            val viewCount: Int // 243
        ) {
            data class FeedPic(
                val height: Int, // 1440
                var url: String, // https://devfile.dreamfruits.cn/app/534216813659328/16C73B7DA13C2D0F860E55DCF3EC204F.jpg
                val width: Int // 1080
            )
        }
    }
