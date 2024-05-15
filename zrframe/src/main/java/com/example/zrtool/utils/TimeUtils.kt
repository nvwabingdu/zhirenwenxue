package com.example.zrtool.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 11:47
 */
object TimeUtils {
    /**
     *  帖子时间规则  和  消息模块时间规则
     *  timeStamp2Date(System.currentTimeMillis(), "yyyy_MM_dd HH_mm_ss_SS")//系统时间
     *  timeStamp2Date(timestamp, "yyyy_MM_dd HH_mm_ss_SS")//帖子发布时间
     */
    fun timeShowRuler(timestamp: Long, isPost: Boolean): String {
        try {
            val temp = System.currentTimeMillis() - timestamp //时间差值

            val second = temp / 1000
            val minute = temp / 1000 / 60
            val hour = temp / 1000 / 60 / 60
            val day = temp / 1000 / 60 / 60 / 24

            var isThisYear = false
            if ( timeStamp2Date(System.currentTimeMillis(), "yyyy")
                    .equals( timeStamp2Date(timestamp, "yyyy"))
            ) {
                isThisYear = true
            }
            if (isPost) {
                if (day > 8) {//若日期在今年则展示xx月xx日   若日期在去年或去年之前则 展示xxxx年xx月xx日
                    return if (isThisYear) {
                        fixTimeText( timeStamp2Date(timestamp, "MM月dd日"))
                    } else {
                         timeStamp2Date(timestamp, "yyyy年MM月dd日")
                    }
                }
                if (day == 1L) {//1天<=t<8天
                    return "昨天"
                }
                if (day == 2L) {//1天<=t<8天
                    return "前天"
                }
                if (day in 1..7) {//1天<=t<8天
                    return "${day}天前"
                }
                if (hour in 1..24) {//1小时<=t<24小时
                    return "${hour}小时前"
                }
                if (minute in 1..60) {//1分钟<=t<60分钟
                    return "${minute}分钟前"
                }
                if (second < 60) {//t<1分钟
                    return "刚刚"
                }
            } else {
                if (isThisYear) {//今年
                    if (day.toInt() >= 3) {
                        return fixTimeText( timeStamp2Date(timestamp, "MM-dd HH:mm"))
                    }
                    if (day.toInt() == 0 && hour >= 1) {
                        return fixTimeText( timeStamp2Date(timestamp, "HH:mm"))
                    }
                    if (day.toInt() == 1 && hour >= 1) {
                        return "昨天 " + fixTimeText( timeStamp2Date(timestamp, "HH:mm"))
                    }
                    if (day.toInt() == 2 && hour >= 1) {
                        return "前天 " + fixTimeText( timeStamp2Date(timestamp, "HH:mm"))
                    }
                } else {
                    return fixTimeText(timeStamp2Date(timestamp, "yyyy-MM-dd HH:mm"))
                }
            }
            return ""
        } catch (e: Exception) {
            LogUtils.e("时间规格异常", e.toString())
            return ""
        }


    }

    /**
     * 去0逻辑 接上面时间文本
     */
    private fun fixTimeText(temp: String): String {
        return try {
            var tempText = temp.replace(" 0", " ")//替换 04-04 09_45     ==》  04-04 9_45

            if (tempText[0].toString() == "0") {
                tempText = tempText.substring(1, tempText.length)// 04-04 9_45    ==>  4_04 9_45
            }
            tempText
        } catch (e: Exception) {
            temp
        }
    }

    /**
     * 使用方法：将时间戳转换为"yyyy-MM-dd"格式的年月日字符串
     * String dateStr =  timeStamp2Date(timeStamp, "yyyy-MM-dd");
     * @param timeStamp 1232131212
     * @param format "yyyy-MM-dd"
     * @return
     */
    fun timeStamp2Date(timeStamp: Long, format: String?): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return if (sdf.format(Date(timeStamp))==null){
            ""
        }else{
            sdf.format(Date(timeStamp))
        }
    }


}