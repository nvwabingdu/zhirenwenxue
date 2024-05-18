package cn.dreamfruits.yaoguo.repository.bean.attention

data class UserFollowListBean(
    val hasNext: Int, // 1
    val lastTime: Long, // 1658302992893
    val list: List<Item>,
    val totalCount: Int // 8
) {
    data class Item(
        val age: Int, // 4
        val avatarUrl: String,
        val descript: String,
        val followCount: Int, // 1
        val followerCount: Int, // 1
        val nickName: String, // 杀杀杀看看
        val relation: Int, // 关系，0-没关系，1-我关注他，2-他关注我，3-相互关注
        val userId: Long // 249501046271456
    )
}