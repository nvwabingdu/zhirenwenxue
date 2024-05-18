package cn.dreamfruits.yaoguo.repository.bean.search

data class SearchUserBean(
    val hasNext: Int, // 0
    val lastTime: Long, // 1660889344837
    val list: List<Item>,
    val page: Int, // 2
    val size: Int, // 10

    //个人中心 关注列表
    val total: Int, // 8
) {
    data class Item(
        var avatarUrl: String, // https://devfile.dreamfruits.cn/app/250376611147232/b836b5d2181d8d491380d9318e6b7967
        val fansCount: Int, // 2
        val id: Long, // 250376611147232
        val nickName: String, // 都是家斌的错
        var relation: Int, // 3
        val ygId: String, // yg250376611147232
        var isSelected:Boolean,//添加是否选中

        //搜索用户  字段修改为了followerCount
        var followerCount: Int, // 7

        //通讯录独有
        val name: String, // 张三
        val userId: Long, // 243242437692896

        //个人中心 关注列表 粉丝列表 搜索关注列表
        val age: Int, // 4
        val descript: String,
        val followCount: Int, // 1



        //推荐好友独有
//        var avatarUrl: String, // https://devfile.dreamfruits.cn/app/243242437692896/4d676bb32c55db9d02240fe72324015b.png
//        var nickName: String, // yg460413145082496
        var reason: String,
//        var relation: Int, // 关系，0-没关系，1-我关注他，2-他关注我，3-相互关注
        var type: Int, // 3
//        var userId: Long // 460413145082496

    )
}