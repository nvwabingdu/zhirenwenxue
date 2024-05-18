package cn.dreamfruits.yaoguo.repository.bean.find

data class DiscoverUserListBean(
    val list: List<Item>
) {
    data class Item(
        val avatarUrl: String, // https://devfile.dreamfruits.cn/admin/10002/9adf55428309322b79b2b08e5adbb14a.jpeg
        val coverUrls: List<CoverUrl>,
        val followerCount: Int, // 1
        val nickName: String, // 大家都是郑强的儿子
        val relation: Int, // 0
        val userId: Long // 275181222964704
    ) {
        data class CoverUrl(
            val height: Int, // 1318
            val url: String, // https://devfile.dreamfruits.cn/app/250376611147232/cb454e3577109f7ef378b4f15b3796cf
            val width: Int // 690
        )
    }
}