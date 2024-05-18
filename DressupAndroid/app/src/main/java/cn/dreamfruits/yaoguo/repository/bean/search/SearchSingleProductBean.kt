package cn.dreamfruits.yaoguo.repository.bean.search

import cn.dreamfruits.yaoguo.repository.bean.feed.WaterfallFeedBean

data class SearchSingleProductBean(
    val hasNext: Int, // 1
    val lastTime: Long, // 1660901976956
    val list: List<WaterfallFeedBean.Item.Info.Single>,
    val page: Int, // 2
    val size: Int // 5
) {
//    data class Item(
//        val coverUrl: String, // https://devfile.dreamfruits.cn/admin/297604402720224/d18fd807c16bf4b64f4e2ec1f5e56bd3.png
//        val id: Long // 313270008679904
//    )
}