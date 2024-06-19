package com.example.zrprint

import java.io.File


val inStr="C:\\Users\\1\\Desktop\\1\\zgsccd.txt"
val outStr="C:\\Users\\1\\Desktop\\1\\113.txt"
fun main() {
//    getLeftTag(inStr,outStr)
//    deleteLeftTag(inStr,outStr,"<")
//    addEndTag(inStr,outStr,"<3>")
    subMiddleStr(inStr,outStr,"<4>","<5>")
//      deleteTagStr(inStr,outStr)

//    main2()
}

fun main2() {
    // 定义一个包含所有中文字符的正则表达式
    val chineseRegex = Regex("[\u4e00-\u9fff]")

    // 从Unicode编码中获取所有中文字符
    val chineseChars = ('\u4e00'..'\u9fff').map { it.toChar() }

    // 将中文字符拼接成字符串
    val chineseText = chineseChars.joinToString("")

    // 将字符串写入到文本文件
    val file = File(outStr)
    file.writeText(chineseText)

    // 统计中文字符的数量
    val chineseCharCount = chineseText.length

    // 输出结果
    println("中文字符已写入 chinese_characters.txt 文件")
    println("中文字符总数: $chineseCharCount")
}







//①提取txt文本中的每一行<之前的文本并重新输出一个文本文件
//佉	 <1>佉 </1> <2>　（术语）[8751]kha又作呿、喀、吃、呵、珂、恪、轗。悉昙体文三十五字。</2>
//佉
fun getLeftTag(inputFileStr: String, outputFileStr: String){
    val inputFile = File(inputFileStr)
    val outputFile = File(outputFileStr)

    try {
        val lines = inputFile.readLines()
        println(lines.size)
        val extractedLines = lines.map { extractTextBeforeDelimiter(it, "<") }
        outputFile.writeText(extractedLines.joinToString("\n"))
        println("提取完成！")
    } catch (e: Exception) {
        println("处理文件时出现错误：${e.message}")
    }
}
fun extractTextBeforeDelimiter(line: String, delimiter: String): String {
    val index = line.indexOf(delimiter)
    return if (index != -1) {
        line.substring(0, index)
    } else {
        line
    }
}

//②遍历每一行，然后删除<标记之前的文本，保留之后的并输出成新的文本文件
//佉	 <1>佉 </1> <2>　（术语）[8751]kha又作呿、喀、吃、呵、珂、恪、轗。悉昙体文三十五字。</2>
//<1>佉 </1> <2>　（术语）[8751]kha又作呿、喀、吃、呵、珂、恪、轗。悉昙体文三十五字。</2>
fun deleteLeftTag(inputFileStr: String, outputFileStr: String,tagStr:String){
    val inputFile = File(inputFileStr)
    val outputFile = File(outputFileStr)

    try {
        val lines = inputFile.readLines()
        println(lines.size)
        val extractedLines = lines.map { extractTextBeforeDelimiter2(it, tagStr) }
        outputFile.writeText(extractedLines.joinToString("\n"))
        println("提取完成！")
    } catch (e: Exception) {
        println("处理文件时出现错误：${e.message}")
    }
}
fun extractTextBeforeDelimiter2(line: String, delimiter: String): String {
    val index = line.indexOf(delimiter)
    return if (index != -1) {
        line.substring(index, line.length)
    } else {
        line
    }
}

//③遍历每一行，然后在末尾添加文本
//佉	 <1>佉 </1> <2>　（术语）[8751]kha又作呿、喀、吃、呵、珂、恪、轗。悉昙体文三十五字。</2>
//佉	 <1>佉 </1> <2>　（术语）[8751]kha又作呿、喀、吃、呵、珂、恪、轗。悉昙体文三十五字。</2></3>
fun addEndTag(inputFileStr: String, outputFileStr: String,addString: String){
    val inputFile = File(inputFileStr)
    val outputFile = File(outputFileStr)

    try {
        val lines = inputFile.readLines()
        outputFile.writeText(lines.joinToString(addString+"\n"))
        println("提取完成！")
    } catch (e: Exception) {
        println("处理文件时出现错误：${e.message}")
    }
}


//④kotlin提取一段文本中<1><2>之间的内容并输入到一个集合中，然后排序，并去重
//这是一段文本<1>示例内容1<2>，<1>示例内容2<2>，<1>示例内容3<2>。
//示例内容1
val subMiddleStrResults = mutableListOf<String>()
fun subMiddleStr(inputFileStr: String, outputFileStr: String,leftStr: String,rightStr:String) {

    val inputFile = File(inputFileStr)
    val outputFile = File(outputFileStr)

    //从文本提取输入到集合
    try {
        val lines = inputFile.readLines()
        println(lines.size)
        val extractedLines = lines.map {
            extractTextBetweenTags(it,leftStr,rightStr)
        }

        extractedLines.forEach { subMiddleStrResults.add(it) }
        outputFile.writeText(extractedLines.joinToString("\n"))

        println("提取完成！1")
    } catch (e: Exception) {
        println("处理文件时出现错误1：${e.message}")
    }

//    // 排序并去重
    val sortedUniqueResults = subMiddleStrResults.distinct().sorted()
    println(sortedUniqueResults.size)
    // 输出结果
    sortedUniqueResults.forEach { println(it) }

}
fun extractTextBetweenTags(input: String,leftStr:String,rightStr:String): String {
    val startIndex = input.indexOf(leftStr)
    val endIndex = input.indexOf(rightStr)

    return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
        input.substring(startIndex + leftStr.length, endIndex)
    }else{
        ""
    }
}


/**
 * ⑤删除标记对 并输出文本
 *
 */
fun deleteTagStr(inputFileStr: String, outputFileStr: String) {

    val inputFile = File(inputFileStr)
    val outputFile = File(outputFileStr)

    //从文本提取输入到集合
    try {
        val lines = inputFile.readLines()
        println(lines.size)

        val extractedLines = lines.map {
            HtmlStatic.delHtmlTag(it)
        }

        outputFile.writeText(extractedLines.joinToString("\n"))

        println("提取完成！1")
    } catch (e: Exception) {
        println("处理文件时出现错误1：${e.message}")
    }


}