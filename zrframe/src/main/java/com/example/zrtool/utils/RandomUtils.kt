package com.example.zrtool.utils

import java.util.Arrays
import java.util.Random

/**
 * @Author qiwangi
 * @Date 2023/7/29
 * @TIME 12:12
 */
object RandomUtils {
    /**
     * 获取随机字符串
     * @param length
     * @return
     */
    fun getRandomString(length: Int): String {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = java.util.Random()
        val sb = StringBuffer()
        for (i in 0 until length) {
            val number = random.nextInt(62)
            sb.append(str[number])
        }
        return sb.toString()
    }

    /**
     * 随机图片
     */
    fun getRandomImageUrl(): String? {
        // 将多个图片链接存储在列表中
        val imageUrls: List<String> = Arrays.asList(
            "https://img2.woyaogexing.com/2023/04/09/55f771c75e6a4883c167aa6a6766cb22.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/7adb5986b2cd7ab25c9d2cba0e8af8e1.jpeg",
            "https://img2.woyaogexing.com/2023/04/08/16a5092608019b35f7172476bf48968e.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/13359d49e99a3d23d37799fff41fcd78.png",
            "https://img2.woyaogexing.com/2023/04/08/c626834b97f48a3f00bda3cd1580db44.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/c0df9e9f51bf18ac6882fb1b9dffe3e2.jpeg",
            "https://img2.woyaogexing.com/2023/04/09/13359d49e99a3d23d37799fff41fcd78.png",
            "https://img2.woyaogexing.com/2023/04/09/2599eb37c7f6cbe0c52006270ec6cf4f.png",
            "https://img2.woyaogexing.com/2023/04/08/ab2c6520fb3d05a998ed0f2ce6d2d007.jpeg"
        )
        // 随机选择一个链接并返回
        val random = Random()
        return imageUrls[random.nextInt(imageUrls.size)]
    }




}