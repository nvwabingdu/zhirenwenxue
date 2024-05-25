package com.example.zrwenxue.moudel.main.pagetwo

class DictionaryIdiomIBean (
    val pinyinIndex: String, // 拼音
    var isOpen:Boolean,
    val list: MutableList<Item>,
) {
    data class Item(
        val idiom: String // 成语
    )
}