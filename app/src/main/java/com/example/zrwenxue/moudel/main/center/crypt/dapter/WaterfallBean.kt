package com.example.zrwenxue.moudel.main.center.crypt.dapter

import android.graphics.Bitmap


data class WaterfallBean(
    val id: String, // 唯一标识
    val author: String, // 涂鸦作者
    val name: String, // 涂鸦名称
    val description: String, // 涂鸦介绍
    val isForSale: Boolean, // 是否售卖
    val createdAt: Long, // 生成时间
    val owner: String, // 持有人
    val value: Double, // 价值
    val bitmap: Bitmap,//画作
)