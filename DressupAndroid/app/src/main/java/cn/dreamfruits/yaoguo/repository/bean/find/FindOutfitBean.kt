package cn.dreamfruits.yaoguo.repository.bean.find

data class FindOutfitBean(
    val list: List<Item>
) {
    data class Item(
        val feedPics: List<FeedPic>,
        val id: Long, // 323275374748560
        val labelList: List<Label>,
        val outfitCoverUrl: String, // https://testfile.dreamfruits.cn/app/322590839375712/315c1d11110c814d17b4c7766a535749
        val tryOnCount: Int // 0
    ) {
        data class FeedPic(
            val height: Int, // 720
            val url: String, // https://testfile.dreamfruits.cn/app/322590839375712/90a201ab85f7ce33fd9c188535c5d3f5
            val width: Int // 720
        )

        data class Label(
            val id: Long, // 323275374748560
            val name: String, // T恤
            val viewCount: Int // 0
        )
    }
}