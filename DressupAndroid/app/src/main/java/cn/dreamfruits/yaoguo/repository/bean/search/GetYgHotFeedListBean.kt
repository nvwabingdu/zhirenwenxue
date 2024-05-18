package cn.dreamfruits.yaoguo.repository.bean.search

data class GetYgHotFeedListBean(
        val list: MutableList<Item>
    ) {
        data class Item(
            val createTime: Long, // 1687968001582
            val feedId: Long, // 535638148467808
            val id: Long, // 882
            val sort: Int, // 1
            val state: Int, // 1
            val title: String, // 斌哥yyds
            val updateTime: Long // 1687968001582
        )
    }
