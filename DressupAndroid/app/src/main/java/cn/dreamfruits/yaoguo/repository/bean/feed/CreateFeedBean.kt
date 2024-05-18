package cn.dreamfruits.yaoguo.repository.bean.feed

data class CreateFeedBean(
    val atUser: String, // [278623243439584,243242437692896]
    val cityCode: String, // 111
    val config: String,
    val content: String, // 测试121212
    val createTime: Long, // 1658742205904
    val id: Long, // 297086205028704
    val isLaud: Int, // 0
    val latitude: String, // 111
    val laudCount: Int, // 0
    val longitude: String, // 111
    val outfitId: Long, // 297085586680160
    val picUrls: List<String>,
    val relation: Int, // 0
    val state: Int, // 1
    val title: String, // 测试21212321321321312312321321312
    val type: Int, // 1
    val updateTime: Long, // 1658742205904
    val userInfo: UserInfo,
    val videoUrl: String // https://devfile.dreamfruits.cn/admin/10002/de3afd4ccbda949ee8edec6816640d7a.JPG
) {
    data class UserInfo(
        val id: Long, // 249501046271456
        val nickName: String // 杀杀杀看看
    )
}