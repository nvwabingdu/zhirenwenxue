package com.example.zrwenxue.moudel.main.center.zr.dapter

import android.graphics.Bitmap


data class WaterfallBean(
    val id: String, // 唯一标识
    val doodleAuthor: String, // 涂鸦作者
    val doodleName: String, // 涂鸦名称
    val doodleDescription: String, // 涂鸦介绍
    val createdAt: Long, // 生成时间
    val owner: String, // 持有人
    val historyValue: Double, // 历史最高价值
    val numberOfSales: Int, // 出售次数
    val value: Double, // 价值多少
    val bitmap: Bitmap,//画作
)