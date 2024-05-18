package cn.dreamfruits.yaoguo.repository.bean.search

data class SearchWordBean(
        val hasNext: Int, // 1
        val lastTime: Long?, // 1662186628247
        val list: MutableList<Item>?,
        val page: Int, // 2
        val size: Int // 10
    ) {
        data class Item(
            val id: Long, // 304805305705296
            val name: String, // 红色
            val viewCount: Long, // 0
            var key:String //搜索词
        )
    }
