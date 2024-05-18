package cn.dreamfruits.yaoguo.repository.bean.search

data class YgHotFeedListBean(
    val list: List<Item>
) {
    data class Item(
        val createTime: Long, // 1661875200549
        val feedId: Long, // 318418115300832
        val id: Int, // 271
        val sort: Int, // 1
        val state: Int, // 1
        val title: String, // 来来来
        val updateTime: Long // 1661875200549
    )
}