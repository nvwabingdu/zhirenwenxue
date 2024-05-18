package cn.dreamfruits.yaoguo.repository.bean.find

data class HomeRecommendUserListBean(
    val hasNext: Int, // 0
    val lastTime: Int, // 0
    val list: List<Item>
) {
    data class Item(
        var avatarUrl: String, // https://devfile.dreamfruits.cn/app/243242437692896/4d676bb32c55db9d02240fe72324015b.png
        var nickName: String, // yg460413145082496
        var reason: String,
        var relation: Int, // 关系，0-没关系，1-我关注他，2-他关注我，3-相互关注
        var type: Int, // 3
        var userId: Long // 460413145082496
    )
}