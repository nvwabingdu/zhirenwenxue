package com.example.zrtool.utils

import java.text.DecimalFormat

/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 12:14
 */
object NumberUtils {



    /**
     * 保留一位小数  用于数字显示规则 大于9999的 保留一位小数
     * @return
     */
    fun getDecimalFormatOne(tempNum: Int): String? {
        return try {
            val df = DecimalFormat("#.0")
            df.format(tempNum / 10000.0)
        } catch (e: Exception) {
            tempNum.toString() + ""
        }
    }


}