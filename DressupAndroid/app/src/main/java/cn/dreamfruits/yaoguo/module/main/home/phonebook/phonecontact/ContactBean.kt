package com.example.phonecontact

data class ContactBean(
    val list: MutableList<Item>
) {
    data class Item(
        val name: String, // https://devfile.dreamfruits.cn/app/243242437692896/913bbd88d667ac73128e804dbe2f47c6
        val phone: String, // 6
        val type: Int, // 张三
    )
}
