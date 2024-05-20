package com.example.zrwenxue.moudel.main.pagetwo

class DictionaryIdiomIBean (
    val pinyinIndex: String, // 拼音
    val isOpen:Boolean,
    val list: MutableList<Item>,
) {
    data class Item(
        val idiom: String, // 成语
        val pinyin: String, // 拼音
        val definition: String, // 释义
        val example: String, // 例子
        val Jìnyìcí: String, // 近义词
        val fanyici: String, // 反义词
        val xiehouyu:String,//歇后语
        val grammar: String, // 语法
        val enn: String, // 英语
    )
}