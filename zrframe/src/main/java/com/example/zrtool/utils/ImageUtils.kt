package com.example.zrtool.utils

import android.util.Log

/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 12:18
 */
object ImageUtils {
    /**
     * 图片比例类型
     */
    fun getImageType(width: Int, height: Int): Int {
        // 计算宽高比
        val aspectRatio = width.toDouble() / height.toDouble()
        Log.e("daddada2121", "图片宽高比" + aspectRatio)
        try {
            // 判断哪种类型
            return when {
                aspectRatio <= (3.0 / 4.0) -> 1//比例小于3：4   就为3：4
                aspectRatio > (3.0 / 4.0) && aspectRatio < (4.0 / 3.0) -> 2//比例大于3：4  小于4：3  就为1：1
                aspectRatio >= (4.0 / 3.0) -> 3//比例大于4：3  就为4：3
                else -> 2
            }
        } catch (e: Exception) {
            Log.e("zqr", "图片宽高比异常" + e.message)
            return 4
        }
    }


}