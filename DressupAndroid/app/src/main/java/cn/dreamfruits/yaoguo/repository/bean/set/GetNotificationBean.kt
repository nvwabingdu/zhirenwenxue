package cn.dreamfruits.yaoguo.repository.bean.set

data class GetNotificationBean(
        val list: List<Item>
    ) {
        data class Item(
            val isOpen: Int, // 1
            val type: Int // 0
        )
    }
