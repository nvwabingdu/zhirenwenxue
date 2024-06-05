package com.example.zrwenxue.moudel.main.pagefour

data class DicBean(//中国诗词词典
    val hanzi: String,// 诗词名   //别的也可以用  这里用于诗词词典的作者   全唐诗的作者
    val pinyin:String,//拼音
    val explain:String,//解释
    val list: MutableList<Item>,
){
    data class Item(
        val hanzi: String,// 诗词名   //别的也可以用  这里用于诗词词典的作者   全唐诗的作者
        val pinyin:String,//拼音
        val explain:String,//解释
    )
}