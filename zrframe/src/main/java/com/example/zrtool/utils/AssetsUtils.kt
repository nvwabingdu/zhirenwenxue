package com.example.zrtool.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @Author qiwangi
 * @Date 2023/8/20
 * @TIME 00:04
 */
object AssetsUtils {

    /**
     * 读取文件并把每一行装入集合
     * val fileLines = readTextFileFromAssets("记忆100.txt")
     */
    fun readTextFileFromAssets(mContext :Context,fileName: String): List<String> {
        val inputStream = mContext.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = mutableListOf<String>()
        reader.useLines { lines.addAll(it) }
        return lines
    }
}