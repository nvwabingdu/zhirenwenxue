package cn.dreamfruits.yaoguo.repository.bean.find

data class FindLabelBean(
    val list: List<Item>
) {
    data class Item(
        val feedPics: List<FeedPic>,
        val id: Long, // 304805305705296
        val name: String, // 红色
        val viewCount: Int // 0
    ) {
        data class FeedPic(
            val height: Int, // 3120
            val url: String, // https://testfile.dreamfruits.cn/app/332988983232048/b43391ee8c25a8ceaff1a500abe4c66c
            val width: Int // 4160
        )
    }
}