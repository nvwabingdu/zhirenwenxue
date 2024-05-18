package cn.dreamfruits.yaoguo.repository.bean.attention

data class RecommendUserListBean(
    val hasNext: Int, // 1
    val list: List<Item>,
    val totalCount: Int // 12
) {
    data class Item(
        val avatarUrl: String, // https://devfile.dreamfruits.cn/admin/10002/9adf55428309322b79b2b08e5adbb14a.jpeg
        val description: String,
        val followerCount: Int, // 1
        val nickName: String, // 皮
        val relation: Int, // 0
        val userId: Long // 275791787134432
    )
}