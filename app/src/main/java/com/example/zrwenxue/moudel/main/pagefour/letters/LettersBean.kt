package com.example.zrwenxue.moudel.main.pagefour.letters



data class LettersBean(
    val letter:String,
    var isClick:Boolean,
    val pinyinList: MutableList<Item>?,
){
    data class Item(
        val pinyin:String,
        var isClick:Boolean,
    )
}