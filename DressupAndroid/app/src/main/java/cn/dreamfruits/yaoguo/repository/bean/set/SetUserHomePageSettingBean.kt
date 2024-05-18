package cn.dreamfruits.yaoguo.repository.bean.set

data class SetUserHomePageSettingBean(
        val list: MutableList<Item>
    ) {
        data class Item(
            val isShow: Int, // 1
            val subType: Int, // 0
            val type: Int // 0
        )
    }
