package cn.dreamfruits.yaoguo.repository.bean.set

data class GetUserHomePageSettingBean(
        val list: MutableList<Item>
    ) {
        data class Item(
            val isShow: Int, // 0-不展示，1-展示
            val subType: Int, // 当type为0时，此字段必填；0:年龄，1-星座
            val type: Int // 0:生日，1:地区

        )
    }
