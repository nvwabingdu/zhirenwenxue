package com.example.zrtool.utils

/**
 * @Author qiwangi
 * @Date 2023/8/28
 * @TIME 10:37
 */
object ColorUtils {
    /**
     * 不带透明度的随机颜色
     */
     fun getRandomHexColor(): String {
        val color = (0..16777215).random() // 生成一个 0 到 16777215 之间的随机整数
        return String.format("#%06X", color) // 将整数转换为 6 位十六进制字符串
    }
    /**
     * 带透明度的随机颜色
     */
    fun getRandomHexColorWithAlpha(): String {
        val color = (0..16777215).random()
        val alpha = (0..255).random()
        return String.format("#%02X%06X", alpha, color)
    }
}