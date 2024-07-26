package com.example.zrwenxue.moudel.main.center.zr


fun main(){
    val originalString = "ggggggggggggggggggggggg12345678912345678912345678911122"
    val result = trimAndTakeLast32(originalString)
    println(result)

}

fun trimAndTakeLast32(input: String): String {
    // 去除字符串前后的空格
    val trimmed = input.trim()

    // 如果字符串长度小于32位,则直接返回
    if (trimmed.length <= 32) {
        return trimmed
    }

    // 否则,截取最后32位
    return trimmed.takeLast(32)
}