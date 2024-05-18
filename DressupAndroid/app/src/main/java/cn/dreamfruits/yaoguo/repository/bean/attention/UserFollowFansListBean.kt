package cn.dreamfruits.yaoguo.repository.bean.attention

data class UserFollowFansListBean(
    val hasNext: Int, // 1
    val lastTime: Long, // 1658303224842
    val list: List<Item>,
    val totalCount: Int // 6
) {
    data class Item(
        val age: Int, // 0
        val avatarUrl: String,
        val descript: String,
        val followCount: Int, // 1
        val followerCount: Int, // 0
        val nickName: String,
        val relation: Int, // 2
        val userId: Long // 293376687697376
    )
}