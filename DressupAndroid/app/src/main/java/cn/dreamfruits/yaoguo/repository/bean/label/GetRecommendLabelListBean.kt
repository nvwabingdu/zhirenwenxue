package cn.dreamfruits.yaoguo.repository.bean.label

data class GetRecommendLabelListBean(
    val list: List<Item>
) {
    data class Item(
        val id: Long, // 284978168139232
        val name: String, // app创建的话题3
        val viewCount: Int // 0
    )
}