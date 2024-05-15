package com.example.zrwenxue.net.bean

/**
 * @Author qiwangi
 * @Date 2023/8/5
 * @TIME 21:06
 * 用于存放bean数据
 */
data class AppData(
    val states: String,
    val data: List<Data>
)

data class Data(
    val id: Int,
    val name: String,
    val url: String
)